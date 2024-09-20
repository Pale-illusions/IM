package com.iflove.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import com.iflove.common.config.thread.ThreadPoolConfiguration;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.event.UserOfflineEvent;
import com.iflove.common.event.UserOnlineEvent;
import com.iflove.user.dao.UserDao;
import com.iflove.user.domain.dto.WSChannelExtraDTO;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.user.service.WebSocketService;
import com.iflove.user.service.adapter.WSAdapter;
import com.iflove.user.service.cache.UserCache;
import com.iflove.websocket.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import utils.JsonUtil;
import utils.RedisUtil;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * 在线 websocket channel 和 用户 uid 关系 ( 多对1 )
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket ( 多端登录情况下 可能有多个channel   1对多 )
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    @Resource
    private UserDao userDao;
    @Resource
    @Qualifier(ThreadPoolConfiguration.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserCache userCache;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 保存 ws 连接
     * @param channel 连接通道
     */
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    /**
     * ws 断开连接
     * @param channel
     */
    @Override
    public void removed(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        Optional<Long> uidOptional = Optional.ofNullable(wsChannelExtraDTO).map(WSChannelExtraDTO::getUid);
        boolean offlineAll = offline(channel, uidOptional);
        // 用户多端全部下线
        if (uidOptional.isPresent() && offlineAll) {
            User user = new User();
            user.setId(uidOptional.get());
            user.setLastOptTime(new Date());
            applicationEventPublisher.publishEvent(new UserOfflineEvent(this, user));
        }
    }

    /**
     * 删除Map中存储的channel和uid对应关系
     * @param channel
     * @param uidOptional
     * @return
     */
    private boolean offline(Channel channel, Optional<Long> uidOptional) {
        ONLINE_WS_MAP.remove(channel);
        if (uidOptional.isPresent()) {
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uidOptional.get());
            if (CollectionUtil.isNotEmpty(channels)) {
                // 删除对应的channel对象，需要判断equals，因为如果多端登录，可能有多个channel
                channels.removeIf(ch -> Objects.equals(ch, channel));
            }
            // 判断是否全部下线
            return CollectionUtil.isEmpty(ONLINE_UID_MAP.get(uidOptional.get()));
        }
        return true;
    }

    /**
     * 校验登录 token
     * @param channel 连接通道
     * @param token token
     * @return 校验结果
     */
    @Override
    public boolean authorize(Channel channel, String token) {
        try{
            String jwtId = (String) JWTUtil.parseToken(token).getPayload("jwt_id");
            // 校验token是否存在白名单且不在黑名单
            if (!RedisUtil.hasKey(RedisKey.getKey(RedisKey.JWT_BLACK_LIST, jwtId))
                    && RedisUtil.hasKey(RedisKey.getKey(RedisKey.JWT_WHITE_LIST, jwtId))) {
                String username = (String) JWTUtil.parseToken(token).getPayload("username");
                User user = userDao.getUserByName(username);
                this.loginSuccess(channel, user, token);
                return true;
            } else {
                // 登录失败，发送验证失败信息
                this.sendMsg(channel, WSAdapter.buildInvalidTokenResp());
            }
        } catch (JWTException e) {
            // 登录失败，发送验证失败信息
            this.sendMsg(channel, WSAdapter.buildInvalidTokenResp());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 推送消息给指定的对象
     * @param wsBaseResp 消息体
     * @param uid 对象uid
     */
    @Override
    public void sendToUid(WSBaseResp<?> wsBaseResp, Long uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
        if (CollectionUtil.isEmpty(channels)) {
            log.info("用户: {} 不在线", uid);
            return;
        }
        channels.forEach(channel -> {
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    /**
     * 推送消息给所有
     * @param wsBaseResp 消息体
     * @param skipUid 跳过的人
     */
    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid) {
        ONLINE_WS_MAP.forEach(((channel, wsChannelExtraDTO) -> {
            if (Objects.nonNull(skipUid) && Objects.equals(skipUid, wsChannelExtraDTO.getUid())) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        }));
    }

    /**
     * 推送消息给所有
     * @param wsBaseResp 消息体
     */
    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp) {
        this.sendToAllOnline(wsBaseResp, null);
    }

    /**
     * 处理登录成功的逻辑
     * @param channel
     * @param user
     * @param token
     */
    private void loginSuccess(Channel channel, User user, String token) {
        // 更新上线
        this.online(channel, user.getId());
        // 发送登录成功信息
        this.sendMsg(channel, WSAdapter.buildWSLoginSuccessResp(user, token));
        boolean online = userCache.isOnline(user.getId());
        if (!online) {
            user.setLastOptTime(new Date());
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
        }
        // TODO 更新IP

    }

    /**
     * 用户上线
     */
    private void online(Channel channel, Long uid) {
        // 如果不存在则新建，并设置uid
        ONLINE_WS_MAP.computeIfAbsent(channel, k -> new WSChannelExtraDTO()).setUid(uid);
        // 如果不存在则新建，添加channel对象
        ONLINE_UID_MAP.computeIfAbsent(uid, k -> new CopyOnWriteArrayList<>()).add(channel);
        NettyUtil.setAttr(channel, NettyUtil.UID, uid);
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toStr(resp)));
    }
}
