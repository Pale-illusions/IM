package com.iflove.api.user.domain.vo.request.ws;

import com.iflove.api.user.domain.enums.WSReqTypeEnum;
import lombok.Data;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 前端请求基本类型
 */
@Data
public class WSBaseReq {
    /**
     * 请求类型 1.心跳检测
     * 可做后续扩展
     *
     * @see WSReqTypeEnum
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}