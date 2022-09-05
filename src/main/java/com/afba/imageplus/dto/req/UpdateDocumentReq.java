package com.afba.imageplus.dto.req;

import com.afba.imageplus.constants.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentReq {

    private MultipartFile doc;

    private String olsSystemId;

    private String olsFolderName;

    private String olsSubDirName;

    private String filFlg1A;

    private String filFlg2A;

    private String filfldx2;

    private String filfldx5;

    private int activeRequest;

    @NotBlank(message = ErrorConstants.DOCUMENT_TYPE_CAN_NOT_BE_NULL_OR_EMPTY)
    @Size(max = 8, message = ErrorConstants.DOCUMENT_TYPE_INVALID_LENGTH)
    private String documentType;

    private String scanningWsId;

    private LocalDate scanningDate;

    private LocalTime scanningTime;

    private String scanningRepId;

    private int batchId;

    private String userLastUpdate;

    @Size(max = 26, message = ErrorConstants.REQ_LIST_DESCRIPTION_INVALID_SIZE)
    private String reqListDescription;

    private int docPage;

    private String caseCreateFlag;

    private String autoIndexFlag;

    private int dasdCounter;

    private String opticalStoreFlag;

    private String nopVolid1;

    private String nopVolid2;

    private String nopVolid3;

    private String inpmetflgA;

    private String optvolA;

    private int objclsA;

    private String versnA;

    private String filler;

    private MetaData metaData;

}
