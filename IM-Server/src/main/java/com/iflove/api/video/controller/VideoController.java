package com.iflove.api.video.controller;

import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.api.video.service.VideoService;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Validated
@RestController
@RequestMapping("api/video")
@Tag(name = "视频模块")
public class VideoController {
    @Resource
    private VideoService videoService;

    /**
     * 发布视频
     * @param req 发布视频请求
     * @return {@link RestBean}
     */
    @PostMapping("publish")
    @Operation(summary = "发布视频",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> publish(@Valid @RequestBody PublishReq req) {
        Long uid = RequestHolder.get().getUid();
        return videoService.publish(uid, req);
    }

    /**
     * 浏览视频
     * @param videoId 视频id
     * @return {@link RestBean}<{@link VideoInfoResp}
     */
    @GetMapping("browse/{videoId}")
    @Operation(summary = "浏览视频",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<VideoInfoResp> browse(@NotNull @PathVariable Long videoId) {
        return videoService.browse(videoId);
    }
}
