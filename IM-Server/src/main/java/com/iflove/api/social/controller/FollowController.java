package com.iflove.api.social.controller;

import com.iflove.api.social.service.FollowService;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
@RequestMapping("api/social")
@Tag(name = "社交模块")
public class FollowController {
    @Resource
    private FollowService followService;

    /**
     * 关注
     * @param targetId 关注对象
     * @return {@link RestBean}
     */
    @PutMapping("subscribe/{targetId}")
    @Operation(summary = "关注",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> subscribe(@NotNull @PathVariable Long targetId) {
        Long uid = RequestHolder.get().getUid();
        return followService.subscribe(uid, targetId);
    }

    /**
     * 取关
     * @param targetId 取关对象
     * @return {@link RestBean}
     */
    @PutMapping("unsubscribe/{targetId}")
    @Operation(summary = "取关",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
    })
    public RestBean<Void> unsubscribe(@NotNull @PathVariable Long targetId) {
        Long uid = RequestHolder.get().getUid();
        return followService.unsubscribe(uid, targetId);
    }


}
