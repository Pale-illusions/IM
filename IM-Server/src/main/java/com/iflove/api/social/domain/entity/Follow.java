package com.iflove.api.social.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注表
 * @TableName follow
 */
@TableName(value ="follow")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
     * 业务实现本质上逻辑删除
     * @see com.iflove.api.social.domain.enums.SubscribeStatusEnum
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