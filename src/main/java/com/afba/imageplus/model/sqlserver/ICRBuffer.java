package com.afba.imageplus.model.sqlserver;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ICRBuffer implements Serializable {

    private DDAPPS ddApps;
    private DDCREDIT ddCredit;
    private DDCHECK ddCheck;
    private List<DDATTACH> ddAttachments;
    private String transactionSource;
    private String companyCode;
    private String productCode;
    private String caseId;
    private String payMode;
    private String listBillId;
    private String payModeFrequency;
    private Integer agent1CommissionPercentage;
    private Integer agent2CommissionPercentage;
    private BigDecimal memberAmountPaid;
    private BigDecimal spouseAmountPaid;
    private String affiliateClientId;
    private String policyType;
    private String procType;
    private String memberAutoIssue;
    private String spouseAutoIssue;
    private boolean casAgentFound;
    private boolean memberGoesToESP;
    private boolean memberNeedEKG;
    private boolean memberNeedParamedi;
    private boolean spouseGoesToESP;
    private boolean spouseNeedEKG;
    private boolean spouseNeedParamedi;
    private String memberOccupationClass;
    private String memberEmployeeClassCode;
    private String spouseEmployeeClassCode;

    // Existing file fields
    // - Member
    private boolean memberSuspendedWithDPFound;
    private boolean memberTerminatedWithDCFound;
    private boolean memberPendingOrApprovedGroupFreeFound;
    private boolean memberTerminatedWithEXFound;
    private String memberPreviousLOB;
    private LocalDate memberPreviousDOB;
    private String memberPreviousSex;
    private String memberPreviousSmokerClass;
    private int memberProductCoverage;
    private int memberTotalCoverage;
    private String memberOldestPolicyId;
    private boolean memberMoveDocumentToPermanentCase;
    private Integer memberExistingChildUnit;
    private String memberGeneratedSSNCommentCode;

    // - Spouse
    private boolean spouseSuspendedWithDPFound;
    private boolean spouseTerminatedWithDCFound;
    private boolean spousePendingOrApprovedGroupFreeFound;
    private boolean spouseTerminatedWithEXFound;
    private LocalDate spousePreviousDOB;
    private String spousePreviousSex;
    private String spousePreviousSmokerClass;
    private int spouseProductCoverage;
    private int spouseTotalCoverage;
    private String spouseOldestPolicyId;
    private boolean spouseMoveDocumentToPermanentCase;
    private Integer spouseExistingChildUnit;
    private String spouseGeneratedSSNCommentCode;

    private String policyId;
    private String rank;
    private String payGrade;

    private String memberPolicyId;
    private String spousePolicyId;

    private boolean memberBeneficiaryInvalid;
    private boolean spouseBeneficiaryInvalid;

    //Common fields
    private String billDay;
    private String ePayType;
}
