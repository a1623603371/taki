package com.taki.push.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HotGoodsVO
 * @Description 热门商品
 * @Author Long
 * @Date 2022/10/6 16:35
 * @Version 1.0
 */
@Data
public class HotGoodsVO implements Serializable {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品关键字
     */
    private List<String> keyWords;


    /**
     * 商品名称
     */
    private String goodsDesc;
}
