package com.iflove.api.user.controller;

import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.service.FriendService;
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
@RestController
@RequestMapping("api/friend")
@Validated
@Tag(name = "好友模块")
public class FriendController {

    @Resource
    FriendService friendService;

    /**
     * 申请好友
     * @param request 请求
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
}
