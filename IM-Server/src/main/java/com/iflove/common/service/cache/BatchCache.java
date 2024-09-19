package com.iflove.common.service.cache;

import java.util.List;
import java.util.Map;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 批量缓存框架接口
 */
public interface BatchCache<IN, OUT> {

    /**
     * 单个获取
     * @param req
     * @return
     */
    OUT get(IN req);

    /**
     * 批量获取
     * @param req
     * @return
     */
    Map<IN, OUT> getBatch(List<IN> req);

    /**
     * 单个删除
     * @param req
     */
    void delete(IN req);

    /**
     * 批量删除
     * @param req
     */
    void deleteBatch(List<IN> req);
}
