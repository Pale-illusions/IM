package com.iflove.oss.controller;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import com.iflove.oss.domain.OssResp;
import com.iflove.oss.domain.vo.request.UploadUrlReq;
import com.iflove.oss.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@RestController
@RequestMapping("/api/oss")
@Tag(name = "oss相关接口")
public class OssController {
    @Resource
    private OssService ossService;

    /**
     * 获取临时上传链接和下载链接
     * @param req 上传申请
     * @return 临时上传链接和下载链接
     */
    @PostMapping("/upload/url")
    @Operation(summary = "获取临时上传链接",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return RestBean.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}