package com.iflove.api.video.service.impl;

import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.dao.es.VideoRepository;
import com.iflove.api.video.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private final VideoRepository videoRepository;

    @Override
    public VideoDTO saveVideo(VideoDTO video) {
        return videoRepository.save(video);
    }

    @Override
    public Optional<VideoDTO> findVideoById(Long id) {
        return videoRepository.findById(id);
    }

    @Override
    public Iterable<VideoDTO> findAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public void deleteVideoById(Long id) {
        videoRepository.deleteById(id);
    }

    @Override
    public List<VideoDTO> findVideosByTitle(String title) {
        return videoRepository.findByTitle(title);
    }
}
