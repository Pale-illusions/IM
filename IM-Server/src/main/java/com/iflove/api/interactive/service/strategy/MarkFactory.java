package com.iflove.api.interactive.service.strategy;

import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.CommonErrorEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class MarkFactory {
    private static final Map<Integer, AbstractMarkStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer markType, AbstractMarkStrategy strategy) {
        STRATEGY_MAP.put(markType, strategy);
    }

    public static AbstractMarkStrategy getStrategyNoNull(Integer markType) {
        AbstractMarkStrategy strategy = STRATEGY_MAP.get(markType);
        if (Objects.isNull(strategy)) {
            throw new BusinessException(CommonErrorEnum.PARAM_VALID);
        }
        return strategy;
    }
}
