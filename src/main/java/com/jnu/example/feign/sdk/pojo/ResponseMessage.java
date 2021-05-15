package com.jnu.example.feign.sdk.pojo;

import com.jnu.example.feign.sdk.constant.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: zy
 * @Description: json返回实体
 * @Date: 2020/3/1
 */
@Data
@AllArgsConstructor
public class ResponseMessage<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;


    /**
     *  自定义消息
     */
    public static <T> ResponseMessage<T> custom(ResponseCode responseCode, T data) {
        return new ResponseMessage<>(responseCode, data);
    }

    /**
     * 创建一个消息实体
     */
    public ResponseMessage (ResponseCode responseCode, T data) {
        responseCode = responseCode == null ? ResponseCode.FAIL:responseCode;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMessage();
        this.data = data;
    }
}