package com.iflove.api.user.service;

import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface FriendService {

    /**
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    RestBean<Void> apply(Long uid, FriendApplyReq request);
}
