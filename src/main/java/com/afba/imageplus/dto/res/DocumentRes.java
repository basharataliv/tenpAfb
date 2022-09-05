package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRes {

    private String documentId;

    private String spDocumentId;

    private String spDocumentUrl;

    private String spDocumentSiteId;

    private String spDocumentLibraryId;

    private String olsSystemId;

    private String olsFolderName;

    private String olsSubDirName;

    private String filFlg1A;

    private String filFlg2A;

    private String filFldx2;

    private String filFldx5;

    private Integer activeRequests;

    private String documentType;

    private Long kBytesInDocument;

    private String scaningWsId;

    private LocalDate scanningDate;

    private LocalTime scanningTime;

    private String scanningRepId;

    private Integer batchId;

    private LocalDate dateLastUpdate;

    private LocalTime timeLastUpdate;

    private String userLastUpdate;

    private String reqListDescription;

    private Integer docPage;

    private String caseCreateFlag;

    private String autoIndexFlag;

    private Integer dasdCounter;

    private String opticalStoreFlag;

    private String nopVolid1;

    private String nopVolid2;

    private String nopVolid3;

    private String inpmetflgA;

    private String optvolA;

    private Integer objclsA;

    private String versnA;

    private String filler;

    private LocalDateTime lastUpdateDateTime;

    private LocalDateTime scanningDateTime;
}
