package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.model.sqlserver.id.EKD0353CaseCommentLineKey;
import com.afba.imageplus.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EKD0353CaseCommentLineRepository extends BaseRepository<EKD0353CaseCommentLine, EKD0353CaseCommentLineKey> {

    void deleteAllByCommentKey(Long commentKey);

    @Query("SELECT MAX(cl.commentSequence) FROM EKD0353CaseCommentLine cl WHERE cl.commentKey = :cmtKey GROUP BY cl.commentKey")
    Optional<Integer> maxSequenceNumberByCommentKey(@Param("cmtKey") Long commentKey);
}
