package com.afba.imageplus.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WorkCaseReq {

    @NotBlank(message = "userId should not be empty.")
    String userId;
}
