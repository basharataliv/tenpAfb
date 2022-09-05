package com.afba.imageplus.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCaseAddDocumentReq {

    @NotBlank(message = "Document Id cannot be null")
    private String documentId;
    @NotBlank(message = "Policy Id cannot be null")
    private String policyId;

}
