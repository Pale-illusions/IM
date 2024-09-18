package com.iflove.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.jwt.JWTUtil;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.enums.WSRespTypeEnum;
import com.iflove.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.user.domain.vo.response.ws.WSLoginSuccess;
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
}
