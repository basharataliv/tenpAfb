package com.afba.imageplus.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostPolicyPROTRMPOLReq {

    @NotBlank(message = "Policy id cannot be null or empty")
    private String extPolId;

    @NotBlank(message = "New Policy id cannot be null or empty")
    private String newPolId;

    @NotBlank(message = "Policy type cannot be null or empty")
    private String extPolType;

    @NotBlank(message = "Policy status cannot be null or empty")
    private String extPolStat;

    @NotNull(message = "Policy coverage amount can not be null or empty")
    private Double covAmt;

    @NotNull(message = "Is Terminated flag can only be true or false")
    private Boolean isTerminated;

}
