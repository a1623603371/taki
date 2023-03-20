package com.taki.careerplan.domain.request;

import com.taki.careerplan.domain.dto.SkuInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName SaveOrUpdateSkuRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/2/24 19:10
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOrUpdateSkuRequest {

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
    private List<SkuInfoDTO.ImageInfo> skuImage;

    /**
     * 商品详情图
     * [{"sort":1, "img": "url"}]
     */
    private List<SkuInfoDTO.ImageInfo> detailImage;

    /**
     * 商品状态  1:上架  2:下架
     */
    private Integer skuStatus;

    private List<Inventory> inventories;

    /**
     * 操作人
     */
    private Integer operator;

    @Data
    public static class Inventory {
        /**
         * 库存数量
         */
        private Integer inventoryNum;
        /**
         * 仓库地址
         */
        private String warehouse;
    }

}
