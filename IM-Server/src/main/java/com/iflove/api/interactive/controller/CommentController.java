package com.iflove.api.interactive.controller;

import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.domain.vo.request.CommentPageReq;
import com.iflove.api.interactive.domain.vo.request.CommentPublishReq;
import com.iflove.api.interactive.service.CommentService;
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
@RequestMapping("api/interactive/comment")
@Tag(name = "互动模块")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 评论
     * @param req 评论请求
     * @return {@link RestBean}
     */
    @PostMapping("publish")
    @Operation(summary = "评论",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> publish(@Valid @RequestBody CommentPublishReq req) {
        Long uid = RequestHolder.get().getUid();
        return commentService.publish(uid, req);
    }

    /**
     * 评论列表
     * @param req 评论列表请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link Comment}
     */
    @GetMapping("list")
    @Operation(summary = "评论列表",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<PageBaseResp<Comment>> listComment(@Valid CommentPageReq req) {
        return commentService.listComment(req);
    }
}
