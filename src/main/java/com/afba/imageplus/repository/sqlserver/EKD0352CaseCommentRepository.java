package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EKD0352CaseCommentRepository extends BaseRepository<EKD0352CaseComment, Long> {

    @Query("SELECT COALESCE(MAX(commentKey), 0) FROM EKD0352CaseComment")
    Long getMaxCommentKey();

    List<EKD0352CaseComment> findByCaseIdAndIsDeleted(String caseId, Integer isDeleted);

    Optional<EKD0352CaseComment> findByCommentKeyAndIsDeleted(Long commentKey, Integer isDeleted);

    @Query("SELECT COALESCE(MAX(cc.commentKey), 0) FROM EKD0352CaseComment cc WHERE cc.caseId = :caseId AND cc.isDeleted = :isDeleted GROUP BY cc.caseId")
    Optional<Long> getMaxCommentKeyByCaseId(@Param("caseId") String caseId, @Param("isDeleted") Integer isDeleted);

    List<EKD0352CaseComment> findByCaseId(String caseId);

    Boolean existsByCaseId(String caseId);
}
