package com.afba.imageplus.dto.req;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingReq {

    @Size(max = 11, message = "Length of policy id cannot exceed 11")
    private String policyId;

    @NotBlank(message = "SSN can not be null or empty")
    @Size(min = 9, max = 9, message = "Length of ssn must be 9")
    private String ssn;

    @NotBlank(message = "Policy type can not be null or empty")
    @Size(max = 4, message = "Length of policy type cannot exceed 4")
    private String policyType;

    @NotNull(message = "Age can not be null")
    @Min(value = 0, message = "Age cannot be less than 0")
    @Max(value = 99, message = "Age cannot be greater than 99")
    private Integer age;

    @NotNull(message = "App coverage amount can not be null")
    @Digits(integer = 4, fraction = 3, message = "App coverage amount can be 4 digit integer and 3 digit decimal")
    private Double appCoverageAmount;

    @NotNull(message = "Acive flag can not be null")
    @Min(value = 0, message = "Acive flag can only be 0 or 1")
    @Max(value = 1, message = "Acive flag can only be 0 or 1")
    private Integer activeFlag;

    @NotNull(message = "Deploy flag can only be true or false")
    private Boolean deployFlag;

    @NotNull(message = "LT ESP flag can only be true or false")
    private Boolean ltEspFlag;

    @NotBlank(message = "Template name can not be null or empty")
    @Size(max = 10, message = "Length of template name cannot exceed 10")
    private String templateName;

}
