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
@JsonPropertyOrder({ "policyNo", "pageNo", "fileName", "docType", "description" })
public class CORRTIFFFileDto {
    private String policyNo;
    private Integer pageNo;
    private String fileName;
    private String docType;
    private String description;

}
