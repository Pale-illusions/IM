package com.iflove.common.domain.vo.response;

import com.iflove.common.exception.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Schema(description = "基础返回体")
public record RestBean<T> (int code, T data, String message) {

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "success");
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, null, "success");
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    public static <T> RestBean<T> failure(ErrorEnum errorEnum) {
        return new RestBean<>(errorEnum.getErrorCode(), null, errorEnum.getErrorMsg());
    }
}