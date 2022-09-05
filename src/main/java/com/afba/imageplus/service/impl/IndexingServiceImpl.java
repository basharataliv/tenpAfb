package com.afba.imageplus.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.PolicyCaseDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.UserReqMapper;
import com.afba.imageplus.dto.req.BeneficiaryReq;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CreateCaseAddDocumentReq;
import com.afba.imageplus.dto.req.DocumentReq;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.dto.req.UpdateIndexFlagInUseReq;
import com.afba.imageplus.dto.req.UserReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.CreateCaseAddDocumentRes;
import com.afba.imageplus.dto.res.GetIndexPoliciesRes;
import com.afba.imageplus.dto.res.MedicalUnderwritingRes;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.LPAPPLifeProApplication;
import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;
import com.afba.imageplus.repository.sqlserver.EKD0210IndexingRepository;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.CodesFlService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.LifeProApplicationService;
import com.afba.imageplus.service.PROTRMPOLService;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.utilities.DateHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexingServiceImpl extends BaseServiceImpl<EKD0210Indexing, EKD0210DocTypeDocIdKey>
        implements IndexingService {

    private final EKD0210IndexingRepository ekd0210IndexingRepository;
    private final DocumentService documentService;
    private final EKDUserService ekdUserService;
    private final DocumentTypeService documentTypeService;
    private final PolicyService policyService;
    private final UserReqMapper userReqMapper;
    private final CodesFlService codesFlService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final Ekd0350ToCaseResTrans caseDtoTrans;
    private final QueueService queueService;
    private final PROTRMPOLService protrmpolService;
    private final LifeProApiService lifeProApiService;
    private final LifeProApplicationService lifeProApplicationService;
    private final DateHelper dateHelper;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    protected IndexingServiceImpl(EKD0210IndexingRepository ekd0210IndexingRepository, DocumentService documentService,
            EKDUserService ekdUserService, DocumentTypeService documentTypeService, PolicyService policyService,
            UserReqMapper userReqMapper, CodesFlService codesFlService, CaseService caseService,
            CaseDocumentService caseDocumentService, QueueService queueService, Ekd0350ToCaseResTrans caseDtoTrans,
            PROTRMPOLService protrmpolService, LifeProApiService lifeProApiService,
            LifeProApplicationService lifeProApplicationService, DateHelper dateHelper) {
        super(ekd0210IndexingRepository);
        this.ekd0210IndexingRepository = ekd0210IndexingRepository;
        this.documentService = documentService;
        this.ekdUserService = ekdUserService;
        this.documentTypeService = documentTypeService;
        this.policyService = policyService;
        this.userReqMapper = userReqMapper;
        this.codesFlService = codesFlService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.caseDtoTrans = caseDtoTrans;
        this.queueService = queueService;
        this.protrmpolService = protrmpolService;
        this.lifeProApiService = lifeProApiService;
        this.lifeProApplicationService = lifeProApplicationService;
        this.dateHelper = dateHelper;
    }

    @Override
    protected EKD0210DocTypeDocIdKey getNewId(EKD0210Indexing entity) {
        return new EKD0210DocTypeDocIdKey(entity.getDocumentType(), entity.getDocumentId(), entity.getScanDate(),
                entity.getScanTime());
    }

    public void deleteIndexingRequest(String documentId) {
        var ekd0210Opt = ekd0210IndexingRepository.findByDocumentId(documentId);

        if (ekd0210Opt.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD210404);
        } else {
            ekd0210IndexingRepository.delete(ekd0210Opt.get());
        }
    }

    public EKD0210Indexing createIndexingRequest(String documentId) {

        var ekd0310Opt = documentService.findById(documentId);

        if (ekd0310Opt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, documentId);
        }

        if (ekd0310Opt.get().getScanningDateTime() == null || ekd0310Opt.get().getScanningRepId().isEmpty()
                || ekd0310Opt.get().getDocumentType().isEmpty()) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD310001);
        }
        var scanningDateTimeIsNotNull = ekd0310Opt.get().getScanningDateTime() != null;
        if (ekd0210IndexingRepository
                .findById(new EKD0210DocTypeDocIdKey(ekd0310Opt.get().getDocumentType(), documentId,
                        scanningDateTimeIsNotNull ? ekd0310Opt.get().getScanningDateTime().toLocalDate()
                                : ekd0310Opt.get().getScanningDate(),
                        scanningDateTimeIsNotNull ? ekd0310Opt.get().getScanningDateTime().toLocalTime()
                                : ekd0310Opt.get().getScanningTime()))
                .isPresent()) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD210001, documentId);
        }

        var ekd0210 = new EKD0210Indexing();
        ekd0210.setDocumentId(documentId);
        ekd0210.setDocumentType(ekd0310Opt.get().getDocumentType());
        ekd0210.setIndexFlag(false);
        ekd0210.setScanningDateTime(ekd0310Opt.get().getScanningDateTime());
        ekd0210.setScanRepId(ekd0310Opt.get().getScanningRepId());
        ekd0210.setFiller("");
        return ekd0210IndexingRepository.save(ekd0210);
    }

    public EKD0210Indexing getIndexingRequest(String documentId) {
        var ekd0210Record = ekd0210IndexingRepository.findByDocumentId(documentId);

        if (ekd0210Record.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD210404);
        }
        return ekd0210Record.get();
    }

    public EKD0210Indexing updateCaseInUse(String id, UpdateIndexFlagInUseReq updateIndexFlagInUseReq) {
        var ekd0210Record = ekd0210IndexingRepository.findByDocumentId(id);

        if (ekd0210Record.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD210404);
        }
        if (ekd0210Record.get().getIndexFlag().compareTo(updateIndexFlagInUseReq.getIndexFlag()) == 0) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD210002, id);
        }
        ekd0210Record.get().setIndexFlag(updateIndexFlagInUseReq.getIndexFlag());

        return ekd0210Record.get();
    }

    public Page<EKD0210Indexing> getAllIndexAbleDocumentsByDocumentType(Pageable pageable, String documentType) {
        return ekd0210IndexingRepository.findByDocumentTypeAndIndexFlag(pageable, documentType, false);

    }

    @Override
    public Page<EKD0210Indexing> getAllIndexAbleDocuments(Pageable page, Integer secRange) {
        var documentTypeIds = authorizationHelper.getAuthorizedDocumentTypeIds();
        if (documentTypeIds == null) {
            return ekd0210IndexingRepository.findAll(page);
        }
        return ekd0210IndexingRepository.findAllByDocumentTypeIsIn(page, documentTypeIds);
    }

    public GetIndexPoliciesRes performIndexingAndGetPolicies(String documentId, String policyOrSsn, String documentType,
            String documentDescription, String firstName, String middleInitial, String lastName, String policyType,
            String productId, String amountPaid) {

        String policyId = null;
        List<String> policies = new ArrayList<>();
        GetIndexPoliciesRes response = new GetIndexPoliciesRes();
        List<PolicyCaseDto> policyCases = new ArrayList<>();
        Optional<EKD0110DocumentType> documentTypeOpt = documentTypeService.findByDocumentTypeAndIsDeleted(documentType,
                0);
        Optional<EKD0310Document> documentOpt = documentService.findById(documentId);
        if (documentTypeOpt.isPresent()) {
            if (documentOpt.isPresent()) {

                EKD0310Document document = documentOpt.get();
                EKD0110DocumentType documentTypeObj = documentTypeOpt.get();
                // If text SSN passed use system generated SSN
                policyOrSsn = "SSN".equals(policyOrSsn) ? codesFlService.generateNewSsn() : policyOrSsn;

                // If SSN passed, verify that entry exists in EKDUSER.
                // If not found and isAppsDoc then create one else error out.
                if (policyOrSsn.length() == 9) {
                    policies = ekdUserService.getBySsn(policyOrSsn).stream().map(EKDUser::getAccountNumber)
                            .collect(Collectors.toList());

                    if (documentTypeObj.getIsAppsDoc()) {
                        if (policies.isEmpty()) {
                            if (StringUtils.isBlank(policyType) || StringUtils.isBlank(productId)) {
                                return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKDIDX002);
                            } else {
                                policyId = policyService.getUniquePolicyId();
                                policies.add(policyId);
                                // Generate 7 permanent cases against the policy
                                caseService.generatePermanentCases(policyId);

                                // Add entry in EKDUSER
                                UserReq newEkdUser = new UserReq(policyId, policyOrSsn, firstName, lastName,
                                        middleInitial, "Y");
                                ekdUserService.insert(userReqMapper.convert(newEkdUser, EKDUser.class));

                                // Add first entry in LPAPP
                                var lpApp = new LPAPPLifeProApplication();
                                lpApp.setSsn(policyOrSsn);
                                lpApp.setPolicyId(policyId);
                                lpApp.setProdcode(documentTypeObj.getProductCode());
                                lpApp.setPoltype(policyType);
                                lpApp.setProctype("N");
                                lpApp.setReqtype("R");
                                lpApp.setIoptype("A");
                                lpApp.setPsn1Ssn(policyOrSsn);
                                lpApp.setPsn1Type("I");
                                lpApp.setPsn1relsy("INS");
                                lpApp.setPsn1FirstName(firstName);
                                lpApp.setPsn1LastName(lastName);
                                lpApp.setPsn1ad1st("VA");
                                lpApp.setCov1seq("1");
//                                lpApp.setPlnAnnPrm("");
                                lpApp.setNumPmtsDue("0");
//                                lpApp.setSignDate("");
                                lpApp.setAmountPaid(amountPaid);
                                lpApp.setProcessedFlag("N");
                                lpApp.setTimeStamp(dateHelper.getCurrentJulianYearJulianDay()
                                        + dateHelper.reformateDate(LocalTime.now(), "HHmmssSS"));
                                lpApp.setProdId(productId);
                                lpApp.setTransDate(dateHelper.localDateToProvidedFormat(
                                        DateTimeFormatter.ofPattern("yyyyMMdd"), LocalDate.now()));
                                lpApp.setDocId(documentId);
                                lifeProApplicationService.insert(lpApp);

                                // Add second entry in LPAPP
                                var lpApp2 = new LPAPPLifeProApplication();
                                lpApp2.setPolicyId(policyId);
                                lpApp2.setProdcode(documentTypeObj.getProductCode());
                                lpApp2.setReqtype("R");
                                lpApp2.setAmountPaid("0");
                                lpApp2.setProdId(productId);
                                lpApp2.setProcessedFlag("N");
                                lifeProApplicationService.insert(lpApp2);
                            }
                        }
                    } else {
                        if (policies.isEmpty()) {
                            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, policyOrSsn);
                        }
                    }
                    response.setSsn(policyOrSsn);
                }
                // If policy number passed
                else {
                    Optional<EKDUser> ekdUser = ekdUserService.findById(policyOrSsn);
                    if (documentTypeObj.getIsAppsDoc()) {

                        return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKDIDX001);
                    } else {
                        if (ekdUser.isEmpty()) {
                            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, policyOrSsn);
                        }
                        response.setSsn(null);
                        policies.add(policyOrSsn);
                    }
                }

                // Fetch cases foreach policy
                for (String policy : policies) {

                    PolicyCaseDto policyCase = new PolicyCaseDto();
                    List<CaseResponse> cases = caseService.findByCmAccountNumber(policy).stream()
                            .map(caseDtoTrans::caseEntityToCaseRes).collect(Collectors.toList());
                    policyCase.setPolicyId(policy);
                    policyCase.setCases(cases);
                    policyCases.add(policyCase);
                }
                // Update document's type
                document.setDocumentType(documentType);
                documentService.changeDocumentType(documentId, documentType, documentDescription);
                // Prepare response
                response.setCreateNewCase(documentTypeObj.getCreateNewCase());
                response.setDocumentId(documentId);
                response.setIsAppsDoc(documentTypeObj.getIsAppsDoc());
                response.setPolicyCases(policyCases);
                return response;
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, documentId);
            }
        }
        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD110404);
    }

    @Override
    @Transactional
    public CreateCaseAddDocumentRes createCaseAddDocumentAndAssignQueue(CreateCaseAddDocumentReq request,
            String userId) {

        Optional<EKD0310Document> documentOpt = documentService.findById(request.getDocumentId());
        if (documentOpt.isPresent()) {
            EKD0310Document document = documentOpt.get();
            EKD0110DocumentType documentType = documentTypeService
                    .findByDocumentTypeAndIsDeleted(document.getDocumentType(), 0).get();

            // Generate case create request
            CaseCreateReq createCaseReq = new CaseCreateReq();
            createCaseReq.setInitialQueueId(documentType.getDefaultQueueId());
            createCaseReq.setInitialRepId(userId);
            createCaseReq.setLastRepId(userId);
            createCaseReq.setCmAccountNumber(request.getPolicyId());
            createCaseReq.setCmFormattedName(
                    queueService.findById(documentType.getDefaultQueueId()).get().getCaseDescription());
            createCaseReq.setChargeBackFlag("");
            createCaseReq.setFiller("");
            CaseResponse caseResponse = caseService.createCase(createCaseReq);

            // Generate add document to case request
            EKD0315CaseDocument caseDocumentReq = new EKD0315CaseDocument();
            caseDocumentReq.setCaseId(caseResponse.getCaseId());
            caseDocumentReq.setDocumentId(request.getDocumentId());
            caseDocumentReq.setDasdFlag("");
            caseDocumentReq.setScanningDateTime(document.getScanningDateTime());
            caseDocumentReq.setScanningUser(document.getScanningRepId());
            caseDocumentReq.setItemType("");
            caseDocumentReq.setItemInit("");
            caseDocumentService.insert(caseDocumentReq);
            return new CreateCaseAddDocumentRes(caseResponse.getCaseId(), request.getDocumentId());
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD310404, request.getDocumentId());
        }

    }

    @Override
    @Transactional
    public String indexBeneficiaryForm(BeneficiaryReq request, String userId) {

        if (ekdUserService.findById(request.getPolicyId()).isPresent()) {
            // Insert Document
            BaseMapper<EKD0310Document, DocumentReq> documentReqMapper = new BaseMapper<>();
            DocumentReq documentReq = new DocumentReq();
            documentReq.setDoc(request.getDoc());
            documentReq.setDocumentType(request.getDocumentType());
            documentReq.setScanningDateTime(LocalDateTime.now());
            documentReq.setScanningRepId(userId);
            documentReq.setUserLastUpdate(userId);
            documentReq.setDocPage(request.getDocPage());
            EKD0310Document documentRes = documentService
                    .insert(documentReqMapper.convert(documentReq, EKD0310Document.class));

            // Create case
            CreateCaseAddDocumentReq caseDocumentReq = new CreateCaseAddDocumentReq();
            caseDocumentReq.setDocumentId(documentRes.getDocumentId());
            caseDocumentReq.setPolicyId(request.getPolicyId());
            createCaseAddDocumentAndAssignQueue(caseDocumentReq, userId);
            return "Beneficiary form indexed successfully";
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, request.getPolicyId());
        }
    }

    public MedicalUnderwritingRes performMedicalUnderwriting(MedicalUnderwritingReq request) {

        // Fetch Total And Product Coverage Amounts from LifePro API's
        List<Double> coverageAmounts = getCoverageAmount(request.getSsn());
        Double totalProductCoverage = coverageAmounts.get(0);
        Double overallCoverage = coverageAmounts.get(1);
        Double overallCoverageUnit = null;
        Double totalProductCoverageUnit = null;
        MedicalUnderwritingRes response = null;

        /**
         * If policy is not empty that means calling program is EKD0116, Subtract app
         * and terminated policies coverage
         */
        if (!StringUtils.isBlank(request.getPolicyId())) {

            if (ekdUserService.findById(request.getPolicyId()).isPresent()) {

                // Since App coverage passed is expected to be already divided by 1000
                // Hence, need to convert all coverages in same unit before any calculation
                overallCoverage -= request.getAppCoverageAmount() * 1000;
                totalProductCoverage -= request.getAppCoverageAmount() * 1000;

                List<PROTRMPOL> terminatedPolicies = protrmpolService.getbyNewPolicyId(request.getPolicyId());
                for (PROTRMPOL protrmpol : terminatedPolicies) {
                    totalProductCoverage -= protrmpol.getCovAmt();
                }
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, request.getPolicyId());
            }
        }
        overallCoverageUnit = overallCoverage / 1000;
        totalProductCoverageUnit = totalProductCoverage / 1000;

        // Check if BA or LT
        if (request.getPolicyType().equals(ApplicationConstants.POLICY_TYPE_BA)) {
            response = performMedicalUnderwritingForBA(request, totalProductCoverageUnit,
                    request.getAppCoverageAmount());
        } else if (request.getPolicyType().equals(ApplicationConstants.POLICY_TYPE_LT)) {
            response = performMedicalUnderwritingForLT(request, totalProductCoverageUnit,
                    request.getAppCoverageAmount());
        }

        // Check Overall coverage
        response = checkTotalCoverage(request, response, overallCoverageUnit, request.getAppCoverageAmount());
        return response;
    }

    public MedicalUnderwritingRes performMedicalUnderwritingForBA(MedicalUnderwritingReq request,
            final Double totalProductCoverageUnit, final Double appCoverageUnit) {

        final Double appPlusProductCoverage = appCoverageUnit + totalProductCoverageUnit;
        MedicalUnderwritingRes baResponse = new MedicalUnderwritingRes("N", "N", "N", "N", "N");

        if (appCoverageUnit < 50) {
            baResponse.setProductFlag("Y");
            return baResponse;
        }
        // When Active Flag = 1
        if (request.getActiveFlag() == 1) {
            if (request.getAge() < 17) {
                baResponse.setProductFlag("Y");
            } else if (request.getAge() == 17) {
                if (appPlusProductCoverage > 50 && appPlusProductCoverage <= 250) {
                    baResponse.setHomeOfficeFlag("Y");

                } else if (appPlusProductCoverage > 250) {
                    baResponse.setProductFlag("Y");
                }
            } else if (request.getAge() >= 18 && request.getAge() < 40) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");
                }
                if (appPlusProductCoverage > 250) {
                    baResponse.setParamediFlag("Y");
                }
            } else if (request.getAge() >= 40 && request.getAge() < 50) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");
                }
                if (appPlusProductCoverage > 250 && appPlusProductCoverage <= 300) {
                    baResponse.setParamediFlag("Y");
                }
                if (appPlusProductCoverage > 300) {
                    baResponse.setParamediFlag("Y");
                    baResponse.setEkgFlag("Y");
                }
            } else if (request.getAge() >= 50 && request.getAge() < 60) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");

                }
                if (appPlusProductCoverage > 100) {
                    baResponse.setParamediFlag("Y");
                    baResponse.setEkgFlag("Y");
                }
            } else if (request.getAge() >= 60) {
                baResponse.setProductFlag("Y");
            }
        }
        // When Active Flag = 0
        else if (request.getActiveFlag() == 0) {
            if (request.getAge() < 17) {
                baResponse.setProductFlag("Y");
            } else if (request.getAge() == 17) {
                if (appPlusProductCoverage > 50 && appPlusProductCoverage <= 250) {
                    baResponse.setHomeOfficeFlag("Y");
                } else if (appPlusProductCoverage > 250) {
                    baResponse.setProductFlag("Y");
                }
            } else if (request.getAge() >= 18 && request.getAge() < 40) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");
                }
                if (appPlusProductCoverage > 250) {
                    baResponse.setParamediFlag("Y");
                }
            } else if (request.getAge() >= 40 && request.getAge() < 50) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");
                }
                if (appPlusProductCoverage > 150 && appPlusProductCoverage <= 300) {
                    baResponse.setParamediFlag("Y");
                }
                if (appPlusProductCoverage > 300) {
                    baResponse.setParamediFlag("Y");
                    baResponse.setEkgFlag("Y");
                }
            } else if (request.getAge() >= 50 && request.getAge() < 60) {
                if (appPlusProductCoverage > 400) {
                    baResponse.setProductFlag("Y");
                }
                if (appPlusProductCoverage > 50) {
                    baResponse.setParamediFlag("Y");
                    baResponse.setEkgFlag("Y");
                }
            } else if (request.getAge() >= 60) {
                baResponse.setProductFlag("Y");
            }
        }
        return baResponse;
    }

    public MedicalUnderwritingRes performMedicalUnderwritingForLT(MedicalUnderwritingReq request,
            final Double totalProductCoverageUnit, final Double appCoverageUnit) {

        MedicalUnderwritingRes ltResponse = new MedicalUnderwritingRes("N", "N", "N", "N", "N");
        final Double appPlusProductCoverage = appCoverageUnit + totalProductCoverageUnit;
        final String APPSLT0116 = "APPSLT0116";

        if (appCoverageUnit < 50 && !request.getTemplateName().equals(APPSLT0116)) {
            ltResponse.setProductFlag("Y");
            return ltResponse;
        }
        // If age <= 17
        if (request.getAge() <= 17) {
            ltResponse.setProductFlag("Y");
        }
        // If age >= 18 and < 40
        else if (request.getAge() >= 18 && request.getAge() < 40) {
            if (appPlusProductCoverage > 500) {
                ltResponse.setProductFlag("Y");
            }
            if (appPlusProductCoverage > 250) {
                ltResponse.setParamediFlag("Y");
            }
        }
        // If age >= 40 and < 50
        else if (request.getAge() >= 40 && request.getAge() < 50) {
            if (appPlusProductCoverage > 500) {
                ltResponse.setProductFlag("Y");

            } else {
                if (request.getLtEspFlag()) {
                    if (appPlusProductCoverage > 250 && appPlusProductCoverage <= 300) {
                        ltResponse.setParamediFlag("Y");
                    } else if (appPlusProductCoverage > 300) {
                        ltResponse.setParamediFlag("Y");
                        ltResponse.setEkgFlag("Y");
                    }
                } else {
                    if (appPlusProductCoverage > 150 && appPlusProductCoverage <= 300) {
                        ltResponse.setParamediFlag("Y");
                    } else if (appPlusProductCoverage > 300) {
                        ltResponse.setParamediFlag("Y");
                        ltResponse.setEkgFlag("Y");
                    }
                }

            }
        }
        // If age >= 50 and < 65
        else if (request.getAge() >= 50 && request.getAge() < 65) {
            if (appPlusProductCoverage > 500) {
                ltResponse.setProductFlag("Y");
            } else {
                if (request.getAge() <= 55) {
                    if (appPlusProductCoverage > 100) {
                        ltResponse.setParamediFlag("Y");
                        ltResponse.setEkgFlag("Y");
                    }
                } else if (request.getAge() > 55 && request.getAge() <= 59) {
                    if (appPlusProductCoverage > 50) {
                        ltResponse.setParamediFlag("Y");
                        ltResponse.setEkgFlag("Y");
                    }
                } else if (request.getAge() > 59) {
                    ltResponse.setParamediFlag("Y");
                    ltResponse.setEkgFlag("Y");
                }
            }
        }
        // If age >= 65
        else if (request.getAge() >= 65 && !request.getTemplateName().equals(APPSLT0116)) {
            ltResponse.setProductFlag("Y");
        }
        return ltResponse;
    }

    public MedicalUnderwritingRes checkTotalCoverage(MedicalUnderwritingReq request,
            MedicalUnderwritingRes underwritingOutput, final Double overallCoverageUnit, final Double appCoverageUnit) {

        final Double appPlusOverallCoverage = appCoverageUnit + overallCoverageUnit;
        if (request.getAge() >= 1 && request.getAge() <= 17) {
            if (request.getPolicyType().equals(ApplicationConstants.POLICY_TYPE_LT)) {
                if (appPlusOverallCoverage > 50) {
                    underwritingOutput.setOverallFlag("Y");
                }
            } else if (request.getPolicyType().equals(ApplicationConstants.POLICY_TYPE_BA)) {
                if (appPlusOverallCoverage > 250) {
                    underwritingOutput.setOverallFlag("Y");
                } else if (appPlusOverallCoverage > 50 && appPlusOverallCoverage < 250) {
                    underwritingOutput.setHomeOfficeFlag("Y");
                }
            }
        } else if (request.getAge() >= 18 && request.getAge() <= 29 && appPlusOverallCoverage > 500) {
            underwritingOutput.setOverallFlag("Y");
        } else if (request.getAge() >= 30 && request.getAge() <= 39 && appPlusOverallCoverage > 500) {
            underwritingOutput.setHomeOfficeFlag("Y");
        } else if (request.getAge() >= 40 && request.getAge() <= 70 && appPlusOverallCoverage > 500) {
            underwritingOutput.setOverallFlag("Y");
        } else if (request.getAge() > 70) {
            if (request.getTemplateName().equals("APPSLT0116")) {
                if (appPlusOverallCoverage > 500) {
                    underwritingOutput.setOverallFlag("Y");
                }
            } else {
                underwritingOutput.setOverallFlag("Y");
            }
        }
        return underwritingOutput;
    }

    /**
     * Returns Product Coverage on index 0 and Total Coverage on index 1
     */
    public List<Double> getCoverageAmount(String ssn) {

        List<Double> coverageAmounts = Arrays.asList(0.0, 0.0);
        // Fetch policies against provided filtering criteria
        List<PartyRelationshipsRes> partyRelationshipsRes = getPoliciesForCoverageAmount(ssn);

        if (partyRelationshipsRes != null) {
            // Calculating Product Coverage Amount
            coverageAmounts.set(0, calculateCoverage(partyRelationshipsRes));

            // Calculating Total Overall Coverage Amount
            coverageAmounts.set(1, calculateTotalCoverage(partyRelationshipsRes));

        }
        return coverageAmounts;
    }

    private Double calculateCoverage(List<PartyRelationshipsRes> partyRelationshipsRes) {
        double coverage = 0.0;
        List<PartyRelationshipsRes> filterRes = partyRelationshipsRes.stream()
                .filter(p -> !p.getProductCode()
                        .matches("^(CIGNA.*|FC1|FF.*|GFRE.*|GSPWL.*|ISP.*|LT-121SP|LT-121FP|NG.*|SSLI.*)$"))
                .collect(Collectors.toList());
        List<GetBenefitSummaryBaseRes> benefitRes = filterRes.stream().map(res -> {
            var getBenefitSummaryReq = new GetBenefitSummaryReq(
                    lifeProApiService.getCompanyCodeFromLifePro(res.getPolicyNumber()), res.getPolicyNumber(), "Y",
                    UUID.randomUUID().toString(), "string", lifeProCoderId);
            return lifeProApiService.getBenefitSummary(new GetBenefitSummaryBaseReq(getBenefitSummaryReq));
        }).collect(Collectors.toList());
        for (GetBenefitSummaryBaseRes listBenefit : benefitRes) {
            coverage += listBenefit.getBenefitSummaryResult.getGetBenefitSummaryRes().stream()
                    .filter(p -> p.getBenefit().getBenefitType().equals("BA")
                            || p.getBenefit().getBenefitType().equals("BF"))
                    .collect(Collectors.toList()).stream().mapToDouble(GetBenefitSummaryRes::getFaceAmount).sum();
        }

        return coverage;
    }

    private Double calculateTotalCoverage(List<PartyRelationshipsRes> partyRelationshipsRes) {
        double coverage = 0.0;
        List<PartyRelationshipsRes> filterRes = partyRelationshipsRes.stream()
                .filter(p -> !p.getProductCode().matches("^(CIGNA.*|FC1|FF.*|GFRE.*|GSPWL.*|ISP.*|NG.*|SSLI.*)$"))
                .collect(Collectors.toList());
        List<GetBenefitSummaryBaseRes> benefitRes = filterRes.stream().map(res -> {
            var getBenefitSummaryReq = new GetBenefitSummaryReq(
                    lifeProApiService.getCompanyCodeFromLifePro(res.getPolicyNumber()), res.getPolicyNumber(), "Y",
                    UUID.randomUUID().toString(), "string", lifeProCoderId);
            return lifeProApiService.getBenefitSummary(new GetBenefitSummaryBaseReq(getBenefitSummaryReq));
        }).collect(Collectors.toList());

        for (GetBenefitSummaryBaseRes listBenefit : benefitRes) {
            coverage += listBenefit.getBenefitSummaryResult.getGetBenefitSummaryRes().stream()
                    .filter(p -> p.getBenefit().getBenefitType().equals("BA")
                            || p.getBenefit().getBenefitType().equals("BF"))
                    .collect(Collectors.toList()).stream().mapToDouble(GetBenefitSummaryRes::getFaceAmount).sum();

        }

        return coverage;
    }

    public List<PartyRelationshipsRes> getPoliciesForCoverageAmount(String ssn) {

        List<PartyRelationshipsRes> partyRelationshipsRes = null;
        try {
            List<PartySearchRes> partySearchRes = lifeProApiService
                    .partySearchDetails(new PartySearchBaseReq(
                            new PartySearch("N", "S", ssn, UUID.randomUUID().toString(), "string", lifeProCoderId)))
                    .getPartySearchResult().getPartySearchRes();

            if (partySearchRes.size() == 1) {
                partyRelationshipsRes = lifeProApiService
                        .PartyRelationships(new PartyRelationshipBaseReq(
                                new PartyRelationshipReq(Integer.parseInt(partySearchRes.get(0).getName_id()),
                                        UUID.randomUUID().toString(), "string", lifeProCoderId)))
                        .getGetPartyRelationshipsResult().getPartyRelationshipsResp().stream()
                        .filter(p -> p.getRelateCode().equals("IN") && p.getContractCode().matches("^[S|A|P]$"))
                        .collect(Collectors.toList());

            }
        } catch (Exception e) {
            log.error("Error occurred while fetching policies for GETCOVAMT against ssn {}", ssn);
        }
        return partyRelationshipsRes;
    }

    @Override
    public List<DocumentTypeDescriptionDocumentCountRes> getDocumentTypeDocumentDescriptionCount() {
        var documentTypeIds = authorizationHelper.getAuthorizedDocumentTypeIds();
        List<String[][]> data;
        if (documentTypeIds == null) {
            data = ekd0210IndexingRepository.findAllDocumentTypesWithCount();
        } else {
            data = ekd0210IndexingRepository.findDocumentTypesWithCount(documentTypeIds);
        }
        List<DocumentTypeDescriptionDocumentCountRes> arraylist = new ArrayList<>();
        data.forEach(data2 -> {
            var document = new DocumentTypeDescriptionDocumentCountRes();
            document.setDocumentType(data2[0][0]);
            document.setDocumentDescription(data2[1][0]);
            document.setCount(data2[2][0]);
            arraylist.add(document);
        });
        return arraylist;
    }

    public Optional<EKD0210Indexing> getOptionalEKD0210ByDocumentId(String documentId) {
        return ekd0210IndexingRepository.findByDocumentId(documentId);
    }
}
