package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBenefitSummaryRes {
    @JsonProperty("IssueDate")
    private Integer issueDate;
    @JsonProperty("SexCode")
    private String sexCode;
    @JsonProperty("DateOfBirth")
    private Integer dateOfBirth;
    @JsonProperty("IssueAge")
    private Integer issueAge;
    @JsonProperty("UnderwritingClass")
    private String underwritingClass;
    @JsonProperty("NumberOfUnits")
    private Double numberOfUnits;
    @JsonProperty("ModePremium")
    private Double modePremium;
    @JsonProperty("FaceAmount")
    private Double faceAmount;
    @JsonProperty("Benefit")
    private Benefit benefit;
    @JsonProperty("Coverage")
    private Coverage coverage;
    @JsonProperty("Status")
    private Status status;
}
