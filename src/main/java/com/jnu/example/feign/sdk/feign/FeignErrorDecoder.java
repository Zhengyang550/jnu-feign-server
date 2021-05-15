package com.jnu.example.feign.sdk.feign;

import com.alibaba.fastjson.JSONObject;
import com.jnu.example.feign.sdk.exception.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author： zy
 * @Date：2021/4/20
 * @Description：自定义Feign异常处理
 * https://www.cnblogs.com/keeya/p/13559151.html
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            //401 一般是token错误、即身份验证失败
            case 401:
            case 403:
            case 404:
                //response.body信息不可以打印，打印后流会自动关闭
                log.error("Ngsp Auth server request original error status " + response.status());
                return new ApiException(response.status(),handleResponse(response));
            default:
                // 其他异常交给Default去解码处理
                return new ErrorDecoder.Default().decode(methodKey, response);
        }
    }

    /**
     * handle response body
     */
    private String handleResponse(Response response) {
        String res = null;

        //反序列化
        JSONObject object = (JSONObject) JSONObject.parse(response.body().toString());
        if(object.get("msg") != null){
            res = (String) object.get("msg") ;
        }

        return res;
    }
}
