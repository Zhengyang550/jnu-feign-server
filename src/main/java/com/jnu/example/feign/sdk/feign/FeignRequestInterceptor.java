package com.jnu.example.feign.sdk.feign;

import com.jnu.example.feign.sdk.feign.factory.TokenFactory;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author： zy
 * @Date：2021/4/18
 * @Description：自定义Feign请求拦截器
 */
@Setter
public class FeignRequestInterceptor implements RequestInterceptor {
    /**
     * loginName
     */
    private String loginName;

    /**
     * password
     */
    private String password;

    /**
     * token factory
     */
    private TokenFactory tokenFactory;

    @Override
    public void apply(RequestTemplate template) {
        template.header("loginName",loginName);
        template.header("password",password);
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (tokenFactory != null) {
            template.header(HttpHeaders.AUTHORIZATION, tokenFactory.getToken(attrs));
        }
    }
}
