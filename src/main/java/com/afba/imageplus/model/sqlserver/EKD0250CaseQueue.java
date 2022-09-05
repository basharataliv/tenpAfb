package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.QueueAuthorization;
import com.afba.imageplus.model.sqlserver.id.EKD0250CaseQueueKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EKD0250")
@NamedEntityGraph(name = "graph.queue.case.with.documents", attributeNodes = {
        @NamedAttributeNode(value = "cases", subgraph = "subgraph.documents"), }, subgraphs = {
                @NamedSubgraph(name = "subgraph.documents", attributeNodes = {
                        @NamedAttributeNode(value = "documents", subgraph = "subgraph.document"), }),
                @NamedSubgraph(name = "subgraph.document", attributeNodes = {
                        @NamedAttributeNode(value = "document"), }) })
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldNameConstants
@IdClass(EKD0250CaseQueueKey.class)
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0250CaseQueue extends SoftDeleteBaseEntity {
    @Id
    @Column(name = "EKD0250_QUEUE_ID", nullable = false, columnDefinition = "varchar(10) default ' '", length = 10)
    @QueueAuthorization
    private String queueId;

    @Id
    @Column(name = "EKD0250_PRIORITY", nullable = false, columnDefinition = "varchar(1) default ' '", length = 1)
    private String priority;

    @Id
    @Column(name = "EKD0250_SCAN_DATE", nullable = false, columnDefinition = "date default CURRENT_TIMESTAMP", length = 8)
    private LocalDate scanDate;

    @Id
    @Column(name = "EKD0250_SCAN_TIME", nullable = false, columnDefinition = "time default CURRENT_TIMESTAMP", length = 6)
    private LocalTime scanTime;

    @Id
    @Column(name = "EKD0250_CASE_ID", nullable = false, length = 9)
    private String caseId;

    @Column(name = "EKD0250_QUEUE_DATE", nullable = false, columnDefinition = "date default CURRENT_TIMESTAMP", length = 8)
    private LocalDate queueDate;

    @Column(name = "EKD0250_QUEUE_TIME", nullable = false, columnDefinition = "time default CURRENT_TIMESTAMP", length = 6)
    private LocalTime queueTime;

    @Column(name = "FILLER", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    private String filler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKD0250_CASE_ID", referencedColumnName = "EKD0350_CASE_ID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0350Case cases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKD0250_QUEUE_ID", referencedColumnName = "QUEID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0150Queue queue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKD0250_CASE_ID", referencedColumnName = "EKD0350_CASE_ID", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private EKD0350Case queuedCase;

    @Column(name = "EKD0250_QUEUE_DATETIME")
    private LocalDateTime queueDateTime;

    @Column(name = "EKD0250_SCAN_DATETIME")
    private LocalDateTime scanDateTime;

    public void setQueueId(String queueId) {
        this.queueId = queueId != null ? queueId.trim() : "";
    }

    public String getQueueId() {
        return queueId != null ? queueId.trim() : "";
    }

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
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EKD0250CaseQueue that = (EKD0250CaseQueue) o;
        return queueId != null && Objects.equals(queueId, that.queueId) && caseId != null
                && Objects.equals(caseId, that.caseId)
                && priority != null && Objects.equals(priority, that.priority)
                && scanDate != null && Objects.equals(scanDate, that.scanDate)
                && scanTime != null && Objects.equals(scanTime, that.scanTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queueId, caseId, priority,scanDate,scanTime);
    }
}
