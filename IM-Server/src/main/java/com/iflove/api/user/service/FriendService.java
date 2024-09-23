package com.iflove.api.user.service;

import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface FriendService {

    /**
     * 申请好友
     * @param uid     uid
     * @param request 请求
     * @return {@link RestBean}
     */
    RestBean<Void> apply(Long uid, FriendApplyReq request);

    /**
     * 获取好友申请列表
     * @param uid 用户id
     * @param request 基础翻页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FriendApplyResp}>
     */
    RestBean<PageBaseResp<FriendApplyResp>> pageApplyFriend(Long uid, PageBaseReq request);
}
