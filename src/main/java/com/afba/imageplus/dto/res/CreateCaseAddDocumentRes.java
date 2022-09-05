package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCaseAddDocumentRes {

    private String caseId;
    private String documentId;
}
