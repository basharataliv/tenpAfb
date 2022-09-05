package com.afba.imageplus.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "policyNo", "pageNo", "fileName", "polType", "printReqType", "issueDate" })
public class LPISSDOCFileDto {
    private String policyNo;
    private Integer pageNo;
    private String fileName;
    private String polType;
    private String printReqType;
    private LocalDate issueDate;

    public void setIssueDate(Integer issueDate) {
        this.issueDate = LocalDate.parse(issueDate.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
