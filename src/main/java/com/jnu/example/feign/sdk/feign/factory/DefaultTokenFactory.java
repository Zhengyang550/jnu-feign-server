package com.jnu.example.feign.sdk.feign.factory;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author： zy
 * @Date：2021/4/22
 * @Description：default implementation of the {@link TokenFactory}
 */
public class DefaultTokenFactory implements TokenFactory {
    @Override
    public String getToken(ServletRequestAttributes attrs) {
        if (attrs != null) {
            HttpServletRequest currentRequest = attrs.getRequest();
            return currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
        }
        return "DEFAULT";
    }
}
