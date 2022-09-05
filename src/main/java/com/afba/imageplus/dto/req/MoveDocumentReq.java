package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveDocumentReq {
    @NotBlank(message = "documentId cannot  be empty or null")
    String documentId;
    @NotBlank(message = "Existing caseId can not be empty or null")
    String existingCaseId;
    @NotBlank(message = "Target case id must not be empty or null")
    String targetCaseId;
}
