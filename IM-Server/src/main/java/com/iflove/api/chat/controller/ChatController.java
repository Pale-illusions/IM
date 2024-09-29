package com.iflove.api.chat.controller;

import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.chat.service.ChatService;
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
 * @implNote
 */
@Validated
@RestController
@RequestMapping("api/chat")
@Tag(name = "聊天模块")
public class ChatController {
    @Resource
    private ChatService chatService;

    /**
     * 发送消息
     * @param request 消息请求体
     * @return {@link RestBean}<{@link ChatMessageResp}
     */
    @PostMapping("msg")
    @Operation(summary = "发送消息",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq request) {
        Long msgId = chatService.sendMsg(request, RequestHolder.get().getUid());
        // 消息返回逻辑
        return RestBean.success(chatService.getMsgResp(msgId));
    }

}
