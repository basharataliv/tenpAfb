package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.model.sqlserver.id.EKD0353CaseCommentLineKey;

public interface CaseCommentLineService extends BaseService<EKD0353CaseCommentLine, EKD0353CaseCommentLineKey> {

    void deleteAllByCommentKey(Long commentKey);

    Integer getCurrentSequenceNumberByCommentKey(Long commentKey);
}
