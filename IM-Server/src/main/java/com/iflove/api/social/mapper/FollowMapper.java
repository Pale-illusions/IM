package com.iflove.api.social.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.vo.response.FollowInfoResp;
import org.apache.ibatis.annotations.Param;

/**
* @author IFLOVE
* @description 针对表【follow(关注表)】的数据库操作Mapper
* @createDate 2024-10-10 19:47:04
* @Entity com.iflove.domain.Follow
*/
public interface FollowMapper extends BaseMapper<Follow> {
    IPage<FollowInfoResp> getFriendsPage(Page<Follow> page, @Param("uid") Long uid);
}




