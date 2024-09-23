package com.iflove.api.user.domain.vo.response.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友申请未读返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友申请未读返回")
public class FriendApplyUnreadResp {
    @Schema(description = "未读数量")
    private Long unReadCount;
}
