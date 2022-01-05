package com.taki.common.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageResult
 * @Description TODO
 * @Author Long
 * @Date 2022/1/5 11:29
 * @Version 1.0
 */
@Data
public class PageResult<T> implements Serializable {


    private static final long serialVersionUID = 3592891892094059485L;

    /**
     *总记录数
     */
    private long totalElements ;

    /**
     * 数据结果集
     */
    private List<T> content;

    /**
     * 页数
     */
    private long number;
    /**
     * 每页记录数
     */
    private long size;

    /**
     * 总页数
     */
    private long  totalPages;
    /**
     * 当前 页的数量 <= pageSize 该属性来自ArrayList size 属性
     */
    private long numberOfElements;

    public PageResult() {
    }

    public PageResult(List<T> content) {

        if (content != null){
            this.number = 1;
            this.size = content.size();
            this.totalElements = content.size();
            this.totalPages = 1;

            this.content = content;
            this.numberOfElements = content.size();
        }

    }


    public PageResult(IPage<T> page) {
        if (page != null){
            this.totalElements = page.getTotal();
            this.content = page.getRecords();
            this.number = page.getCurrent();
            this.size = page.getSize();
            this.totalPages = page.getPages();

            if (content != null && !content.isEmpty()){
                this.numberOfElements = content.size();
            }{
                this.numberOfElements = 0;
            }


        }
    }
}
