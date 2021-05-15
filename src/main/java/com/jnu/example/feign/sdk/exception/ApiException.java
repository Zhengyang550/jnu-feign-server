package com.jnu.example.feign.sdk.exception;

import lombok.Getter;

/**
 * @Author： zy
 * @Date：2021/4/20
 * @Description： API exception.
 */
@Getter
public class ApiException extends RuntimeException {
    /**
     * code that is deserialized from the response body
     */
     private final int code;

    /**
     * @param code：
     * @param msg:
     */
    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
