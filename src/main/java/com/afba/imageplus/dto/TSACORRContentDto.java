package com.afba.imageplus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TSACORRContentDto {
    private String fileName;
    private String ssn;
    private String docType;
    private String pages;

}
