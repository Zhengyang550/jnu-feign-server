package com.jnu.example.feign.sdk.swagger;


import com.jnu.example.feign.sdk.swagger.client.ApiClient;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description： api 根资源
 */
public class ApiRootResource {
    /**
     * api client
     */
    private ApiClient apiClient;

    /**
     * demo resource
     */
    private DemoResourceApi demoResourceApi;


    /**
     * constructor
     */
    public ApiRootResource(ApiClient apiClient){
        if(apiClient == null){
            throw new NullPointerException("Api client NullPointerException");
        }
        this.apiClient = apiClient;
    }

    /**
     * demo resource
     */
    public DemoResourceApi getDemoResourceApi(){
        if(demoResourceApi == null) {
            demoResourceApi = new DemoResourceApi(apiClient);
        }
        return demoResourceApi;
    }

}
