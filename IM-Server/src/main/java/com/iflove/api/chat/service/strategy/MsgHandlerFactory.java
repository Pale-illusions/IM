package com.iflove.api.chat.service.strategy;

import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.CommonErrorEnum;
import jakarta.validation.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 多类型消息处理策略工厂类
 */
public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler handler) {
        STRATEGY_MAP.put(code, handler);
    }

    public static AbstractMsgHandler getStrategyNonNull(Integer code) {
        AbstractMsgHandler handler = STRATEGY_MAP.get(code);
        if (Objects.isNull(handler)) {
            throw new BusinessException(CommonErrorEnum.PARAM_VALID);
        }
        return handler;
    }
}
