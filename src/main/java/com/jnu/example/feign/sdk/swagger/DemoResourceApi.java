package com.jnu.example.feign.sdk.swagger;
import com.jnu.example.feign.sdk.service.DemoFeignClient;
import com.jnu.example.feign.sdk.swagger.client.ApiClient;
import com.jnu.example.feign.sdk.util.FeignClientUtils;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description：demo API
 */
public class DemoResourceApi {
    /**
     * demo client feign service
     */
    private DemoFeignClient demoFeignClient;

    /**
     * api client
     */
    private ApiClient apiClient;

    /**
     * constructor
     * @param apiClient: api client
     */
    public DemoResourceApi(ApiClient apiClient){
        if(apiClient == null){
            throw new NullPointerException("Api client NullPointerException");
        }
        this.apiClient = apiClient;
        this.demoFeignClient = FeignClientUtils.build(apiClient,"DemoFeignClient", DemoFeignClient.class);
    }

    /**
     * @Author： zy
     * @Date：2021/4/15 14:51
     * @Description：say hello
     * @param name
     * @return: User
     */
    public String sayHello(String name)  {
        String res = demoFeignClient.sayHello(name);
        return res;
    }

}
