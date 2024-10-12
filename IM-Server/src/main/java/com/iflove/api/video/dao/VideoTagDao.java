package com.iflove.api.video.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.video.domain.entity.VideoTag;
import com.iflove.api.video.mapper.VideoTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【video_tag(视频标签表)】的数据库操作Service实现
* @createDate 2024-10-11 15:27:06
*/
@Service
public class VideoTagDao extends ServiceImpl<VideoTagMapper, VideoTag> {

    public List<VideoTag> getByVideoId(Long videoId) {
        return lambdaQuery()
                .eq(VideoTag::getVideoId, videoId)
                .select(VideoTag::getTagId, VideoTag::getVideoId)
                .list();
    }
}




