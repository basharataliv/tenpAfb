package com.afba.imageplus.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseDocumentsDto {
    private String caseId;
    private String documentType;
    private String documentId;
    private String spDdocumentSitId;
    private String spDocumentlibraryId;
    private String spDocumentId;
    private LocalDate scanningDate;
}
