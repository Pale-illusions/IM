package com.iflove.api.video.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.dao.es.VideoRepository;
import com.iflove.api.video.service.ElasticSearchService;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.VideoErrorEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private final VideoRepository videoRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveVideo(VideoDTO video) {
        videoRepository.save(video);
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

    // 根据标题和标签进行查询，并进行分页
    @Override
    public List<VideoDTO> searchByTitleAndTags(String title, List<String> tags) {
        // title 和 tags 不能同时为空
        if (title.isBlank() && CollectionUtils.isEmpty(tags)) {
            throw new BusinessException(VideoErrorEnum.SEARCH_FAIL);
        }

//        Pageable pageable = PageRequest.of(page, size);

        // 映射tags
        // https://stackoverflow.com/questions/72197344/creating-a-termquery-with-a-list-by-using-elasticsearch-java-api-client
        TermsQueryField tagTerms = new TermsQueryField.Builder()
                .value(tags.stream().map(FieldValue::of).toList())
                .build();

        // title为空 使用 标签 查询
        // 标签为空 使用 title 查询
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (!title.isBlank()) {
                                b.must(m -> m
                                        .match(t -> t
                                                .field("title")
                                                .query(title)
                                        )
                                );
                            }
                            if (Objects.nonNull(tagTerms) && !CollectionUtils.isEmpty(tags)) {
                                b.filter(f -> f
                                        .terms(t -> t
                                                .field("tags.keyword")
                                                .terms(tagTerms)
                                        )
                                );
                            }
                            return b;
                        })
                )
//                .withPageable(pageable)
                .build();

        SearchHits<VideoDTO> search = elasticsearchOperations.search(searchQuery, VideoDTO.class);
        return search.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }


}
