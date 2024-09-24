package com.iflove.api.user.domain.vo.request.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友关系检查请求")
public class FriendCheckReq {
    @NotEmpty
    @Size(max = 50)
    @Schema(description = "校验好友的uid")
    private List<Long> uidList;
}
