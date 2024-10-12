package com.iflove.api.video.dao.es;

import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface VideoRepository extends ElasticsearchRepository<VideoDTO, Long> {
    /**
     * 根据视频标题查询视频列表
     * @param title 标题
     * @return 视频集合
     */
    List<VideoDTO> findByTitle(String title);
}
