package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.CCSSNREL;
import com.afba.imageplus.model.sqlserver.CREDITHIS;
import com.afba.imageplus.model.sqlserver.DDCHECK;
import com.afba.imageplus.model.sqlserver.DDCREDIT;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.FINTRGTQ;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.LPAPPLifeProApplication;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.repository.sqlserver.ICRFileRepository;
import com.afba.imageplus.service.ACHDIRECTService;
import com.afba.imageplus.service.BPAYRCService;
import com.afba.imageplus.service.CCSSNRELService;
import com.afba.imageplus.service.CREDITHISService;
import com.afba.imageplus.service.CaseCommentService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.FINTRGTQService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.LifeProApplicationService;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.TRNIDPOLRService;
import com.afba.imageplus.utilities.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICRFileServiceImpl extends BaseServiceImpl<ICRFile, String> implements ICRFileService {

    private final ICRFileRepository icrFileRepository;
    private final TRNIDPOLRService trnidpolrService;
    private final DocumentService documentService;
    private final CaseDocumentService caseDocumentService;
    private final BPAYRCService bpayrcService;
    private final CaseCommentService caseCommentService;
    private final LifeProApplicationService lifeProApplicationService;
    private final LPAUTOISSService lpautoissService;
    private final FINTRGTQService fintrgtqService;
    private final CCSSNRELService ccssnrelService;
    private final ACHDIRECTService achdirectService;
    private final CREDITHISService credithisService;
    private final DateTimeFormatter dateFormatter;


    @Autowired
    protected ICRFileServiceImpl(
            ICRFileRepository repository,
            TRNIDPOLRService trnidpolrService,
            DocumentService documentService,
            CaseDocumentService caseDocumentService,
            BPAYRCService bpayrcService,
            CaseCommentService caseCommentService,
            LifeProApplicationService lifeProApplicationService,
            LPAUTOISSService service,
            FINTRGTQService fintrgtqService,
            CCSSNRELService ccssnrelService,
            PolicyService policyService,
            ACHDIRECTService achdirectService,
            CREDITHISService credithisService
    ) {
        super(repository);
        this.icrFileRepository = repository;
        this.trnidpolrService = trnidpolrService;
        this.documentService = documentService;
        this.caseDocumentService = caseDocumentService;
        this.bpayrcService = bpayrcService;
        this.caseCommentService = caseCommentService;
        this.lifeProApplicationService = lifeProApplicationService;
        this.lpautoissService = service;
        this.fintrgtqService = fintrgtqService;
        this.ccssnrelService = ccssnrelService;
        this.achdirectService = achdirectService;
        this.credithisService = credithisService;
        dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    @Override
    protected String getNewId(ICRFile entity) {
        return entity.getDocumentId();
    }

    @Override
    public void processCreditCardForm(String documentId, ICRFile appIcrFile) {
        Optional<ICRFile> icrFile = icrFileRepository.findById(documentId);
        if (icrFile.isEmpty()) {
            return;
        }
        var ddCredit = icrFile.get().getIcrBuffer().getDdCredit();
        var trnidpolr = trnidpolrService.findByTransitionId(ddCredit.getTransactionId());

        for (TRNIDPOLR trindPolr : trnidpolr) {

            var caseDocuments = caseDocumentService.getDocumentsByCaseId(trindPolr.getTpCaseId());
            for (EKD0315CaseDocument ekd0315CaseDocument : caseDocuments) {
                var document = documentService.findById(ekd0315CaseDocument.getDocumentId());
                if (document.isEmpty() || (!"APPSBA".equals(document.get().getDocumentType()) && !"APPSLT".equals(document.get().getDocumentType()))) {
                    continue;
                }

                var appsIcrFile = icrFileRepository.findById(document.get().getDocumentId());
                if (appsIcrFile.isEmpty()) {
                    continue;
                }
                var ddApps = appsIcrFile.get().getIcrBuffer().getDdApps();
                List<Long> ssnCreditCard = getSSNs(ddCredit.getPayorSSN(), ddCredit.getInsSSN1(), ddCredit.getInsSSN2(),
                        ddCredit.getInsSSN3(), ddCredit.getInsSSN4(), ddCredit.getInsSSN5(),
                        ddCredit.getInsSSN6(), ddCredit.getInsSSN7(), ddCredit.getInsSSN8());
                List<Long> ssnDdapp = getSSNs(ddApps.getPayorSSN(), ddApps.getMemberSSN(), ddApps.getMemberBeneficiary1SSN(),
                        ddApps.getMemberBeneficiary2SSN(), ddApps.getChildBeneficiarySSN(), ddApps.getOwnerSSN(), ddApps.getSponSSN(),
                        ddApps.getSpouseBeneficiarySSN(), ddApps.getXRefSSN());

                boolean ssnMatched = matchSsns(ssnCreditCard, ssnDdapp);

                if (!ssnMatched) {
                    var ekdCase = new EKD0350Case();
                    ekdCase.setCaseId(trindPolr.getTpCaseId());
                    String caseComment = "Credit card: SSN mismatches between app and CC form";
                    caseCommentService.addCommentToCase(ekdCase, caseComment);
                    var lpautoiss = lpautoissService.findById(trindPolr.getTpPolicyId());
                    var caseDocument = new EKD0315CaseDocument();
                    caseDocument.setDocumentId(icrFile.get().getDocumentId());
                    caseDocument.setCaseId(trindPolr.getTpCaseId());
                    caseDocumentService.insert(caseDocument);
                    checkAgentCategory(
                            appIcrFile.getIcrBuffer().isCasAgentFound(),
                            trindPolr.getTpPolicyId(),
                            appsIcrFile.get().getAccountId(),
                            ddApps.getContractState());
                    if (lpautoiss.isPresent()) {
                        lpautoiss.get().setProcessFlag(false);
                        lpautoissService.save(lpautoiss.get());
                    }
                } else {
                    if (ddCredit.getBillDay() == null || ddCredit.getBillDay().trim().equals("")) {
                        ddCredit.setBillDay("00");
                    }
                    var fieldsVerified = processCreditCardField(ddCredit, trindPolr.getTpCaseId());
                    if (fieldsVerified) {

                        saveCreditCardCheckmaticInfo(ddCredit.getFirstName(),ddCredit.getLastName(), getEPAYTYPECreditCard(ddCredit.getAccountNumber()),
                                ddCredit.getAccountNumber(), "209912", "0",ddCredit.getBillDay(),
                                trindPolr.getTpPolicyId(),documentId,appIcrFile,"","","","");
                        var credithis = new CREDITHIS();
                        credithis.setDateIn(LocalDate.now());
                        credithis.setDollarAmount(0.0);
                        credithis.setPolicyId(trindPolr.getTpPolicyId());
                        credithis.setSsn(ssnCreditCard.get(0));
                        credithis.setTimeIn(LocalTime.now());
                        credithis.setProductDescription("");
                        credithis.setCompCode("");
                        credithis.setRc("");
                        credithis.setReCur("");
                        credithisService.save(credithis);

                    } else {
                        var ccssnrel = new CCSSNREL();
                        ccssnrel.setCsCaseId(trindPolr.getTpCaseId());
                        ccssnrel.setCsDocumentId(icrFile.get().getDocumentId());
                        ccssnrel.setCsPaySsn(ssnCreditCard.get(0));
                        ccssnrel.setCsPolicyId(trindPolr.getTpPolicyId());
                        ccssnrel.setCsPolicyType(icrFile.get().getAccountId().substring(0, 2));
                        ccssnrel.setCsTemplateName(icrFile.get().getTemplateName());
                        ccssnrel.setCsTransitionId(trindPolr.getTpTransactionId());
                        ccssnrelService.save(ccssnrel);
                    }
                }
            }
        }
    }

    @Override
    public void processCheckMaticForm(String documentId, ICRFile appIcrFileNew) {
        Optional<ICRFile> icrFile = icrFileRepository.findById(documentId);
        if (icrFile.isPresent()) {
            var ddCheck = icrFile.get().getIcrBuffer().getDdCheck();
            var trnidpolr = trnidpolrService.findByTransitionId(ddCheck.getTransactionId());

            for (TRNIDPOLR value : trnidpolr) {
                var caseDocuments = caseDocumentService.getDocumentsByCaseId(value.getTpCaseId());
                for (EKD0315CaseDocument ekd0315CaseDocument : caseDocuments) {
                    var document = documentService.findById(ekd0315CaseDocument.getDocumentId());
                    if (document.isPresent() && (document.get().getDocumentType().equals("APPSBA") || document.get().getDocumentType().equals("APPSLT"))) {
                        var appsIcrFile = icrFileRepository.findById(document.get().getDocumentId());
                        if (appsIcrFile.isEmpty()) {
                            continue;
                        }
                        var ddApps = appsIcrFile.get().getIcrBuffer().getDdApps();

                        List<Long> ssnCheckMatic = getSSNs(ddCheck.getPayorSSN(), ddCheck.getInsSSN1(), ddCheck.getInsSSN2(),
                                ddCheck.getInsSSN3(), ddCheck.getInsSSN4(), ddCheck.getInsSSN5(),
                                ddCheck.getInsSSN6(), ddCheck.getInsSSN7(), ddCheck.getInsSSN8());
                        List<Long> ssnDdapp = getSSNs(ddApps.getPayorSSN(), ddApps.getMemberSSN(), ddApps.getMemberBeneficiary1SSN(),
                                ddApps.getMemberBeneficiary2SSN(), ddApps.getChildBeneficiarySSN(), ddApps.getOwnerSSN(), ddApps.getSponSSN(),
                                ddApps.getSpouseBeneficiarySSN(), ddApps.getXRefSSN());

                       var ssnMatched = matchSsns(ssnCheckMatic, ssnDdapp);

                        if (!ssnMatched) {
                            var ekdCase = new EKD0350Case();
                            ekdCase.setCaseId(value.getTpCaseId());
                            String caseComment = "Checkmatic form :- SSN mismatches between app and CK form";
                            caseCommentService.addCommentToCase(ekdCase, caseComment);
                            var caseDocument = new EKD0315CaseDocument();
                            caseDocument.setDocumentId(icrFile.get().getDocumentId());
                            caseDocument.setCaseId(value.getTpCaseId());
                            caseDocumentService.insert(caseDocument);
                            checkAgentCategory(
                                    appIcrFileNew.getIcrBuffer().isCasAgentFound(),
                                    value.getTpPolicyId(),
                                    appsIcrFile.get().getAccountId(),
                                    ddApps.getContractState());
                        } else {
                            if (ddCheck.getBillDay().equals("") || ddCheck.getBillDay().equals("  ")) {
                                ddCheck.setBillDay("00");
                            }
                            var fieldsVerified = processCheckMaticFormField(ddCheck, value.getTpCaseId());
                            if (fieldsVerified) {
                                var fintrgtq = fintrgtqService.findById(value.getTpPolicyId());
                                if (fintrgtq.isEmpty()) {
                                    var fintrgtqEntity = new FINTRGTQ();
                                    fintrgtqEntity.setPolicyId(value.getTpPolicyId());
                                    fintrgtqEntity.setQueueId("MOVE");
                                }

                                saveCreditCardCheckmaticInfo(ddCheck.getFirstName(),ddCheck.getLastName(), ddCheck.getAccountType(),
                                        ddCheck.getAccountNumber(),"",
                                        ddCheck.getSignDate().toString().replace("-",""), ddCheck.getBillDay(),
                                        value.getTpPolicyId(),documentId,appIcrFileNew,"P",
                                        String.valueOf(ddCheck.getRtnNumber()), appIcrFileNew.getIcrBuffer().getDdApps().getContractState(),
                                        String.valueOf(ddCheck.getPayorSSN()));
                            } else {
                                var lpautoEntity = new LPAUTOISS();
                                lpautoEntity.setLpPolicyType(icrFile.get().getAccountId().substring(0, 2));
                                lpautoEntity.setLpDocumentId(icrFile.get().getDocumentId());
                                lpautoEntity.setLpPolicyId(value.getTpPolicyId());
                                lpautoissService.save(lpautoEntity);
                            }
                        }


                    }
                }

            }


        }
    }

    private List<Long> getSSNs(Long... args) {
        return Arrays.stream(args).filter(aLong -> aLong != null && aLong > 0).collect(Collectors.toList());
    }

    private boolean processCheckMaticFormField(DDCHECK ddcheck, String caseId) {
        var ekdCase = new EKD0350Case();
        ekdCase.setCaseId(caseId);
        boolean verified = true;
        if (ddcheck.getAddress1().equals("") || ddcheck.getAddress1() == (null)) {
            var caseComment = "CheckMatic:- Address1 verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (ddcheck.getCity().equals("") || ddcheck.getCity() == null) {
            var caseComment = "CheckMatic:-City verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (ddcheck.getState().equals("") || ddcheck.getState() == null) {
            var caseComment = "CheckMatic:-State verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (ddcheck.getZipCode().toString().length() < 5 || ddcheck.getZipCode() == null) {
            var caseComment = "CheckMatic:-Zipcode verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (!ddcheck.getAccountType().equals("C") && !ddcheck.getAccountType().equals("S")) {
            verified = false;
            var caseComment = "CheckMatic:-AccountType verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (ddcheck.getSignFlag().equals(false)) {
            verified = false;
            var caseComment = "CheckMatic:-SignFlag verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (ddcheck.getAccountNumber().equals("") || ddcheck.getAccountNumber() == null) {
            verified = false;
            var caseComment = "CheckMatic:-Account Number verification failed";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        var achdirect = achdirectService.findById(ddcheck.getRtnNumber());
        if (achdirect.isEmpty()) {
            verified = false;
            var caseComment = "CheckMatic:-Routing Number Not found";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        return verified;
    }

    private boolean processCreditCardField(DDCREDIT ddcredit, String caseId) {
        var ekdCase = new EKD0350Case();
        ekdCase.setCaseId(caseId);
        boolean verified = true;
        try {
            var billDay = Long.valueOf(ddcredit.getBillDay());
            if (billDay < 0 || billDay > 31) {
                var caseComment = "Credit Card:-Bill Day not correct";
                caseCommentService.addCommentToCase(ekdCase, caseComment);
            }
        } catch (Exception e) {
            var caseComment = "Credit Card:-Bill Day not correct";
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }

        var bpayrc = bpayrcService.findById(ddcredit.getAccountNumber().substring(0, 1));
        if (bpayrc.isPresent()) {
            verified = false;
            var caseComment = bpayrc.get().getRcText();
            caseCommentService.addCommentToCase(ekdCase, caseComment);
        }
        if (!ddcredit.getSignFlag().equals(true)) {
            verified = false;
            var caseComment = "Signature error on Credit Card";
            caseCommentService.addCommentToCase(ekdCase, caseComment);

        }
        return verified;
    }

    private boolean matchSsns(List<Long> snn, List<Long> appSsn) {
        boolean ssnMatched = false;
        var k = 0;
        var l = 0;
        while (k < snn.size() && !ssnMatched) {
            while (l < appSsn.size() && !ssnMatched) {
                if (snn.get(k).equals(appSsn.get(l))) {
                    ssnMatched = true;
                }

                l = l + 1;
            }
            k = k + 1;
            l = 0;
        }
        return ssnMatched;
    }

    private void checkAgentCategory(boolean isCasAgent, String police, String accountId, String contractState) {
        var fintrgtq = fintrgtqService.findById(police);
        var polType = accountId.substring(0, 2);
        var queue = "";
        if (polType.equals("BA")) {
            queue = "FSROBAADM";
        } else if (isCasAgent) {
            queue = "FSROCAADM";
        } else if (contractState.equals("PR")) {
            queue = "FSROPRADM";
        } else {
            queue = "FSROLTADM";
        }
        if (fintrgtq.isEmpty()) {
            var fintrgtqEntity = new FINTRGTQ();
            fintrgtqEntity.setPolicyId(police);
            fintrgtqEntity.setQueueId(queue);
            fintrgtqService.insert(fintrgtqEntity);
        }
    }


    private void saveCreditCardCheckmaticInfo(String firstName,String lastName,String payType, String accountNumber,
                                              String expDate,String signDate, String billDay,String policieNumber,String documentId,
                                              ICRFile icrFile,String psn1Type,String routNumber, String signState,String eSsn){
        policieNumber=policieNumber.trim();
        var isMemberPolicy = policieNumber.equals(icrFile.getIcrBuffer().getMemberPolicyId());
        if(icrFile.getIcrBuffer().getDdApps().getPremiumPaid().toString().equals("0.00")){
            icrFile.getIcrBuffer().getDdApps().setPremiumPaid(BigDecimal.ZERO);
        }
        var lpApp = new LPAPPLifeProApplication();
        lpApp.setSsn("000000000");
        lpApp.setPolicyId(policieNumber);
        lpApp.setCov1occup(isMemberPolicy ? icrFile.getIcrBuffer().getMemberOccupationClass() : "S");
        lpApp.setReqtype(getReqType(policieNumber,icrFile));
        lpApp.setEFName(firstName);
        lpApp.setELName(lastName);
        lpApp.setEExpDte(expDate);
        lpApp.setTransDate(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now()));
        lpApp.setTimeStamp(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now())
                +DateHelper.localTimeToProvidedFormat("hhmmss",LocalTime.now()));
        lpApp.setDocId(documentId);
        lpApp.setSavSignDate(DateHelper.localDateToProvidedFormat(dateFormatter,getSAVSIGNDTE(policieNumber,icrFile)));
        lpApp.setPsn1Type(psn1Type);
        lpApp.setProcessedFlag("N");
        lpApp.setSignDate(signDate);
        lpApp.setEPayType(payType);
        lpApp.setEAcctNum(accountNumber);
        lpApp.setNumPmtsDue("0");
        lpApp.setBillDay(billDay);
        lpApp.setERecurFlg("Y");
        lpApp.setProdcode(icrFile.getIcrBuffer().getDdApps().getProductType());
        lpApp.setPoltype(icrFile.getIcrBuffer().getPolicyType());
        lpApp.setProdId(icrFile.getIcrBuffer().getPolicyType());
        lpApp.setAmountPaid("0");
        lpApp.setPlnAnnPrm(icrFile.getIcrBuffer().getDdApps().getPremiumPaid().toString());
        lpApp.setPsn1empcl(isMemberPolicy ? icrFile.getIcrBuffer().getMemberEmployeeClassCode() : icrFile.getIcrBuffer().getSpouseEmployeeClassCode());
        lpApp.setERoutNum(routNumber);
        lpApp.setSignState(signState);
        lpApp.setESsn(eSsn);
        lifeProApplicationService.insert(lpApp);
    }
    public String getEPAYTYPECreditCard(String accountNumber){
        if(accountNumber.startsWith("4")){
            return "V";
        }
        return "M";
    }
    public LocalDate getSAVSIGNDTE(String policyId,ICRFile icrFile){
        if(policyId.equals(icrFile.getIcrBuffer().getSpousePolicyId()) &&
                icrFile.getIcrBuffer().getDdApps().getSpouseSignDate()!=null){
            return icrFile.getIcrBuffer().getDdApps().getSpouseSignDate();
        }
        return icrFile.getIcrBuffer().getDdApps().getMemberSignDate();
    }

    private String getReqType(String policyId,ICRFile icrFile){
        if(policyId.equals(icrFile.getIcrBuffer().getMemberPolicyId())){
            return icrFile.getIcrBuffer().getMemberAutoIssue();
        }else{
            return icrFile.getIcrBuffer().getSpouseAutoIssue();
        }
    }

}
