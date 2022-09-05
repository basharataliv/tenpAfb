package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseCommentReq {
    @NotEmpty(message = "At least one comment must be provided.")
    @Valid
    private LinkedHashSet<CaseCommentLineReq> commentLines;

}
