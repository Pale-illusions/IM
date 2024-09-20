package com.iflove.common.utils;

import com.iflove.common.domain.dto.RequestInfo;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 请求上下文
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
