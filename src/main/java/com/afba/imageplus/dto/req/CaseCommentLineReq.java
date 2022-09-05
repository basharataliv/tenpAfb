package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseCommentLineReq {
    @NotBlank(message = "Comment line should not be empty.")
    private String commentLine;
}
