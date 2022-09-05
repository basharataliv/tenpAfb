package com.afba.imageplus.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDocumentReq {
    @NotNull(message = "caseId cannot be null", groups = {Insert.class, Update.class})
    private String caseId;
    @NotBlank(message = "documentId cannot be null", groups = {Insert.class, Update.class})
    private String documentId;

}
