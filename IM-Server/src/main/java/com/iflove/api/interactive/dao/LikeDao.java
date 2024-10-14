package com.iflove.api.interactive.dao;

import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import com.iflove.common.constant.RedisKey;
import utils.RedisUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class LikeDao {

    public static boolean exist(Long uid, Long videoId, MarkTypeEnum markType) {
        String key = mapId2Key(uid, videoId);
        // 根据标记类型判断
        // 点赞
        if (Objects.equals(markType, MarkTypeEnum.LIKE)) {
            return RedisUtil.hHasKey(RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION), key);
        }
        // 点踩
        if (Objects.equals(markType, MarkTypeEnum.DISLIKE)) {
            return RedisUtil.hHasKey(RedisKey.getKey(RedisKey.VIDEO_DISLIKE_RELATION), key);
        }
        return false;
    }

    public static void update(Long uid, Long videoId, MarkTypeEnum markType, boolean exist) {
        String key = mapId2Key(uid, videoId);

        // 选择相应的 Redis 键和分数变化量
        String relationKey = null;
        String countKey = null;
        int scoreChange = exist ? -1 : 1;

        if (Objects.equals(markType, MarkTypeEnum.LIKE)) {
            relationKey = RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION);
            countKey = RedisKey.getKey(RedisKey.VIDEO_LIKE_COUNT);
        } else if (Objects.equals(markType, MarkTypeEnum.DISLIKE)) {
            relationKey = RedisKey.getKey(RedisKey.VIDEO_DISLIKE_RELATION);
            countKey = RedisKey.getKey(RedisKey.VIDEO_DISLIKE_COUNT);
        }

        // 如果关系键和计数键都存在，则进行操作
        if (relationKey != null && countKey != null) {
            if (exist) {
                // 存在则删除关系
                RedisUtil.hdel(relationKey, key);
            } else {
                // 不存在则新增关系
                RedisUtil.hset(relationKey, key, "1");
            }

            // 更新分数并根据分数变化判断是否成功
            RedisUtil.zIncrementScore(countKey, videoId.toString(), scoreChange);
        }
    }

    private static String mapId2Key(Long uid, Long videoId) {
        return uid + "::" + videoId;
    }

}
