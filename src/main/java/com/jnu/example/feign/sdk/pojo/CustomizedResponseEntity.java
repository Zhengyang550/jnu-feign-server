package com.jnu.example.feign.sdk.pojo;
import com.alibaba.fastjson.JSON;
import com.jnu.example.feign.sdk.constant.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *  @Author: zy
 *  @Date: 2020/4/14 22:30
 *  @Description: 返回体构造工具  只有200状态码是正确的 非200状态码都是有问题的，根据内部自定义状态码判断错误类别
 */
public class CustomizedResponseEntity<T> extends ResponseEntity<ResponseMessage<T>> {

    /**
     * @author: zy
     * @description: 构造函数
     * @date: 2020/4/15 13:07
     */
    protected CustomizedResponseEntity(ResponseCode responseCode, T data) {
        super(ResponseMessage.custom(responseCode, data), num2HttpStatus(responseCode.getCode()));
    }

    /**
     * 将自定义code转换为Http status
     * @param code: 自定义code
     * @return
     */
    public static HttpStatus num2HttpStatus(int code) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        for (HttpStatus httpStatus : HttpStatus.values()) {
            boolean b = code == httpStatus.value();
            if (b) {
                return httpStatus;
            }
        }
        return status;
    }

    /**
     * 成功
     */
    public static <T> CustomizedResponseEntity<T> success() {
        return success(null);
    }

    public static <T> CustomizedResponseEntity<T> success(T data) {
        return new CustomizedResponseEntity<>(ResponseCode.SUCCESS,data);
    }

    /**
     * 失败
     */
    public static <T> CustomizedResponseEntity<T> fail(ResponseCode responseCode) {
        return new CustomizedResponseEntity<>(responseCode,null);
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this.getBody());
    }
}
