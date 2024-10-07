package com.iflove.common.handler;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.CommonErrorEnum;
import com.iflove.common.exception.UserErrorEnum;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 全局异常捕获器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义业务异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BusinessException.class)
    public RestBean<Void> businessExceptionHandler(BusinessException e) {
        log.warn("business exception！The reason is：{}", e.getMessage(), e);
        return RestBean.failure(e.getErrorCode(), e.getMessage());
    }

    /**
     * 用户登录验证失败
     * @param e 报错信息
     * @return 报错结果集
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadCredentialsException.class)
    public RestBean<Void> badCredentialException(BadCredentialsException e) {
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        return RestBean.failure(UserErrorEnum.WRONG_USERNAME_OR_PASSWORD);
    }

    /**
     * 参数校验不通过打印警告信息
     * @param e 验证异常
     * @return 校验结果
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ValidationException.class)
    public RestBean<Void> badCredentialException(ValidationException e) {
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        return RestBean.failure(502, e.getMessage());
    }

    /**
     * 参数校验不通过打印警告信息
     * @param e 验证异常
     * @return 校验结果
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public RestBean<Void> bindException(BindException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        log.warn("validation parameters error！The reason is:{}", message);
        return RestBean.failure(CommonErrorEnum.PARAM_VALID.getErrorCode(), message.substring(0, message.length() - 1));
    }

    /**
     * 参数校验不通过打印警告信息
     * @param e 验证异常
     * @return 校验结果
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RestBean<Void> validateException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        log.warn("validation parameters error！The reason is:{}", message);
        return RestBean.failure(CommonErrorEnum.PARAM_VALID.getErrorCode(), message.substring(0, message.length() - 1));
    }

    /**
     * 处理空指针的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NullPointerException.class)
    public RestBean<Void> exceptionHandler(NullPointerException e) {
        log.error("null point exception！The reason is: ", e);
        return RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
    }

    /**
     * 未知异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public RestBean<Void> systemExceptionHandler(Exception e) {
        log.error("system exception！The reason is：{}", e.getMessage(), e);
        return RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
    }
}
