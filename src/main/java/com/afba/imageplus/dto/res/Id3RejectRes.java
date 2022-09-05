package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Id3RejectRes {
    private Long id;
    private Long ssnNo;
    private String policyCode;
    private String rejectCauseCode;
    private LocalDate rejectDate;
    private LocalDate reappDate;
    private LocalDate audtDate;
    private String referenceContNo;
    private String referenceUserId;
    private String refComment;
    private String referenceComment2;
}
