package com.jnu.example.feign.sdk.config;

import com.jnu.example.feign.sdk.feign.factory.TokenFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description：用户权限http请求属性
 */
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {
    /**
     * LoginName of the auth server
     */
    private String loginName = "BDMSAdmin";

    /**
     * Password of the auth server
     */
    private String password = "pwd@24680";

    /**
     * Server name of the auth.
     */
    private String serverName ;


    /**
     * Fully qualified name of the token factory.
     */
    private Class<? extends TokenFactory> tokenFactory;

    /**
     * When you don’t use the nacos registry, specify url for auth server
     */
    private String url ;

    /**
     * Connect timeout of the auth server
     */
    private int connectTimeoutMillis;

    /**
     * Read timeout of the auth server
     */
    private int readTimeoutMillis;

}
