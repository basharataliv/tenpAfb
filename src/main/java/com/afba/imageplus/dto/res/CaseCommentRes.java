package com.afba.imageplus.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseCommentRes {
    private Long commentKey;
    private String caseId;
    private Set<CaseCommentLineRes> commentLines;
    private String userId;
    private LocalDate commentDate;
    private LocalTime commentTime;
    private Integer isDeleted;
    private LocalDateTime commentDateTime;
}
