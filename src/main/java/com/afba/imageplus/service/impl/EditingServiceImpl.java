package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.Template;
import com.afba.imageplus.constants.TransactionSource;
import com.afba.imageplus.dto.EditingResponse;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.afba.imageplus.model.sqlserver.id.APPSWTCKey;
import com.afba.imageplus.model.sqlserver.id.STATEEYESSysTemSysStateKey;
import com.afba.imageplus.model.sqlserver.id.STETBLPKey;
import com.afba.imageplus.service.AFFCLIENTService;
import com.afba.imageplus.service.APPSWTAService;
import com.afba.imageplus.service.APPSWTCService;
import com.afba.imageplus.service.CodesFlService;
import com.afba.imageplus.service.DDCHECKService;
import com.afba.imageplus.service.DDCREDITService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.EditingService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.ID3REJECTService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.STATEYESService;
import com.afba.imageplus.service.STETBLPService;
import com.afba.imageplus.service.ZIPCITY1Service;
import com.afba.imageplus.service.ZIPCITY2Service;
import com.afba.imageplus.utilities.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.afba.imageplus.constants.Template.APPSBA0217;
import static com.afba.imageplus.constants.Template.APPSCO0810;
import static com.afba.imageplus.constants.Template.APPSCT1013;
import static com.afba.imageplus.constants.Template.APPSEP1009;
import static com.afba.imageplus.constants.Template.APPSGF0317;
import static com.afba.imageplus.constants.Template.APPSLT0116;
import static com.afba.imageplus.constants.Template.APPSLT0322;
import static com.afba.imageplus.constants.Template.APPSNG0702;
import static com.afba.imageplus.constants.Template.APPSPR1009;
import static com.afba.imageplus.constants.Template.APPSSS0810;
import static com.afba.imageplus.constants.Template.APPSSS0818;

@Service
public class EditingServiceImpl implements EditingService {

    private final String DEFAULT_AGENT = "AVA01";

    private final ID3REJECTService id3REJECTService;
    private final AFFCLIENTService affclientService;
    private final APPSWTAService appswtaService;
    private final APPSWTCService appswtcService;
    private final STATEYESService stateyesService;
    private final IndexingService indexingService;
    private final STETBLPService stetblpService;
    private final ZIPCITY1Service zipCity1Service;
    private final ZIPCITY2Service zipCity2Service;
    private final DocumentService documentService;
    private final LifeProApiService lifeProApiService;
    private final CodesFlService codesFlService;
    private Map<String, Integer> queuePriorities;
    private final DDCREDITService ddcreditService;
    private final DDCHECKService ddcheckService;
    private final ICRFileService icrFileService;



    public EditingServiceImpl(
            ID3REJECTService id3REJECTService,
            AFFCLIENTService affclientService,
            APPSWTAService appswtaService,
            APPSWTCService appswtcService,
            STATEYESService stateyesService,
            IndexingService indexingService,
            STETBLPService stetblpService,
            ZIPCITY1Service zipCity1Service,
            ZIPCITY2Service zipCity2Service,
            DocumentService documentService,
            LifeProApiService lifeProApiService,
            CodesFlService codesFlService,
            DDCREDITService ddcreditService,
            DDCHECKService ddcheckService,
            ICRFileService icrFileService) {
        this.id3REJECTService = id3REJECTService;
        this.affclientService = affclientService;
        this.appswtaService = appswtaService;
        this.appswtcService = appswtcService;
        this.stateyesService = stateyesService;
        this.indexingService = indexingService;
        this.stetblpService = stetblpService;
        this.zipCity1Service = zipCity1Service;
        this.zipCity2Service = zipCity2Service;
        this.documentService = documentService;
        this.lifeProApiService = lifeProApiService;
        this.codesFlService = codesFlService;
        this.ddcreditService = ddcreditService;
        this.ddcheckService = ddcheckService;
        this.icrFileService = icrFileService;
    }

    @Override
    public Map<String, Integer> getQueuePriorities() {
        if (queuePriorities == null) {
            // Lower the value higher the priority.
            queuePriorities = Map.ofEntries(
                    // Replacement (Policy will go to ESP)
                    Map.entry("APPSREPLQ", 10),
                    // EMSI (Policy will go to ESP)
                    Map.entry("FSROCAEMSI", 20),
                    Map.entry("FSROEMSIQ", 30),
                    Map.entry("APPSEMSICA", 40),
                    Map.entry("APPSEMSIBA", 50),
                    Map.entry("APPSEMSILT", 50),
                    Map.entry("EMSIDONEQ", 60),
                    // NON-EMSI (Policy stays in house)
                    Map.entry("MEDIDECLQ", 70),
                    Map.entry("FSROCAADM", 80),
                    Map.entry("FSROBAADM", 90),
                    Map.entry("FSROPRADM", 90),
                    Map.entry("FSROLTADM", 95),
                    Map.entry("FSROGFQ", 90),
                    Map.entry("APPSLT121", 100),
                    Map.entry("APPSCAADM", 110),
                    Map.entry("APPSBAADM", 110),
                    Map.entry("APPSLTADM", 110),
                    Map.entry("APPSGFQ", 110)
            );
        }
        return queuePriorities;
    }

    @Override
    public String determineFinalQueue(List<EditingResponse> editingResponses, String templateName, ICRBuffer icrBuffer) {
        String finalQueue = "IMAGLPHLDQ";
        var minResponse = editingResponses.stream().filter(er -> er.getQueue() != null).min(Comparator.comparingInt(er -> getQueuePriorities().get(er.getQueue())));
        if (minResponse.isPresent()) {
            finalQueue = minResponse.get().getQueue();
        }

        // If member does Not need to go to ESP (in this case, it is either auto issued or can be approved/declined in house),
        // If replacement queue flag is Yes, check to see if the application is Senior Protect (APPSLT0116 template).
        // If it is Senior Protect, check to see if auto declination flag is Yes,

        if (editingResponses.stream().noneMatch(editingResponse -> editingResponse.getQueue() != null && editingResponse.getQueue().contains("EMSI")) &&
                finalQueue.equals("APPSREPLQ") &&
                templateName.equals("APPSLT0116") &&
                editingResponses.stream().anyMatch(EditingResponse::isAutoDecline)
        ) {
            // If it is move ‘MEDIDECLQ’ to final target queue
            finalQueue = "MEDIDECLQ";
        }

        if (finalQueue.contains("EMSI")
                && editingResponses.stream().anyMatch(editingResponse -> editingResponse.getQueue() != null && editingResponse.getQueue().contains("FSRO"))) {
            finalQueue = getFsroEmsiQueue(icrBuffer);
        }

        return finalQueue;
    }

    private String getFsroEmsiQueue(ICRBuffer icrBuffer) {
        return icrBuffer.isCasAgentFound() ? "FSROCAEMSI" : "FSROEMSIQ";
    }

    private String getAppsEmsiQueue(ICRBuffer icrBuffer, EditingSubject... subject) {
        if (icrBuffer.getPolicyType() == null) {
            editPolicyType(icrBuffer);
        }
        if (subject != null && Arrays.asList(subject).contains(EditingSubject.MEMBER)) {
            icrBuffer.setMemberGoesToESP(true);
        } else if (subject != null && Arrays.asList(subject).contains(EditingSubject.SPOUSE)) {
            icrBuffer.setSpouseGoesToESP(true);
        }
        if (icrBuffer.getPolicyType().startsWith("BA")) {
            return "APPSEMSIBA";
        } else if (icrBuffer.getPolicyType().startsWith("LT")) {
            return icrBuffer.isCasAgentFound() ? "APPSEMSICA" : "APPSEMSILT";
        } else {
            return "APPSEMSILT";
        }

    }

    private String getFsroNonEmsiQueue(ICRBuffer icrBuffer) {
        if (icrBuffer.getPolicyType() == null) {
            editPolicyType(icrBuffer);
        }
        if (icrBuffer.getPolicyType().startsWith("GF")) {
            return "FSROGFQ";
        } else if (icrBuffer.getPolicyType().startsWith("BA")) {
            return "FSROBAADM";
        } else if (icrBuffer.isCasAgentFound()) {
            return "FSROCAADM";
        } else if ("PR".equals(icrBuffer.getDdApps().getContractState())) {
            return "FSROPRADM";
        } else {
            return "FSROLTADM";
        }
    }

    private String getAppsNonEmsiQueue(ICRBuffer icrBuffer) {
        if (icrBuffer.getPolicyType() == null) {
            editPolicyType(icrBuffer);
        }
        if (icrBuffer.getPolicyType().startsWith("GF")) {
            return "APPSGFQ";
        } else if (icrBuffer.getPolicyType().startsWith("BA")) {
            return "APPSBAADM";
        } else if (List.of(APPSLT0116, APPSLT0322).contains(Template.valueOf(icrBuffer.getDdApps().getTemplateName()))) {
            return "APPSLT121";
        } else if (icrBuffer.isCasAgentFound()) {
            return "APPSCAADM";
        } else {
            return "APPSLTADM";
        }
    }

    private Integer getPayFrequencyMultiple(String payFrequency) {
        if (payFrequency == null) {
            return 1;
        }
        switch (payFrequency) {
            case "Q":
                return 3;
            case "S":
                return 6;
            case "A":
                return 12;
            case "M":
            default:
                return 1;
        }
    }

    private EditingResponse editAgentCode(ICRBuffer icrBuffer, String agentCode) {
        // if either of agent sign flag and agent sign date is available and agent code is missing
        if ((icrBuffer.getDdApps().getAgentSignFlag() != null || icrBuffer.getDdApps().getAgentSignDate() != null) &&
                StringUtils.isEmpty(agentCode)
        ) {
            // return FSRO queue with comment.
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("006A")
                    .autoDecline(false)
                    .build();
        }
        // If there is no agent involved
        if (StringUtils.isEmpty(agentCode)) {
            // put default home office agent Code AVA01
            icrBuffer.getDdApps().setAgentCode(DEFAULT_AGENT);
            return EditingResponse.builder().build();
        }

        // Verify if the agent is CAS agent.
        icrBuffer.setCasAgentFound(
                lifeProApiService.verifyIfCaseAgent(
                        agentCode,
                        icrBuffer.getCompanyCode(),
                        icrBuffer.getDdApps().getAgentSignDate() == null ? LocalDate.now() : icrBuffer.getDdApps().getAgentSignDate())
        );

        // Verify if the agent's license for the state is active and is not expired.
        var licenseError = lifeProApiService.verifyIfLicenseActive(
                icrBuffer.getCompanyCode(),
                agentCode,
                icrBuffer.getDdApps().getContractState(),
                icrBuffer.getDdApps().getAgentSignDate() == null ? icrBuffer.getDdApps().getMemberSignDate() : icrBuffer.getDdApps().getAgentSignDate()
        );
        if (StringUtils.isNotEmpty(licenseError)) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(licenseError)
                    .autoDecline(false)
                    .build();
        }
        return EditingResponse.builder().build();
    }

    // Editing Rules
    @Override
    public EditingResponse editAgentCode(ICRBuffer icrBuffer) {
        icrBuffer.setAgent1CommissionPercentage(100);
        return editAgentCode(icrBuffer, icrBuffer.getDdApps().getAgentCode());
    }

    @Override
    public EditingResponse editPolicyType(ICRBuffer icrBuffer) {
        // Move value to POLTYPE
        // ID3Loadata
        if (List.of("APPSGF0901", "APPSGF0317").contains(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPolicyType("GF");
        } else if ("APPSBA0217".equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPolicyType("BA85");
        } else if (List.of("APPSEP1009", "APPSPR1009").contains(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPolicyType("15".equals(icrBuffer.getDdApps().getAgentLevel()) ? "LT15" : "LT20");
        } else if ("APPSLT0116".equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPolicyType("LT98");
        } else if ("APPSCT1013".equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPolicyType("LT10");
        }
        // TERMEDDTDATA
        else if (Template.valueOf(icrBuffer.getDdApps().getTemplateName()).isTermedtdta()) {
            icrBuffer.setPolicyType("BA85");
        } else {
            icrBuffer.setPolicyType("LT");
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editPayMode(ICRBuffer icrBuffer) {
        // if PayMode == 0
        if ("0".equals(icrBuffer.getDdApps().getPayMode())) {
            // memberDataStructure.paymode = A
            icrBuffer.setPayMode("A");
        }
        // else if PayMode == L
        else if ("L".equals(icrBuffer.getDdApps().getPayMode())) {
            // memberDataStructure.paymode = E
            icrBuffer.setPayMode("E");
            // memberDataStructure.listBillId = memberData.listBillId
        }
        // else if PayMode == P
        else if ("P".equals(icrBuffer.getDdApps().getPayMode())) {
            // memberDataStructure.paymode = B
            icrBuffer.setPayMode("B");
        }
        // else if Fed Protect
        else if (APPSLT0322.toString().equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPayMode("F");
        } else {
            // memberDataStructure.paymode = paymode
            icrBuffer.setPayMode(icrBuffer.getDdApps().getPayMode());
        }

        // PayMode frequency
        switch (icrBuffer.getPayMode()) {
            // Quarterly
            case "6":
                icrBuffer.setPayModeFrequency("Q");
                break;
            // Semi Annually
            case "7":
                icrBuffer.setPayModeFrequency("S");
                break;
            // Annually
            case "8":
                icrBuffer.setPayModeFrequency("A");
                break;
            // Payroll
            case "P":
                icrBuffer.setPayModeFrequency("B");
                break;
            // Fed Protect
            case "F":
                icrBuffer.setPayModeFrequency("C");
                break;
            // Monthly
            default:
                icrBuffer.setPayModeFrequency("M");
        }


        // TERMEDTDTA
        if (List.of(APPSSS0810, APPSSS0818, APPSCO0810).contains(Template.valueOf(icrBuffer.getDdApps().getTemplateName()))) {
            icrBuffer.setPayMode("3");
            icrBuffer.setPayModeFrequency("M");
        } else if (APPSNG0702.toString().equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setPayMode("+");
            icrBuffer.setPayModeFrequency("M");
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editProcType(ICRBuffer icrBuffer) {
        // Move ‘I’ to PROCTYPE of ID342 parameter.
        icrBuffer.setProcType("I");
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAgentCode2(ICRBuffer icrBuffer) {
        var agentCode2 = StringUtils.isNotEmpty(icrBuffer.getDdApps().getAgentCode2())
                ? icrBuffer.getDdApps().getAgentCode2()
                : icrBuffer.getDdApps().getAgentMarketCode();

        // If both Agent codes are specified, move 50 to FIELD1 and FIELD2 of LONG-DATA-BUFFER (These are the agent commission percentage)
        if (StringUtils.isNotEmpty(agentCode2)) {
            if (agentCode2.equals(icrBuffer.getDdApps().getAgentCode())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("171D")
                        .build();
            }
            icrBuffer.setAgent1CommissionPercentage(50);
            icrBuffer.setAgent2CommissionPercentage(50);

            return editAgentCode(icrBuffer, agentCode2);

        } else {
            icrBuffer.setAgent1CommissionPercentage(100);
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editPremiumPaid(ICRBuffer icrBuffer) {

        // if PayMode and PayModeFrequency is not yet edited, edit that first.
        if (icrBuffer.getPayMode() == null) {
            editPayMode(icrBuffer);
        }

        // If pay mode is quarterly, semi-annually or annually this is a required Field
        if (
                List.of("Q", "S", "A").contains(icrBuffer.getPayModeFrequency())
                        && (icrBuffer.getDdApps().getPremiumPaid() == null
                        || BigDecimal.ZERO.compareTo(icrBuffer.getDdApps().getPremiumPaid()) <= 0)
        ) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("043A")
                    .build();
        }

        // Move PREM-PAID of data structure to AMOUNT-PAID of LONG-DATA-BUFFER
        if (icrBuffer.getDdApps().getMemberCoverUnit() > 0 && icrBuffer.getDdApps().getSpouseCoverUnit() == 0) {
            icrBuffer.setMemberAmountPaid(icrBuffer.getDdApps().getPremiumPaid());
        } else if (icrBuffer.getDdApps().getMemberCoverUnit() > 0 && icrBuffer.getDdApps().getSpouseCoverUnit() > 0) {
            var premiumPaid = icrBuffer.getDdApps().getPremiumPaid();
            var memberTotalPremium =
                    icrBuffer.getDdApps().getMemberPremium()
                            .add(icrBuffer.getDdApps().getChildPremium())
                            .multiply(BigDecimal.valueOf(getPayFrequencyMultiple(icrBuffer.getPayModeFrequency())));
            var spouseTotalPremium = icrBuffer.getDdApps().getSpousePremium()
                    .multiply(BigDecimal.valueOf(getPayFrequencyMultiple(icrBuffer.getPayModeFrequency())));
            if (premiumPaid.compareTo(memberTotalPremium) > 0) {
                icrBuffer.setMemberAmountPaid(memberTotalPremium);
                premiumPaid = premiumPaid.subtract(memberTotalPremium);
            } else {
                icrBuffer.setMemberAmountPaid(premiumPaid);
                premiumPaid = BigDecimal.ZERO;
            }
            if (premiumPaid.compareTo(spouseTotalPremium) > 0) {
                icrBuffer.setSpouseAmountPaid(spouseTotalPremium);
                premiumPaid = premiumPaid.subtract(spouseTotalPremium);
            } else {
                icrBuffer.setSpouseAmountPaid(premiumPaid);
                premiumPaid = BigDecimal.ZERO;
            }
            if (premiumPaid.compareTo(BigDecimal.ZERO) > 0) {
                icrBuffer.setMemberAmountPaid(icrBuffer.getMemberAmountPaid().add(premiumPaid));
            }
        } else if (icrBuffer.getDdApps().getMemberCoverUnit() == 0 && icrBuffer.getDdApps().getSpouseCoverUnit() > 0) {
            icrBuffer.setSpouseAmountPaid(icrBuffer.getDdApps().getPremiumPaid());
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAffiliationCode(ICRBuffer icrBuffer) {
        // TERMEDTDTA
        if (List.of("APPSSS0810", "APPSSS0818", "APPSCO0810").contains(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setAffiliateClientId("TSA");
        } else if ("APPSNG0702".equals(icrBuffer.getDdApps().getTemplateName())) {
            icrBuffer.setAffiliateClientId(
                    String.format(
                            "MAS%s",
                            Objects.toString(icrBuffer.getDdApps().getContractState(), "")
                    )
            );
        }
        // ID3LOADATA
        else {
            // Use the affiliation code of data structure to read against AFFCLIENT table
            var affiliationClient = affclientService.findById(icrBuffer.getDdApps().getAffCode());
            if (affiliationClient.isPresent()) {
                icrBuffer.setAffiliateClientId(affiliationClient.get().getAcClientId());
            }
            // If record not found and the first 5 bytes of agent code is equal to ‘CTX70’ set ‘BENARCH’ to the client ID of LONG-DATA-BUFFER
            else if (icrBuffer.getDdApps().getAgentCode() != null && icrBuffer.getDdApps().getAgentCode().startsWith("CTX70")) {
                icrBuffer.setAffiliateClientId("BENARCH");
            }
            // If we still cannot get client ID
            else if (List.of("APPSEP1009", "APPSPR1009", "APPTLT0116").contains(icrBuffer.getDdApps().getTemplateName())) {
                // PR
                if ("PR".equals(icrBuffer.getDdApps().getContractState()) &&
                        icrBuffer.getDdApps().getAgentCode() != null &&
                        (icrBuffer.getDdApps().getAgentCode().startsWith("EVA360") ||
                                icrBuffer.getDdApps().getAgentCode().startsWith("EPR")
                        )
                ) {
                    if ("P".equals(icrBuffer.getPayMode())) {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "IMC-GOV PAYROLL" : "IMC-ESP PAYROLL");
                    } else if ("9".equals(icrBuffer.getPayMode())) {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "IMC-GOV 1199" : "IMC-ESP 1199");
                    } else {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "IMC-GOV" : "IMC-ESP");
                    }
                }
                // Non PR
                else {
                    if ("P".equals(icrBuffer.getPayMode())) {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "GOV PAYROLL" : "ESP PAYROLL");
                    } else if ("9".equals(icrBuffer.getPayMode())) {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "GOV 1199" : "ESP 1199");
                    } else {
                        icrBuffer.setAffiliateClientId("G".equals(icrBuffer.getDdApps().getEspGovInd()) ? "GOV" : "ESP");
                    }
                }
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editMedicalRejection(ICRBuffer icrBuffer, Long ssn, EditingSubject subject) {
        // find any record in id2Reject against member's SSN.
        var id3Rejects = id3REJECTService.findAll(Pageable.unpaged(), Map.of("ssnNo", ssn));
        // if member SSN is in reject table
        // and reject status starts with following characters.
        for (var id3Reject : id3Rejects) {
            if (!StringUtils.isEmpty(id3Reject.getRejectCauseCode())
                    && Stream.of("M", "N", "S", "U", "X", "Y").anyMatch(s -> id3Reject.getRejectCauseCode().startsWith(s))) {
                // Set queue to APPSESP
                return EditingResponse
                        .builder()
                        .queue(getAppsEmsiQueue(icrBuffer, subject))
                        .commentCode("139")
                        .autoDecline(false)
                        .build();
            }
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAgentSignFlag(ICRBuffer icrBuffer) {
        if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getAgentCode())
                && !DEFAULT_AGENT.equals(icrBuffer.getDdApps().getAgentCode())
                && !Boolean.TRUE.equals(icrBuffer.getDdApps().getAgentSignFlag())) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("044")
                    .build();
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAgentSignDate(ICRBuffer icrBuffer) {
        if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getAgentCode())
                && !DEFAULT_AGENT.equals(icrBuffer.getDdApps().getAgentCode())
                && icrBuffer.getDdApps().getAgentSignDate() == null
        ) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("045")
                    .build();
        }
        if (icrBuffer.getDdApps().getAgentSignDate() != null) {
            // Agent sign date is greater than system date.
            if (icrBuffer.getDdApps().getAgentSignDate().isAfter(LocalDate.now())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("081")
                        .build();
            }
            // Agent sign date is older than 60 days.
            if (icrBuffer.getDdApps().getAgentSignDate().until(LocalDate.now(), ChronoUnit.DAYS) > 60) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("040C")
                        .build();
            }
            // if Agent sign date is lesser than applicant date.
            if (icrBuffer.getDdApps().getMemberSignDate() != null &&
                    icrBuffer.getDdApps().getAgentSignDate().isBefore(icrBuffer.getDdApps().getMemberSignDate())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("046")
                        .build();
            }
            // if Agent sign date is lesser than spouse date.
            if (icrBuffer.getDdApps().getSpouseSignDate() != null &&
                    icrBuffer.getDdApps().getAgentSignDate().isBefore(icrBuffer.getDdApps().getSpouseSignDate())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("046A")
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAgentLevel(ICRBuffer icrBuffer) {
        // No Editing Rules.
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editMedicalUnderwriting(ICRBuffer icrBuffer
            , Long ssn, LocalDate dateOfBirth, LocalDate signDate, Integer coverageUnit
            , EditingSubject subject) {

        if (signDate == null && EditingSubject.SPOUSE.equals(subject)) {
            signDate = icrBuffer.getDdApps().getMemberSignDate();
        }

        if (dateOfBirth == null || signDate == null) {
            return EditingResponse.builder().build();
        }

        // Calculate true age.
        var age = DateHelper.getAge(dateOfBirth, signDate);

        // Use applicant’s age and coverage amount to perform medical underwriting.
        var prodType = icrBuffer.getPolicyType().substring(0, 2);
        var medicalUnderwritingReq = new MedicalUnderwritingReq();
        medicalUnderwritingReq.setSsn(String.valueOf(ssn));
        medicalUnderwritingReq.setPolicyType(prodType);
        medicalUnderwritingReq.setAge(age[0]);
        medicalUnderwritingReq.setAppCoverageAmount(Double.valueOf(coverageUnit));
        medicalUnderwritingReq.setActiveFlag("BA".equals(prodType) && EditingSubject.MEMBER.equals(subject) ? 1 : 0);
        medicalUnderwritingReq.setDeployFlag(false);
        medicalUnderwritingReq.setLtEspFlag("LT".equals(prodType) && EditingSubject.MEMBER.equals(subject));
        var medicalUnderwritingRes = indexingService.performMedicalUnderwriting(medicalUnderwritingReq);
        // The applicant age is outside product range
        if ("Y".equals(medicalUnderwritingRes.getProductFlag())) {
            return EditingResponse
                    .builder()
                    .queue(getAppsEmsiQueue(icrBuffer, subject))
                    .commentCode("114")
                    .autoDecline(false)
                    .build();
        }
        // The applicant total coverage amount is greater than 500K.
        if ("Y".equals(medicalUnderwritingRes.getOverallFlag())) {
            return EditingResponse
                    .builder()
                    .queue(getAppsEmsiQueue(icrBuffer, subject))
                    .commentCode("115")
                    .autoDecline(false)
                    .build();
        }
        // If Paramedi and/or EKG is needed, set APPS ESP queue
        var paramediFlag = "Y".equals(medicalUnderwritingRes.getParamediFlag());
        var ekgFlag = "Y".equals(medicalUnderwritingRes.getEkgFlag());
        if (paramediFlag && ekgFlag) {
            return EditingResponse
                    .builder()
                    .queue(getAppsEmsiQueue(icrBuffer, subject))
                    .commentCode(EditingSubject.MEMBER.equals(subject) ? "092A" : "092C")
                    .autoDecline(false)
                    .build();
        } else if (paramediFlag) {
            return EditingResponse
                    .builder()
                    .queue(getAppsEmsiQueue(icrBuffer, subject))
                    .commentCode(EditingSubject.MEMBER.equals(subject) ? "092" : "092B")
                    .autoDecline(false)
                    .build();
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editHeightWeight(ICRBuffer icrBuffer, Integer height, Integer weight, String sex, LocalDate dateOfBirth, LocalDate signDate, EditingSubject subject) {

        // if height or weight not specified.
        if (height == null || weight == null || height == 0 || weight == 0) {
            return EditingResponse.builder()
                    .queue(getAppsEmsiQueue(icrBuffer, subject))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "018" : "018A")
                    .build();
        }

        // Check height boundary
        var heightInFeet = "";
        if (height.toString().length() < 3) {
            heightInFeet = (int) Math.floor(height / 12.0) + String.format("%02d", height % 12);
        } else {
            heightInFeet = height.toString();
        }


        if (signDate == null && EditingSubject.SPOUSE.equals(subject)) {
            signDate = icrBuffer.getDdApps().getMemberSignDate();
        }
        if (dateOfBirth == null || signDate == null) {
            return EditingResponse.builder().build();
        }

        // Calculate true age.
        var age = DateHelper.getAge(dateOfBirth, signDate);

        // Check applicant’s height and weight
        // Adult
        if (age[0] >= 16) {
            var appsWta = appswtaService.findById(heightInFeet);
            if (appsWta.isEmpty()) {
                return EditingResponse.builder()
                        .queue("EMSIDONEQ")
                        .commentCode(subject.equals(EditingSubject.MEMBER) ? "019" : "019A")
                        .build();
            }
            if (weight < appsWta.get().getLowWeight() ||
                    weight > appsWta.get().getHighWeight()) {

                return EditingResponse.builder()
                        .queue("EMSIDONEQ")
                        .commentCode(subject.equals(EditingSubject.MEMBER) ? "020" : "020A")
                        .build();
            }
        }
        // Non Adult
        else {
            var appsWtc = appswtcService.findById(new APPSWTCKey(age[0], age[1], sex));
            if (appsWtc.isEmpty()) {
                return EditingResponse.builder()
                        .queue("EMSIDONEQ")
                        .commentCode("019B")
                        .build();
            }
            if (weight < appsWtc.get().getLowWeight() ||
                    weight > appsWtc.get().getHighWeight()) {
                return EditingResponse.builder()
                        .queue("EMSIDONEQ")
                        .commentCode(subject.equals(EditingSubject.MEMBER) ? "020" : "020A")
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editSSN(ICRBuffer icrBuffer, Long ssn, EditingSubject subject) {
        if (subject.equals(EditingSubject.SPOUSE) && icrBuffer.getSpouseGeneratedSSNCommentCode() != null) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(icrBuffer.getSpouseGeneratedSSNCommentCode())
                    .build();
        } else if (icrBuffer.getMemberGeneratedSSNCommentCode() != null) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(icrBuffer.getMemberGeneratedSSNCommentCode())
                    .build();
        }
        // if SSN is not valid
        if (ssn == null || ssn.toString().length() != 9) {
            var commentCode = "001B";
            // Generate a dummy SSN
            var newSsn = codesFlService.generateNewSsn();
            if (subject.equals(EditingSubject.SPOUSE)) {
                icrBuffer.getDdApps().setSpouseSSN(Long.valueOf(newSsn));
                if (APPSBA0217.toString().equals(icrBuffer.getDdApps().getTemplateName())) {
                    commentCode = "001B";
                } else {
                    commentCode = "001D";
                }
                icrBuffer.setSpouseGeneratedSSNCommentCode(commentCode);
            } else if (subject.equals(EditingSubject.MEMBER)) {
                icrBuffer.getDdApps().setMemberSSN(Long.valueOf(newSsn));
                icrBuffer.setMemberGeneratedSSNCommentCode(commentCode);
            }
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(commentCode)
                    .autoDecline(false)
                    .build();
        }

        // if editing spouse and the spouse SSN is equals to the member's SSN
        if (subject.equals(EditingSubject.SPOUSE) && ssn.equals(icrBuffer.getDdApps().getMemberSSN())) {
            // Generate dummy SSN for spouse.
            icrBuffer.getDdApps().setSpouseSSN(Long.valueOf(codesFlService.generateNewSsn()));
            var commentCode = "001A";
            icrBuffer.setSpouseGeneratedSSNCommentCode(commentCode);
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(commentCode)
                    .autoDecline(false)
                    .build();
        }

        if (List.of("APPSGF0901", "APPSGF0317").contains(icrBuffer.getDdApps().getTemplateName())) {
            // If Suspend policy is found and status reason is DP or Terminated policy and status reason is DC, set APPS queue and auto declination
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberSuspendedWithDPFound() || icrBuffer.isMemberTerminatedWithDCFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpouseSuspendedWithDPFound() || icrBuffer.isSpouseTerminatedWithDCFound()))) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .comment("Found suspended or terminated policy with reason DP or DC")
                        .autoDecline(true)
                        .build();
            }
            // If previous pending or approved Group free policy is found, set FSRO queue and auto declination
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberPendingOrApprovedGroupFreeFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpousePendingOrApprovedGroupFreeFound()))) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .comment("Found existing Group Free policy.")
                        .autoDecline(true)
                        .build();
            }
            // If two previous terminated policies found and the status reason is EX, set FSRO queue and  auto declination.
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberTerminatedWithEXFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpouseTerminatedWithEXFound()))) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .comment("Found previously terminated policies with reason EX.")
                        .autoDecline(true)
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editPreviousPoliciesStatus(ICRBuffer icrBuffer, EditingSubject subject) {
        if (List.of("APPSGF0901", "APPSGF0317").contains(icrBuffer.getDdApps().getTemplateName())) {
            // If Suspend policy is found and status reason is DP or Terminated policy and status reason is DC, set APPS queue and auto declination
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberSuspendedWithDPFound() || icrBuffer.isMemberTerminatedWithDCFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpouseSuspendedWithDPFound() || icrBuffer.isSpouseTerminatedWithDCFound()))) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .comment("Found suspended or terminated policy with reason DP or DC")
                        .autoDecline(true)
                        .build();
            }
            // If previous pending or approved Group free policy is found, set FSRO queue and auto declination
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberPendingOrApprovedGroupFreeFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpousePendingOrApprovedGroupFreeFound()))) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .comment("Found existing Group Free policy.")
                        .autoDecline(true)
                        .build();
            }
            // If two previous terminated policies found and the status reason is EX, set FSRO queue and  auto declination.
            if ((EditingSubject.MEMBER.equals(subject)
                    && (icrBuffer.isMemberTerminatedWithEXFound()))
                    || (EditingSubject.SPOUSE.equals(subject)
                    && (icrBuffer.isSpouseTerminatedWithEXFound()))) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .comment("Found previously terminated policies with reason EX.")
                        .autoDecline(true)
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editInsurerEligibility(ICRBuffer icrBuffer) {
        // It is required
        if (StringUtils.isEmpty(icrBuffer.getDdApps().getMemberEligibility())
                || !List.of("1", "2", "3", "4", "5", "6", "7", "8", "F", "L", "M", "D", "N", "E", "S")
                .contains(icrBuffer.getDdApps().getMemberEligibility())
        ) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("084A")
                    .autoDecline(false)
                    .build();
        }

        // call lifepro service to get previously set BOS/LOB value if does not match return comment.
        /*if (!icrBuffer.getDdApps().getMemberEligibility().equals(icrBuffer.getMemberPreviousLOB())) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Member's LOB does not match with existing profile.")
                    .build();
        }*/
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editInsurerDutyStatus(ICRBuffer icrBuffer) {

        // Required, if eligibility is military (between 1 and 7)
        if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getMemberEligibility())
                && icrBuffer.getDdApps().getMemberEligibility().compareTo("1") >= 0
                && icrBuffer.getDdApps().getMemberEligibility().compareTo("7") <= 0) {

            // if not a valid status
            if (!List.of("2", "3", "5", "7", "I").contains(icrBuffer.getDdApps().getMemberDutyStatus())) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("118A")
                        .build();
            }

            // If duty status is 5 then eligibility should be either 1 or 2
            if ("5".equals(icrBuffer.getDdApps().getMemberDutyStatus())
                    && !List.of("1", "2").contains(icrBuffer.getDdApps().getMemberEligibility())) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("065C")
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editInsurerRank(ICRBuffer icrBuffer) {

        icrBuffer.setRank(icrBuffer.getDdApps().getRank());

        // If cannot validate Rank/Pay Grade due to no BOS.
        if (StringUtils.isEmpty(icrBuffer.getDdApps().getMemberEligibility())) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("024C")
                    .build();
        }

        // Required, if eligibility is military (between 1 and 7)
        if (icrBuffer.getDdApps().getMemberEligibility().compareTo("1") >= 0
                && icrBuffer.getDdApps().getMemberEligibility().compareTo("7") <= 0) {

            // Valid ranks or pay grade for military personnel.
            if (StringUtils.isEmpty(icrBuffer.getDdApps().getRank())) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("024A")
                        .build();
            }

            // Validate against CODESFL to find rank and pay grade (024)
            // Validate as if the Rank column has Rank value in it.
            var codes = this.codesFlService.findBySysCodeTypeAndSysCode("RANK", icrBuffer.getDdApps().getMemberEligibility());
            var code = codes.stream().filter(codesfl -> codesfl.getSysCodeDescription().equals(icrBuffer.getDdApps().getRank())).findFirst();
            if (code.isPresent() && code.get().getSysCodeBuff().length() >= 2) {
                icrBuffer.setPayGrade(code.get().getSysCodeBuff().substring(0, 2));
                return EditingResponse.builder().build();
            }
            // Validate as if the Rank column has Grade value in it.
            var grade = icrBuffer.getDdApps().getRank() == null ? "" : icrBuffer.getDdApps().getRank();
            grade = grade.replace("O", "0");
            grade = grade.length() > 2 ? grade.substring(grade.length() - 2) : grade;
            var finalGrade = grade;
            var gradeCode = codes.stream().filter(codesfl -> codesfl.getSysCodeBuff().startsWith(finalGrade)).findFirst();
            if (gradeCode.isPresent()) {
                icrBuffer.setRank(gradeCode.get().getSysCodeDescription());
                icrBuffer.setPayGrade(finalGrade);
                return EditingResponse.builder().build();
            }
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("024")
                    .build();
        } else {
            if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getRank()) &&
                    !List.of("MR", "MISS", "MRS", "MISS").contains(icrBuffer.getDdApps().getRank())) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("024D")
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editLastName(ICRBuffer icrBuffer, String lastName, EditingSubject subject) {
        if (StringUtils.isEmpty(lastName)) {
            String commentCode;
            switch (subject) {
                case SPOUSE:
                    commentCode = "007A";
                    break;
                case MEMBER:
                default:
                    commentCode = "007";
                    break;
            }
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(commentCode)
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editFirstName(ICRBuffer icrBuffer, String firstName, EditingSubject subject) {
        if (StringUtils.isEmpty(firstName)) {
            String commentCode;
            switch (subject) {
                case SPOUSE:
                    commentCode = "008A";
                    break;
                case MEMBER:
                default:
                    commentCode = "008";
                    break;
            }
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(commentCode)
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editMiddleName(ICRBuffer icrBuffer, String firstName, EditingSubject subject) {
        // No editing rules.
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editDateOfBirth(ICRBuffer icrBuffer, LocalDate dateOfBirth, LocalDate signDate, EditingSubject subject) {

        // If date of birth is null
        if (dateOfBirth == null) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "011" : "011A")
                    .build();
        }

        // If date of birth is greater or equal to current date.
        if (dateOfBirth.compareTo(LocalDate.now()) >= 0) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "010" : "010A")
                    .build();
        }

        // check for DOB matches with previous policies calling lifepro API
        if (EditingSubject.MEMBER.equals(subject) && icrBuffer.getMemberPreviousDOB() != null
                && !dateOfBirth.equals(icrBuffer.getMemberPreviousDOB())) {
            // if DOB does not match generate a system PIN to replace the SSN on app.
            var ssn = codesFlService.generateNewSsn();
            icrBuffer.getDdApps().setMemberSSN(Long.valueOf(ssn));
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Member's DOB does not match with existing profile.")
                    .build();
        } else if ((EditingSubject.SPOUSE.equals(subject) && icrBuffer.getSpousePreviousDOB() != null
                && !dateOfBirth.equals(icrBuffer.getSpousePreviousDOB()))) {
            // if DOB does not match generate a system PIN to replace the SSN on app.
            var ssn = codesFlService.generateNewSsn();
            icrBuffer.getDdApps().setSpouseSSN(Long.valueOf(ssn));
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Spouse's DOB does not match with existing profile.")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editSex(ICRBuffer icrBuffer, String sex, EditingSubject subject) {
        if (sex == null || !List.of("M", "F").contains(sex)) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "015" : "015A")
                    .build();
        }
        // check with existing file with lifepro.
        if ((EditingSubject.MEMBER.equals(subject) && StringUtils.isNotEmpty(icrBuffer.getMemberPreviousSex())
                && !sex.equals(icrBuffer.getMemberPreviousSex()))) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Member's sex does not match with existing profile.")
                    .build();
        } else if ((EditingSubject.SPOUSE.equals(subject) && StringUtils.isNotEmpty(icrBuffer.getSpousePreviousSex())
                && !sex.equals(icrBuffer.getSpousePreviousSex()))) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Spouse's sex does not match with existing profile.")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editSmokerFlag(ICRBuffer icrBuffer, String smokerFlag, EditingSubject subject) {
        if (smokerFlag == null || !List.of("0", "1").contains(smokerFlag)) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "017" : "017A")
                    .build();
        }
        // check with existing file with lifepro.
        if ((EditingSubject.MEMBER.equals(subject)
                && "0".equals(smokerFlag)
                && "T".equals(icrBuffer.getMemberPreviousSmokerClass()))) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Member's smoker flag does not match with existing profile.")
                    .build();
        } else if ((EditingSubject.SPOUSE.equals(subject)
                && "0".equals(smokerFlag)
                && "T".equals(icrBuffer.getSpousePreviousSmokerClass()))) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .comment("Spouse's smoker flag not match existing profile.")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAddressLine1(ICRBuffer icrBuffer, String addressLine1, EditingSubject subject) {
        if (StringUtils.isEmpty(addressLine1)) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("025")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editAddressLine2(ICRBuffer icrBuffer, String addressLine2, EditingSubject subject) {
        // No editing rules.
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editCity(ICRBuffer icrBuffer, String city, EditingSubject subject) {
        if (StringUtils.isEmpty(city)) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("025")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editState(ICRBuffer icrBuffer, String state, EditingSubject subject) {
        if (StringUtils.isEmpty(state)) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("025")
                    .build();
        }
        var stateRecord = stetblpService.findByCrcpCd(state);
        if (stateRecord.isEmpty()) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("025C")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editZipCode(ICRBuffer icrBuffer, Long zipCode, String city, EditingSubject subject) {
        if (zipCode == null || zipCode == 0 || zipCode.toString().length() < 5) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("025")
                    .build();
        }
        var zipPrefix = Long.valueOf(zipCode.toString().substring(0, 5));
        var zipCity1Record = this.zipCity1Service.findByZip1AndCity1(zipPrefix, city);
        if (zipCity1Record.isEmpty()) {
            var zipCity2Record = this.zipCity2Service.findById(zipPrefix);
            if (zipCity2Record.isEmpty() || zipCity2Record.get().getCity2() == null || !zipCity2Record.get().getCity2().equalsIgnoreCase(city)) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("025E")
                        .build();
            }

        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editEmailAddress(ICRBuffer icrBuffer, String emailAddress, EditingSubject subject) {
        // No editing rules.
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editDayPhoneSecondPhone(ICRBuffer icrBuffer, String dayPhone, String secondPhone, EditingSubject subject) {
        if (StringUtils.isEmpty(dayPhone) && StringUtils.isEmpty(secondPhone)
                && ((EditingSubject.MEMBER.equals(subject) && icrBuffer.isMemberGoesToESP())
                || (EditingSubject.SPOUSE.equals(subject) && icrBuffer.isSpouseGoesToESP()))) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("095")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editCoverageUnit(ICRBuffer icrBuffer, Integer coverageUnit, EditingSubject subject) {
        if (coverageUnit == null || coverageUnit == 0) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(EditingSubject.MEMBER.equals(subject) ? "022" : "022A")
                    .build();
        }
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (APPSNG0702.equals(template)) {
            var existingCoverageAmount = EditingSubject.MEMBER.equals(subject)
                    ? icrBuffer.getMemberTotalCoverage()
                    : icrBuffer.getSpouseTotalCoverage();
            if (coverageUnit <= (existingCoverageAmount / 1000)) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode(EditingSubject.MEMBER.equals(subject) ? "137" : "137A")
                        .build();
            }
        }
        // Special rules for SSLI app
        else if (List.of(APPSSS0810, APPSCO0810, APPSSS0818).contains(template)) {
            var existingCoverageAmount = EditingSubject.MEMBER.equals(subject)
                    ? icrBuffer.getMemberTotalCoverage()
                    : icrBuffer.getSpouseTotalCoverage();
            if (coverageUnit <= (existingCoverageAmount / 1000)) {
                if (EditingSubject.MEMBER.equals(subject)) {
                    icrBuffer.setMemberPolicyId(icrBuffer.getMemberOldestPolicyId());
                } else {
                    icrBuffer.setSpousePolicyId(icrBuffer.getSpouseOldestPolicyId());
                }
                if (coverageUnit < (existingCoverageAmount / 1000)) {
                    return EditingResponse.builder()
                            .queue(getFsroNonEmsiQueue(icrBuffer))
                            .commentCode("163")
                            .build();
                }
                // if the coverage amount is equals to the existing coverage on file.
                else {
                    if (EditingSubject.MEMBER.equals(subject)) {
                        icrBuffer.setMemberMoveDocumentToPermanentCase(true);
                    } else {
                        icrBuffer.setSpouseMoveDocumentToPermanentCase(true);
                    }
                }
            }
        }
        // 054 => Applicant's coverage amount is not in 50K increment.
        // Only if not Senior or FED protect.
        if (!List.of(APPSLT0116, APPSLT0322).contains(template)
                && coverageUnit % 50 != 0) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("054")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editChildUnits(ICRBuffer icrBuffer) {
        // 056 => Invalid child indicator.
        if (icrBuffer.getDdApps().getChildUnit() != null
                && icrBuffer.getDdApps().getChildUnit() > 0
                && !"1".equals(icrBuffer.getDdApps().getChildInd())) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("056")
                    .build();
        }
        // 023 => Child coverage selected but child unit = zero.
        if ("1".equals(icrBuffer.getDdApps().getChildInd())
                && (icrBuffer.getDdApps().getChildUnit() == null
                || icrBuffer.getDdApps().getChildUnit() == 0)) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("023")
                    .build();
        }
        if (icrBuffer.getDdApps().getChildUnit() == null) {
            if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getChildHq())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("083A")
                        .build();
            } else {
                return EditingResponse.builder().build();
            }
        }

        var totalChildUnit = icrBuffer.getDdApps().getChildUnit();
        var existingChildUnit = icrBuffer.getMemberExistingChildUnit() == null ? 0 : icrBuffer.getMemberExistingChildUnit();
        existingChildUnit += icrBuffer.getSpouseExistingChildUnit() == null ? 0 : icrBuffer.getSpouseExistingChildUnit();

        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (!template.isTermedtdta()) {
            // Add the existing to incremental.
            totalChildUnit += existingChildUnit;
        } else {
            if (existingChildUnit > totalChildUnit) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("138")
                        .build();
            }
        }
        if (totalChildUnit > 5) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(template.isTermedtdta() ? "083" : "083B")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editEmployerTaxId(ICRBuffer icrBuffer) {
        if ("L".equals(icrBuffer.getDdApps().getPayMode())) {
            if (icrBuffer.getDdApps().getEmploymentTaxId() == null
                    || icrBuffer.getDdApps().getEmploymentTaxId() == 0) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("103C")
                        .build();
            }
            var listBillId = lifeProApiService.getListBillId(icrBuffer.getDdApps().getEmploymentTaxId().toString(), icrBuffer.getCompanyCode());
            if (listBillId == null) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("103D")
                        .build();
            }
            icrBuffer.setListBillId(listBillId);
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editPremiumAmount(ICRBuffer icrBuffer) {
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editChildPremium(ICRBuffer icrBuffer) {
        if (icrBuffer.getDdApps().getChildUnit() != null
                && icrBuffer.getDdApps().getChildUnit() > 0
                && icrBuffer.getDdApps().getChildPremium() != null
                && icrBuffer.getDdApps().getChildPremium().intValue() != icrBuffer.getDdApps().getChildUnit()) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .comment("Child unit does not match child premium on app")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editTotalPremiumAmount(ICRBuffer icrBuffer) {
        // If pay mode is quarterly, semi-annually or annually
        if (
                List.of("Q", "S", "A").contains(icrBuffer.getPayModeFrequency())
                        && (icrBuffer.getDdApps().getPremiumPaid() != null)
        ) {
            var frequencyMultiplier = getPayFrequencyMultiple(icrBuffer.getPayModeFrequency());
            var memberPrem = icrBuffer.getDdApps().getMemberPremium() == null ? BigDecimal.ZERO : icrBuffer.getDdApps().getMemberPremium();
            var spousePrem = icrBuffer.getDdApps().getSpousePremium() == null ? BigDecimal.ZERO : icrBuffer.getDdApps().getSpousePremium();
            var childPrem = icrBuffer.getDdApps().getChildPremium() == null ? BigDecimal.ZERO : icrBuffer.getDdApps().getChildPremium();
            var totalPrem = memberPrem.add(spousePrem).add(childPrem).multiply(BigDecimal.valueOf(frequencyMultiplier));
            if (icrBuffer.getDdApps().getPremiumPaid().compareTo(totalPrem) < 0) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .commentCode("043")
                        .build();
            }
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editOwnerSSN(ICRBuffer icrBuffer) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (APPSCT1013.equals(template)) {
            if (icrBuffer.getDdApps().getMemberSSN() != null &&
                    icrBuffer.getDdApps().getMemberSSN().equals(icrBuffer.getDdApps().getOwnerSSN())) {
                return EditingResponse.builder()
                        .queue(getAppsNonEmsiQueue(icrBuffer))
                        .comment("Owner SSN cannot be same as applicant SSN.")
                        .build();
            }
        } else if (icrBuffer.getDdApps().getOwnerSSN() != null
                && icrBuffer.getDdApps().getOwnerSSN() > 0
                && !icrBuffer.getDdApps().getMemberSSN().equals(icrBuffer.getDdApps().getOwnerSSN())
        ) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("082")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editPayorSSN(ICRBuffer icrBuffer) {
        // No editing rules.
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editApplicationSpecifiedState(ICRBuffer icrBuffer) {
        if (StringUtils.isNotEmpty(icrBuffer.getDdApps().getAppState())) {
            if (!icrBuffer.getDdApps().getAppState().equals(icrBuffer.getDdApps().getContractState())) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("109")
                        .build();
            }
        } else {
            var appState = stateyesService.findById(
                    new STATEEYESSysTemSysStateKey(icrBuffer.getDdApps().getTemplateName(), icrBuffer.getDdApps().getContractState())
            );
            if (appState.isPresent()) {
                return EditingResponse.builder()
                        .queue(getFsroNonEmsiQueue(icrBuffer))
                        .commentCode("109A")
                        .build();
            }
            icrBuffer.getDdApps().setAppState(icrBuffer.getDdApps().getContractState());
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editBeneficialFirstName(ICRBuffer icrBuffer, String firstName, Long ssn, EditingSubject subject) {
        if ((ssn == null || ssn == 0) &&
                StringUtils.isEmpty(firstName)
        ) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("033")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editBeneficialLastName(ICRBuffer icrBuffer, String lastName, Long ssn, EditingSubject subject) {
        if ((ssn == null || ssn == 0) &&
                StringUtils.isEmpty(lastName)
        ) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("033")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editBeneficialSSN(ICRBuffer icrBuffer, Long ssn, EditingSubject subject) {
        var insurerSsn = EditingSubject.MEMBER.equals(subject)
                ? icrBuffer.getDdApps().getMemberSSN()
                : icrBuffer.getDdApps().getSpouseSSN();
        if (ssn != null && ssn != 0 && ssn.equals(insurerSsn)) {
            if (EditingSubject.MEMBER.equals(subject)) {
                icrBuffer.setMemberBeneficiaryInvalid(true);
            } else {
                icrBuffer.setSpouseBeneficiaryInvalid(true);
            }
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("031")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editBeneficialRelationCode(ICRBuffer icrBuffer, String relationCode, EditingSubject subject) {
        if (StringUtils.isNotEmpty(relationCode) &&
                !List.of("CHD", "SPS", "PAR", "SBL", "OTH", "S").contains(relationCode)
        ) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .comment("Invalid beneficiary relation.")
                    .build();
        }
        if ((relationCode.equals("SPS") || relationCode.equals("S")) && EditingSubject.MEMBER.equals(subject)) {
            var ssn = icrBuffer.getDdApps().getMemberBeneficiary1SSN();
            icrBuffer.getDdApps().setMemberBeneficiary1SSN(
                    ssn == null || ssn == 0 ? icrBuffer.getDdApps().getSpouseSSN() : ssn
            );
            var firstName = icrBuffer.getDdApps().getMemberBeneficiary1FirstName();
            icrBuffer.getDdApps().setMemberBeneficiary1FirstName(
                    StringUtils.isEmpty(firstName) ? icrBuffer.getDdApps().getSpouseFirstName() : firstName
            );
            var lastName = icrBuffer.getDdApps().getMemberBeneficiary1LastName();
            icrBuffer.getDdApps().setMemberBeneficiary1LastName(
                    StringUtils.isEmpty(lastName) ? icrBuffer.getDdApps().getSpouseLastName() : lastName
            );
        } else if ((relationCode.equals("SPS") || relationCode.equals("S")) && EditingSubject.SPOUSE.equals(subject)) {
            var ssn = icrBuffer.getDdApps().getSpouseBeneficiarySSN();
            icrBuffer.getDdApps().setSpouseBeneficiarySSN(
                    ssn == null || ssn == 0 ? icrBuffer.getDdApps().getMemberSSN() : ssn
            );
            var firstName = icrBuffer.getDdApps().getSpouseBeneficiaryFirstName();
            icrBuffer.getDdApps().setSpouseBeneficiaryFirstName(
                    StringUtils.isEmpty(firstName) ? icrBuffer.getDdApps().getMemberFirstName() : firstName
            );
            var lastName = icrBuffer.getDdApps().getSpouseBeneficiaryLastName();
            icrBuffer.getDdApps().setSpouseBeneficiaryLastName(
                    StringUtils.isEmpty(lastName) ? icrBuffer.getDdApps().getMemberLastName() : lastName
            );
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editApplicantReplacementIndicator(ICRBuffer icrBuffer) {
        if ("1".equals(icrBuffer.getDdApps().getReplacementIndicator()) &&
                (TransactionSource.STAUNTON.getValue().equals(icrBuffer.getTransactionSource()) ||
                        (TransactionSource.DATA_DIMENSION.getValue().equals(icrBuffer.getTransactionSource())
                                && (icrBuffer.getDdAttachments() == null || icrBuffer.getDdAttachments().isEmpty())))) {
            return EditingResponse.builder()
                    .queue("APPSREPLQ")
                    .commentCode("175")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editStatementOfHealth(ICRBuffer icrBuffer, String hq, EditingSubject subject) {
        if (StringUtils.isEmpty(hq)) {
            return EditingResponse.builder()
                    .queue(getAppsEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "035" : "035A")
                    .build();
        }
        if (hq.contains("1")) {
            return EditingResponse.builder()
                    .queue("EMSIDONEQ")
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "035" : "035A")
                    .autoDecline(true)
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editChildStatementOfHealth(ICRBuffer icrBuffer) {
        if (icrBuffer.getDdApps().getChildUnit() != null
                && icrBuffer.getDdApps().getChildUnit() > 0
                && (StringUtils.isEmpty(icrBuffer.getDdApps().getChildHq()) ||
                icrBuffer.getDdApps().getChildHq().contains("1"))
        ) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("083A")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editGeneralNote(ICRBuffer icrBuffer) {
        if (Boolean.TRUE.equals(icrBuffer.getDdApps().getGeneralNote())) {
            return EditingResponse.builder()
                    .queue(getAppsEmsiQueue(icrBuffer, EditingSubject.MEMBER, EditingSubject.SPOUSE))
                    .commentCode("037")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editApplicantSignFlag(ICRBuffer icrBuffer, Boolean signFlag, EditingSubject subject) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        // if spouse's sign is being validated and the app is not senior protect or fed protect and
        // if member has already signed
        if (EditingSubject.SPOUSE.equals(subject) && !APPSLT0116.equals(template) && !APPSLT0322.equals(template)
                && Boolean.TRUE.equals(icrBuffer.getDdApps().getMemberSignFlag())) {
            // then no need to validate spouse's sign
            return EditingResponse.builder().build();

        }
        if (!Boolean.TRUE.equals(signFlag)) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "061" : "061B")
                    .build();
        }

        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editApplicantSignDate(ICRBuffer icrBuffer, LocalDate signDate, EditingSubject subject) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        // if spouse's sign is being validated and the app is not senior protect or fed protect and
        // if member has already signed
        if (EditingSubject.SPOUSE.equals(subject) && !APPSLT0116.equals(template) && !APPSLT0322.equals(template)
                && Boolean.TRUE.equals(icrBuffer.getDdApps().getMemberSignFlag())) {
            // then no need to validate spouse's sign
            return EditingResponse.builder().build();

        }
        if (signDate == null) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "039" : "039A")
                    .build();
        }
        if (signDate.isAfter(LocalDate.now())) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "080" : "080E")
                    .build();
        }
        if (signDate.until(LocalDate.now(), ChronoUnit.DAYS) > 180) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "040B" : "040A")
                    .build();
        } else if (signDate.until(LocalDate.now(), ChronoUnit.DAYS) > 60) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode(subject.equals(EditingSubject.MEMBER) ? "040" : "040A")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editContractState(ICRBuffer icrBuffer) {
        var stateRecord = stetblpService.findById(new STETBLPKey("US", icrBuffer.getDdApps().getContractState()));
        if (stateRecord.isEmpty()) {
            return EditingResponse.builder()
                    .queue(getFsroNonEmsiQueue(icrBuffer))
                    .commentCode("109")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editParamediOrderFlag(ICRBuffer icrBuffer) {
        if ("1".equals(icrBuffer.getDdApps().getParaMedi())) {
            return EditingResponse.builder()
                    .queue(getAppsEmsiQueue(icrBuffer, EditingSubject.MEMBER, EditingSubject.SPOUSE))
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editSOHInitialFlag(ICRBuffer icrBuffer) {
        if (List.of("APPSEP1009", "APPSPR1009").contains(icrBuffer.getDdApps().getTemplateName())
                && !"1".equals(icrBuffer.getDdApps().getMemberHqInit())) {
            EditingResponse.builder()
                    .queue(getAppsEmsiQueue(icrBuffer, EditingSubject.MEMBER, EditingSubject.SPOUSE))
                    .commentCode("034")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    @Override
    public EditingResponse editNumberOfPages(ICRBuffer icrBuffer, String documentId) {
        var document = this.documentService.findById(documentId);
        var noOfPages = 0;
        if (document.isPresent()) {
            noOfPages = document.get().getDocPage();
        }
        if (noOfPages == 0) {
            EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode("003A")
                    .build();
        }
        if (List.of("APPSPR1009", "APPSCO0810").contains(icrBuffer.getDdApps().getTemplateName())
                && noOfPages != 3
        ) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(noOfPages > 3 ? "003" : "003A")
                    .build();
        } else if (!List.of("APPSPR1009", "APPSCO0810").contains(icrBuffer.getDdApps().getTemplateName())
                && noOfPages != 2) {
            return EditingResponse.builder()
                    .queue(getAppsNonEmsiQueue(icrBuffer))
                    .commentCode(noOfPages > 2 ? "003" : "003A")
                    .build();
        }
        return EditingResponse.builder().build();
    }

    public EditingResponse editMemberOccupationClass(ICRBuffer icrBuffer) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (template.equals(APPSEP1009) || template.equals(APPSPR1009)) {
            icrBuffer.setMemberOccupationClass("C");
        } else if (List.of(APPSLT0116, APPSLT0322, APPSBA0217).contains(template)) {
            var memberElg = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
            if (memberElg.contains(icrBuffer.getDdApps().getMemberEligibility())) {
                var memberDuty = Arrays.asList("3", "S");
                if (memberDuty.contains(icrBuffer.getDdApps().getMemberDutyStatus())) {
                    icrBuffer.setMemberOccupationClass("3");
                } else {
                    icrBuffer.setMemberOccupationClass("A");
                }
            } else if (icrBuffer.getDdApps().getMemberEligibility().equals("S")) {
                icrBuffer.setMemberOccupationClass("S");
            } else {
                icrBuffer.setMemberOccupationClass("C");
            }
        }
        return EditingResponse.builder().build();
    }

    public EditingResponse editMemberEmployeeClassCode(ICRBuffer icrBuffer) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (List.of(APPSLT0116, APPSLT0322).contains(template)) {
            icrBuffer.setMemberEmployeeClassCode("");
        } else if (APPSGF0317.equals(template)) {
            switch (icrBuffer.getDdApps().getMemberEligibility()) {
                case "M":
                    icrBuffer.setMemberEmployeeClassCode("E");
                    break;
                case "F":
                    icrBuffer.setMemberEmployeeClassCode("F");
                    break;
                default:
                    //When eligibility is L or law enforcement
                    icrBuffer.setMemberEmployeeClassCode("P");

            }
        } else if (APPSCT1013.equals(template)) {
            icrBuffer.setMemberEmployeeClassCode("8");
        } else if (List.of(APPSEP1009, APPSPR1009).contains(template)) {
            switch (icrBuffer.getDdApps().getEspGovInd()) {
                case "G":
                    icrBuffer.setMemberEmployeeClassCode("G");
                    break;
                case "L":
                    icrBuffer.setMemberEmployeeClassCode("P");
                    break;
                case "M":
                    icrBuffer.setMemberEmployeeClassCode("E");
                    break;
                default:
                    //When EspGovInd is "" or "F"
                    icrBuffer.setMemberEmployeeClassCode("F");
            }
        }
        return EditingResponse.builder().build();

    }

    public EditingResponse editSpouseEmployeeClassCode(ICRBuffer icrBuffer) {
        var template = Template.valueOf(icrBuffer.getDdApps().getTemplateName());
        if (List.of(APPSLT0116, APPSLT0322).contains(template)) {
            icrBuffer.setSpouseEmployeeClassCode("");
        } else if (List.of(APPSNG0702, APPSSS0810, APPSCO0810, APPSSS0818, APPSBA0217, APPSEP1009, APPSPR1009).contains(template)) {
            icrBuffer.setSpouseEmployeeClassCode("S");
        }
        return EditingResponse.builder().build();

    }

    public EditingResponse editBillDayEpayType(ICRBuffer icrBuffer){
        var ddCredit=ddcreditService.findById(icrBuffer.getDdApps().getTransactionId());
        if(ddCredit.isPresent()){
            if(ddCredit.get().getBillDay()!=null || ddCredit.get().getBillDay().trim().equals("")){
                ddCredit.get().setBillDay("0");
            }
            icrBuffer.setBillDay(ddCredit.get().getBillDay());
            icrBuffer.setEPayType(icrFileService.getEPAYTYPECreditCard(ddCredit.get().getAccountNumber()));

        }
        var ddCheck=ddcheckService.findById(icrBuffer.getDdApps().getTransactionId());
        if (ddCheck.isPresent()){
            if(ddCheck.get().getBillDay()!=null || ddCheck.get().getBillDay().trim().equals("")){
                ddCheck.get().setBillDay("0");
            }
            icrBuffer.setBillDay(ddCheck.get().getBillDay());
            icrBuffer.setEPayType(ddCheck.get().getAccountType());
        }

        return EditingResponse.builder().build();

    }

}
