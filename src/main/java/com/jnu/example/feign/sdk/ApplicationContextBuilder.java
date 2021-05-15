package com.jnu.example.feign.sdk;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryAutoConfiguration;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.RibbonNacosAutoConfiguration;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author： zy
 * @Date：2021/4/19
 * @Description：A builder for creating Spring ApplicationContext without using the SpringApplication.run().
 */
@Slf4j
@Getter
public class ApplicationContextBuilder {

    /**
     * spring context
     */
    private  ApplicationContext applicationContext;

    /**
     * constructor
     */
    public ApplicationContextBuilder(Boolean useRegistry,String serverAddr,String namespace) {
        this.applicationContext = createContext(useRegistry,serverAddr,namespace);
    }

    /**
     * create Spring ApplicationContext
     * The difference between register and registerBeanDefinition: the former encapsulates the latter, the former helps you create the BeanDefinition
     * @param useRegistry：use register
     * @param serverAddr：nacos discovery server address
     * @param namespace: namespace, separation registry of different environments.
     */
    private  ApplicationContext createContext(Boolean useRegistry,String serverAddr,String namespace){
        //create Spring ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //Nacos registration center is used, Use Ribbon load balancing
        if(useRegistry) {
            //首先需要注入NacosDiscoveryProperties  该bean保存spring cloud nacos配置信息，同时创建NacosNamingService实例用于向注册中心注册当前服务
            context.register(UtilAutoConfiguration.class);
            context.register(StandardEnvironment.class);
            registerNacosDiscoveryProperties(context,serverAddr,namespace);

            //用于注入NacosServiceRegistry，其成员namingService(NacosNamingService) 接口的 registerInstance 方法可以将服务注册到nacos注册中心
            context.register(NacosDiscoveryAutoConfiguration.class);

            //ribbon负载均衡
            context.register(RibbonAutoConfiguration.class);

            //注入LoadBalancerFeignClient LoadBalancerFeignClient 也是一个feign.Client 客户端实现类。内部先使用 Ribbon 负载均衡算法计算server服务器，然后使用包装的 delegate 客户端实例，去完成 HTTP URL请求处理。
            context.register(FeignRibbonClientAutoConfiguration.class);

            //会注入ribbonServerList，用于从注册中心拉取服务列表
            context.register(RibbonNacosAutoConfiguration.class);
        }

        //Feign自动装配 会注入FeignContext
        context.register(FeignAutoConfiguration.class);

        //起到在配置未见中配置feign.client.xx参数的作用
        registerFeignClientProperties(context);

        //刷新 加载bean、以及bean实例化等过程
        context.refresh();

        log.info("jnu-feign-server-sdk application context refresh success");

        return context;
    }

    /**
     * 为所有的FeignClient服务创建默认配置属性  这个优先级会覆盖掉在子容器中设置的配置项  可以将一些没有在子容器中设置的配置项放在这里设置
     * @param context: application context
     */
    private void registerFeignClientProperties(AnnotationConfigApplicationContext context){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientProperties.class);
        //可以设置Feign配置 比如解码器、编码器等等
        FeignClientProperties.FeignClientConfiguration defaultConfig = new FeignClientProperties.FeignClientConfiguration();
        Map<String, FeignClientProperties.FeignClientConfiguration> config = new HashMap<>();
        config.put("default",defaultConfig);
        builder.addPropertyValue("config",config);
        //覆盖默认的
        context.registerBeanDefinition("feign.client-" + FeignClientProperties.class.getName(),builder.getBeanDefinition());
    }


    /**
     * 向容器注入Nacos配置属性
     * @param context : application context
     * @param serverAddr：nacos discovery server address
     * @param namespace: namespace, separation registry of different environments.
     */
    private void registerNacosDiscoveryProperties(AnnotationConfigApplicationContext context,String serverAddr,String namespace){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NacosDiscoveryProperties.class);
        builder.addPropertyValue("serverAddr",serverAddr);
        builder.addPropertyValue("namespace",namespace);
        builder.addPropertyValue("service","jnu-feign-server-sdk");
        context.registerBeanDefinition("nacosDiscoveryProperties",builder.getBeanDefinition());
    }
}
