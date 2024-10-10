package com.iflove.api.social.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 关注表
 * @TableName follow
 */
@TableName(value ="follow")
@Data
public class Follow implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 粉丝id
     */
    @TableField(value = "follower_id")
    private Long followerId;

    /**
     * 目标id
     */
    @TableField(value = "followee_id")
    private Long followeeId;

    /**
     * 关注状态 0 关注 / 1 未关注
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}