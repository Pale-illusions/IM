package com.iflove.api.interactive.dao;

import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import com.iflove.common.constant.RedisKey;
import utils.RedisUtil;

import java.util.List;
import java.util.Objects;

import static com.iflove.common.constant.RedisKey.VIDEO_DISLIKE_RELATION;
import static com.iflove.common.constant.RedisKey.VIDEO_LIKE_RELATION;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class LikeDao {

    public static boolean exist(Long uid, Long videoId, MarkTypeEnum markType) {
        // 点赞
        if (Objects.equals(markType, MarkTypeEnum.LIKE)) {
            return RedisUtil.zIsMember(RedisKey.getKey(VIDEO_LIKE_RELATION, uid), videoId);
        }
        // 点踩
        if (Objects.equals(markType, MarkTypeEnum.DISLIKE)) {
            return RedisUtil.zIsMember(RedisKey.getKey(VIDEO_DISLIKE_RELATION, uid), videoId);
        }
        return false;
    }

    public static void update(Long uid, Long videoId, MarkTypeEnum markType, boolean exist) {
        // 选择相应的 Redis 键和分数变化量
        String relationKey = null;
        String countKey = null;
        int scoreChange = exist ? -1 : 1;

        if (Objects.equals(markType, MarkTypeEnum.LIKE)) {
            relationKey = RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION, uid);
            countKey = RedisKey.getKey(RedisKey.VIDEO_LIKE_COUNT);
        } else if (Objects.equals(markType, MarkTypeEnum.DISLIKE)) {
            relationKey = RedisKey.getKey(VIDEO_DISLIKE_RELATION, uid);
            countKey = RedisKey.getKey(RedisKey.VIDEO_DISLIKE_COUNT);
        }

        // 如果关系键和计数键都存在，则进行操作
        if (relationKey != null && countKey != null) {
            if (exist) {
                // 存在则删除关系
                RedisUtil.zRemove(relationKey, videoId);
            } else {
                // 不存在则新增关系
                RedisUtil.zAdd(relationKey, videoId, System.currentTimeMillis());
            }
            // 更新分数并根据分数变化判断是否成功
            RedisUtil.zIncrementScore(countKey, videoId.toString(), scoreChange);
        }
    }
}
