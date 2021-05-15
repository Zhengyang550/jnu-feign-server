package com.jnu.example.feign.sdk.feign.factory;

import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author： zy
 * @Date：2021/4/22
 * @Description： {@code TokenFactory} may be configured for adding Authorization header to  all requests.
 */
public interface TokenFactory {
    /**
     * Called for every request. Get token using methods on the supplied {@link ServletRequestAttributes}.
     */
    String getToken(ServletRequestAttributes attrs);
}
