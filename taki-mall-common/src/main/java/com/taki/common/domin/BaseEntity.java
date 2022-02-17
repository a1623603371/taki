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
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;



    public  static  final  String ID = "id";

    public  static  final  String CREATE_TIME = "create_time";

    public  static final  String UPDATE_TIME = "update_time";

}
