package com.iflove.api.video.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频标签表
 * @TableName video_tag
 */
@TableName(value ="video_tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoTag implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 视频id
     */
    @TableField(value = "video_id")
    private Long videoId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static VideoTag init(Long videoId, Long tagId) {
        VideoTag videoTag = new VideoTag();
        videoTag.setTagId(tagId);
        videoTag.setVideoId(videoId);
        return videoTag;
    }
}