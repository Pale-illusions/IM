package com.iflove.api.video.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.video.domain.entity.Tag;
import com.iflove.api.video.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2024-10-11 15:27:06
*/
@Service
public class TagDao extends ServiceImpl<TagMapper, Tag> {

}




