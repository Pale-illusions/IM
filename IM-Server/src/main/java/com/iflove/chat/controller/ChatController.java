package com.iflove.chat.controller;

import com.iflove.common.domain.vo.response.RestBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@RestController
@RequestMapping("api/chat")
@Tag(name = "聊天模块")
public class ChatController {

//    @PostMapping("chat")
//    @Operation(summary = "发送消息")
//    public RestBean sendMs() {
//
//    }
}
