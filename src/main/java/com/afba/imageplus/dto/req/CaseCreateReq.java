package com.afba.imageplus.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseCreateReq {

    @JsonIgnore
    private String caseId;
    private String initialQueueId;
    private String initialRepId;
    private String lastRepId;

    @JsonIgnore
    private LocalDate scanningDate;

    @JsonIgnore
    private LocalTime scanningTime;
    @NotBlank(message = "cmAccountNumber cannot be null")
    private String cmAccountNumber;
    private String cmFormattedName;
    private String chargeBackFlag;
    private String filler;

    // Annotation to hide this field from swagger.
    @ApiModelProperty(hidden = true)
    private LocalDateTime scanningDateTime = LocalDateTime.now();
    @ApiModelProperty(hidden = true)
    private LocalDateTime caseCloseDateTime;
    @ApiModelProperty(hidden = true)
    private LocalDateTime lastUpdateDateTime = LocalDateTime.now();
}
