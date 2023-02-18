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
 * 菜谱商品关联表
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cookbook_sku_relation")
public class CookbookSkuRelationDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜谱ID
     */
    @TableField("cookbook_id")
    private Long cookbookId;

    /**
     * 商品编码
     */
    @TableField("sku_id")
    private String skuId;

    /**
     * 删除标识 0:有效 1:删除
     */
    @TableField("del_flag")
    private Integer delFlag;

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


    public static final String COOKBOOK_ID = "cookbook_id";

    public static final String SKU_ID = "sku_id";

    public static final String DEL_FLAG = "del_flag";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
