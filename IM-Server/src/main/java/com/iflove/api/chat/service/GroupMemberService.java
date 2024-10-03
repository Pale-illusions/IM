package com.iflove.api.chat.service;


import com.iflove.api.chat.domain.vo.request.admin.AdminAddReq;
import com.iflove.api.chat.domain.vo.request.admin.AdminRevokeReq;
import com.iflove.common.domain.vo.response.RestBean;

/**
* @author IFLOVE
* @description 针对表【group_member(群聊成员表)】的数据库操作Service
* @createDate 2024-09-17 14:57:36
*/
public interface GroupMemberService {
    /**
     * 添加管理员
     * @param uid 用户id
     * @param req 添加管理员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> addAdmin(Long uid, AdminAddReq req);

    /**
     * 撤销管理员
     * @param uid 用户id
     * @param req 撤销管理员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> revokeAdmin(Long uid, AdminRevokeReq req);

    /**
     * 退出群聊
     * @param roomId 房间id
     * @param uid 用户id
     * @return {@link RestBean}
     */
    RestBean<Void> exitGroup(Long roomId, Long uid);
}
