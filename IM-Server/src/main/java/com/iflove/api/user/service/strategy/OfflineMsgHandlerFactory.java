package com.iflove.api.user.service.strategy;

import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import jakarta.validation.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 多类型离线消息处理策略工厂类
 */
public class OfflineMsgHandlerFactory {
    private static final Map<Integer, AbstractOfflineMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractOfflineMsgHandler handler) {
        STRATEGY_MAP.put(code, handler);
    }

    public static AbstractOfflineMsgHandler getStrategyNonNull(Integer code) {
        AbstractOfflineMsgHandler handler = STRATEGY_MAP.get(code);
        if (Objects.isNull(handler)) {
            throw new ValidationException("参数校验失败");
        }
        return handler;
    }
}
