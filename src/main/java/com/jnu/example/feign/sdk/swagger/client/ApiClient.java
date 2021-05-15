package com.jnu.example.feign.sdk.swagger.client;

import cn.hutool.core.util.StrUtil;
import com.jnu.example.feign.sdk.exception.ApiException;
import com.jnu.example.feign.sdk.feign.factory.TokenFactory;
import com.jnu.example.feign.sdk.pojo.CustomizedPageResponseEntity;
import com.jnu.example.feign.sdk.pojo.CustomizedResponseEntity;
import com.jnu.example.feign.sdk.pojo.PageData;
import com.jnu.example.feign.sdk.swagger.ApiRootResource;
import feign.Logger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * @Author： zy
 * @Date：2021/4/15
 * @Description：auth server api client
 */
@Getter
@Slf4j
public class ApiClient {

    /**
     * loginName
     */
    private String loginName;

    /**
     * password
     */
    private String password;

    /**
     * server name
     */
    private String serverName;

    /**
     * nacos discovery server address
     */
    private String serverAddr;

    /**
     * namespace, separation registry of different environments.
     */
    private String namespace;

    /**
     * http url path
     */
    private String url;

    /**
     * connect timeout ms
     */
    private int connectTimeoutMillis;

    /**
     * read timeout ms
     */
    private int readTimeoutMillis;

    /**
     * logger level
     */
    private Logger.Level level;

    /**
     * 使用nacos注册中心，or直接使用url请求
     */
    private Boolean useRegistry;

    /**
     * token工厂、用于生成token，ApiClient在每次请求用户权限服务时，都会调用其apply方法
     */
    private TokenFactory tokenFactory;

    /**
     * api root resource
     */
    private ApiRootResource apiRootResource;

    /*
     * Constructor for ApiClient
     */
    private ApiClient(Builder builder) {
        //赋值
        this.loginName = builder.loginName;
        this.password = builder.password;
        this.tokenFactory = builder.tokenFactory;
        this.serverName = builder.serverName;
        this.url = builder.url;
        this.connectTimeoutMillis = builder.connectTimeoutMillis;
        this.readTimeoutMillis = builder.readTimeoutMillis;
        this.level = builder.level;
        this.serverAddr = builder.serverAddr;
        this.namespace = builder.namespace;

        if(StrUtil.isBlank(this.serverName)){
            throw new IllegalArgumentException("serverName cannot be all empty");
        }

        if(StrUtil.isNotBlank(this.url)){
            this.useRegistry = false;
            return;
        }

        if(StrUtil.isBlank(this.serverAddr)){
            throw new IllegalArgumentException("If you use nacos registry, you need import spring-cloud-starter-alibaba-nacos-discovery jar package" +
                    " and configure spring.cloud.nacos.discovery.server-addr");
        }

        this.useRegistry = true;
    }

    /**
     * Handle the given response
     * @param <T> Type
     * @param response Http request body
     * @throws ApiException If the response fail throw new ApiException
     */
    public <T> T handleResponse(CustomizedResponseEntity<T> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody()).getData();
        } else {
            log.error("Ngsp Auth server request original error " + response.toString());
            throw new ApiException(Objects.requireNonNull(response.getBody()).getCode(), response.getBody().getMsg());
        }
    }

    /**
     * Handle the given response
     * @param <T> Type
     * @param response Http request body
     * @throws ApiException If the response fail throw new ApiException
     */
    public <T> PageData<T> handleResponse(CustomizedPageResponseEntity<T> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody()).getData();
        } else {
            log.error("Ngsp Auth server request original error " + response.toString());
            throw new ApiException(Objects.requireNonNull(response.getBody()).getCode(), response.getBody().getMsg());
        }
    }

   /**
    * get api root resource
    */
    public ApiRootResource getApiRootResource(){
        if(apiRootResource == null) {
            apiRootResource = new ApiRootResource(this);
        }
        return apiRootResource;
    }


    /**
     * api client builder
     */
    public static class Builder{

        private String loginName;

        private String password;

        private String serverName = "ngsp-s-auth-server";

        private String url;

        private int connectTimeoutMillis = 20000;

        private int readTimeoutMillis = 20000;

        private Logger.Level level = Logger.Level.FULL;

        private String serverAddr;

        private String namespace;

        private TokenFactory tokenFactory;

        /**
         * constructor
         */
        public Builder(String loginName,String password){
            if(StrUtil.isBlank(loginName) || StrUtil.isBlank(password)){
                throw new IllegalArgumentException("loginName and password cannot be empty");
            }
            this.loginName = loginName;
            this.password = password;
        }

        /**
         * constructor
         */
        public Builder(TokenFactory tokenFactory){
            if(tokenFactory == null){
                throw new IllegalArgumentException("tokenFactory cannot be empty");
            }
            this.tokenFactory = tokenFactory;
        }

        /**
         * set server name
         */
        public Builder serverName(String serverName){
            this.serverName = serverName;
            return this;
        }

        /**
         * set connectTimeoutMillis
         */
        public Builder connectTimeoutMillis(int connectTimeoutMillis){
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        /**
         * set readTimeoutMillis
         */
        public Builder readTimeoutMillis(int readTimeoutMillis){
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        /**
         * set url
         */
        public Builder url(String url){
            this.url = url;
            return this;
        }

        /**
         * sey logger level
         */
        public Builder level(Logger.Level level){
            this.level = level;
            return this;
        }

        /**
         * nacos discovery server address
         */
        public Builder serverAddr(String serverAddr){
            this.serverAddr = serverAddr;
            return this;
        }

        /**
         * namespace, separation registry of different environments.
         */
        public Builder namespace(String namespace){
            this.namespace = namespace;
            return this;
        }

        /**
         * build ApiClient
         */
        public ApiClient build(){
            return new ApiClient(this);
        }
    }
}
