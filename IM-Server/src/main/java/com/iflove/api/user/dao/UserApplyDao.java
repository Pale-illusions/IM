package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.enums.ApplyReadStatusEnum;
import com.iflove.api.user.domain.enums.ApplyStatusEnum;
import com.iflove.api.user.domain.enums.ApplyTypeEnum;
import com.iflove.api.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author IFLOVE
* @description 针对表【user_apply(用户申请表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    /**
     * 获取好友请求
     * @param uid
     * @param targetUid
     * @return
     */
    public UserApply getFriendApply(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUserId, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    /**
     * 获取请求未读数
     * @param targetUid
     * @return
     */
    public Long getUnreadCount(Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .count();
    }

    /**
     * 获取好友请求分页
     * @param uid
     * @param page
     * @return
     */
    public IPage<UserApply> getFriendApplyPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .orderByDesc(UserApply::getCreateTime)
                .page(page);
    }

    /**
     * 阅读请求
     * @param uid
     * @param applyIds
     */
    public void readApplies(Long uid, List<Long> applyIds) {
        lambdaUpdate()
                .set(UserApply::getReadStatus, ApplyReadStatusEnum.READ.getCode())
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .in(UserApply::getId, applyIds)
                .eq(UserApply::getTargetId, uid)
                .update();
    }

    /**
     * 同意请求
     * @param applyId
     */
    public void applyApprove(Long applyId) {
        lambdaUpdate()
                .set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .update();
    }

    /**
     * 拒绝请求
     * @param applyId
     */
    public void applyDisapprove(Long applyId) {
        lambdaUpdate()
                .set(UserApply::getStatus, ApplyStatusEnum.DISAGREE.getCode())
                .eq(UserApply::getId, applyId)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .update();
    }
}




