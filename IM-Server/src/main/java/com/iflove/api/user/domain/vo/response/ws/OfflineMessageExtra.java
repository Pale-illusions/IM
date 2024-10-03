package com.iflove.api.user.domain.vo.response.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfflineMessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;

    private ChatMessageResp wsMessage;
    private WSFriendApply wsFriendApply;
    private WSMemberChange wsMemberChange;
    private WSGroupDismissedResp wsGroupDismissedResp;
}
