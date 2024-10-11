package com.iflove.api.video.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 视频标签表
 * @TableName video_tag
 */
@TableName(value ="video_tag")
@Data
public class VideoTag implements Serializable {
    /**
     * 视频id
     */
    @TableId(value = "video_id")
    private Long videoId;

    /**
     * 标签id
     */
    @TableId(value = "tag_id")
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}