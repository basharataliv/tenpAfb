package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "EKD0310")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE EKD0310 SET IS_DELETED = 1 WHERE EKD0310_DOCUMENT_ID=?")
@Where(clause = "IS_DELETED=0")
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0310Document extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKD0310_DOCUMENT_ID")
    private String documentId;

    @Column(name = "EKD0310_DOCUMENT_EXT")
    private String documentExt;

    @Column(name = "EKD0310_SP_DOC_ID")
    private String spDocumentId;

    @Column(name = "EKD0310_SP_DOC_URL")
    private String spDocumentUrl;

    @Column(name = "EKD0310_SP_DOC_SITE_ID")
    private String spDocumentSiteId;

    @Column(name = "EKD0310_SP_DOC_LIBRARY_ID")
    private String spDocumentLibraryId;

    @Column(name = "EKD0310_SYSTEM_ID")
    private String systemId;

    @Column(name = "EKD0310_FOLDER_NAME")
    private String folderName;

    @Column(name = "EKD0310_SUBDIRECTORY_NAME")
    private String subDirName;

    @Column(name = "EKD0310_OLS_SYSTEM_ID")
    private String olsSystemId;

    @Column(name = "EKD0310_OLS_FOLDER_NAME")
    private String olsFolderName;

    @Column(name = "EKD0310_OLS_SUBDIRECTORY_NAME")
    private String olsSubDirName;

    @Column(name = "EKD0310_FILFLG1_A")
    private String filFlg1A;

    @Column(name = "EKD0310_FILFLG2_A")
    private String filFlg2A;

    @Column(name = "EKD0310_FILFLDX2")
    private String filFldx2;

    @Column(name = "EKD0310_FILFLDX5")
    private String filFldx5;

    @Column(name = "EKD0310_ACTIVE_REQUESTS")
    private Integer activeRequests;

    @Column(name = "EKD0310_DOCUMENT_TYPE")
    @DocumentTypeAuthorization
    private String documentType;

    @Column(name = "EKD0310_KBYTES_IN_DOCUMENT")
    private Float kBytesInDocument;

    @Column(name = "EKD0310_SCANNING_WS_ID")
    private String scaningWsId;

    @Column(name = "EKD0310_SCANNING_DATE")
    private LocalDate scanningDate;

    @Column(name = "EKD0310_SCANNING_TIME")
    private LocalTime scanningTime;

    @Column(name = "EKD0310_SCANNING_REP_ID")
    private String scanningRepId;

    @Column(name = "EKD0310_BATCH_ID")
    private Integer batchId;

    @Column(name = "EKD0310_DATE_LAST_UPDATE")
    private LocalDate dateLastUpdate;

    @Column(name = "EKD0310_TIME_LAST_UPDATE")
    private LocalTime timeLastUpdate;

    @Column(name = "EKD0310_USER_LAST_UPDATE")
    private String userLastUpdate;

    @Column(name = "EKD0310_REQ_LIST_DESCRIPTION")
    private String reqListDescription;

    @Column(name = "EKD0310_DOCUMENT_PAGES")
    private Integer docPage;

    @Column(name = "EKD0310_CASE_CREATE_FLAG")
    private String caseCreateFlag;

    @Column(name = "EKD0310_AUTO_INDEX_FLAG")
    private String autoIndexFlag;

    @Column(name = "EKD0310_DASD_COUNTER")
    private Integer dasdCounter;

    @Column(name = "EKD0310_OPTICAL_STORE_FLAG")
    private String opticalStoreFlag;

    @Column(name = "EKD0310_NOP_VOLID_1")
    private String nopVolid1;

    @Column(name = "EKD0310_NOP_VOLID_2")
    private String nopVolid2;

    @Column(name = "EKD0310_NOP_VOLID_3")
    private String nopVolid3;

    @Column(name = "EKD0310_INPMETFLG_A")
    private String inpmetflgA;

    @Column(name = "EKD0310_OPTVOL_A")
    private String optvolA;

    @Column(name = "EKD0310_OBJCLS_A")
    private Integer objclsA;

    @Column(name = "EKD0310_VERSN_A")
    private String versnA;

    @Column(name = "EKD0310_FILLER")
    private String filler;

    @Transient
    private MultipartFile doc;

    @Column(name = "EKD0310_LAST_UPDATE_DATETIME")
    private LocalDateTime lastUpdateDateTime;

    @Column(name = "EKD0310_SCANNING_DATETIME")
    private LocalDateTime scanningDateTime;

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
