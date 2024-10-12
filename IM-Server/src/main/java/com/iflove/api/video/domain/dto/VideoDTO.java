package com.iflove.api.video.domain.dto;

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
public class VideoDTO {
    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频简介
     */
    private String description;

    /**
     * 视频地址
     */

    private String url;

    /**
     * 视频作者id
     */

    private Long userId;

    /**
     * 视频分数
     */

    private Double score;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 视频标签
     */

    private List<String> tags;
}
