package com.jnu.example.feign.sdk.pojo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jnu.example.feign.sdk.constant.enums.ResponseCode;


/**
 *  @Author: zy
 *  @Date: 2020/4/14 22:26
 *  @Description: 分页返回体构造工具
 */
public class CustomizedPageResponseEntity<T> extends CustomizedResponseEntity<PageData<T>> {
     private CustomizedPageResponseEntity(ResponseCode responseCode, PageData<T> data) {
         super(responseCode, data);
    }

    public static <T> CustomizedPageResponseEntity<T> success(IPage<T> data) {
         return success(new PageData<>(data));
    }

    public static <T> CustomizedPageResponseEntity<T> success(PageData<T> data) {
        return new CustomizedPageResponseEntity<>(ResponseCode.SUCCESS,data);
    }
}