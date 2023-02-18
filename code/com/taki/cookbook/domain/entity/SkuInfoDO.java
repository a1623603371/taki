package com.taki.cookbook.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sku_info")
public class SkuInfoDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编码
     */
    @TableField("sku_id")
    private String skuId;

    /**
     * 商品名称
     */
    @TableField("sku_name")
    private String skuName;

    /**
     * 价格（单位为分）
     */
    @TableField("price")
    private Integer price;

    /**
     * 会员价（单位为分）
     */
    @TableField("vip_price")
    private Integer vipPrice;

    /**
     * 主图链接
     */
    @TableField("main_url")
    private String mainUrl;

    /**
     * 商品轮播图（[{"sort":1,	"img": "url"}]）
     */
    @TableField("sku_image")
    private String skuImage;

    /**
     * 商品详情图（[{"sort":1,	"img": "url"}]）
     */
    @TableField("detail_image")
    private String detailImage;

    /**
     * 商品状态 1:上架 2:下架
     */
    @TableField("sku_status")
    private Integer skuStatus;

    /**
     * 创建⼈
     */
    @TableField("create_user")
    private Integer createUser;

    /**
     * 修改⼈
     */
    @TableField("update_user")
    private Integer updateUser;


    public static final String SKU_ID = "sku_id";

    public static final String SKU_NAME = "sku_name";

    public static final String PRICE = "price";

    public static final String VIP_PRICE = "vip_price";

    public static final String MAIN_URL = "main_url";

    public static final String SKU_IMAGE = "sku_image";

    public static final String DETAIL_IMAGE = "detail_image";

    public static final String SKU_STATUS = "sku_status";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
