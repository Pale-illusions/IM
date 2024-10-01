package com.iflove.transaction.service;

import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class SecureInvokeHolder {
    private static final ThreadLocal<Boolean> INVOKE_THREAD_LOCAL = new ThreadLocal<>();

    public static boolean isInvoking() {
        return Objects.nonNull(INVOKE_THREAD_LOCAL.get());
    }

    public static void setInvoking() {
        INVOKE_THREAD_LOCAL.set(Boolean.TRUE);
    }

    public static void invoked() {
        INVOKE_THREAD_LOCAL.remove();
    }
}
