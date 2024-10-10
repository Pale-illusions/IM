package com.iflove.api.social.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.enums.SubscribeStatusEnum;
import com.iflove.api.social.mapper.FollowMapper;
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
     * 获取粉丝 ( targetId 关注 uid )
     * @param uid 用户id
     * @param targetId 粉丝id
     * @return
     */
    public Follow getFollower(Long uid, Long targetId) {
        return lambdaQuery()
                .eq(Follow::getFollowerId, targetId)
                .eq(Follow::getFolloweeId, uid)
                .eq(Follow::getStatus, SubscribeStatusEnum.SUBSCRIBE.getStauts())
                .select(Follow::getId)
                .one();
    }

    /**
     * 获取粉丝集合
     * @param uid 用户id
     * @return
     */
    public List<Follow> getFollowerList(Long uid) {
        return lambdaQuery()
                .eq(Follow::getFolloweeId, uid)
                .eq(Follow::getStatus, SubscribeStatusEnum.SUBSCRIBE.getStauts())
                .list();
    }

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

    public void unsubscribe(Follow followee) {
        lambdaUpdate()
                .set(Follow::getStatus, SubscribeStatusEnum.UNSUBSCRIBE.getStauts())
                .eq(Follow::getId, followee.getId())
                .update();
    }
}




