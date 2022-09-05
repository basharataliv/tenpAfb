package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QcRunTimCheckReq {

    @NotBlank(message = "User id cannot be empty")
    @Size(min = 1, max = 10, message = "Length of repId must be between 1-10")
    private String userId;

    @NotNull(message = "Case id cannot be empty")
    private String caseId;

    @NotBlank(message = "Policy id cannot be empty")
    @Size(max = 11, message = "Length of policy id cannot exceed 11")
    private String policyId;

    @NotBlank(message = "Document type cannot be empty")
    private String documentType;
}
