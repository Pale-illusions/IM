package com.iflove.common.service.cache;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.data.util.Pair;
import utils.RedisUtil;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote redis string类型的批量缓存框架
 */
public abstract class AbstractRedisStringCache<IN, OUT> implements BatchCache<IN, OUT> {

    private Class<OUT> outClass;

    /**
     * 动态获取 OUT 的具体类型
     */
    protected AbstractRedisStringCache() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.outClass = (Class<OUT>) genericSuperclass.getActualTypeArguments()[1];
    }

    /**
     * 将请求参数映射为 Redis 对应的 Key
     */
    protected abstract String getKey(IN req);

    /**
     * 获得缓存过期时间
     * @return 过期时间(秒)
     */
    protected abstract Long getExpireSeconds();

    /**
     * 缓存不存在，查询数据库
     * @param req
     * @return
     */
    protected abstract Map<IN, OUT> load(List<IN> req);

    /**
     * 单个获取
     * @param req
     * @return
     */
    @Override
    public OUT get(IN req) {
        return getBatch(Collections.singletonList(req)).get(req);
    }

    /**
     * 批量获取
     * @param req
     * @return
     */
    @Override
    public Map<IN, OUT> getBatch(List<IN> req) {
        // 空请求直接返回
        if (CollectionUtil.isEmpty(req)) {
            return new HashMap<>();
        }
        // 去重
        req = req.stream().distinct().collect(Collectors.toList());
        // 请求参数映射为 Redis Key
        List<String> keys = req.stream().map(this::getKey).toList();
        // 批量 get
        List<OUT> valueList = RedisUtil.mget(keys, outClass);
        // 差集计算，找到缓存中不存在的数据，批量查询数据库
        List<IN> loadReqs = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            if (Objects.isNull(valueList.get(i))) {
                loadReqs.add(req.get(i));
            }
        }
        Map<IN, OUT> load = new HashMap<>();
        // 批量查询数据库，并加入 Redis 缓存
        if (CollectionUtil.isNotEmpty(loadReqs)) {
            // 批量load
            load = load(loadReqs);
            // 映射为 Redis 键值形式，加入 Redis 缓存
            Map<String, OUT> loadMap = load.entrySet().stream()
                    .map(s -> Pair.of((this.getKey(s.getKey())), s.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            // 批量缓存，并设置过期时间
            RedisUtil.mset(loadMap, getExpireSeconds());
        }
        // 组装最后的结果
        Map<IN, OUT> resultMap = new HashMap<>();
        for (int i = 0; i < req.size(); i++) {
            IN in = req.get(i);
            OUT out = Optional.ofNullable(valueList.get(i))
                    .orElse(load.get(in));
            resultMap.put(in, out);
        }
        return resultMap;
    }

    /**
     * 单个删除
     * @param req
     */
    @Override
    public void delete(IN req) {
        deleteBatch(Collections.singletonList(req));
    }

    /**
     * 批量删除
     * @param req
     */
    @Override
    public void deleteBatch(List<IN> req) {
        List<String> keys = req.stream().map(this::getKey).toList();
        RedisUtil.del(keys);
    }
}
