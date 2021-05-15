package com.jnu.example.feign.sdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author： zy
 * @Date：2021/4/20
 * @Description：nacos discovery properties
 */
@ConfigurationProperties("spring.cloud.nacos.discovery")
@Data
public class AuthNacosProperties {
    /**
     * nacos discovery server address
     */
    private String serverAddr;

    /**
     * namespace, separation registry of different environments.
     */
    private String namespace;
}
