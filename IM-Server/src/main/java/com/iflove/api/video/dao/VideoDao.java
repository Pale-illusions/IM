package com.iflove.api.video.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【video(视频表)】的数据库操作Service实现
* @createDate 2024-10-10 19:47:04
*/
@Service
public class VideoDao extends ServiceImpl<VideoMapper, Video> {

    public IPage<Video> rank(Page page) {
        return lambdaQuery()
                .orderByDesc(Video::getScore)
                .page(page);
    }
}




