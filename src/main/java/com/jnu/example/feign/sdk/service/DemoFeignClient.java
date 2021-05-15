package com.jnu.example.feign.sdk.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author： zy
 * @Date：2021/5/11
 * @Description：demo
 */
public interface DemoFeignClient {
    @GetMapping("/hello")
    String sayHello(@RequestParam("name") String name);
}
