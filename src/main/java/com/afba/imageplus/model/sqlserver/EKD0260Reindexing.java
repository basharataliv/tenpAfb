package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.id.EKD0260DocTypeDocIdKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "EKD0260")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@IdClass(EKD0260DocTypeDocIdKey.class)
@Builder
public class EKD0260Reindexing extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKD0260_DOCUMENT_TYPE", nullable = false, columnDefinition = "varchar(8) default ' '", length = 8)
    @DocumentTypeAuthorization
    private String documentType;

    @Id
    @Column(name = "EKD0260_SCAN_DATE", nullable = false, columnDefinition = "date default CURRENT_TIMESTAMP", length = 8)
    private LocalDate scanDate;

    @Id
    @Column(name = "EKD0260_SCAN_TIME", nullable = false, columnDefinition = "time default CURRENT_TIMESTAMP", length = 6)
    private LocalTime scanTime;

    @Id
    @Column(name = "EKD0260_DOCUMENT_ID", nullable = false, columnDefinition = "varchar(12) default ' '", length = 12)
    private String documentId;

    @Column(name = "EKD0260_SCAN_REP_ID", nullable = false, columnDefinition = "varchar(10) default ' '", length = 10)
    private String scanRepId;

    @Column(name = "EKD0260_INDX_REP_ID", nullable = true, columnDefinition = "varchar(10) default ' '", length = 10)
    private String indexRepId;

    @Column(name = "EKD0260_WORK_REP_ID", nullable = true, columnDefinition = "varchar(10) default ' '", length = 10)
    private String workRepId;

    @Column(name = "EKD0260_STATUS_CODE", nullable = false, columnDefinition = "varchar(1) default ' '", length = 1)
    private String statusCode;

    @Column(name = "EKD0260_SCAN_FLD1", nullable = true, columnDefinition = "varchar(10) default ' '", length = 10)
    private String scanFld1;

    @Column(name = "EKD0260_SCAN_FLD2", nullable = true, columnDefinition = "varchar(64) default ' '", length = 64)
    private String scanFld2;

    @Column(name = "EKD0260_SCAN_FLD3", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    private String scanFld3;

    @Column(name = "EKD0260_SCAN_FLD4", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    private String scanFld4;

    @Column(name = "EKD0260_SCAN_FLD5", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    private String scanFld5;

    @Column(name = "EKD0260_SCAN_FLD6", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    private String scanFld6;

    @Column(name = "EKD0260_SCAN_FLD7", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    public String scanFld7;

    @Column(name = "EKD0260_IDENTIFIER", nullable = false, columnDefinition = "varchar(40) default ' '", length = 40)
    private String identifier;

    @Column(name = "EKD0260_FORM_CLASS", nullable = true, columnDefinition = "varchar(36) default ' '", length = 36)
    private String formClass;

    @Column(name = "EKD0260_FORM_NAME", nullable = true, columnDefinition = "varchar(36) default ' '", length = 36)
    private String formName;

    @Column(name = "EKD0260_FILE_CABINET_CODE", nullable = true, columnDefinition = "varchar(8) default ' '", length = 8)
    private String fileCabinetCode;

    @Column(name = "EKD0260_FILE_CABINET_FLD1", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld1;

    @Column(name = "EKD0260_FILE_CABINET_FLD2", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld2;

    @Column(name = "EKD0260_FILE_CABINET_FLD3", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld3;

    @Column(name = "EKD0260_FILE_CABINET_FLD4", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld4;

    @Column(name = "EKD0260_FILE_CABINET_FLD5", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld5;

    @Column(name = "EKD0260_FILE_CABINET_FLD6", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld6;

    @Column(name = "EKD0260_FILE_CABINET_FLD7", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld7;

    @Column(name = "EKD0260_FILE_CABINET_FLD8", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String fileCabinetFld8;

    @Column(name = "EKD0260_USERDATA_FLD1", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld1;

    @Column(name = "EKD0260_USERDATA_FLD2", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld2;

    @Column(name = "EKD0260_USERDATA_FLD3", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld3;

    @Column(name = "EKD0260_USERDATA_FLD4", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld4;

    @Column(name = "EKD0260_USERDATA_FLD5", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld5;

    @Column(name = "EKD0260_USERDATA_FLD6", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld6;

    @Column(name = "EKD0260_USERDATA_FLD7", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld7;

    @Column(name = "EKD0260_USERDATA_FLD8", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld8;

    @Column(name = "EKD0260_USERDATA_FLD9", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld9;

    @Column(name = "EKD0260_USERDATA_FLD10", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    private String userDataFld10;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0260_INDEX_FLAG", columnDefinition = "varchar(1) default 'N'", nullable = false, length = 1)
    private Boolean indexFlag;

    @Column(name = "FILLER_50", nullable = true, columnDefinition = "varchar(50) default ' '", length = 50)
    private String filler50;

    @Column(name = "EKD0260_SCAN_DATETIME")
    private LocalDateTime scanningDateTime;

    public String getDocumentType() {
        return  documentType != null ? documentType.trim() : "";
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType != null ? documentType.trim() : "";
    }

    public LocalDateTime getScanningDateTime() {
        if (this.scanningDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        }
        return this.scanningDateTime;
    }

    public void setScanningDateTime(LocalDateTime scanningDateTime) {
        if (scanningDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        } else {
            this.scanningDateTime = scanningDateTime;
            this.scanDate = scanningDateTime.toLocalDate();
            this.scanTime = scanningDateTime.toLocalTime();
        }
    }
}
