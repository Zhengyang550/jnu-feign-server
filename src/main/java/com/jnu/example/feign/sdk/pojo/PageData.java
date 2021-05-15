package com.jnu.example.feign.sdk.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 *  @Author: zy
 *  @Date: 2020/4/14 22:13
 *  @Description: 分页数据对象
 */
@Data
public class PageData<T> {
    /*
     * 每页大小
     */
    private long pageSize;

    /*
     * 当前页码
     */
    private long current ;

    /*
     * 总共记录数
     */
    private long total ;

    /*
     * 数据
     */
    private List<T> list;

    public PageData() {}

    public PageData(List<T> list, long total, long pageSize, long current) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.current = current;
    }

    public PageData(IPage<T> page) {
        this.list = page.getRecords();
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.pageSize = page.getSize();
    }
}
