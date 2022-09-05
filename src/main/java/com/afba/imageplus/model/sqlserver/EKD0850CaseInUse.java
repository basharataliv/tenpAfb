package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EKD0850")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0850CaseInUse extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "CASEID", nullable = false)
    public String caseId;

    @Column(name = "QUEID", nullable = true, columnDefinition = "varchar(10) default ' '", length = 10)
    public String queueId;

    @Column(name = "PRORTY", nullable = false, columnDefinition = "varchar(1) default ' '", length = 1)
    public String priority;

    @Column(name = "SCNDAT", nullable = false, columnDefinition = "Date default ' '", length = 8)
    public LocalDate scanDate;

    @Column(name = "SCNTIM", nullable = false, columnDefinition = "time default ' '", length = 6)
    public LocalTime scanTime;

    @Column(name = "QUEDAT", nullable = false, columnDefinition = "Date default ' '", length = 8)
    public LocalDate queueDate;

    @Column(name = "QUETIM", nullable = false, columnDefinition = "time default ' '", length = 6)
    public LocalTime queueTime;

    @Column(name = "QREPID", nullable = false, columnDefinition = "varchar(10) default ' '", length = 10)
    public String qRepId;

    @Column(name = "FILL20", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    public String fill;

    @OneToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    @JoinColumn(name = "CASEID", referencedColumnName = "EKD0350_CASE_ID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0350Case ekd0350Case;

    @Column(name = "CREATED_DATETIME", updatable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "QUE_DATETIME")
    private LocalDateTime queueDateTime;

    @Column(name = "SCN_DATETIME")
    private LocalDateTime scanDateTime;

    public LocalDateTime getQueueDateTime() {
        if (this.queueDateTime == null) {
            if (this.queueDate != null && this.queueTime != null) {
                this.queueDateTime = LocalDateTime.of(this.queueDate, this.queueTime);
            }
        }
        return this.queueDateTime;
    }

    public void setQueueDateTime(LocalDateTime queueDateTime) {
        if (queueDateTime == null) {
            if (this.queueDate != null && this.queueTime != null) {
                this.queueDateTime = LocalDateTime.of(this.queueDate, this.queueTime);
            }
        } else {
            this.queueDateTime = queueDateTime;
            this.queueDate = queueDateTime.toLocalDate();
            this.queueTime = queueDateTime.toLocalTime();
        }
    }

    public LocalDateTime getScanDateTime() {
        if (this.scanDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        }
        return this.scanDateTime;
    }

    public void setScanDateTime(LocalDateTime scanDateTime) {
        if (scanDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        } else {
            this.scanDateTime = scanDateTime;
            this.scanDate = scanDateTime.toLocalDate();
            this.scanTime = scanDateTime.toLocalTime();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKD0850CaseInUse that = (EKD0850CaseInUse) o;
        return caseId != null && Objects.equals(caseId, that.caseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
