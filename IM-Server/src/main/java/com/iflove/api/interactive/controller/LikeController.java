package com.iflove.api.interactive.controller;

import com.iflove.api.interactive.domain.vo.request.VideoActionReq;
import com.iflove.api.interactive.domain.vo.response.ActionListResp;
import com.iflove.api.interactive.service.LikeService;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Validated
@RestController
@RequestMapping("api/interactive/like")
@Tag(name = "点赞模块")
public class LikeController {
    @Resource
    private LikeService likeService;

    /**
     * 点赞/点踩
     * @param req 视频点赞点踩请求
     * @return {@link RestBean}
     */
    @PutMapping("mark")
    @Operation(summary = "点赞/点踩",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> mark(@Valid @RequestBody VideoActionReq req) {
        Long uid = RequestHolder.get().getUid();
        return likeService.mark(uid, req);
    }

    /**
     * 点赞列表
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ActionListResp}
     */
    @GetMapping("like/list")
    @Operation(summary = "点赞列表(游标分页)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<CursorPageBaseResp<ActionListResp>> likeList(@Valid CursorPageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return likeService.likeList(uid, req);
    }

    /**
     * 点踩列表
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ActionListResp}
     */
    @GetMapping("unlike/list")
    @Operation(summary = "点踩列表(游标分页)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<CursorPageBaseResp<ActionListResp>> unlikeList(@Valid CursorPageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return likeService.unlikeList(uid, req);
    }
}
