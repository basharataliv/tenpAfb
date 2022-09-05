package com.afba.imageplus.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GETLPOINFODto {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String sexCode;
    private String adress1;
    private String zipcode;
    private String phoneNo;
    private Double childCoverage;
    private String smoker;
    private String agentId;
    private String countryCode;
    private String stateCode;
    private String contractState;
    private String contractCountry;
    private String companyCode;
    private Double coverageAmout;
    private LocalDate applicationDate;
    private Boolean isValidLic;
    private String ssn;
}
