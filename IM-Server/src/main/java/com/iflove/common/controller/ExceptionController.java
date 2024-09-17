package com.iflove.common.controller;

import cn.hutool.json.JSONException;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.CommonErrorEnum;
import com.iflove.common.exception.LoginErrorEnum;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 全局异常捕获器
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {

    /**
     * 用户登录验证失败
     * @param e 报错信息
     * @return 报错结果集
     */
    @ExceptionHandler(BadCredentialsException.class)
    public RestBean<Void> badCredentialException(BadCredentialsException e) {
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        return RestBean.failure(LoginErrorEnum.WRONG_USERNAME_OR_PASSWORD);
    }

    /**
     * 校验不通过打印警告信息
     * @param e 验证异常
     * @return 校验结果
     */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException e) {
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        return RestBean.failure(CommonErrorEnum.PARAM_VALID);
    }
}
