package com.afba.imageplus.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class CaseCreateReqExtended extends CaseCreateReq {

    private List<CaseCommentReq> caseComments;
    private boolean created;

}
