package com.afba.imageplus.dto.req;

import com.afba.imageplus.annotation.EnumValueConstraint;
import com.afba.imageplus.annotation.RLSEMSIWTConstraint;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RLSEMSIWTConstraint(
        callingProgram = "callingProgram",
        doFsroOverride = "doFsroOverride",
        policyId = "policyId"
)
public class UnPendCaseReq {
    String userId;
    @EnumValueConstraint(enumClass = UnPendCallingProgram.class)
    String callingProgram;
    Boolean emsiUser;
    String caseId;
    String policyId;
    Boolean doFsroOverride;
}
