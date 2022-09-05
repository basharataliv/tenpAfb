package com.afba.imageplus.model.sqlserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EKD0110")
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicInsert
@DynamicUpdate
@Builder
public class EKD0110DocumentType extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKD0110_DOCUMENT_TYPE", nullable = false)
    @DocumentTypeAuthorization
    private String documentType;

    // provided sample contained all blanks
    @Column(name = "EKD0110_DOCUMENT_USE")
    private String documentUse;

    @Column(name = "EKD0110_DOCUMENT_DESCRIPTION")
    private String documentDescription;

    @Column(name = "EKD0110_DEFAULT_SUSPEND_DAYS")
    private Integer defaultSuspendDays;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_DASD_CONTROL_FLAG", length = 1)
    private Boolean dasdControlFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_OLS_STORE_FLAG", length = 1)
    private Boolean olsStoreFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_OUTPUT_REQUIRED", length = 1)
    private Boolean outputRequired;

    // provided sample contained all blanks
    @Column(name = "EKD0110_OUTPUT_FORM_ID")
    private String outputFormId;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_INFORM_RETRIEVAL_DEPT", length = 1)
    private Boolean informRetrievalDept;

    @Column(name = "EKD0110_CREATE_NEW_CASE", length = 1)
    private String createNewCase;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_INDEX_SCAN_FLAG", length = 1)
    private Boolean indexScanFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_REQUEST_SUSPEND_FLAG", length = 1)
    private Boolean requestSuspendFlag;

    @Column(name = "EKD0110_DEFAULT_QUEUE_ID")
    private String defaultQueueId;

    @Column(name = "EKD0110_DATE_LAST_UPDATE")
    private LocalDate dateLastUpdate;

    @Column(name = "EKD0110_TIME_LAST_UPDATE")
    private LocalTime timeLastUpdate;

    @Column(name = "EKD0110_USER_LAST_UPDATE")
    private String userLastUpdate;

    @Column(name = "EKD0110_NO_IN_BATCH")
    private Integer noInBatch;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_REQUEST_INPUT_FLAG", length = 1)
    private Boolean requestInputFlag;

    @Column(name = "EKD0110_RETENTION_PERIOD")
    private Integer retentionPeriod;

    // Kept String, because there are records with value 0
    @Column(name = "EKD0110_SECURITY_CLASS")
    private Integer securityClass;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_ALLOWIMP_A", length = 1)
    private Boolean allowImpA;

    @Column(name = "EKD0110_INPDTYPE_A", length = 2)
    private String inpdTypeA;

    @Column(name = "EKD0110_FAXPSIZE_A")
    private String faxPSizeA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_ALWANNFL_A", length = 1)
    private Boolean alwannflA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_ALWREDFL_A", length = 1)
    private Boolean alwredflA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_ALLOW_OCR", length = 1)
    private Boolean allowOcr;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_CONFIRM_ALL", length = 1)
    private Boolean confirmAll;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_WORKSTATION_ONLY", length = 1)
    private Boolean workstationOnly;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0110_MATCHCP", length = 1)
    private Boolean matchCp;

    // provided sample contained all blanks
    @Column(name = "EKD0110_CP_PROCESS")
    private String cpProcess;

    // provided sample contained all blanks
    @Column(name = "EKD0110_CPNAME", insertable = false)
    private String cpName;

    @Column(name = "EKD0110_STORMETH", length = 1)
    private String storeMethod;

    @Column(name = "EKD0110_STORCLASS", length = 30)
    private String storeClass;

    @Column(name = "EKD0110_OPTSYSID", length = 1)
    private String optSysId;

    @Column(name = "EKD0110_LAN3995", length = 1)
    private String lan3995;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "IS_APPS_DOC", length = 1)
    private Boolean isAppsDoc;

    // provided sample contained all blanks
    @Column(name = "FILLER50")
    private String filler50;

    @Column(name = "EKD0110_LAST_UPDATE_DATETIME")
    private LocalDateTime lastUpdateDateTime;

    @Column(name = "EKD0110_PRODUCT_CODE")
    private String productCode;

    public void setDocumentType(String documentType) {
        this.documentType = documentType != null ? documentType.trim() : "";
    }

    public String getDocumentType() {
        return documentType != null ? documentType.trim() : "";
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EKD0110DocumentType that = (EKD0110DocumentType) o;
        return documentType != null && Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}