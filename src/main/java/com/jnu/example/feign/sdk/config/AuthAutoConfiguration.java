package com.jnu.example.feign.sdk.config;

import cn.hutool.core.util.StrUtil;
import com.jnu.example.feign.sdk.feign.factory.TokenFactory;
import com.jnu.example.feign.sdk.swagger.client.ApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description：自动装配  https://blog.csdn.net/feeltouch/article/details/89443306
 * 引入当前sdk包后，如果第三方是Spring Boot应用该类会自动注入Spring容器中
 */
@Configuration
@EnableConfigurationProperties({AuthProperties.class, AuthNacosProperties.class})
public class AuthAutoConfiguration  {

    /**
     * Auth properties
     */
    @Resource
    private AuthProperties authProperties;

    @Resource
    private AuthNacosProperties authNacosProperties;

    /*
     * Inject auth api client into Spring ApplicationContext
     */
    @Bean(value="apiClient")
    @ConditionalOnMissingBean
    public ApiClient getApiClient()  {
        //Null check
        if((StrUtil.isBlank(authProperties.getLoginName()) || StrUtil.isBlank(authProperties.getPassword()))
           && authProperties.getTokenFactory() == null){
            throw new IllegalArgumentException("You must specify a login name and password or specify a token factory");
        }

        ApiClient.Builder builder;

        if(authProperties.getTokenFactory() != null) {
            TokenFactory tokenFactory;
            try {
                tokenFactory = authProperties.getTokenFactory().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Create tokenFactory instantiation failed,please make sure the configuration is correct");
            }

            builder = new ApiClient.Builder(tokenFactory);
        }else{
            builder = new ApiClient.Builder(authProperties.getLoginName(),authProperties.getPassword());
        }

        //return
        return  builder.serverName(authProperties.getServerName())
                .url(authProperties.getUrl())
                .connectTimeoutMillis(authProperties.getConnectTimeoutMillis())
                .readTimeoutMillis(authProperties.getReadTimeoutMillis())
                .serverAddr(authNacosProperties.getServerAddr())
                .namespace(authNacosProperties.getNamespace())
                .build();
    }
}
