package com.iflove.api.video.service;

import com.iflove.api.video.domain.dto.VideoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface ElasticSearchService {
    void saveVideo(VideoDTO video);

    Optional<VideoDTO> findVideoById(Long id);

    Iterable<VideoDTO> findAllVideos();

    void deleteVideoById(Long id);

    List<VideoDTO> findVideosByTitle(String title);

    List<VideoDTO> searchByTitleAndTags(String title, List<String> tags);
}
