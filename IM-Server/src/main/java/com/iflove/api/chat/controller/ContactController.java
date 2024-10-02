package com.iflove.api.chat.controller;

import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.api.chat.service.RoomAppService;
import com.iflove.api.chat.service.RoomService;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 会话相关接口
 */
@Validated
@RestController
@RequestMapping("api/chat/contact")
@Tag(name = "会话模块")
public class ContactController {
    @Resource
    private RoomAppService roomService;

    /**
     * 会话列表
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatRoomResp}
     */
    @GetMapping("page")
    @Operation(summary = "会话列表",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<CursorPageBaseResp<ChatRoomResp>> getContactPage(@Valid CursorPageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return roomService.getContactPage(req, uid);
    }

    /**
     * 会话详情(房间id)
     * @param roomId 房间id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    @GetMapping("detail/{roomId}")
    @Operation(summary = "会话详情(房间id)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<ChatRoomResp> getContactDetail(@PathVariable Long roomId) {
        Long uid = RequestHolder.get().getUid();
        return roomService.getContactDetail(uid, roomId);
    }

    /**
     * 会话详情(好友id)
     * @param friendId 好友id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    @GetMapping("detail/friend/{friendId}")
    @Operation(summary = "会话详情(好友id)",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<ChatRoomResp> getContactDetailByFriend(@PathVariable Long friendId) {
        Long uid = RequestHolder.get().getUid();
        return roomService.getContactDetailByFriend(uid, friendId);
    }
}
