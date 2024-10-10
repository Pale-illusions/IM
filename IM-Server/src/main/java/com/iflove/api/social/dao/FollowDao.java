package com.iflove.api.social.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.enums.SubscribeStatusEnum;
import com.iflove.api.social.domain.vo.response.FollowInfoResp;
import com.iflove.api.social.mapper.FollowMapper;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2024-10-10 19:47:04
*/
@Service
public class FollowDao extends ServiceImpl<FollowMapper, Follow> {
    /**
     * 获取关注对象 ( uid 关注 targetId )
     * @param uid
     * @param targetId
     * @return
     */
    public Follow getFollowee(Long uid, Long targetId) {
        return lambdaQuery()
                .eq(Follow::getFollowerId, uid)
                .eq(Follow::getFolloweeId, targetId)
                .select(Follow::getId, Follow::getStatus)
                .one();
    }

    /**
     * 取关
     * @param followee
     */
    public void unsubscribe(Follow followee) {
        lambdaUpdate()
                .set(Follow::getStatus, SubscribeStatusEnum.UNSUBSCRIBE.getStauts())
                .eq(Follow::getId, followee.getId())
                .update();
    }

    /**
     * 粉丝分页
     * @param uid
     * @param page
     * @return
     */
    public IPage<Follow> getFollowerPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(Follow::getFolloweeId, uid)
                .eq(Follow::getStatus, SubscribeStatusEnum.SUBSCRIBE.getStauts())
                .orderByDesc(Follow::getUpdateTime)
                .page(page);
    }

    /**
     * 关注分页
     * @param uid
     * @param page
     * @return
     */
    public IPage<Follow> getFolloweePage(Long uid, Page page) {
        return lambdaQuery()
                .eq(Follow::getFollowerId, uid)
                .eq(Follow::getStatus, SubscribeStatusEnum.SUBSCRIBE.getStauts())
                .orderByDesc(Follow::getUpdateTime)
                .page(page);
    }

    public IPage<FollowInfoResp> getFriendsPage(Long uid, Page page) {
        return baseMapper.getFriendsPage(page, uid);
    }
}




