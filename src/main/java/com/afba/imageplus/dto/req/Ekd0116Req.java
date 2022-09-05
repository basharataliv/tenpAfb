package com.afba.imageplus.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Ekd0116Req {
    private String caseId;
    private String policyId;
    @NotBlank(message = "target Queue should not be empty.")
    private String targetQueue;
}
