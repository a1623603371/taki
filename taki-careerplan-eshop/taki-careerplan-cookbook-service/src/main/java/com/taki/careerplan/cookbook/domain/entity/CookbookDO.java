package com.taki.careerplan.cookbook.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜谱表
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cookbook")
@Builder
public class CookbookDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 作者ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 菜谱名称
     */
    @TableField("cookbook_name")
    private String cookbookName;

    /**
     * 菜谱类型 1:PGC 2:UGC
     */
    @TableField("cookbook_type")
    private Integer cookbookType;

    /**
     * 菜谱描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 分类标签 1:私房菜 2:下饭菜	3:快⼿菜
     */
    @TableField("category_tag")
    private Integer categoryTag;

    /**
     * 主图链接
     */
    @TableField("main_url")
    private String mainUrl;

    /**
     * 视频链接
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 制作时⻓ 1:10分钟以内 2:10-	30分钟 3:30-60分钟 4:1⼩时以上
     */
    @TableField("cooking_time")
    private Integer cookingTime;

    /**
     * 难度 1:简单 2:⼀般 3:较难	4:极难
     */
    @TableField("difficulty")
    private Integer difficulty;

    /**
     * 菜谱详情	（[{"step":"1", "content":"步骤1xx", "img":"url"}]）
     */
    @TableField("cookbook_detail")
    private String cookbookDetail;

    /**
     * ⻝材清单
     */
    @TableField("foods")
    private String foods;

    /**
     * ⼩贴⼠
     */
    @TableField("tips")
    private String tips;

    /**
     * 菜谱状态 0:有效 1:删除
     */
    @TableField("cookbook_status")
    private Integer cookbookStatus;

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


    public static final String USER_ID = "user_id";

    public static final String COOKBOOK_NAME = "cookbook_name";

    public static final String COOKBOOK_TYPE = "cookbook_type";

    public static final String DESCRIBE = "describe";

    public static final String CATEGORY_TAG = "category_tag";

    public static final String MAIN_URL = "main_url";

    public static final String VIDEO_URL = "video_url";

    public static final String COOKING_TIME = "cooking_time";

    public static final String DIFFICULTY = "difficulty";

    public static final String COOKBOOK_DETAIL = "cookbook_detail";

    public static final String FOODS = "foods";

    public static final String TIPS = "tips";

    public static final String COOKBOOK_STATUS = "cookbook_status";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
