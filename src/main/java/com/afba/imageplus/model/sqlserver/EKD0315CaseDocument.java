package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Table(name = "EKD0315")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EKD0315CaseDocumentKey.class)
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0315CaseDocument extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKD0315_CASE_ID", nullable = false, columnDefinition = "integer default 0", length = 9)
    private String caseId;

    @Id
    @Column(name = "EKD0315_DOCUMENT_ID", nullable = false)
    private String documentId;

    @Column(name = "EKD0315_SCANNING_DATE")
    private LocalDate scanningDate;

    @Column(name = "EKD0315_SCANNING_TIME")
    private LocalTime scanningTime;

    @Column(name = "EKD0315_SCANNING_USER")
    private String scanningUser;

    @Column(name = "EKD0315_DASD_FLAG")
    private String dasdFlag;

    @Column(name = "EKD0315_ITEM_TYPE")
    private String itemType;

    @Column(name = "EKD0315_ITEM_INIT")
    private String itemInit;

    @Column(name = "FILLER")
    private String filler;
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKD0315_DOCUMENT_ID", referencedColumnName = "EKD0310_DOCUMENT_ID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0310Document document;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKD0315_CASE_ID", referencedColumnName = "EKD0350_CASE_ID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0350Case cases;

    @Column(name = "EKD0315_SCANNING_DATETIME")
    private LocalDateTime scanningDateTime;

    public LocalDateTime getScanningDateTime() {
        if (this.scanningDateTime == null) {
            if (this.scanningDate != null && this.scanningTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanningDate, this.scanningTime);
            }
        }
        return this.scanningDateTime;
    }

    public void setScanningDateTime(LocalDateTime scanningDateTime) {
        if (scanningDateTime == null) {
            if (this.scanningDate != null && this.scanningTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanningDate, this.scanningTime);
            }
        } else {
            this.scanningDateTime = scanningDateTime;
            this.scanningDate = scanningDateTime.toLocalDate();
            this.scanningTime = scanningDateTime.toLocalTime();
        }
    }
}
