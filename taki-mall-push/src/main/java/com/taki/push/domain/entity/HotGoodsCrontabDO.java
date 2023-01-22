package com.taki.push.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 热门商品任务表
 * </p>
 *
 * @author long
 * @since 2022-10-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("hot_goods_crontab")
public class HotGoodsCrontabDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 热门商品id
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 热门商品名
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 热门商品描述
     */
    @TableField("goods_desc")
    private String goodsDesc;

    /**
     * 热门商品关键字
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 定时任务日期
     */
    @TableField("crontab_date")
    private LocalDateTime crontabDate;

    /**
     * 用户画像
     */
    @TableField("portrayal")
    private String portrayal;

    /**
     * 任务分片后缀
     */
    @TableField("job_shard_suffix")
    private Integer jobShardSuffix;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private Integer createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private Integer updateUser;


    public static final String GOODS_ID = "goods_id";

    public static final String GOODS_NAME = "goods_name";

    public static final String GOODS_DESC = "goods_desc";

    public static final String KEYWORDS = "keywords";

    public static final String CRONTAB_DATE = "crontab_date";

    public static final String PORTRAYAL = "portrayal";

    public static final String JOB_SHARD_SUFFIX = "job_shard_suffix";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_USER = "UPDATE_USER";

}
