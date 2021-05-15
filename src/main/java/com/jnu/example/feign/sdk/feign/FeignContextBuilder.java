package com.jnu.example.feign.sdk.feign;

import cn.hutool.core.util.ReflectUtil;
import com.jnu.example.feign.sdk.feign.factory.TokenFactory;
import com.jnu.example.feign.sdk.swagger.client.ApiClient;
import feign.Logger;
import feign.Request;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： zy
 * @Date： 2021/4/18
 * @Description：A builder for creating Application context for feign client.
 */
@Slf4j
@Getter
public class FeignContextBuilder {

    /**
     * A factory that creates instances of feign classes. It creates a Spring
     * ApplicationContext per feign client, and extracts the beans that it needs from there.
     */
    private FeignContext feignContext;

    /**
     * Unique Spring ApplicationContext identification for feign client
     */
    private String contextId;

    /*
     * Spring ApplicationContext for feign client
     */
    private ApplicationContext feignClientContext;

    /**
     * constructor
     * @param feignContext:
     * @param apiClient
     * @param contextId
     */
    public FeignContextBuilder(FeignContext feignContext, ApiClient apiClient, String contextId){
        //save parameter
        this.feignContext = feignContext;
        this.contextId = contextId;

        //create Spring ApplicationContext for feign client .
        this.feignClientContext = createContext(feignContext,apiClient,contextId);
    }

    /**
     * create Spring ApplicationContext for feign client
     * FeignContext cannot find the Spring ApplicationContext we created based on the contextId, it will create  Spring ApplicationContext for feign client
     * @param apiClient: api client
     * @param contextId : Unique Spring ApplicationContext identification for feign client
     */
    private ApplicationContext createContext(FeignContext feignContext, ApiClient apiClient, String contextId){
        //create Spring ApplicationContext for feign client
        Method getContext =  ReflectUtil.getMethod(feignContext.getClass(),"getContext",String.class);
        getContext.setAccessible(true);

        AnnotationConfigApplicationContext context= null;
        try {
            //Inject Feign request interceptor bean into the Spring ApplicationContext
            context = (AnnotationConfigApplicationContext)getContext.invoke(feignContext,contextId);

            //Inject Feign request interceptor bean into the Spring ApplicationContext
            registerRequestInterceptor(context,apiClient.getLoginName(),apiClient.getPassword(),apiClient.getTokenFactory());

            //Inject Feign request options bean into the Spring ApplicationContext
            registerRequestOptions(context,apiClient.getConnectTimeoutMillis(),apiClient.getReadTimeoutMillis());

            //Inject Feign logger level bean into the Spring ApplicationContext
            registerLoggerLevel(context,apiClient.getLevel());

            //Inject Feign decoder bean into the Spring ApplicationContext
            registerDecoder(context);

            //Inject Feign error decoder bean into the Spring ApplicationContext
            registerErrorDecoder(context);

            log.info("jnu-feign-server-sdk:" + contextId + " feign client context refresh success");
        } catch (Exception e) {
            log.error(contextId +": create sub context fail");
        }

        return context;
    }


    /**
     * Inject Feign request interceptor bean into the Spring ApplicationContext
     * @param context : Spring ApplicationContext for feign client
     * @param loginName：loginName
     * @param password: password
     */
    private void registerRequestInterceptor(AnnotationConfigApplicationContext context, String loginName, String password
            , TokenFactory tokenFactory){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignRequestInterceptor.class);
        builder.addPropertyValue("loginName",loginName);
        builder.addPropertyValue("password",password);
        builder.addPropertyValue("tokenFactory",tokenFactory);
        context.registerBeanDefinition("feignRequestInterceptor",builder.getBeanDefinition());
    }


    /**
     * Inject Feign request options bean into the Spring ApplicationContext
     * @param context : Spring ApplicationContext for feign client
     * @param connectTimeoutMillis : connect timeout
     * @param readTimeoutMillis: read timeout
     */
    private void registerRequestOptions(AnnotationConfigApplicationContext context,int connectTimeoutMillis,int readTimeoutMillis){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Request.Options.class);
        builder.addConstructorArgValue(connectTimeoutMillis);
        builder.addConstructorArgValue(readTimeoutMillis);
        builder.addConstructorArgValue(true);
        context.registerBeanDefinition("feignRequestOptions",builder.getBeanDefinition());
    }

    /**
     * Inject Feign decoder bean into the Spring ApplicationContext
     * @param context : Spring ApplicationContext for feign client
     */
    private void registerDecoder(AnnotationConfigApplicationContext context){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringDecoder.class);
        builder.addConstructorArgValue(feignHttpMessageConverter());
        context.registerBeanDefinition("feignDecoder",builder.getBeanDefinition());
    }

    /**
     * HttpMessageConverters factory
     */
    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());
        return () -> httpMessageConverters;
    }

    /**
     * HttpMessageConverter
     */
    private static class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        GateWayMappingJackson2HttpMessageConverter(){
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
            setSupportedMediaTypes(mediaTypes);
        }
    }

    /**
     * Inject Feign logger level bean into the Spring ApplicationContext
     */
    private void registerLoggerLevel(AnnotationConfigApplicationContext context,Logger.Level level){
        //https://blog.csdn.net/u014252478/article/details/84869997
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Logger.Level.class, () -> level);
        context.registerBeanDefinition("feignLoggerLevel",beanDefinitionBuilder.getBeanDefinition());
    }


    /**
     * Inject Feign error decoder bean into the Spring ApplicationContext
     */
    private void registerErrorDecoder(AnnotationConfigApplicationContext context){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(FeignErrorDecoder.class, () -> new FeignErrorDecoder());
        context.registerBeanDefinition("feignErrorDecoder",beanDefinitionBuilder.getBeanDefinition());
    }
}
