package com.iflove.user.service.cache;

import com.iflove.common.constant.RedisKey;
import com.iflove.user.dao.UserDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import utils.RedisUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户相关缓存
 */
@Component
public class UserCache {

    /**
     * 获得在线人数数量
     * @return 在线人数
     */
    public Long getOnlineNum() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        return RedisUtil.zCard(onlineKey);
    }

    /**
     * 获得离线人数数量
     * @return 离线人数
     */
    public Long getOfflineNum() {
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZSET);
        return RedisUtil.zCard(offlineKey);
    }

    /**
     * 移除用户
     * @param uid uid
     */
    public void remove(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZSET);
        //移除离线表
        RedisUtil.zRemove(offlineKey, uid);
        //移除上线表
        RedisUtil.zRemove(onlineKey, uid);
    }

    /**
     * 获取在线用户列表
     * @return 在线用户列表
     */
    public List<Long> getOnlineUidList() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        Set<String> strings = RedisUtil.zAll(onlineKey);
        return strings.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    /**
     * 用户上线
     * @param uid uid
     * @param optTime 操作时间
     */
    public void online(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZSET);
        //移除离线表
        RedisUtil.zRemove(offlineKey, uid);
        //更新上线表
        RedisUtil.zAdd(onlineKey, uid, optTime.getTime());
    }

    /**
     * 判断用户是否在线
     * @param uid uid
     * @return 是否在线
     */
    public boolean isOnline(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        return RedisUtil.zIsMember(onlineKey, uid);
    }

    /**
     * 用户离线
     * @param uid uid
     * @param optTime 操作时间
     */
    public void offline(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZSET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZSET);
        //移除上线表
        RedisUtil.zRemove(onlineKey, uid);
        //更新离线表
        RedisUtil.zAdd(offlineKey, uid, optTime.getTime());
    }

}
