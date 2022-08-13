package com.black.software.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Software implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long fileId;

    private Long downTick;

    private Long shareTick;

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private Long size;
    @TableField(exist = false)
    private String realName;
    @TableField(exist = false)
    private String path;
    @TableField(exist = false)
    private String md5;
    @TableField(exist = false)
    private int level;
    @TableField(exist = false)
    private int share;
    @TableField(exist = false)
    private int page;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private Long categoryId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
