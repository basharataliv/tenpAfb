package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDocumentRes {

    private String caseId;
    private String documentId;
    private String dasdFlag;
    private LocalDate scanningDate;
    private LocalTime scanningTime;
    private String scanningUser;
    private String itemType;
    private String itemInit;
    private String filler;
    private LocalDateTime scanningDateTime;
}
