package com.iflove.api.social.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.mapper.FollowMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2024-10-10 19:47:04
*/
@Service
public class FollowDao extends ServiceImpl<FollowMapper, Follow> {

}




