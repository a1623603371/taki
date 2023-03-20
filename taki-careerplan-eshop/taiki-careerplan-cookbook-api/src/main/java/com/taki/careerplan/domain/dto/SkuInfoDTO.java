package com.taki.careerplan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SkuInfoDTO
 * @Description TODO
 * @Author Long
 * @Date 2023/2/24 19:11
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuInfoDTO {
    /**
     * 商品编码
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 价格（单位为分）
     */
    private Integer price;

    /**
     * 会员价（单位为分）
     */
    private Integer vipPrice;

    /**
     * 主图链接
     */
    private String mainUrl;

    /**
     * 商品轮播图
     * [{"sort":1, "img": "url"}]
     */
    private List<ImageInfo> skuImage;

    /**
     * 商品详情图
     * [{"sort":1, "img": "url"}]
     */
    private List<ImageInfo> detailImage;

    /**
     * 商品状态  1:上架  2:下架
     */
    private Integer skuStatus;

    @Data
    public static class ImageInfo implements Serializable {

        /**
         * 排序字段，从小到大
         */
        private Integer sort;

        /**
         * 图片url
         */
        private String img;
    }

    private LocalDateTime updateTime;
}
