package com.jnu.example.feign.sdk;
import com.jnu.example.feign.sdk.exception.ApiException;
import com.jnu.example.feign.sdk.swagger.DemoResourceApi;
import com.jnu.example.feign.sdk.swagger.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class Demo {
    public static void main(String[] args){
        ApiClient apiClient = new ApiClient.Builder(attrs -> {
            if (attrs != null) {
                HttpServletRequest currentRequest = attrs.getRequest();
                return currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
            }
            return null;
        })
                .serverName("nacos-produce")
//                .serverAddr("nacos地址")
                .url("nacos-produce服务地址")
                .build();
        DemoResourceApi demoResourceApi = apiClient.getApiRootResource().getDemoResourceApi();
        String res = null;
        try {
            res = demoResourceApi.sayHello("1");
        } catch (ApiException e) {
            log.error("Error " + e.getMessage(),e);
        }
        log.info("---" + res);
    }
}
