package com.afba.imageplus.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryReq {

    private MultipartFile doc;

    @NotBlank(message = "Document Type cannot be null")
    private String documentType;

    @ApiModelProperty(hidden = true)
    private LocalDate scanningDate;

    @ApiModelProperty(hidden = true)
    private LocalTime scanningTime;

    private String scanningRepId;

    private String userLastUpdate;

    private String reqListDescription;

    @Min(value = 1, message = "Document pages cannot be less than 1")
    private Integer docPage;

    @NotBlank(message = "Policy Id cannot be null")
    private String policyId;

}
