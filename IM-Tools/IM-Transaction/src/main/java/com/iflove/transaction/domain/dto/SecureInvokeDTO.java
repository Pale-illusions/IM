package com.iflove.transaction.domain.dto;

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
public class SecureInvokeDTO {
    private String className;
    private String methodName;
    private String parameterTypes;
    private String args;
}
