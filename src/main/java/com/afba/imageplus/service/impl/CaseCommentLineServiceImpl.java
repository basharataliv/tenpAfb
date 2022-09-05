package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.model.sqlserver.id.EKD0353CaseCommentLineKey;
import com.afba.imageplus.repository.sqlserver.EKD0353CaseCommentLineRepository;
import com.afba.imageplus.service.CaseCommentLineService;
import org.springframework.stereotype.Service;

@Service
public class CaseCommentLineServiceImpl extends BaseServiceImpl<EKD0353CaseCommentLine, EKD0353CaseCommentLineKey> implements CaseCommentLineService {

    private final EKD0353CaseCommentLineRepository caseCommentLineRepository;

    protected CaseCommentLineServiceImpl(
            EKD0353CaseCommentLineRepository repository
    ) {
        super(repository);
        this.caseCommentLineRepository = repository;
    }

    @Override
    protected EKD0353CaseCommentLineKey getNewId(EKD0353CaseCommentLine entity) {
        return new EKD0353CaseCommentLineKey(entity.getCommentKey(), entity.getCommentSequence());
    }

    @Override
    public void deleteAllByCommentKey(Long commentKey) {
        caseCommentLineRepository.deleteAllByCommentKey(commentKey);
    }

    public Integer getCurrentSequenceNumberByCommentKey(Long commentKey) {
        return caseCommentLineRepository.maxSequenceNumberByCommentKey(
                commentKey
        ).orElse(0);
    }
}
