package com.afba.imageplus.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "policyNumber", "pageNumber", "tiffName", "billIndicator", "companyCode", "groupBcn", "policyBcn",
        "recordKey", "batchId" })
public class BILLTIFFFileDto {
    private String policyNumber;
    private Integer pageNumber;
    private String tiffName;
    private String billIndicator;
    private String companyCode;
    private String groupBcn;
    private String policyBcn;
    private Integer recordKey;
    private String batchId;
}
