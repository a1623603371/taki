package com.taki.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName BaseEntity
 * @Description DO 基础父类
 * @Author Long
 * @Date 2021/12/14 16:13
 * @Version 1.0
 */
@Data
public class BaseEntity extends AbstractObject {
    /**
     * 主见
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;



    public  static  final  String ID = "id";

    public  static  final  String GMT_CREATE = "gmtCreate";

    public  static final  String GMT_MODIFIED = "gmtModified";

}
