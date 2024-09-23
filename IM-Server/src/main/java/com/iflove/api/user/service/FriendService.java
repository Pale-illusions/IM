package com.iflove.api.user.service;

import com.iflove.api.user.domain.vo.request.friend.FriendApplyApproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyDisapproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyUnreadResp;
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

    /**
     * 好友申请未读数
     * @return {@link RestBean}
     */
    RestBean<FriendApplyUnreadResp> unread(Long uid);

    /**
     * 同意好友申请
     * @param req 好友申请同意请求
     * @return {@link RestBean}
     */
    RestBean<Void> applyApprove(Long uid, FriendApplyApproveReq req);

    /**
     * 拒绝好友申请
     * @param req 好友申请拒绝请求
     * @return {@link RestBean}
     */
    RestBean<Void> applyDisapprove(Long uid, FriendApplyDisapproveReq req);

    /**
     * 删除好友
     * @param uid 用户id
     * @param targetUid 好友id
     * @return {@link RestBean}
     */
    RestBean<Void> deleteFriend(Long uid, Long targetUid);

}
