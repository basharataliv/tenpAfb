package com.afba.imageplus.dto;

import com.afba.imageplus.dto.res.CaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CaseQueueDto {
    private String queueId;
    private String priority;
    private LocalDate scanDate;
    private LocalTime scanTime;
    private String caseId;
    private LocalDate queueDate;
    private LocalTime queueTime;
    private LocalDateTime scanDateTime;
    private LocalDateTime queueDateTime;
    @JsonProperty("case")
    private CaseResponse ekdCase;
}
