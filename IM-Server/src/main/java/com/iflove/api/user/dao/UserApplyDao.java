package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.enums.ApplyReadStatusEnum;
import com.iflove.api.user.domain.enums.ApplyStatusEnum;
import com.iflove.api.user.domain.enums.ApplyTypeEnum;
import com.iflove.api.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【user_apply(用户申请表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    public UserApply getFriendApply(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUserId, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    public Long getUnreadCount(Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .count();
    }
}




