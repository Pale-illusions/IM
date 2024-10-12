package com.iflove.api.video.domain.vo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "视频信息")
public class VideoInfoResp {

    private Long id;

    /**
     * 视频标题
     */
    @Schema(description = "视频标题")
    private String title;

    /**
     * 视频简介
     */
    @Schema(description = "视频简介")
    private String description;

    /**
     * 视频地址
     */
    @Schema(description = "视频地址")
    private String url;

    /**
     * 视频作者id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 视频分数
     */
    @Schema(description = "视频分数")
    private Double score;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 视频标签
     */
    @Schema(description = "视频标签")
    private List<String> tags;
}

// TODO 增加 评论数，点赞数，点击量等字段