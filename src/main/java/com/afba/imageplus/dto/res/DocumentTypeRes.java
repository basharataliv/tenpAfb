package com.afba.imageplus.dto.res;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentTypeRes {

    private String documentType;

    private String documentDescription;

    private Integer defaultSuspendDays;

    private Boolean dasdControlFlag;

    private Boolean olsStoreFlag;

    private Boolean outputRequired;

    private Boolean informRetrievalDept;

    private Integer createNewCase;

    private Boolean indexScanFlag;

    private Boolean requestSuspendFlag;

    private String defaultQueueId;

    private LocalDate dateLastUpdate;

    private LocalTime timeLastUpdate;

    private String userLastUpdate;

    private Integer noInBatch;

    private Boolean requestInputFlag;

    private Integer retentionPeriod;

    private String securityClass;

    private Boolean allowImpA;

    private String inpdTypeA;

    private String faxPSizeA;

    private Boolean alwannflA;

    private Boolean alwredflA;

    private Boolean allowOcr;

    private Boolean confirmAll;

    private Boolean workstationOnly;

    private Boolean matchCp;

    private Integer storeMethod;

    private String storeClass;

    private String optSysId;

    private Integer lan3995;

    private LocalDateTime lastUpdateDateTime;

    private Boolean isAppsDoc;

    private String productCode;
}
