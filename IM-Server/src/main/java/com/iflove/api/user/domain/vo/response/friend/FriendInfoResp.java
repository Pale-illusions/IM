package com.iflove.api.user.domain.vo.response.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友信息返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友信息返回")
public class FriendInfoResp {
    @Schema(description = "好友uid")
    private Long uid;

    /**
     * 在线状态
     * @see com.iflove.api.user.domain.enums.ChatActiveStatusEnum
     */
    @Schema(description = "0 在线 / 1 下线")
    private Integer activeStatus;
}
