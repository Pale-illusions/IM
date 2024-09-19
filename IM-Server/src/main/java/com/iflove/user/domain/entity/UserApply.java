package com.iflove.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户申请表
 * @TableName user_apply
 */
@TableName(value ="user_apply")
@Data
public class UserApply implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 申请人uid
     */
    private Long userId;

    /**
     * 申请类型 1 加好友
     */
    private Integer type;

    /**
     * 接收人uid
     */
    private Long targetId;

    /**
     * 申请信息
     */
    private String msg;

    /**
     * 申请状态 0 待审批 / 1 同意 / 2 拒绝
     */
    private Integer status;

    /**
     * 阅读状态 0 未读 / 1 已读
     */
    private Integer readStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}