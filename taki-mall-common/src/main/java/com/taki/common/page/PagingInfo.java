package com.taki.common.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PagingInfo
 * @Description 分页查询结果
 * @Author Long
 * @Date 2022/2/26 20:33
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class PagingInfo<T> {

    /**
     * 页码
     */
    private int pageNo =1;

    /**
     * 每页数据
     */
    private int pageSize = 10;


    /**
     * 总条数
     */
    private Long total;

    /**
     * 起始位置
     */
    private Integer startPos = (pageNo - 1) * pageSize;

    /**
     *
     */
    private List<T> list;


    public  PagingInfo(Long total,List list){
        this.total = total;
        this.list = list;
    }

  public static  <T> PagingInfo toResponse(List<T> data,Long total,Integer currentPageNo,Integer currentPageSize){
    PagingInfo<T> pagingInfo = new PagingInfo<>();
    pagingInfo.setPageNo(currentPageNo);
    pagingInfo.setPageSize(currentPageSize);
    pagingInfo.setTotal(total);
    pagingInfo.setList(data);
    return pagingInfo;
  }

}
