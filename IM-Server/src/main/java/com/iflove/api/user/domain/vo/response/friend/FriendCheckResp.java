package com.iflove.api.user.domain.vo.response.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友关系检查返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友关系检查返回")
public class FriendCheckResp {
    @Schema(description = "校验结果")
    private List<FriendCheck> checkedList;

    @Data
    public static class FriendCheck {
        private Long uid;
        private Boolean isFriend;
    }
}
