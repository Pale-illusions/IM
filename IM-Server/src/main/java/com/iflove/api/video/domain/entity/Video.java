package com.iflove.api.video.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频表
 * @TableName video
 */
@TableName(value ="video")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 视频标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 视频简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 视频地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 视频作者id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 视频分数
     */
    @TableField(value = "score")
    private Double score;

    /**
     * 逻辑删除 0 正常 / 1 删除
     */
    @TableField(value = "delete_status")
    @TableLogic(value = "0", delval = "1")
    private Integer deleteStatus;

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