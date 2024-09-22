package com.iflove.api.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWTUtil;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSChatMemberResp;
import com.iflove.api.user.domain.vo.response.ws.WSLoginSuccess;
import com.iflove.api.user.domain.vo.response.ws.WSOnlineOfflineNotify;
import com.iflove.api.user.service.cache.UserCache;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class WSAdapter {

    public static WSBaseResp<String> buildInvalidTokenResp() {
        WSBaseResp<String> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALID_TOKEN.getType());
        resp.setData(WSRespTypeEnum.INVALID_TOKEN.getDesc());
        return resp;
    }

    public static WSBaseResp<WSLoginSuccess> buildWSLoginSuccessResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        WSLoginSuccess wsLoginSuccess = new WSLoginSuccess();
        BeanUtil.copyProperties(user, wsLoginSuccess);
        wsLoginSuccess.setToken(token);
        Date expireTime = new Date(((NumberWithFormat) JWTUtil.parseToken(token).getPayload("expire_time")).longValue() * 1000);
        wsLoginSuccess.setExpireTime(expireTime);
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        resp.setData(wsLoginSuccess);
        return resp;
    }

    public static WSBaseResp<WSOnlineOfflineNotify> buildOnlineNotifyResp(User user) {
        WSBaseResp<WSOnlineOfflineNotify> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        wsBaseResp.setData(WSOnlineOfflineNotify
                .builder()
                .change(buildOnlineInfo(user))
                .onlineNum(SpringUtil.getBean(UserCache.class).getOnlineNum())
                .build());
        return wsBaseResp;
    }

    private static WSChatMemberResp buildOnlineInfo(User user) {
        return WSChatMemberResp
                .builder()
                .uid(user.getId())
                .activeStatus(ChatActiveStatusEnum.ONLINE.getStatus())
                .lastOptTime(user.getLastOptTime())
                .build();
    }

    public static WSBaseResp<WSOnlineOfflineNotify> buildOfflineNotifyResp(User user) {
        WSBaseResp<WSOnlineOfflineNotify> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        wsBaseResp.setData(WSOnlineOfflineNotify
                .builder()
                .change(buildOfflineInfo(user))
                .onlineNum(SpringUtil.getBean(UserCache.class).getOnlineNum())
                .build());
        return wsBaseResp;
    }

    private static WSChatMemberResp buildOfflineInfo(User user) {
        return WSChatMemberResp
                .builder()
                .uid(user.getId())
                .activeStatus(ChatActiveStatusEnum.OFFLINE.getStatus())
                .lastOptTime(user.getLastOptTime())
                .build();
    }
}
