package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Table(name = "MOVETRAIL")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EKD0315CaseDocumentKey.class)
@Builder
public class MOVETRAIL extends BaseEntity {
    @Id
    @Column(name = "MOVE_CASE_ID", nullable = false, columnDefinition = "integer default 0", length = 9)
    private String caseId;

    @Id
    @Column(name = "MOVE_DOC_ID", nullable = false)
    private String documentId;

    @Column(name = "MOVE_DATE")
    private LocalDate moveDate;
}
