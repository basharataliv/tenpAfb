package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "EKD0350")
@NamedEntityGraph(name = "graph.case.with.documents", attributeNodes = {
        @NamedAttributeNode(value = "documents", subgraph = "subgraph.documents"), }, subgraphs = {
                @NamedSubgraph(name = "subgraph.documents", attributeNodes = {
                        @NamedAttributeNode(value = "document", subgraph = "subgraph.document"), }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0350Case extends SoftDeleteBaseEntity implements Serializable {

    @Id
    @Column(name = "EKD0350_CASE_ID", unique = true, nullable = false)
    private String caseId;

    @Column(name = "EKD0350_CASE_CLOSE_DATE")
    private LocalDate caseCloseDate;

    @Column(name = "EKD0350_CASE_CLOSE_TIME")
    private LocalTime caseCloseTime;

    @Column(name = "EKD0350_INITIAL_QUEUE_ID")
    private String initialQueueId;

    @Column(name = "EKD0350_INITIAL_REP_ID", nullable = true)
    private String initialRepId;

    @Column(name = "EKD0350_LAST_REP_ID", nullable = true)
    private String lastRepId;

    @Enumerated(EnumType.STRING)
    @Column(name = "EKD0350_CASE_STATUS")
    private CaseStatus status;

    @Column(name = "EKD0350_SCANNING_DATE")
    private LocalDate scanningDate;

    @Column(name = "EKD0350_SCANNING_TIME")
    private LocalTime scanningTime;

    @Column(name = "EKD0350_CM_ACCOUNT_NUMBER")
    private String cmAccountNumber;

    @Column(name = "EKD0350_CM_FORMATTED_NAME", nullable = true)
    private String cmFormattedName;

    @Column(name = "EKD0350_DATE_LAST_UPDATE")
    private LocalDate dateLastUpdate;

    @Column(name = "EKD0350_TIME_LAST_UPDATE")
    private LocalTime timeLastUpdate;

    @Column(name = "EKD0350_CHARGEBACK_FLAG", nullable = true)
    private String chargeBackFlag;

    @Column(name = "EKD0350_CURRENT_QUEUE_ID", nullable = true)
    private String currentQueueId;

    @Column(name = "FILLER", nullable = true)
    private String filler;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(mappedBy = "ekd0350Case", targetEntity = EKD0850CaseInUse.class, fetch = FetchType.LAZY)
    private EKD0850CaseInUse caseInUse;

    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    @OneToMany(mappedBy = "cases", targetEntity = EKD0315CaseDocument.class)
    private List<EKD0315CaseDocument> documents = new ArrayList<>();

    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    @OneToOne(mappedBy = "queuedCase", targetEntity = EKD0250CaseQueue.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EKD0250CaseQueue queuedCase;

    @Column(name = "EKD0350_CASE_CLOSE_DATETIME")
    private LocalDateTime caseCloseDateTime;

    @Column(name = "EKD0350_SCANNING_DATETIME")
    private LocalDateTime scanningDateTime;

    @Column(name = "EKD0350_LAST_UPDATE_DATETIME")
    private LocalDateTime lastUpdateDateTime;

    public LocalDateTime getCaseCloseDateTime() {
        if (this.caseCloseDateTime == null) {
            if (this.caseCloseDate != null && this.caseCloseTime != null) {
                this.caseCloseDateTime = LocalDateTime.of(this.caseCloseDate, this.caseCloseTime);
            }
        }
        return this.caseCloseDateTime;
    }

    public void setCaseCloseDateTime(LocalDateTime caseCloseDateTime) {
        if (caseCloseDateTime == null) {
            if (this.caseCloseDate != null && this.caseCloseTime != null) {
                this.caseCloseDateTime = LocalDateTime.of(this.caseCloseDate, this.caseCloseTime);
            }
        } else {
            this.caseCloseDateTime = caseCloseDateTime;
            this.caseCloseDate = caseCloseDateTime.toLocalDate();
            this.caseCloseTime = caseCloseDateTime.toLocalTime();
        }
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        if (lastUpdateDateTime == null)
            lastUpdateDateTime = LocalDateTime.now();

        this.lastUpdateDateTime = lastUpdateDateTime;
        this.dateLastUpdate = lastUpdateDateTime.toLocalDate();
        this.timeLastUpdate = lastUpdateDateTime.toLocalTime();
    }

    public LocalDateTime getLastUpdateDateTime() {
        if (this.lastUpdateDateTime == null) {
            if (this.dateLastUpdate != null && this.timeLastUpdate != null) {
                this.lastUpdateDateTime = LocalDateTime.of(this.dateLastUpdate, this.timeLastUpdate);
            }
        }
        return this.lastUpdateDateTime;
    }

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
