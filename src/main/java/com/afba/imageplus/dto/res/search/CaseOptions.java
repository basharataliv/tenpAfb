package com.afba.imageplus.dto.res.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseOptions {
    private Boolean inWorking;
    private Boolean release;
    private String repId;
    private Boolean currentlyWorking;
    private Boolean caseInQueue;
    private Boolean reAssign;
    private Boolean viewCaseComments;
    private Boolean addCaseComments;
    private Boolean workWithCaseComments;
    private Boolean importCaseComments;
    private Boolean proposedTerminations;
    private Boolean reassignCaseOutOfEmsiHotQueue;
    private Boolean enterQcReview;
    private Boolean pendACase;
    private Boolean unpendACase;
    private Boolean endorsement;
}
