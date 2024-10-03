package com.iflove.api.chat.controller;

import com.iflove.api.chat.domain.vo.request.admin.AdminAddReq;
import com.iflove.api.chat.domain.vo.request.admin.AdminRevokeReq;
import com.iflove.api.chat.domain.vo.request.member.GroupCreateReq;
import com.iflove.api.chat.domain.vo.request.member.MemberAddReq;
import com.iflove.api.chat.domain.vo.request.member.MemberDelReq;
import com.iflove.api.chat.domain.vo.request.member.MemberPageReq;
import com.iflove.api.chat.domain.vo.response.ChatMemberResp;
import com.iflove.api.chat.service.RoomAppService;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
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
 * @implNote 房间相关接口
 */
@Validated
@RestController
@RequestMapping("api/room")
@Tag(name = "聊天室模块")
public class RoomController {
    @Resource
    private RoomAppService roomService;

    // TODO ，，，退出群聊，，群组详情，解散群聊
    // 已完成: 添加成员 移除成员 群成员列表 添加管理，撤销管理员

    /**
     * 创建群聊
     * @param req 创建请求
     * @return {@link RestBean}<{@link Long}
     */
    @PostMapping("group")
    @Operation(summary = "创建群聊",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Long> createGroup(@Valid @RequestBody GroupCreateReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.createGroup(req, uid);
    }

    /**
     * 邀请成员
     * @param req 邀请成员请求体
     * @return {@link RestBean}
     */
    @PostMapping("group/member")
    @Operation(summary = "邀请成员",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> addMember(@Valid @RequestBody MemberAddReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.addMember(req, uid);
    }

    /**
     * 删除成员
     * @param req 删除成员请求体
     * @return {@link RestBean}
     */
    @DeleteMapping("group/member")
    @Operation(summary = "删除成员",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> delMember(@Valid @RequestBody MemberDelReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.delMember(req, uid);
    }

    /**
     * 成员列表
     * @param req 成员列表请求体
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatMemberResp}
     */
    @GetMapping("group/member/page")
    @Operation(summary = "成员列表",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<CursorPageBaseResp<ChatMemberResp>> getMemberPage(@Valid MemberPageReq req) {
        return roomService.getMemberPage(req);
    }

    /**
     * 添加管理员
     * @param req 添加管理员请求体
     * @return {@link RestBean}
     */
    @PutMapping("group/admin")
    @Operation(summary = "添加管理员",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> addAdmin(@Valid @RequestBody AdminAddReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.addAdmin(uid, req);
    }

    /**
     * 撤销管理员
     * @param req 撤销管理员请求体
     * @return {@link RestBean}
     */
    @DeleteMapping("group/admin")
    @Operation(summary = "撤销管理员",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> revokeAdmin(@Valid @RequestBody AdminRevokeReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.revokeAdmin(uid, req);
    }
}
