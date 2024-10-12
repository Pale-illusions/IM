package com.iflove.api.video.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "video")
public class VideoDTO {

    @Id
    @Field(type = FieldType.Long, index = false)
    private Long id;

    /**
     * 视频标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 视频简介
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String description;

    /**
     * 视频地址
     */
    @Field(type = FieldType.Text)
    private String url;

    /**
     * 视频作者id
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 视频分数
     */
    @Field(type = FieldType.Double)
    private Double score;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 视频标签
     */
    @Field(type = FieldType.Keyword)  // 关键词字段用于精确匹配，不需要分词
    private List<String> tags;
}
