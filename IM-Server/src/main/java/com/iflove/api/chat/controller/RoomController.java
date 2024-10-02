package com.iflove.api.chat.controller;

import com.iflove.api.chat.domain.vo.request.GroupCreateReq;
import com.iflove.api.chat.service.GroupMemberService;
import com.iflove.api.chat.service.RoomAppService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Resource
    private GroupMemberService groupMemberService;

    // TODO 添加管理，撤销管理员，移除成员，添加成员，退出群聊，群成员列表，群组详情，解散群聊

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
}
