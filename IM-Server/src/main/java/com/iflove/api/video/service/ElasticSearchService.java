package com.iflove.api.video.service;

import com.iflove.api.video.domain.dto.VideoDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface ElasticSearchService {
    public VideoDTO saveVideo(VideoDTO video);

    public Optional<VideoDTO> findVideoById(Long id);

    public Iterable<VideoDTO> findAllVideos();

    public void deleteVideoById(Long id);

    public List<VideoDTO> findVideosByTitle(String title);
}
