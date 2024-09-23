package com.iflove.api.user.controller;

import com.iflove.api.user.domain.vo.request.friend.FriendApplyApproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyDisapproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.request.friend.FriendDeleteReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyUnreadResp;
import com.iflove.api.user.domain.vo.response.friend.FriendInfoResp;
import com.iflove.api.user.service.FriendService;
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
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@RestController
@RequestMapping("api/user/friend")
@Validated
@Tag(name = "好友模块")
public class FriendController {
    @Resource
    FriendService friendService;

    /**
     * 申请好友
     * @param request 请求
     * @return {@link RestBean}
     */
    @PostMapping("apply")
    @Operation(summary = "好友申请",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> apply(@Valid @RequestBody FriendApplyReq request) {
        return friendService.apply(RequestHolder.get().getUid(), request);
    }

    /**
     * 获取好友申请列表 (基础分页)
     * @param request 基础翻页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FriendApplyResp}>
     */
    @GetMapping("apply/page")
    @Operation(summary = "好友申请列表 (基础分页)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<PageBaseResp<FriendApplyResp>> page(@Valid PageBaseReq request) {
        return friendService.pageApplyFriend(RequestHolder.get().getUid(), request);
    }

    /**
     * 好友申请未读数
     * @return {@link RestBean}
     */
    @GetMapping("apply/unread")
    @Operation(summary = "好友申请未读数",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<FriendApplyUnreadResp> unread() {
        return friendService.unread(RequestHolder.get().getUid());
    }

    /**
     * 同意好友申请
     * @param req 好友申请同意请求
     * @return {@link RestBean}
     */
    @PutMapping("apply/approve")
    @Operation(summary = "同意好友申请",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> applyApprove(@Valid @RequestBody FriendApplyApproveReq req) {
        return friendService.applyApprove(RequestHolder.get().getUid(), req);
    }

    /**
     * 拒绝好友申请
     * @param req 好友申请拒绝请求
     * @return {@link RestBean}
     */
    @PutMapping("apply/disapprove")
    @Operation(summary = "拒绝好友申请",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> applyDisapprove(@Valid @RequestBody FriendApplyDisapproveReq req) {
        return friendService.applyDisapprove(RequestHolder.get().getUid(), req);
    }

    /**
     * 删除好友
     * @param req 好友删除请求
     * @return {@link RestBean}
     */
    @DeleteMapping("delete")
    @Operation(summary = "删除好友",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> delete(@Valid @RequestBody FriendDeleteReq req) {
        return friendService.deleteFriend(RequestHolder.get().getUid(), req.getTargetUid());
    }

    /**
     * 好友列表 (游标分页)
     * @param req 好友删除请求
     * @return {@link RestBean}
     */
    @GetMapping("page")
    @Operation(summary = "好友列表 (游标分页)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<CursorPageBaseResp<FriendInfoResp>> friendList(@Valid CursorPageBaseReq req) {
        return friendService.friendList(RequestHolder.get().getUid(), req);
    }
}
