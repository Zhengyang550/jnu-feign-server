package com.jnu.example.feign.sdk.util;

import com.jnu.example.feign.sdk.ApplicationContextBuilder;
import com.jnu.example.feign.sdk.feign.FeignContextBuilder;
import com.jnu.example.feign.sdk.swagger.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description：动态创建FeignClient实例  https://www.jianshu.com/p/172e002e0eb4
 * 关于动态创建Feign Client的问题 https://blog.csdn.net/qq_37312208/article/details/112476051
 */
@Slf4j
public final class FeignClientUtils {
    /**
     * forbid instantiate
     */
    private FeignClientUtils(){
        throw new AssertionError();
    }

    /**
     * Spring ApplicationContext
     */
    private  static ApplicationContext applicationContext;

    /**
     * save contextId -> feign client mapping
     */
    private static final Map<String, Object> FEIGN_CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * build feign client
     */
    public static <T> T build(ApiClient apiClient, String contextId, Class<T> targetClass){
        return buildClient(apiClient,contextId,targetClass);
    }

    /**
     * build feign client without using the {@link FeignClient} annotation
     * @param apiClient: api client
     * @param contextId:  Unique Spring ApplicationContext identification for feign client
     * corresponding to the contextId in @FeignClient.  example  "NamedContextFactory"
     * @param targetClass: target class.  example: NamedContextFactory.class，
     */
    private static <T> T buildClient(ApiClient apiClient,String contextId, Class<T> targetClass) {
        //get
        T t = (T)FEIGN_CLIENT_CACHE.get(contextId);
        if(Objects.isNull(t)){
            synchronized(FeignClientUtils.class) {
                t = (T)FEIGN_CLIENT_CACHE.get(contextId);
                if(Objects.isNull(t)) {
                    //null check
                    if (applicationContext == null) {
                        //create Spring ApplicationContext
                        ApplicationContextBuilder builder = new ApplicationContextBuilder(apiClient.getUseRegistry()
                                ,apiClient.getServerAddr(),apiClient.getNamespace());
                        applicationContext = builder.getApplicationContext();
                    }

                    //get feign context
                    FeignContext feignContext = applicationContext.getBean(FeignContext.class);

                    //create Spring ApplicationContext for feign client
                    new FeignContextBuilder(feignContext, apiClient, contextId);

                    //A builder for creating Feign client without using the {@link FeignClient} annotation
                    FeignClientBuilder.Builder<T> builder = new FeignClientBuilder(applicationContext).forType(targetClass, apiClient.getServerName())
                            .contextId(contextId)
                            .url(apiClient.getUrl());
                    t = builder.build();

                    FEIGN_CLIENT_CACHE.put(contextId, t);
                }
            }
        }
        return t;
    }
}
