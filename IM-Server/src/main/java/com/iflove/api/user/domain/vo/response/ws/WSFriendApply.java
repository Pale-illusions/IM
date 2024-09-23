package com.iflove.api.user.domain.vo.response.ws;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WSFriendApply {
    // 申请人
    private Long uid;
    // 申请未读数
    private Long unReadCount;
}
