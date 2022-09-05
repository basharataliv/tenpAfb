package com.afba.imageplus.service.impl;

import static com.afba.imageplus.constants.ApplicationConstants.BATCH_JOB_USER;
import static com.afba.imageplus.constants.ApplicationConstants.DOCTYPE_APPSBA;
import static com.afba.imageplus.constants.ApplicationConstants.DOCTYPE_APPSLT;
import static com.afba.imageplus.constants.ApplicationConstants.EMSI_DEFAULT_PEND_DAYS;
import static com.afba.imageplus.constants.ApplicationConstants.EMSI_DONE_QUEUE;
import static com.afba.imageplus.constants.ApplicationConstants.ENQFLR_TIME_PATTERN;
import static com.afba.imageplus.constants.ApplicationConstants.GOTOEMSIQ;
import static com.afba.imageplus.constants.ApplicationConstants.HOT_QUEUE_DEFAULT_PEND_DAYS;
import static com.afba.imageplus.constants.ApplicationConstants.LIFEPRO_SUCCESS_RETURN_CODE;
import static com.afba.imageplus.constants.ApplicationConstants.MEDIATTNQ;
import static com.afba.imageplus.constants.Queues.APPSBAADM;
import static com.afba.imageplus.constants.Queues.APPSCAADM;
import static com.afba.imageplus.constants.Queues.APPSCAEMCA;
import static com.afba.imageplus.constants.Queues.APPSEMSIBA;
import static com.afba.imageplus.constants.Queues.APPSEMSICA;
import static com.afba.imageplus.constants.Queues.APPSEMSILT;
import static com.afba.imageplus.constants.Queues.APPSEMSMAS;
import static com.afba.imageplus.constants.Queues.APPSGFQ;
import static com.afba.imageplus.constants.Queues.APPSINQ;
import static com.afba.imageplus.constants.Queues.APPSLT121;
import static com.afba.imageplus.constants.Queues.APPSLTADM;
import static com.afba.imageplus.constants.Queues.APPSMSADM;
import static com.afba.imageplus.constants.Queues.APPSNGADM;
import static com.afba.imageplus.constants.Queues.APPSQC;
import static com.afba.imageplus.constants.Queues.APPSREPLQ;
import static com.afba.imageplus.constants.Queues.APPSVERIQ;
import static com.afba.imageplus.constants.Queues.CHEKADDQ;
import static com.afba.imageplus.constants.Queues.DELETE;
import static com.afba.imageplus.constants.Queues.DELETES;
import static com.afba.imageplus.constants.Queues.EMSIDONEQ;
import static com.afba.imageplus.constants.Queues.FSROBAADM;
import static com.afba.imageplus.constants.Queues.FSROCAADM;
import static com.afba.imageplus.constants.Queues.FSROCAEMI;
import static com.afba.imageplus.constants.Queues.FSROCAEMSI;
import static com.afba.imageplus.constants.Queues.FSROEMSIQ;
import static com.afba.imageplus.constants.Queues.FSROGFQ;
import static com.afba.imageplus.constants.Queues.FSROLTADM;
import static com.afba.imageplus.constants.Queues.FSRONGADM;
import static com.afba.imageplus.constants.Queues.FSROPRADM;
import static com.afba.imageplus.constants.Queues.IMAGCHEKHQ;
import static com.afba.imageplus.constants.Queues.IMAGCREDHQ;
import static com.afba.imageplus.constants.Queues.IMAGHLDQ;
import static com.afba.imageplus.constants.Queues.IMAGLPHLDQ;
import static com.afba.imageplus.constants.Queues.MEDIINQ;
import static com.afba.imageplus.constants.Queues.MEDIREVWQ;
import static com.afba.imageplus.constants.Queues.MOVE;
import static com.afba.imageplus.constants.Queues.PERMCREDQ;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import com.afba.imageplus.dto.res.CaseCommentRes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.req.PolicySearchBaseReq;
import com.afba.imageplus.api.dto.req.PolicySearchReq;
import com.afba.imageplus.api.dto.req.SearchAgentHierarchyReq;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.HotQueues;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.CaseDocumentsDto;
import com.afba.imageplus.dto.CaseOptionsDto;
import com.afba.imageplus.dto.GETLPOINFODto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.Ekd0116Req;
import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.MedicalUnderwritingReq;
import com.afba.imageplus.dto.req.PendCaseReq;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.CasePendRes;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.res.ClosFlrResponse;
import com.afba.imageplus.dto.res.DeqFlrRes;
import com.afba.imageplus.dto.res.EKD0116Res;
import com.afba.imageplus.dto.res.EnqFlrRes;
import com.afba.imageplus.dto.res.MedicalUnderwritingRes;
import com.afba.imageplus.dto.res.WorkCaseRes;
import com.afba.imageplus.dto.res.search.CaseOptions;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.AFB0660;
import com.afba.imageplus.model.sqlserver.EKD0050NextCase;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.MOVECASEH;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.model.sqlserver.id.EKD0250CaseQueueKey;
import com.afba.imageplus.repository.sqlserver.EKD0050NextCaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0850CaseInUseRepository;
import com.afba.imageplus.service.AFB0660Service;
import com.afba.imageplus.service.CaseCommentService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseInUseService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.EMSITIFFService;
import com.afba.imageplus.service.EditingService.EditingSubject;
import com.afba.imageplus.service.GetLPOInfoService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.MOVECASEHService;
import com.afba.imageplus.service.PNDDOCTYPService;
import com.afba.imageplus.service.PendCaseService;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.StringHelper;


@Service
public class CaseServiceImpl extends BaseServiceImpl<EKD0350Case, String> implements CaseService {

    private final EKD0350CaseRepository ekd0350CaseRepository;
    private final EKD0050NextCaseRepository ekd0050NextCaseRepository;
    private final Ekd0350ToCaseResTrans dtoTrans;
    private final CaseDocumentService caseDocumentService;
    private final DocumentService documentService;
    private final EKD0850CaseInUseRepository ekd0850CaseInUseRepository;
    private final EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;
    private final CaseQueueService caseQueueService;
    private final QueueService queueService;
    private final StringHelper stringHelper;
    private final UserProfileService userProfileService;
    private final CaseInUseService caseInUseService;
    private final PNDDOCTYPService pnddoctypService;
    private final PendCaseService pendCaseService;
    private final DocumentTypeService documentTypeService;
    private final DateHelper dateHelper;
    private final MOVECASEHService moveCaseHService;
    private final ICRFileService icrFileService;
    private final EKDUserService ekdUserService;
    private final LPAUTOISSService lpautoissService;
    private final EMSITIFFService emsitiffService;
    private final AFB0660Service afb0660Service;
    private final LifeProApiService lifeProApiService;
    private final Logger logger = LoggerFactory.getLogger(CaseServiceImpl.class);
    private final CaseCommentService caseCommentService;
    private final IndexingService indexingService;
    private final QCRUNHISService qcrunhisService;
    private final GetLPOInfoService getLPOInfoService;
    private final CaseCommentResMapper caseCommentResponseMapper;

    @Autowired
    public CaseServiceImpl(EKD0350CaseRepository ekd0350CaseRepository,

                           EKD0050NextCaseRepository ekd0050NextCaseRepository, Ekd0350ToCaseResTrans dtoTrans,
                           CaseDocumentService caseDocumentService, DocumentService documentService,
                           EKD0850CaseInUseRepository ekd0850CaseInUseRepository,
                           EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository, CaseQueueService caseQueueService,
                           QueueService queueService, UserProfileService userProfileService, DateHelper dateHelper,
                           CaseInUseService caseInUseService, PNDDOCTYPService pnddoctypService, PendCaseService pendCaseService,
                           DocumentTypeService documentTypeService, StringHelper stringHelper, @Lazy ICRFileService icrFileService,
                           EKDUserService ekdUserService, @Lazy LPAUTOISSService lpautoissService, EMSITIFFService emsitiffService,
                           AFB0660Service afb0660Service, MOVECASEHService moveCaseHService, LifeProApiService lifeProApiService,
                           @Lazy CaseCommentService caseCommentService, @Lazy IndexingService indexingService,
                           QCRUNHISService qcrunhisService, GetLPOInfoService getLPOInfoService,
                           CaseCommentResMapper caseCommentResponseMapper) {

        super(ekd0350CaseRepository);
        this.ekd0350CaseRepository = ekd0350CaseRepository;
        this.ekd0050NextCaseRepository = ekd0050NextCaseRepository;
        this.dtoTrans = dtoTrans;
        this.caseDocumentService = caseDocumentService;
        this.documentService = documentService;
        this.ekd0850CaseInUseRepository = ekd0850CaseInUseRepository;
        this.ekd0315CaseDocumentRepository = ekd0315CaseDocumentRepository;
        this.caseQueueService = caseQueueService;
        this.queueService = queueService;
        this.stringHelper = stringHelper;
        this.userProfileService = userProfileService;
        this.caseInUseService = caseInUseService;
        this.pnddoctypService = pnddoctypService;
        this.pendCaseService = pendCaseService;
        this.documentTypeService = documentTypeService;
        this.afb0660Service = afb0660Service;
        this.dateHelper = dateHelper;
        this.moveCaseHService = moveCaseHService;
        this.icrFileService = icrFileService;
        this.ekdUserService = ekdUserService;
        this.lpautoissService = lpautoissService;
        this.emsitiffService = emsitiffService;
        this.lifeProApiService = lifeProApiService;
        this.caseCommentService = caseCommentService;
        this.indexingService = indexingService;
        this.qcrunhisService = qcrunhisService;
        this.getLPOInfoService = getLPOInfoService;
        this.caseCommentResponseMapper = caseCommentResponseMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CaseResponse createCase(CaseCreateReq req) {
        EKD0350Case caseSaved = null;
        String id = req.getCaseId() == null ? generateCaseId() : req.getCaseId();
        EKD0350Case caseSaving = new EKD0350Case(id, null, null,
                req.getInitialQueueId(), req.getInitialRepId(),
                req.getLastRepId(), CaseStatus.N, null, null,
                req.getCmAccountNumber(), req.getCmFormattedName(), null, null,
                req.getChargeBackFlag(), req.getInitialQueueId(), req.getFiller(), null, null, null,
                null, null, null);
        /*
        * Using setters for below: to populate new and old datetime fields together;
        * avoiding redundant code: see setters
         */
        caseSaving.setScanningDateTime(req.getScanningDateTime());
        caseSaving.setLastUpdateDateTime(req.getLastUpdateDateTime());

        if (ApplicationConstants.permanentCaseDes.contains(req.getCmFormattedName().trim())) {
            caseSaving.setStatus(CaseStatus.U);
            caseSaving.setInitialQueueId(null);
            caseSaving.setCurrentQueueId(null);
            caseSaved = ekd0350CaseRepository.save(caseSaving);
        }
        if (req.getInitialQueueId() != null && !req.getInitialQueueId().isEmpty()) {
            Optional<EKD0150Queue> queueOpt = queueService.findById(req.getInitialQueueId().trim());
            if (queueOpt.isEmpty()) {
                errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404, req.getInitialQueueId());
            }
            if (queueOpt.get().getCaseDescription().equals(req.getCmFormattedName().trim())) {
                caseSaving.setStatus(CaseStatus.A);
                caseSaved = ekd0350CaseRepository.save(caseSaving);
                var scanningDateTimeIsNotNull = caseSaving.getScanningDateTime() != null;
                String date = dateHelper.reformateDate(scanningDateTimeIsNotNull ? caseSaving.getScanningDateTime().toLocalDate() : caseSaving.getScanningDate(), "yyyyMMdd");
                String time = dateHelper.reformateDate(scanningDateTimeIsNotNull ? caseSaving.getScanningDateTime().toLocalTime() : caseSaving.getScanningTime(), "hhmmss");
                caseEnqueue(id, new EnqFLrReq(null, req.getInitialQueueId(), 0 + date + time));
            } else {
                errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150405);
            }

            return dtoTrans.caseEntityToCaseRes(caseSaved);
        }
        caseSaved = ekd0350CaseRepository.save(caseSaving);
        return dtoTrans.caseEntityToCaseRes(caseSaved);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String generateCaseId() {

        Optional<EKD0050NextCase> nextCase = ekd0050NextCaseRepository.findFirstBy();
        if (nextCase.isPresent()) {

            String nextCaseId = stringHelper.addNumberToNumericString(nextCase.get().getNextCase(), 1L);
            nextCase.get().setNextCase(nextCaseId);
            return this.ekd0050NextCaseRepository.save(nextCase.get()).getNextCase();
        } else {
            String nextCaseId = stringHelper.convertNumberToString(0L);
            return this.createNextCaseRecord(new EKD0050NextCase(nextCaseId)).getNextCase();
        }
    }

    private EKD0050NextCase createNextCaseRecord(EKD0050NextCase nextCase) {
        return this.ekd0050NextCaseRepository.save(nextCase);
    }

    @Override
    public BaseResponseDto<String> deleteCase(String caseId) {
        var exists = ekd0850CaseInUseRepository.existsByCaseId(caseId);
        if (!exists) {
            Optional<EKD0350Case> ekd0350CaseOptional = ekd0350CaseRepository.findById(caseId);
            EKD0350Case ekd0350Case;

            if (ekd0350CaseOptional.isPresent()) {
                ekd0350Case = ekd0350CaseOptional.get();
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
            }
            if (ekd0350Case.getStatus().equals(CaseStatus.C)) {

                if (!ekd0315CaseDocumentRepository.existsByCaseId(caseId)) {
                    ekd0350CaseRepository.delete(ekd0350Case);
                } else {
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD315409);
                }
            } else {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350409, ekd0350Case.getStatus());
            }
        } else {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850409);
        }

        return BaseResponseDto.success("Case is deleted");

    }

    @Override
    public void updateCase(CaseUpdateReq req, String caseId) {

        // Check if case id exists
        Optional<EKD0350Case> caseOpt = ekd0350CaseRepository.findById(caseId);
        if (caseOpt.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404, caseId);
            return;
        }
        EKD0350Case caseUpdate = caseOpt.get();
        if (req.getCaseCloseDateTime() != null) {
            caseUpdate.setCaseCloseDateTime(req.getCaseCloseDateTime());
        }
        if (req.getChargeBackFlag() != null) {
            caseUpdate.setChargeBackFlag(req.getChargeBackFlag());
        }
        if (req.getCmAccountNumber() != null) {
            caseUpdate.setCmAccountNumber(req.getCmAccountNumber());
        }
        if (req.getCmFormattedName() != null) {
            caseUpdate.setCmFormattedName(req.getCmFormattedName());
        }
        if (req.getLastUpdateDateTime() != null) {
            caseUpdate.setLastUpdateDateTime(req.getLastUpdateDateTime());
        }
        if (req.getFiller() != null) {
            caseUpdate.setFiller(req.getFiller());
        }
        if (req.getInitialRepId() != null) {
            caseUpdate.setInitialRepId(req.getInitialRepId());
        }
        if (req.getLastRepId() != null) {
            caseUpdate.setLastRepId(req.getLastRepId());
        }
        if (req.getScanningDateTime() != null) {
            caseUpdate.setScanningDateTime(req.getScanningDateTime());
        }
        if (req.getStatus() != null) {
            caseUpdate.setStatus(req.getStatus());
        }
        if (req.getCurrentQueue() != null) {
            caseUpdate.setCurrentQueueId(req.getCurrentQueue());
        }
        ekd0350CaseRepository.save(caseUpdate);
    }

    @Override
    public CaseResponse getCase(String caseId) {

        Optional<EKD0350Case> caseOpt = ekd0350CaseRepository.findById(caseId);
        if (caseOpt.isPresent()) {
            return dtoTrans.caseEntityToCaseRes(caseOpt.get());
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
    }

    @Override
    public Page<EKD0350Case> getAllcases(Pageable pageable) {
        return ekd0350CaseRepository.findAll(pageable);

    }

    @Override
    public byte[] getAllDocumentsFile(String caseId) {
        var caseDocuments = caseDocumentService.getDocumentsByCaseId(caseId);
        if (caseDocuments.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350004);
        }
        return documentService.downloadDocumentsFile(
                caseDocuments.stream().map(EKD0315CaseDocument::getDocument).collect(Collectors.toList()));

    }

    @Override
    @Transactional
    public ClosFlrResponse closeCase(String caseId) {
        List<String> canBeClosed = List.of("N", "A", "U");
        List<String> canNotBeClosed = List.of("C", "P", "W");
        List<String> needsToBeChecked = List.of("U");
        List<String> caseDesc2Byte = List.of("01", "02", "03", "04", "05", "06", "07");
        ClosFlrResponse closFlrResponse = new ClosFlrResponse();
        Optional<EKD0350Case> ekd0350CaseOptional = ekd0350CaseRepository.findById(caseId);

        EKD0350Case ekd0350Case;
        if (ekd0350CaseOptional.isPresent()) {
            ekd0350Case = ekd0350CaseOptional.get();
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        String caseDes = ekd0350Case.getCmFormattedName().substring(0, 2);
        String caseStatus = String.valueOf(ekd0350Case.getStatus());
        if (canNotBeClosed.contains(caseStatus)
                || (needsToBeChecked.contains(caseStatus) && caseDesc2Byte.contains(caseDes))) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350409, caseStatus);

        } else if (canBeClosed.contains(caseStatus)) {

            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String queueId = ekd0350Case.getCurrentQueueId();
            ekd0350Case.setCurrentQueueId("");
            ekd0350Case.setCaseCloseDateTime(
                    LocalDateTime.of(localDate, LocalTime.parse(localTime.format(timeFormatter))));
            ekd0350Case.setStatus(CaseStatus.C);
            ekd0350Case.setLastUpdateDateTime(
                    LocalDateTime.of(localDate, LocalTime.parse(localTime.format(timeFormatter))));
            ekd0350CaseRepository.save(ekd0350Case);

            var scanningDateTimeIsNotNull = ekd0350Case.getScanningDateTime() != null;
            EKD0250CaseQueueKey ekd0250CaseQueueKey = new EKD0250CaseQueueKey(queueId, caseId, "0",
                    scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalDate() : ekd0350Case.getScanningDate(),
                    scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalTime() : ekd0350Case.getScanningTime());
            var caseQueue = caseQueueService.findById(ekd0250CaseQueueKey);
            if (caseQueue.isPresent()) {

                caseQueueService.delete(ekd0250CaseQueueKey);
            }
            closFlrResponse.setMessage("Case is Closed");
        }
        closFlrResponse.setNextCaseId(getNextCaseId());
        return closFlrResponse;
    }

    @Transactional
    @Override
    public EnqFlrRes caseEnqueue(String caseId, EnqFLrReq request) {
        EnqFlrRes response = new EnqFlrRes();
        var ekd0850CaseInUseOptional = ekd0850CaseInUseRepository.findByCaseId(caseId);
        var ekd0250CaseQueueOptional = caseQueueService.findByCaseId(caseId);
        var ekd0350CaseOptional = ekd0350CaseRepository.findById(caseId);
        var userId = "";

        if (ekd0350CaseOptional.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }

        ekd0250CaseQueueOptional.ifPresent(ekd0250CaseQueue -> response.setQueueId(ekd0250CaseQueue.getQueueId()));

        ekd0350CaseOptional.ifPresent(ekd0350Case -> response.setCaseStatus(String.valueOf(ekd0350Case.getStatus())));

        if (ekd0850CaseInUseOptional.isPresent()) {
            EKD0850CaseInUse ekd0850CaseInUse = ekd0850CaseInUseOptional.get();
            if (request.getUserId() != null) {
                userId = request.getUserId();
                if (!ekd0850CaseInUse.getQRepId().equals(request.getUserId())) {
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850401);
                }
            } else {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850401);

            }

        }

        EKD0350Case ekd0350Case = ekd0350CaseOptional.get();
        String caseDescription = StringUtils.isNotEmpty(ekd0350Case.getCmFormattedName())
                && ekd0350Case.getCmFormattedName().length() >= 2 ? ekd0350Case.getCmFormattedName().substring(0, 2)
                        : "";
        List<String> caseDesc2Byte = List.of("01", "02", "03", "04", "05", "06", "07");
        response.setCaseStatus(String.valueOf(ekd0350Case.getStatus()));
        if (ekd0350Case.getStatus().equals(CaseStatus.C) || ekd0350Case.getStatus().equals(CaseStatus.P)
                || ekd0350Case.getStatus().equals(CaseStatus.W)
                || (ekd0350Case.getStatus().equals(CaseStatus.U) && caseDesc2Byte.contains(caseDescription))) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350002);
        } else {
            if (queueService.findById(request.getQueueId()).isEmpty() && !request.getQueueId().equals("")) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404, request.getQueueId());
            }

            if (ekd0350Case.getStatus().equals(CaseStatus.U)) {
                ekd0350Case.setStatus(CaseStatus.N);
            } else {
                ekd0350Case.setStatus(CaseStatus.A);
            }
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime localTime = LocalTime.now();
            ekd0350Case.setCurrentQueueId(request.getQueueId());
            ekd0350Case.setLastRepId(userId);
            ekd0350Case.setLastUpdateDateTime(LocalDateTime.now());
            ekd0350CaseRepository.saveAndFlush(ekd0350Case);
            getEntityManager().detach(ekd0350Case);

            var dateTime = dateHelper.getDateTimeFormatFromStringCombination(request.getCombination().substring(1, 15),
                    "yyyyMMddHHmmss");
            if (dateTime == null) {
                return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD250422);
            }


            if (ekd0850CaseInUseOptional.isPresent()) {
                EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
                ekd0850CaseInUse.setQueueId(ekd0350Case.getCurrentQueueId());
                ekd0850CaseInUse.setCaseId(ekd0350Case.getCaseId());
                ekd0850CaseInUse.setPriority("0");
                ekd0850CaseInUse.setScanDateTime(ekd0350Case.getScanningDateTime());
                ekd0850CaseInUse.setQueueDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.parse(localTime.format(timeFormatter))));
                ekd0850CaseInUse.setFill("");
                ekd0850CaseInUse.setQRepId(userId);
                ekd0850CaseInUseRepository.deleteByCaseId(caseId);
                ekd0850CaseInUseRepository.save(ekd0850CaseInUse);
            } else {
                EKD0250CaseQueue edk0250CaseQueue = new EKD0250CaseQueue();
                edk0250CaseQueue.setCaseId(ekd0350Case.getCaseId());
                edk0250CaseQueue.setQueueId(ekd0350Case.getCurrentQueueId());
                edk0250CaseQueue.setPriority(request.getCombination().substring(0, 1));
                edk0250CaseQueue.setScanDateTime(ekd0350Case.getScanningDateTime());
                edk0250CaseQueue.setQueueDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.parse(localTime.format(timeFormatter))));
                edk0250CaseQueue.setFiller("");
                caseQueueService.deleteByCaseId(caseId);
                caseQueueService.save(edk0250CaseQueue);

            }

        }
        return response;
    }

    @Transactional
    public DeqFlrRes removeCaseFromQueue(String caseId) {

        Optional<EKD0350Case> ekd0350CaseOptional = ekd0350CaseRepository.findById(caseId);

        if (ekd0350CaseOptional.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }

        boolean caseInUse = ekd0850CaseInUseRepository.existsByCaseId(caseId);

        if (caseInUse) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850409);
        }

        EKD0350Case ekd0350Case = ekd0350CaseOptional.get();

        if (!CaseStatus.isValidStatusForDeqFLR(ekd0350Case.getStatus())) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350001);
        }

        Optional<EKD0250CaseQueue> ekd0250CaseQueueOptional = caseQueueService.findByCaseId(caseId);

        if (ekd0250CaseQueueOptional.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD250404);
        }

        EKD0250CaseQueue ekd0250CaseQueue = ekd0250CaseQueueOptional.get();

        DeqFlrRes deqFlrRes = DeqFlrRes.builder().caseStatus(ekd0350Case.getStatus())
                .queueId(ekd0250CaseQueue.getQueueId()).build();

        ekd0350Case.setStatus(CaseStatus.U);
        ekd0350Case.setCurrentQueueId("");

        caseQueueService.deleteByCaseId(caseId);

        return deqFlrRes;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String getNextCaseId() {

        Optional<EKD0050NextCase> nextCase = ekd0050NextCaseRepository.findFirstBy();
        if (nextCase.isPresent()) {
            return stringHelper.addNumberToNumericString(nextCase.get().getNextCase(), 1L);
        } else {
            return stringHelper.convertNumberToString(1L);
        }
    }

    @Override
    protected String getNewId(EKD0350Case entity) {
        return null;
    }

    public EKD0350Case getCaseDocuments(String caseId) {
        var result = ekd0350CaseRepository.findCaseDocumentsByNativeQueryGraph(caseId);
        if (result.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        return result.get();
    }

    @Override
    public List<EKD0350Case> getCaseDocumentsByPolicy(String policyNo) {
        var result = ekd0350CaseRepository.findCaseDocumentsByPolicyByNativeQueryGraph(policyNo);
        if (result.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        return result.get();
    }

    @Override
    public List<EKD0350Case> getCaseDocumentsByPolicyAndCmfnames(String policyNo, String cmFormattedName1,
            String cmFormattedName2) {
        var result = ekd0350CaseRepository.findCaseDocumentsByPolicyAndCmfNameByNativeQueryGraph(policyNo,
                cmFormattedName1, cmFormattedName2);
        if (result.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        return result.get();
    }

    @Override
    public List<CaseDocumentsDto> getCaseDocumentsByPolicyAndCmfnamesByNative(String policyNo, String cmFormattedName1,
            String cmFormattedName2) {
        var result = ekd0350CaseRepository.findCaseDocumentsByPolicyAndFMName(policyNo, cmFormattedName1,
                cmFormattedName2);
        if (result.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }

        return result.get();
    }

    @Override
    public List<EKD0350Case> findByCmAccountNumber(String cmAccountNumber) {
        return ekd0350CaseRepository.findByCmAccountNumber(cmAccountNumber);
    }

    @Override
    public List<EKD0350Case> findByCmAccountNumberAndName(String cmAccountNumber, String cmFormatedName) {
        return ekd0350CaseRepository.findByCmAccountNumberAndCmFormattedNameStartsWith(cmAccountNumber, cmFormatedName);
    }

    @Override
    public List<EKD0350Case> findByCmAccountNumberAndName(String cmAccountNumber, String cmFormatedName1,
            String cmFormatedName2) {
        return ekd0350CaseRepository
                .findFirstByCmFormattedNameStartingWithOrCmFormattedNameStartingWithAndCmAccountNumberOrderByCmFormattedNameAsc(
                        cmFormatedName1, cmFormatedName2, cmAccountNumber);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public WorkCaseRes workCase(String caseId, String userId) {

        EKD0350Case ekd350Case = findByIdOrElseThrow(caseId);

        if (List.of(CaseStatus.P, CaseStatus.N, CaseStatus.A, CaseStatus.U).contains(ekd350Case.getStatus())) {
            if (!ekd0850CaseInUseRepository.existsByCaseId(caseId)) {

                // Set entity for work a case
                Optional<EKD0250CaseQueue> ekd0250CaseQueueOptional = caseQueueService.findByCaseId(caseId);
                EKD0850CaseInUse workCase = new EKD0850CaseInUse();
                workCase.setCaseId(caseId);
                workCase.setPriority("0");
                workCase.setQRepId(userId);
                workCase.setFill("");
                if (ekd0250CaseQueueOptional.isPresent()) {
                    EKD0250CaseQueue ekd0250CaseQueue = ekd0250CaseQueueOptional.get();
                    workCase.setScanDateTime(ekd0250CaseQueue.getScanDateTime());
                    workCase.setQueueDateTime(ekd0250CaseQueue.getQueueDateTime());
                    workCase.setQueueId(ekd0250CaseQueue.getQueueId());
                    // If case is in queue remove it from queue
                    ekd0350CaseRepository.saveAndFlush(ekd350Case);
                    getEntityManager().detach(ekd350Case);
                    caseQueueService.delete(new EKD0250CaseQueueKey(ekd0250CaseQueue.getQueueId(), caseId,
                            ekd0250CaseQueue.getPriority(), ekd0250CaseQueue.getScanDate(),
                            ekd0250CaseQueue.getScanTime()));
                } else {
                    workCase.setScanDateTime(ekd350Case.getScanningDateTime());
                    workCase.setQueueDateTime(LocalDateTime.now());
                }
                ekd0850CaseInUseRepository.save(workCase);
                ekd350Case.setLastRepId(userId);
                ekd350Case.setLastUpdateDateTime(LocalDateTime.now());

                super.update(ekd350Case.getCaseId(), ekd350Case);
                return new WorkCaseRes(userId, caseId);
            } else {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850409);
            }
        } else {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350003);
        }
    }

    @Override
    @Transactional
    public WorkCaseRes releaseCase(String caseId, String userId) {

        EKD0350Case ekd0350Case = findByIdOrElseThrow(caseId);

        EKD0850CaseInUse ekdCaseInUse = caseInUseService.findByIdOrElseThrow(caseId);

        // This queue will be used to put the case back to EKD0250
        final var queueId = ekdCaseInUse.getQueueId();

        // Authorization check; only a user who is currently working, from AFBAJOB,
        // or an Admin can release the case.
        if (!super.authorizationHelper.isAdmin() && !super.authorizationHelper.isRequestFromBatchJob()
                && userId.compareTo(ekdCaseInUse.getQRepId()) != 0) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850409);
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.now();
        ekd0350Case.setLastRepId(userId);
        ekd0350Case.setLastUpdateDateTime(LocalDateTime.of(
                LocalDate.now(), LocalTime.parse(localTime.format(timeFormatter))
        ));
        // Removing lock from the case, so it can be released
        ekd0350CaseRepository.saveAndFlush(ekd0350Case);
        getEntityManager().detach(ekd0350Case);
        caseInUseService.delete(caseId);

        if (CaseStatus.P.equals(ekd0350Case.getStatus())
                || (CaseStatus.U.equals(ekd0350Case.getStatus()) && (ekd0350Case.getCmFormattedName().startsWith("01")
                        || ekd0350Case.getCmFormattedName().startsWith("02")
                        || ekd0350Case.getCmFormattedName().startsWith("03")
                        || ekd0350Case.getCmFormattedName().startsWith("04")
                        || ekd0350Case.getCmFormattedName().startsWith("05")
                        || ekd0350Case.getCmFormattedName().startsWith("06")
                        || ekd0350Case.getCmFormattedName().startsWith("07")))) {
            return new WorkCaseRes(userId, caseId);
        }
        // Putting case back to EKD0250
        var scanningDateTimeIsNotNull = ekd0350Case.getScanningDateTime() != null;
        caseEnqueue(caseId, new EnqFLrReq(null, queueId, "0"
                .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                        scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalDate() : ekd0350Case.getScanningDate()))
                .concat(DateHelper.localTimeToProvidedFormat(ENQFLR_TIME_PATTERN,
                        scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalTime() : ekd0350Case.getScanningTime()
                ))));

        return new WorkCaseRes(userId, caseId);
    }

    @Override
    @Transactional
    public CasePendRes findCaseAndPendIt(String caseId, PendCaseReq req) {
        // If case exists, with statuses A || N
        var ekd0350Case = ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(caseId, 0,
                List.of(CaseStatus.A, CaseStatus.N));
        if (ekd0350Case.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        // If 0850 record exists, for the provided case-id
        var ekd0850CaseInUse = caseInUseService.findByIdOrElseThrow(caseId);
        if (req.getUserId().isEmpty()) {
            return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
        }

        // If 0850 entry has s
        //
        //
        //
        //
        // ame QREPID as provided USER-ID
        if (!ekd0850CaseInUse.getQRepId().equals(req.getUserId())) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD850409);
        }
        var currentQueueId = ekd0850CaseInUse.getQueueId();
        pendCaseWhenTransferringCaseToEMSI(req, ekd0350Case.get(), currentQueueId);
        pendForDocType(req, ekd0350Case.get(), currentQueueId);
        pendForReleaseDate(req, ekd0350Case.get(), currentQueueId);
        pendForDays(req, ekd0350Case.get(), currentQueueId);
        if (!Arrays.asList(CaseStatus.P, CaseStatus.W).contains(ekd0350Case.get().getStatus())) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350006);
        }

        if(caseInUseService.existsById(ekd0350Case.get().getCaseId())){
            ekd0850CaseInUse.setQueueId("");
            caseInUseService.update(ekd0850CaseInUse.getCaseId(), ekd0850CaseInUse);
        }

        return new CasePendRes(req.getReturnQueueId());
    }

    @Override
    @Transactional
    public EKD0350Case findCaseAndUnPendIt(UnPendCaseReq req) {
        // RLSEMSIWT PROCESS
        Optional<EKD0350Case> ekd0350Case;
        if (req.getCaseId() == null || req.getCaseId().isEmpty()) {
            ekd0350Case = ekd0350CaseRepository.findByCmAccountNumberAndStatus(req.getPolicyId(), CaseStatus.W);
            if (ekd0350Case.isEmpty()) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
            }
            unPendCaseFromEMSIWAITProcess(req, ekd0350Case.get());
        } else {
            // If case exists, with statuses P || W
            ekd0350Case = ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(req.getCaseId(), 0,
                    List.of(CaseStatus.P, CaseStatus.W));
            if (ekd0350Case.isEmpty()) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
            }

            var ekd0370PendCase = pendCaseService.findByIdOrElseThrow(req.getCaseId());

            unPendCaseFromIndexOrReIndex(req, ekd0350Case.get(), ekd0370PendCase);
            unPendCaseFromWorkAnyOrWorkQueuedProcess(req, ekd0350Case.get(), ekd0370PendCase);
        }

        boolean caseIsReleased = ekd0350Case.get().getStatus().equals(CaseStatus.A);
        if (!caseIsReleased) {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350007);
        }
        return ekd0350Case.get();
    }

    public void pendCaseWhenTransferringCaseToEMSI(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case,
            final String currentQueueId) {
        // Set case status to W if target queue is EMSIDONEQ || GOTOEMSIQ

        if (pendCaseReq.getTargetQueue() != null && !pendCaseReq.getTargetQueue().isEmpty()
                && Arrays.asList(GOTOEMSIQ, EMSI_DONE_QUEUE).contains(pendCaseReq.getTargetQueue())) {
            // If QueueProfile exists
            queueService.findByIdOrElseThrow(pendCaseReq.getTargetQueue());

            LocalDate now = LocalDate.now();
            LocalTime time = LocalTime.now();
            // If PEND is from HOT QUEUE add 5 days otherwise add ESP_DEFAULT_SUSPEND_DAYS
            var releaseDate = getHotQueues().contains(currentQueueId) ? now.plusDays(HOT_QUEUE_DEFAULT_PEND_DAYS)
                    : now.plusDays(EMSI_DEFAULT_PEND_DAYS);

            logger.info("Pend while transferring case to EMSI");

            // Update 0350 record
            ekd0350Case.setStatus(CaseStatus.W);
            ekd0350Case.setLastUpdateDateTime(LocalDateTime.of(now, time));
            ekd0350Case.setLastRepId(pendCaseReq.getUserId());
            // Using CURRENT QUEUE as RETURN QUEUE
            pendCaseReq.setReturnQueueId(currentQueueId);
            // CURRENT QUEUE should be empty in case of PENDING-W
            ekd0350Case.setCurrentQueueId("");
            // Create 0370 record
            pendCaseService.createRecord(pendCaseReq, ekd0350Case.getCaseId(), releaseDate,
                    ekd0350Case.getCmAccountNumber());
            ekd0350CaseRepository.saveAndFlush(ekd0350Case);
            getEntityManager().detach(ekd0350Case);
            caseInUseService.delete(ekd0350Case.getCaseId());




        }
    }

    public void pendForDays(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, String currentQueueId) {

        boolean caseIsPendedAlready = ekd0350Case.getStatus().equals(CaseStatus.P)
                || ekd0350Case.getStatus().equals(CaseStatus.W);

        if (!caseIsPendedAlready && pendCaseReq.getPendForDays() != null && pendCaseReq.getPendForDays() > 0) {
            logger.info("Pend for days");
            // If QueueProfile exists
            if(currentQueueId==null || currentQueueId.equals("")){
                currentQueueId=getQueueIdForPend(pendCaseReq.getReturnQueueId());
            }
            queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId());

            // Update 0350 record
            ekd0350Case.setStatus(CaseStatus.P);
            ekd0350Case.setLastUpdateDateTime(LocalDateTime.now());
            ekd0350Case.setLastRepId(pendCaseReq.getUserId());
            ekd0350Case.setCurrentQueueId("");

            LocalDate releaseDate = LocalDate.now();

            if (getHotQueues().contains(currentQueueId)) {
                releaseDate = pendCaseReq.getPendForDays() > HOT_QUEUE_DEFAULT_PEND_DAYS
                        ? releaseDate.plusDays(HOT_QUEUE_DEFAULT_PEND_DAYS)
                        : releaseDate.plusDays(pendCaseReq.getPendForDays());
            } else {
                releaseDate = releaseDate.plusDays(pendCaseReq.getPendForDays());
            }
            // Create 0370 record
            pendCaseService.createRecord(pendCaseReq, ekd0350Case.getCaseId(), releaseDate,
                    ekd0350Case.getCmAccountNumber());

        }
    }
    private String getQueueIdForPend(String queueId){
        if(queueId==null || queueId.equals("")){
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD150408);
        }else{
            if(!queueService.existsById(queueId)){
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404,queueId);
            }
            return queueId;
        }
    }
    public void pendForDocType(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, String currentQueueId) {

        boolean caseIsPendedAlready = ekd0350Case.getStatus().equals(CaseStatus.P)
                || ekd0350Case.getStatus().equals(CaseStatus.W);

        if (!caseIsPendedAlready && pendCaseReq.getPendDocType() != null && !pendCaseReq.getPendDocType().isEmpty()) {
            logger.info("Pend for doc type");
            if(currentQueueId==null || currentQueueId.equals("")){
                currentQueueId=getQueueIdForPend(pendCaseReq.getReturnQueueId());
            }
            /*
             * If pendDocType is specified: add defaultSuspendDays from the adjacent EKD0110
             * record to current date and use the resultant as pend-released-date
             */
            pnddoctypService.findByIdOrElseThrow(pendCaseReq.getPendDocType());
            // See if EKD0110DocumentType profile exists against this PNDDOCTYPE
            var docType = documentTypeService.findByDocumentTypeAndIsDeletedOrElseThrow(pendCaseReq.getPendDocType(),
                    0);

            if (docType.getDefaultSuspendDays() < 1) {
                throw new DomainException(HttpStatus.CONFLICT, EKDError.EKD110406.code(),
                        "DocumentType Default Suspend Days must be greater than 0");
            }

            LocalDate releaseDate = getHotQueues().contains(currentQueueId)
                    ? LocalDate.now().plusDays(HOT_QUEUE_DEFAULT_PEND_DAYS)
                    : LocalDate.now().plusDays(docType.getDefaultSuspendDays());

            // Update 0350 record
            ekd0350Case.setStatus(CaseStatus.P);
            ekd0350Case.setLastUpdateDateTime(LocalDateTime.now());
            ekd0350Case.setLastRepId(pendCaseReq.getUserId());
            ekd0350Case.setCurrentQueueId("");


            // If no RETURN QUEUE provided, case CURRENT QUEUE will be used
            if (pendCaseReq.getReturnQueueId() == null || pendCaseReq.getReturnQueueId().isEmpty()) {
                pendCaseReq.setReturnQueueId(currentQueueId);
            } else {
                // If QueueProfile exists
                queueService.findByIdOrElseThrow(pendCaseReq.getReturnQueueId());
            }

            // Create 0370 record
            pendCaseService.createRecord(pendCaseReq, ekd0350Case.getCaseId(), releaseDate,
                    ekd0350Case.getCmAccountNumber());

        }
    }

    public void pendForReleaseDate(PendCaseReq pendCaseReq, EKD0350Case ekd0350Case, String currentQueueId) {

        boolean caseIsPendedAlready = ekd0350Case.getStatus().equals(CaseStatus.P)
                || ekd0350Case.getStatus().equals(CaseStatus.W);

        if (!caseIsPendedAlready && pendCaseReq.getPendReleaseDate() != null
                && !pendCaseReq.getPendReleaseDate().isEmpty()) {
            logger.info("Pend for release date");
            if(currentQueueId==null || currentQueueId.equals("")){
                currentQueueId=getQueueIdForPend(pendCaseReq.getReturnQueueId());
            }

            LocalDate releaseDate = LocalDate.now();

            if (getHotQueues().contains(currentQueueId)) {
                releaseDate = LocalDate.parse(pendCaseReq.getPendReleaseDate(), DateTimeFormatter.BASIC_ISO_DATE)
                        .isAfter(releaseDate.plusDays(HOT_QUEUE_DEFAULT_PEND_DAYS))
                                ? releaseDate.plusDays(HOT_QUEUE_DEFAULT_PEND_DAYS)
                                : LocalDate.parse(pendCaseReq.getPendReleaseDate(), DateTimeFormatter.BASIC_ISO_DATE);
            } else {
                releaseDate = LocalDate.parse(pendCaseReq.getPendReleaseDate(), DateTimeFormatter.BASIC_ISO_DATE);
            }

            // Update 0350 record
            ekd0350Case.setStatus(CaseStatus.P);
            ekd0350Case.setLastUpdateDateTime(LocalDateTime.now());
            ekd0350Case.setLastRepId(pendCaseReq.getUserId());
            ekd0350Case.setCurrentQueueId("");


            // Create 0370 record
            pendCaseService.createRecord(pendCaseReq, ekd0350Case.getCaseId(), releaseDate,
                    ekd0350Case.getCmAccountNumber());

        }
    }

    public void unPendCaseFromWorkAnyOrWorkQueuedProcess(UnPendCaseReq req, EKD0350Case ekd0350Case,
            EKD0370PendCase ekd0370PendCase) {

        if ((req.getCallingProgram().equals(UnPendCallingProgram.WORKANY.name())
                || req.getCallingProgram().equals(UnPendCallingProgram.WORKQUEUED.name()))
                && ekd0350Case.getStatus().equals(CaseStatus.W)) {
            logger.info("Release case from WorkAny or WorkQueued Process");
            basicPendReleaseSteps(req, ekd0350Case, ekd0370PendCase);
        }
    }

    private void basicPendReleaseSteps(UnPendCaseReq req, EKD0350Case ekd0350Case, EKD0370PendCase ekd0370PendCase) {
        ekd0350Case.setLastRepId(req.getUserId());
        if (ekd0350Case.getCurrentQueueId().isEmpty()) {
            ekd0350Case.setCurrentQueueId(ekd0370PendCase.getReturnQueue());
        }
        ekd0350Case.setStatus(CaseStatus.N);

        if (ekd0350Case.getScanningDateTime() == null) {
            throw new DomainException(HttpStatus.CONFLICT, EKDError.EKD350406.code(),
                    String.format("Scanning information is missing against caseId: %s", ekd0350Case.getCaseId()));
        }

        var scanningDateTimeIsNotNull = ekd0350Case.getScanningDateTime() != null;
        EnqFLrReq enqFLrReq = new EnqFLrReq(authorizationHelper.getRequestRepId(), ekd0370PendCase.getReturnQueue(),
                "0".concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                                scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalDate() : ekd0350Case.getScanningDate()))
                        .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                                scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalTime() : ekd0350Case.getScanningTime())));

        pendCaseService.deletePendCaseByCaseId(ekd0350Case.getCaseId());

        // Setting CaseStatus to 'N', so it can be enqueued by ENQFLR
        ekd0350Case.setStatus(CaseStatus.N);
        // This will create only an entry in 0250, when called from this scope
        // ENQFLR change the case status to 'A'
        caseEnqueue(ekd0350Case.getCaseId(), enqFLrReq);
    }

    public void unPendCaseFromEMSIWAITProcess(UnPendCaseReq req, EKD0350Case ekd0350Case) {

        if (req.getCallingProgram().equals(UnPendCallingProgram.RLSEMSIWT.name())) {
            if (Boolean.FALSE.equals(req.getEmsiUser())) {
                throw new DomainException(HttpStatus.UNAUTHORIZED, EKDError.EKD370401.code(),
                        "Only a supervisor is allowed to perform this action.");
            }

            var ekd0370PendCase = pendCaseService.findByIdOrElseThrow(ekd0350Case.getCaseId());

            var ekd0315CaseDocumentList = caseDocumentService.getDocumentsByCaseId(ekd0350Case.getCaseId());

            var documentId = "";
            for (var ekd0315CaseDocument : ekd0315CaseDocumentList) {
                if (ekd0315CaseDocument.getDocument() != null
                        && (ekd0315CaseDocument.getDocument().getDocumentType().startsWith(DOCTYPE_APPSBA)
                                || ekd0315CaseDocument.getDocument().getDocumentType().startsWith(DOCTYPE_APPSLT))) {
                    documentId = ekd0315CaseDocument.getDocument().getDocumentId();
                    break;
                }
            }

            if (documentId.isEmpty()) {
                throw new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD315404.code(), String.format(
                        "Couldn't find any APPSBA or APPSLT document against CASE ID: %s", ekd0350Case.getCaseId()));
            }

            var icrFile = icrFileService.findByIdOrElseThrow(documentId);
            final var templateName = icrFile.getTemplateName();
            var ekdUser = ekdUserService.getByAccountNo(req.getPolicyId());

            if (ekdUser.getSsn().isEmpty()) {
                throw new DomainException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404.code(),
                        String.format("No SSN found against identifier %s in EKDUSER", req.getPolicyId()));
            }

            final var response = lifeProApiService
                    .policySearch(new PolicySearchBaseReq(new PolicySearchReq(req.getPolicyId(), "", "", "")));

            if (response == null || response.getPolicySearchRESTResult() == null
                    || response.getPolicySearchRESTResult().getReturnCode() != LIFEPRO_SUCCESS_RETURN_CODE
                    || response.getPolicySearchRESTResult().getPolicySearchResp().isEmpty()) {
                throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.LPRAPI500.code(),
                        String.format("Error in parsing response from LP PolicySearch service against POLICY ID %s",
                                req.getPolicyId()));
            }

            final var policyDetails = response.getPolicySearchRESTResult().getPolicySearchResp().get(0);
            final var companyCode = policyDetails.getCompanyCode();

            if (companyCode == null || companyCode.isEmpty()) {
                throw new DomainException(HttpStatus.NOT_FOUND, EKDError.LPRAPI404.code(),
                        String.format("Couldn't find COMPANY CODE from PolicySearch service, using POLICY ID: %s",
                                req.getPolicyId()));
            }
            if (icrFile.getIcrBuffer() == null || icrFile.getIcrBuffer().getDdApps() == null) {
                throw new DomainException(HttpStatus.NOT_FOUND, EKDError.DDAPPS404.code(),
                        String.format("Unable to find record in DDAPPS against WAFDOCID: %s", documentId));
            }

            final var agentCode = icrFile.getIcrBuffer().getDdApps().getAgentCode();
            if (agentCode == null) {
                throw new DomainException(HttpStatus.NOT_FOUND, EKDError.DDAPPS404.code(),
                        String.format("Unable to find AGENT CODE in DDAPPS against WAFDOCID: %s", documentId));
            }

            final var isCaseAgent = lifeProApiService.verifyIfCaseAgent(agentCode, companyCode, LocalDate.now());
            final var emsitiffOptional = emsitiffService.findByTifDocIdAndWafDocIdAndProcessFlagAndAcknowledged(
                    icrFile.getIcrBuffer().getDdApps().getTransactionId(), documentId, true, true);

            var targetQueue = identifyTargetQueueInRLSEMSIWT(req, emsitiffOptional, templateName, isCaseAgent);
            // overriding return queue to target queue, only if it is identified in the
            // above logic
            ekd0370PendCase.setReturnQueue(!targetQueue.isEmpty() ? targetQueue : ekd0370PendCase.getReturnQueue());
            logger.info("Release case from EMSIWAIT Process");
            basicPendReleaseSteps(req, ekd0350Case, ekd0370PendCase);
        }
    }

    private String identifyTargetQueueInRLSEMSIWT(UnPendCaseReq req, Optional<EMSITIFF> emsitiff,
            final String templateName, boolean isCaseAgent) {
        var targetQueue = "";
        if (emsitiff.isEmpty()) {
            targetQueue = MEDIATTNQ;
        } else {
            emsitiff.get().setAcknowledged(false);
            emsitiff.get().setProcessFlag(false);

            if (Boolean.TRUE.equals(req.getDoFsroOverride()) && isCaseAgent) {
                targetQueue = HotQueues.FSROCAEMSI.name();

            } else if (Boolean.FALSE.equals(req.getDoFsroOverride())
                    && lifeProApiService.getProductCodeFromLifePro(req.getPolicyId()).startsWith("BA")) {
                targetQueue = templateName.compareTo(ApplicationConstants.APPSNG0702) == 0 ? APPSEMSMAS : APPSEMSIBA;
            } else if (lifeProApiService.getProductCodeFromLifePro(req.getPolicyId()).startsWith("LT")) {
                targetQueue = isCaseAgent ? APPSEMSICA : APPSEMSILT;
            }
        }
        return targetQueue;
    }

    public void unPendCaseFromIndexOrReIndex(UnPendCaseReq req, EKD0350Case ekd0350Case,
            EKD0370PendCase ekd0370PendCase) {
        if ((req.getCallingProgram().compareTo(UnPendCallingProgram.INDEX.name()) == 0
                || req.getCallingProgram().compareTo(UnPendCallingProgram.REINDEX.name()) == 0)
                && ekd0350Case.getStatus().equals(CaseStatus.P)) {
            var logStr = String.format("Release case from %s Process", req.getCallingProgram());
            logger.info(logStr);
            basicPendReleaseSteps(req, ekd0350Case, ekd0370PendCase);
        }
    }

    @Transactional
    public void unPendCaseFromJob(UnPendCaseReq req, EKD0350Case ekd0350Case, EKD0370PendCase ekd0370PendCase) {
        if ((req.getCallingProgram().compareTo(UnPendCallingProgram.JOB.name()) == 0)
                && ekd0350Case.getStatus().equals(CaseStatus.P)) {
            var logStr = String.format("Release case from %s Process", req.getCallingProgram());
            logger.info(logStr);
            basicPendReleaseSteps(req, ekd0350Case, ekd0370PendCase);
        }
    }

    // To be called in batch job
    @Transactional
    public void unPendCasesByNightlyJob() {
        logger.info("Release case from Nightly Job");
        List<EKD0370PendCase> pendCaseList = pendCaseService.getAllPendedCases();
        pendCaseList.forEach(pendCase -> {
            if(!caseInUseService.existsById(pendCase.caseId)) {
                LocalDateTime now = LocalDateTime.now();
                if (pendCase.getReleaseDateTime().equals(now) || now.isAfter(pendCase.getReleaseDateTime())) {

                    findById(pendCase.caseId).ifPresent(aCase -> basicPendReleaseSteps(new UnPendCaseReq(BATCH_JOB_USER,
                            UnPendCallingProgram.JOB.name(), false, aCase.getCaseId(), "", false), aCase, pendCase));
                }
            }
        });
    }

    // To be called in batch job
    @Transactional
    public void unPendCaseFromIMPEMSITIF(UnPendCaseReq req) {

        if (req.getCallingProgram().equals(UnPendCallingProgram.IMPEMSITIF.name())) {
            logger.info("Release case from IMPEMSITIF (Job)");

            var pendCase = pendCaseService.findByCaseId(req.getCaseId());
            var ekdCase = findById(pendCase.caseId);

            if (ekdCase.isPresent() && (ekdCase.get().getStatus().equals(CaseStatus.W)
                    || ekdCase.get().getStatus().equals(CaseStatus.P))) {
                basicPendReleaseSteps(new UnPendCaseReq(req.getUserId(), UnPendCallingProgram.IMPEMSITIF.name(), false,
                        ekdCase.get().getCaseId(), "", false), ekdCase.get(), pendCase);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void generatePermanentCases(String policyId) {

        for (String permanentCaseDesc : ApplicationConstants.permanentCaseDes) {

            // Generate case create request
            CaseCreateReq createCaseReq = new CaseCreateReq();
            createCaseReq.setInitialQueueId("");
            createCaseReq.setInitialRepId("");
            createCaseReq.setLastRepId("");
            createCaseReq.setCmAccountNumber(policyId);
            createCaseReq.setCmFormattedName(permanentCaseDesc);
            createCaseReq.setChargeBackFlag("");
            createCaseReq.setFiller("");
            createCase(createCaseReq);
        }
    }

    public Optional<EKD0350Case> getCaseByPolicyIdAndCurrentQueueId(String cmAccountNumber, String currentQueueId) {

        return ekd0350CaseRepository.findByCmAccountNumberAndCurrentQueueIdAndIsDeleted(cmAccountNumber, currentQueueId,
                0);
    }

    @Transactional
    public String moveQueueCase(String userId, String caseId, String targetQueue) {

        List<String> invalidTargetQueues = Arrays.asList("DELETE", "DELETES", "IMAGHOLDQ", "IMAGCREDQ", "MOVE",
                "IMAGLPHLDQ", EMSIDONEQ);
        Optional<EKD0350Case> ekd0350CaseOpt = findById(caseId);
        if (ekd0350CaseOpt.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        } else {
            EKD0350Case ekd0350Case = ekd0350CaseOpt.get();
            if (CaseStatus.P.equals(ekd0350Case.getStatus()) || CaseStatus.W.equals(ekd0350Case.getStatus())
                    || ApplicationConstants.EMSI_DONE_QUEUE.equals(ekd0350Case.getCurrentQueueId())) {

                return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350008, ekd0350Case.getCaseId());
            } else {
                if (invalidTargetQueues.contains(targetQueue)) {
                    return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350009, targetQueue);
                } else {
                    LocalDate arrivalDate = null;
                    Optional<EKD0250CaseQueue> caseQueue = caseQueueService.findByCaseId(ekd0350Case.getCaseId());
                    if (caseQueue.isPresent()) {
                        arrivalDate = caseQueue.get().getQueueDateTime() != null ? caseQueue.get().getQueueDateTime().toLocalDate() : caseQueue.get().getQueueDate();
                    }
                    // History entry to be done first as current queue data is required
                    AFB0660 afb0660 = AFB0660.builder().policyId(ekd0350Case.getCmAccountNumber()).caseId(caseId)
                            .caseType(ekd0350Case.getCmFormattedName().substring(0, 2))
                            .fromQueue(ekd0350Case.getCurrentQueueId()).toQueue(targetQueue).userId(userId)
                            .arrivalDate(arrivalDate).currentDate(LocalDate.now()).currentTime(LocalTime.now()).build();
                    afb0660Service.save(afb0660);

                    MOVECASEH moveCaseH = MOVECASEH.builder().mcCaseId(caseId)
                            .mcFromQueue(ekd0350Case.getCurrentQueueId()).mcToQueue(targetQueue).mcUserId(userId)
                            .mcDate(LocalDate.now()).mcTime(LocalTime.now()).build();
                    moveCaseHService.save(moveCaseH);

                    // If all validations are success, do case enqueue
                    // Insert entry in MOVECASEH and AFB0660
                    var scanningDateTimeIsNotNull = ekd0350Case.getScanningDateTime() != null;
                    caseEnqueue(ekd0350Case.getCaseId(), new EnqFLrReq(null, targetQueue, "0"
                            .concat(DateHelper.localDateToProvidedFormat(DateTimeFormatter.BASIC_ISO_DATE,
                                    scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalDate() : ekd0350Case.getScanningDate()))
                            .concat(DateHelper.localTimeToProvidedFormat("HHmmss",
                                    scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalTime() : ekd0350Case.getScanningTime()))));

                    return "Success";
                }
            }
        }
    }

    private EKD0116Res saveEkd0116Program(Ekd0116Req req, String userId, String currentQueueId,
            EKD0350Case ekd0350Case) {
        var scanningDateTimeIsNotNull = ekd0350Case.getScanningDateTime() != null;
        String date = dateHelper.reformateDate(scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalDate() : ekd0350Case.getScanningDate(), "yyyyMMdd");
        String time = dateHelper.reformateDate(scanningDateTimeIsNotNull ? ekd0350Case.getScanningDateTime().toLocalTime() : ekd0350Case.getScanningTime(), "hhmmss");
        var afb660 = new AFB0660();
        afb660.setCaseId(req.getCaseId());
        afb660.setCurrentDate(LocalDate.now());
        afb660.setCurrentTime(LocalTime.now());
        afb660.setCaseType(ekd0350Case.getCmFormattedName().substring(0, 2));
        afb660.setUserId(userId);
        afb660.setToQueue(req.getTargetQueue());
        afb660.setFromQueue(currentQueueId);
        afb660.setPolicyId(ekd0350Case.getCmAccountNumber());

        var caseQueue = caseQueueService.findByCaseId(ekd0350Case.getCaseId());
        if (caseQueue.isPresent()) {
            afb660.setArrivalDate(caseQueue.get().getQueueDateTime() != null ? caseQueue.get().getQueueDateTime().toLocalDate() : caseQueue.get().getQueueDate());
        } else {
            var caseInUse = caseInUseService.findById(ekd0350Case.getCaseId());
            caseInUse.ifPresent(ekd0850CaseInUse -> afb660.setArrivalDate(
                    ekd0850CaseInUse.getQueueDateTime().toLocalDate() != null ? ekd0850CaseInUse.getQueueDateTime().toLocalDate() : ekd0850CaseInUse.getQueueDate()));
        }
        if (!req.getTargetQueue().equals(GOTOEMSIQ)) {
            caseEnqueue(req.getCaseId(), new EnqFLrReq(userId, req.getTargetQueue(), 0 + date + time));
        } else {
            verifyTargetQueueIdGOTOEMSIQ(ekd0350Case, userId);
        }
        afb0660Service.save(afb660);

        return new EKD0116Res(afb660);
    }

    private List<EKD0310Document> getAppsDocuments(String caseId) {
        var caseDocuments = caseDocumentService.getDocumentsByCaseId(caseId);
        var documents = new ArrayList<EKD0310Document>();
        for (EKD0315CaseDocument caseDocument : caseDocuments) {
            if (caseDocument.getDocument().getDocumentType().startsWith("APPS")) {
                documents.add(caseDocument.getDocument());
            }
        }
        return documents;
    }

    private List<ICRFile> getIcrFiles(List<EKD0310Document> documents) {
        var icrFiles = new ArrayList<ICRFile>();
        for (EKD0310Document document : documents) {
            var icrFileRecord = icrFileService.findById(document.getDocumentId());
            icrFileRecord.ifPresent(icrFiles::add);
        }
        return icrFiles;
    }

    private List<String> getDocumentTypes(List<EKD0315CaseDocument> caseDocuments) {
        List<String> documentTypes = new ArrayList<>();
        for (EKD0315CaseDocument caseDocument : caseDocuments) {
            documentTypes.add(caseDocument.getDocument().getDocumentType());
        }
        return documentTypes;
    }
    private boolean documentTypesEXECMAILOrEXECLTRSOrEXECCMNTOrCASECMNT(List<String> documentTypes){
        if (documentTypes.contains("EXECMAIL") && (!documentTypes.contains("EXECLTRS")
                && !documentTypes.contains("EXECCMNT") && !documentTypes.contains("CASECMNT"))) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350439);
        }
        return true;
    }
    private boolean verifyEmsiTiffRecords(List<EMSITIFF> emsiTiffList,Ekd0116Req req){
        for (EMSITIFF emsitiff : emsiTiffList) {
            if (List.of("1","2","3").contains(emsitiff.getSendToEmsi())) {
                var companyCode=lifeProApiService.getCompanyCodeFromLifePro(req.getPolicyId());
                var benefitSummaryRes=lifeProApiService
                        .getBenefitSummaryResForBAOrBF(companyCode,req.getPolicyId(),"Y");
                if(benefitSummaryRes!=null){
                    var status=lifeProApiService.getIfPolicyADP(benefitSummaryRes);
                    if(status){
                        verifyConditionForMove(companyCode,req.getPolicyId(),emsitiff.getWafDocId(),
                                List.of("1", "3").contains(emsitiff.getSendToEmsi()));
                    }
                }
            return true;
            }
        }
        return false;
    }
    private boolean verifyConditionForMove(String companyCode,String policyId,String documentId,boolean memberPolicy){
        var ekdUser = ekdUserService.findById(policyId);
        if (ekdUser.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404,policyId);
        }
        if(ekdUser.get().getIndices().startsWith("999")){
            var icrFile=icrFileService.findById(documentId);
            if(icrFile.isEmpty()){
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.ICRFIL404,documentId);
            }
            var clientId=lifeProApiService.getClientIdUsingPolicyDetails(companyCode,policyId);
            if(memberPolicy){
                if(icrFile.get().getTemplateName().equals("APPSBA0217") &&
                        !List.of("TSA","NGASC","NGAMS","NGATN").contains(clientId)){
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350846);
                }
            }else{
                if(!List.of("TSA","NGASC","NGAMS","NGATN").contains(clientId) || !icrFile.get().getTemplateName().equals("APPSBA0217")){
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350846);
                }
            }

        }
        return true;
    }
    private String verifyTargetQueueIdMOVE(EKD0350Case ekd0350Case, Ekd0116Req req) {
        var caseDocuments = caseDocumentService.getDocumentsByCaseId(ekd0350Case.getCaseId());
        var documentTypes = getDocumentTypes(caseDocuments);
        var currentQueue = ekd0350Case.getCurrentQueueId();
        var appsDocument=getAppsDocuments(ekd0350Case.getCaseId());
        documentTypesEXECMAILOrEXECLTRSOrEXECCMNTOrCASECMNT(documentTypes);
        boolean isHotQueue=false;
        boolean espOutBound=false;
        if (getHotQueues().contains(currentQueue)) {
            isHotQueue=true;
            var emsiTiffList = getSendToEmsiValue(ekd0350Case,appsDocument);
            espOutBound=verifyEmsiTiffRecords(emsiTiffList,req);
        }
        if((!isHotQueue || !espOutBound) && appsDocument.size()>=1){
            var companyCode=lifeProApiService.getCompanyCodeFromLifePro(req.getPolicyId());
            var benefitSummaryRes=lifeProApiService
                    .getBenefitSummaryResForBAOrBF(companyCode,req.getPolicyId(),"Y");
            if(benefitSummaryRes!=null){
                var status=lifeProApiService.getStatusCode(benefitSummaryRes);
                if(status.equals("P")){
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350848);
                }else if(!List.of("A","P","D").contains(status)){
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350849);
                } else if (status.equals("A")) {
                    appsDocument.forEach(document->{
                        var appIcrFile=icrFileService.findById(document.getDocumentId());
                        if(appIcrFile.isPresent() &&
                                (appIcrFile.get().getIcrBuffer().getMemberPolicyId()!=null ||
                                        appIcrFile.get().getIcrBuffer().getSpousePolicyId()!=null)){
                            verifyConditionForMove(companyCode,req.getPolicyId(),document.getDocumentId(),
                                    appIcrFile.get().getIcrBuffer().getMemberPolicyId() != null
                                            && appIcrFile.get().getIcrBuffer().getMemberPolicyId().equals(req.getPolicyId()));
                        }
                    });
                }
            }
        }

        if(ekd0350Case.getCmFormattedName().startsWith("11") || ekd0350Case.getCmFormattedName().startsWith("21")){
            if(ekd0350Case.getCurrentQueueId().equals(APPSQC)){
                verifyCurrentQueueIdAPPSQC(ekd0350Case);
            }else{
                for (EKD0310Document ekd0310Document : appsDocument) {
                    var qcResponse = qcrunhisService.
                            qcRunTimeCheck(authorizationHelper.getRequestRepId(), req.getCaseId(), req.getPolicyId(),
                                    ekd0310Document.getDocumentType());
                    if (qcResponse.getQcFlag().equals("Y")) {
                        return APPSQC;
                    }

                    try {
                        var caseComments = caseCommentService.getCommentsSetByCaseId(req.getCaseId());
                        List<CaseCommentRes> commentResDtoList = new ArrayList<>();
                        ekd0350Case = caseComments.get(0).getEkdCase();

                        caseComments.forEach(x ->
                                commentResDtoList.add(caseCommentResponseMapper.convert(x, CaseCommentRes.class)));
                        var ekd0310caseCommentDocument = caseCommentService
                                .generateCaseCommentDocument(commentResDtoList, ekd0350Case);
                        var ekd0315 = new EKD0315CaseDocument();
                        ekd0315.setCaseId(ekd0350Case.getCaseId());
                        ekd0315.setDocumentId(ekd0310caseCommentDocument.getDocumentId());
                        caseDocumentService.insert(ekd0315);

                    } catch (Exception ex) {
                        logger.error("No comments found or Conversion to document Failed " + ex.toString());
                    }

                }
            }
        }

        return "";
    }
    private boolean verifyCurrentQueueIdAPPSQC(EKD0350Case ekd0350Case){
        var qcrunHis=qcrunhisService.findById(ekd0350Case.getCaseId());
        if(qcrunHis.isEmpty()){
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.QCHIST404,ekd0350Case.getCaseId());
        }
        if(qcrunHis.get().getQcQcPass().equals("")){
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.QCHIST405);
        }
        if(qcrunHis.get().getQcQcPass().equals("N")){
            var caseComment=caseCommentService.findByCaseId(ekd0350Case.getCaseId());
            boolean commentFound=false;
            for (EKD0352CaseComment ekd0352CaseComment : caseComment) {
                if (ekd0352CaseComment.getCommentDateTime().isBefore(LocalDateTime.now().minus(1,ChronoUnit.HOURS))) {
                    commentFound = true;
                    break;
                }
            }
            if(!commentFound){
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.QCHIST406);
            }

        }
        return false;
    }
    private List<String> getCheckMaticDocumentTypes() {
        return Arrays.asList("AUTHCHEK", "AUTHCORR", "AUTHMAIL", "CHEKMAIL", "CHEKCORR");
    }

    private List<String> getHotQueues() {
        return queueService.getHotQueuesName();
    }


    
    private boolean verifyEmsEligible(EKD0350Case ekd0350Case) {
        var appsDocuments = getAppsDocuments(ekd0350Case.getCaseId());
        var icrFileRecords = getIcrFiles(appsDocuments);
        for (int i = 0; i < icrFileRecords.size(); i++) {
            var emsitiff = emsitiffService.findByWafDocId(icrFileRecords.get(i).getDocumentId());
            if (icrFileRecords.get(i).getTemplateName().equals("APPSLT0116")) {
                return false;
            }
            for (int j = 0; j < emsitiff.size(); j++) {
                if (emsitiff.get(i).getSendToEmsi().equals("3")) {
                    continue;
                }
                if (!checkSpouseOrMemberPolicy(icrFileRecords.get(i)).equals(emsitiff.get(i).getSendToEmsi())) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<EMSITIFF> getSendToEmsiValue(EKD0350Case ekd0350Case,List<EKD0310Document> appsDocuments) {
        var emsitiff = new ArrayList<EMSITIFF>();
        for (EKD0310Document appsDocument : appsDocuments) {
            var emsitiffList = emsitiffService.findByWafDocId(appsDocument.getDocumentId());
            emsitiff.addAll(emsitiffList);
        }
        return emsitiff;
    }

    private boolean verifyCurrentQueueIdAPPSREPLQ(EKD0350Case ekd0350Case, Ekd0116Req req) {
        if (!verifyEmsEligible(ekd0350Case)
                && (getHotQueues().contains(req.getTargetQueue()) || req.getTargetQueue().equals(EMSIDONEQ))) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350463);
        }
        return true;
    }

    private String checkSpouseOrMemberPolicy(ICRFile icrFile) {
        if (icrFile.getIcrBuffer().getDdApps().getMemberCoverUnit() > 0
                && icrFile.getIcrBuffer().getDdApps().getSpouseCoverUnit() > 0) {
            return "3";
        }
        if (icrFile.getIcrBuffer().getDdApps().getMemberCoverUnit() > 0) {
            return "1";
        }
        if (icrFile.getIcrBuffer().getDdApps().getSpouseCoverUnit() > 0) {
            return "2";
        }
        return "0";
    }

    @Transactional
    public WorkCaseRes getCaseFromQueueAndLockIt(String queueId, String userId) {

        queueService.findByIdOrElseThrow(queueId);

        var ekdCaseQueue = caseQueueService.getFirstCaseFromQueue(queueId);

        return workCase(ekdCaseQueue.getCaseId(), userId);
    }

    public List<EKD0350Case> getQueuedCaseDocumentsByPolicy(String policyNo) {
        var result = ekd0350CaseRepository.findQueuedCaseDocumentsByPolicyByNativeQueryGraph(policyNo);
        if (result.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        return result.get();
    }

    private boolean verifyCurrentQueueIdAPPSCAADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSLTADM)
                && !req.getTargetQueue().equals(FSROLTADM) && !req.getTargetQueue().equals(CHEKADDQ)
                && !req.getTargetQueue().equals(FSROPRADM) && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350467);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            return verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }
        if (req.getTargetQueue().equals(FSROPRADM)) {
            return verifyTargetQueueIdFSROPRADM(req);
        }
        if (req.getTargetQueue().equals(FSROCAADM) && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350451);
        }
        if (req.getTargetQueue().equals(FSROLTADM)) {
            return verifyTargetQueueIdFSROLTADM(req);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSRONGADM(Ekd0116Req req) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSNGADM)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350445);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSNGADM(Ekd0116Req req) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(FSRONGADM)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350443);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSMSADM(Ekd0116Req req) {
        if (!req.getTargetQueue().equals(MOVE)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350444);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdIMAGCHEKHQ(Ekd0116Req req) {

        if (!req.getTargetQueue().equals(IMAGHLDQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350465);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSROGFQ(Ekd0116Req req) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSGFQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350436);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSGFQ(Ekd0116Req req) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(FSROGFQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350437);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSBAADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(FSROBAADM)
                && !req.getTargetQueue().equals(CHEKADDQ) && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350442);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }

        return true;
    }

    private boolean verifyTargetQueueIdCHEKADDQ(EKD0350Case ekd0350Case) {
        var caseDocuments = caseDocumentService.getDocumentsByCaseId(ekd0350Case.getCaseId());
        var documentTypes = getDocumentTypes(caseDocuments);
        if (!documentTypes.retainAll(getCheckMaticDocumentTypes())) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350441);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSROBAADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals("APPSBAADM")
                && !req.getTargetQueue().equals(CHEKADDQ) && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350446);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }

        return true;
    }

    private boolean verifyCurrentQueueIdFSROLTADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSBAADM)
                && !req.getTargetQueue().equals(CHEKADDQ) && !req.getTargetQueue().equals(FSROPRADM)
                && !req.getTargetQueue().equals(APPSCAADM) && !req.getTargetQueue().equals(FSROCAADM)
                && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350449);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }
        if (req.getTargetQueue().equals(FSROPRADM)) {
            return verifyTargetQueueIdFSROPRADM(req);
        }
        if ((req.getTargetQueue().equals(FSROCAADM) || req.getTargetQueue().equals(APPSCAADM))
                && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350451);
        }

        return true;
    }

    private boolean verifyTargetQueueIdDELETEOrDELETES(Ekd0116Req req, String userId) {
        var ekd0360User = userProfileService.findById(userId);
        if (ekd0360User.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404);
        }
        if (ekd0360User.get().getRepDep().equals("3025")) {
            return true;
        }
        if (ekd0360User.get().getDeleteFl().equals(false)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350461);
        }
        if (req.getTargetQueue().equals(DELETE)) {
            if (!ekd0360User.get().getRepDep().equals("3025")) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350461);
            }
        } else {
            if (!ekd0360User.get().getRepId().equals("PWATERS") && !ekd0360User.get().getRepId().equals("ABAKER")) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350462);
            }
        }
        return true;
    }

    private boolean verifyTargetQueueIdIMAGLPHLDQ(EKD0350Case ekd0350Case, Ekd0116Req req) {
        if (ekd0350Case.getCurrentQueueId().equals(IMAGCHEKHQ)) {

            var lpautoiss = lpautoissService.findById(req.getPolicyId());
            if (lpautoiss.isEmpty()) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.LPAUTO404);
            }
            if (lpautoiss.get().getProcessFlag().equals(true)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350459);
            }
        } else {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350485);
        }
        return true;
    }

    private boolean verifyTargetQueueIdAPPSVERIQ(EKD0350Case ekd0350Case) {
        if (ekd0350Case.getCurrentQueueId().equals(IMAGCHEKHQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350457);
        }
        return true;
    }

    private boolean verifyTargetQueueIdAPPSLT121(EKD0350Case ekd0350Case) {
        boolean appslt121 = false;
        var appsDocuments = getAppsDocuments(ekd0350Case.getCaseId());
        var icrFiles = getIcrFiles(appsDocuments);
        for (ICRFile icrFile : icrFiles) {
            if (icrFile.getTemplateName().equals("APPSLT0116") || icrFile.getTemplateName().equals("APPSLT0322")) {
                appslt121 = true;
                break;
            }
        }
        if (!appslt121) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350464);
        }

        return true;
    }

    private boolean verifyCurrentQueueIdAPPSLTADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSCAADM)
                && !req.getTargetQueue().equals(FSROLTADM) && !req.getTargetQueue().equals(FSROCAADM)
                && !req.getTargetQueue().equals(CHEKADDQ) && !req.getTargetQueue().equals(MEDIREVWQ)
                && !req.getTargetQueue().equals(FSROPRADM)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350449);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            return verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }
        if (req.getTargetQueue().equals(FSROPRADM)) {
            return verifyTargetQueueIdFSROPRADM(req);
        }
        if ((req.getTargetQueue().equals(FSROCAADM) || req.getTargetQueue().equals(APPSCAADM))
                && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350451);
        }
        if (req.getTargetQueue().equals(FSROLTADM)) {
            return verifyTargetQueueIdFSROLTADM(req);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSROCAADM(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSLTADM)
                && !req.getTargetQueue().equals(FSROLTADM) && !req.getTargetQueue().equals(FSROPRADM)
                && !req.getTargetQueue().equals(CHEKADDQ) && !req.getTargetQueue().equals(APPSCAADM)
                && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350468);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            return verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }
        if (req.getTargetQueue().equals(FSROPRADM)) {
            return verifyTargetQueueIdFSROPRADM(req);
        }
        if (req.getTargetQueue().equals(APPSCAADM) && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350451);
        }
        if (req.getTargetQueue().equals(FSROLTADM)) {
            return verifyTargetQueueIdFSROLTADM(req);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSROPRADM(Ekd0116Req req, EKD0350Case ekd0350Case) {

        if (!req.getTargetQueue().equals(MOVE) && !req.getTargetQueue().equals(APPSLTADM)
                && !req.getTargetQueue().equals(APPSCAADM) && !req.getTargetQueue().equals(FSROLTADM)
                && !req.getTargetQueue().equals(FSROCAADM) && !req.getTargetQueue().equals(CHEKADDQ)
                && !req.getTargetQueue().equals(MEDIREVWQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350469);
        }
        if (req.getTargetQueue().equals(CHEKADDQ)) {
            return verifyTargetQueueIdCHEKADDQ(ekd0350Case);
        }
        if ((req.getTargetQueue().equals(FSROCAADM) || req.getTargetQueue().equals(APPSCAADM))
                && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350451);
        }
        if (req.getTargetQueue().equals(FSROLTADM)) {
            return verifyTargetQueueIdFSROLTADM(req);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSEMSIBA(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (req.getTargetQueue().equals(FSROEMSIQ)) {
            if (!verifyEmsEligible(ekd0350Case)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350470);
            }
        } else {
            if (verifyEmsEligible(ekd0350Case)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350471);
            }
        }
        return true;
    }

    private boolean verifyCurrentQueueIdAPPSEMSILT(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (verifyEmsEligible(ekd0350Case)) {
            if (!req.getTargetQueue().equals(FSROEMSIQ) && !req.getTargetQueue().equals(FSROCAEMSI)
                    && !req.getTargetQueue().equals(APPSEMSICA)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350472);
            }
        } else {
            if (req.getTargetQueue().equals(FSROEMSIQ) || req.getTargetQueue().equals(FSROCAEMSI)
                    || req.getTargetQueue().equals(APPSEMSICA)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350470);
            }
        }
        if ((req.getTargetQueue().equals(FSROCAEMSI) || req.getTargetQueue().equals(APPSEMSICA))
                && !verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350473);
        } else if (req.getTargetQueue().equals(FSROEMSIQ) && verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350474);
        }
        return true;
    }

    private boolean verifyCurrentQueueIdFSROEMSIQ(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (verifyEmsEligible(ekd0350Case)) {
            if (!req.getTargetQueue().equals(APPSEMSIBA) && !req.getTargetQueue().equals(APPSEMSILT)
                    && !req.getTargetQueue().equals(FSROCAEMSI) && !req.getTargetQueue().equals(APPSEMSICA)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350475);
            }
        } else {
            if (req.getTargetQueue().equals(APPSEMSIBA) || req.getTargetQueue().equals(APPSEMSILT)
                    || req.getTargetQueue().equals(FSROCAEMSI) || req.getTargetQueue().equals(APPSEMSICA)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350470);
            }
        }
        switch (req.getTargetQueue()) {
        case FSROCAEMSI:
            return verifyTargetQueueIdFSROCAEMSI(req);
        case APPSCAEMCA:
            return verifyTargetQueueIdAPPSCAEMCA(req);
        case APPSEMSILT:
            return verifyTargetQueueIdAPPSEMSILT(req);
        case APPSEMSIBA:
            return verifyTargetQueueIdAPPSEMSIBA(req);

        default:
            return true;
        }

    }

    private boolean verifyCurrentQueueIdFSROCAEMSI(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (verifyEmsEligible(ekd0350Case)) {
            if (!req.getTargetQueue().equals(APPSEMSICA) && !req.getTargetQueue().equals(APPSEMSILT)
                    && !req.getTargetQueue().equals(FSROEMSIQ)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350476);
            }
        } else {
            if (req.getTargetQueue().equals(APPSEMSILT) || req.getTargetQueue().equals(APPSEMSICA)
                    || req.getTargetQueue().equals(FSROEMSIQ)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350470);
            }
        }
        switch (req.getTargetQueue()) {
        case APPSCAEMCA:
            return verifyTargetQueueIdAPPSCAEMCA(req);
        case APPSEMSILT:
            return verifyTargetQueueIdAPPSEMSILT(req);
        case FSROEMSIQ:
            return verifyTargetQueueIdFSROEMSIQ(req);
        default:
            return true;
        }
    }

    private boolean verifyCurrentQueueIdAPPSEMSICA(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (verifyEmsEligible(ekd0350Case)) {
            if (!req.getTargetQueue().equals(FSROCAEMSI) && !req.getTargetQueue().equals(APPSEMSILT)
                    && !req.getTargetQueue().equals(FSROEMSIQ)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350477);
            }
        } else {
            if (req.getTargetQueue().equals(APPSEMSILT) || req.getTargetQueue().equals(FSROCAEMSI)
                    || req.getTargetQueue().equals(FSROEMSIQ)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350470);
            }
        }
        switch (req.getTargetQueue()) {
        case APPSEMSILT:
            return verifyTargetQueueIdAPPSEMSILT(req);
        case FSROEMSIQ:
            return verifyTargetQueueIdFSROEMSIQ(req);
        case FSROCAEMI:
            return verifyTargetQueueIdFSROCAEMI(req);
        default:
            return true;
        }
    }

    private String getCommanyCodeFromLifePro(String policyId) {
        var policySearchReq = new PolicySearchReq();
        policySearchReq.setPolicyNumber(policyId);
        policySearchReq.setCoderID("");
        policySearchReq.setGUID("");
        policySearchReq.setUserType("");
        var policySearchBaseReq = new PolicySearchBaseReq();
        policySearchBaseReq.setPolicySearchReq(policySearchReq);
        var policySearchBaseRes = lifeProApiService.policySearch(policySearchBaseReq);
        return policySearchBaseRes.getPolicySearchRESTResult().getPolicySearchResp().get(0).getCompanyCode();
    }

    private String getContractStateFromLifePro(String policyId) {
        var policySearchReq = new PolicySearchReq();
        policySearchReq.setPolicyNumber(policyId);
        policySearchReq.setCoderID("");
        policySearchReq.setGUID("");
        policySearchReq.setUserType("");
        var policySearchBaseReq = new PolicySearchBaseReq();
        policySearchBaseReq.setPolicySearchReq(policySearchReq);
        var policySearchBaseRes = lifeProApiService.policySearch(policySearchBaseReq);
        return policySearchBaseRes.getPolicySearchRESTResult().getPolicySearchResp().get(0).getContractCode();
    }

    private String getProductCodeFromLifePro(String policyId) {
        var policyDetailsReq = new PolicyDetailsReq();
        policyDetailsReq.setCompany_Code(getCommanyCodeFromLifePro(policyId));
        policyDetailsReq.setPolicy_Number(policyId);
        policyDetailsReq.setCoderID("");
        policyDetailsReq.setGUID("");
        policyDetailsReq.setUserType("");
        var policyDetailsBaseReq = new PolicyDetailsBaseReq();
        policyDetailsBaseReq.setPolicyDetailsReq(policyDetailsReq);
        var policyDetailsBaseRes = lifeProApiService.getPolicyDetails(policyDetailsBaseReq);
        return policyDetailsBaseRes.getGetPolicyResult().getGetPolicyResp().get(0).getProduct_Code().substring(0, 2);
    }

    private boolean verifyTargetQueueIdFSROCAEMI(Ekd0116Req req) {
        if (!getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350478);
        }
        if (!verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350479);
        }
        return true;
    }

    private boolean verifyTargetQueueIdFSROEMSIQ(Ekd0116Req req) {
        if (!getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350478);
        }
        if (verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350480);
        }
        return true;
    }

    private boolean verifyTargetQueueIdAPPSEMSIBA(Ekd0116Req req) {
        if (!getProductCodeFromLifePro(req.getPolicyId()).equals("BA")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350478);
        }
        return true;
    }

    private boolean verifyTargetQueueIdAPPSEMSILT(Ekd0116Req req) {
        if (!getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350478);
        }
        if (verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350481);
        }
        return true;
    }

    private boolean verifyTargetQueueIdAPPSCAEMCA(Ekd0116Req req) {
        if (!getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350478);
        }
        if (!verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350479);
        }
        return true;
    }

    private boolean verifyTargetQueueIdFSROCAEMSI(Ekd0116Req req) {
        return verifyTargetQueueIdAPPSCAEMCA(req);
    }

    private boolean verifyTargetQueueIdFSROPRADM(Ekd0116Req req) {
        if (!getContractStateFromLifePro(req.getPolicyId()).equals("PR")
                || !getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350482);
        }
        return true;

    }

    private boolean verifyTargetQueueIdFSROLTADM(Ekd0116Req req) {
        if (getContractStateFromLifePro(req.getPolicyId()).equals("PR")
                && getProductCodeFromLifePro(req.getPolicyId()).equals("LT")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350483);
        }
        if (verifyConsolidateAgent(req)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350484);
        }
        return true;
    }

    private boolean verifyConsolidateAgent(Ekd0116Req req) {
        var appsDocuments = getAppsDocuments(req.getCaseId());
        var icrFile = getIcrFiles(appsDocuments);
        if (!icrFile.isEmpty()) {
            var agentCode = icrFile.get(0).getIcrBuffer().getDdApps().getAgentCode();
            var searchAgentHierarchyReq = new SearchAgentHierarchyReq();
            searchAgentHierarchyReq.setCompanyCode(getCommanyCodeFromLifePro(req.getPolicyId()));
            searchAgentHierarchyReq.setAgentNumber(agentCode);
            var searchAgentHierachyBaseRes = lifeProApiService.getSearchAgentHierarchy(searchAgentHierarchyReq);
            for (int i = 0; i < searchAgentHierachyBaseRes.getAgentHierarchy().size(); i++) {
                if (searchAgentHierachyBaseRes.getAgentHierarchy().get(i).getDealCode().startsWith("CA")) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean verifyTargetQueueIdIMAGHLDQ(EKD0350Case ekd0350Case, Ekd0116Req req) {
        if (ekd0350Case.getCurrentQueueId().equals(IMAGCREDHQ) || ekd0350Case.getCurrentQueueId().equals(IMAGCHEKHQ)) {
            var lpautoiss = lpautoissService.findById(req.getPolicyId());
            if (lpautoiss.isEmpty()) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.LPAUTO404);
            }
            if (lpautoiss.get().getProcessFlag().equals(false)) {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350458);
            }
        } else {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350466);
        }
        return true;
    }

    public boolean verifyTargetQueueEMSIDONEQ(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (!getHotQueues().contains(ekd0350Case.getCurrentQueueId())
                && !ekd0350Case.getCurrentQueueId().equals(APPSREPLQ)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350433);
        }
        if (verifyEmsEligible(ekd0350Case)) {
            var user = ekdUserService.getByAccountNo(req.getPolicyId());
            GETLPOINFODto lpoIno = getLPOInfoService.getLPOInfo(req.getPolicyId(), user.getSsn());
            lpoIno.setSsn(user.getSsn());
            lpoIno.setCoverageAmout(indexingService.getCoverageAmount(user.getSsn()).get(1));
            logger.info("LPOInfo details " + lpoIno);
            verifyLPOInfoValidation(lpoIno);
            UpdateRecordEmsiTiff(ekd0350Case, req.getPolicyId(), lpoIno.getSsn(), lpoIno.getDob(),
                    lpoIno.getApplicationDate(), lpoIno.getSmoker());
            return true;
        } else {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350453);
        }

    }

    private boolean UpdateRecordEmsiTiff(EKD0350Case ekd0350Case, String policyId, String ssn, LocalDate dateOfBirth,
            LocalDate appDate, String smokerFlg) {

        var appsDocuments = getAppsDocuments(ekd0350Case.getCaseId());
        var icrFileRecords = getIcrFiles(appsDocuments);
        for (int i = 0; i < icrFileRecords.size(); i++) {
            var emsitiff = emsitiffService.findByWafDocId(icrFileRecords.get(i).getDocumentId());
            if (icrFileRecords.get(i).getTemplateName().equals("APPSLT0116")) {
                return false;
            }
            // identify the subject
            EditingSubject subject;
            if (icrFileRecords.get(i).getIcrBuffer().getMemberPolicyId().equals(policyId)) {
                subject = EditingSubject.MEMBER;
            } else if (icrFileRecords.get(i).getIcrBuffer().getSpousePolicyId().equals(policyId)) {
                subject = EditingSubject.MEMBER;
            } else {
                continue;
            }
            // validate if not null
            if (icrFileRecords.get(i).getIcrBuffer().getDdApps() == null
                    || icrFileRecords.get(i).getIcrBuffer().getDdApps().getMemberCoverUnit() == 0) {
                continue;
            }
            // call medical underwriting
            var medicalUnderwritingRes = callMedicalUnderWriting(icrFileRecords.get(i), ssn, dateOfBirth, appDate,
                    subject);
            // validate if not null
            if (medicalUnderwritingRes == null) {
                continue;
            }
            // check jet issue
            Boolean jetFlg = false;
            if (medicalUnderwritingRes.getParamediFlag().equals("N") && medicalUnderwritingRes.getEkgFlag().equals("N")
                    && medicalUnderwritingRes.getProductFlag().equals("N")
                    && medicalUnderwritingRes.getOverallFlag().equals("N")
                    && List.of("N", "S", "T").contains(smokerFlg)) {
                jetFlg = true;
            }

            for (EMSITIFF emTif : emsitiff) {
                // update into emsi tif
                UpdateEmsiTiffRecords(emTif, jetFlg, policyId, icrFileRecords.get(i));
            }
        }
        return true;
    }

    private MedicalUnderwritingRes callMedicalUnderWriting(ICRFile icrFileRecords, String ssn, LocalDate dateOfBirth,
            LocalDate appDate, EditingSubject subject) {
        String polType = icrFileRecords.getIcrBuffer().getPolicyType();
        var age = DateHelper.getAge(dateOfBirth, appDate);
        var medicalUnderwritingReq = new MedicalUnderwritingReq();
        medicalUnderwritingReq.setSsn(String.valueOf(ssn));
        medicalUnderwritingReq.setPolicyType(polType);
        medicalUnderwritingReq.setAge(age[0]);
        medicalUnderwritingReq
                .setAppCoverageAmount(Double.valueOf(icrFileRecords.getIcrBuffer().getDdApps().getMemberCoverUnit()));
        medicalUnderwritingReq.setActiveFlag("BA".equals(polType) && EditingSubject.MEMBER.equals(subject) ? 1 : 0);
        medicalUnderwritingReq.setDeployFlag(false);
        medicalUnderwritingReq.setLtEspFlag("LT".equals(polType) && EditingSubject.MEMBER.equals(subject));
        medicalUnderwritingReq.setTemplateName(icrFileRecords.getTemplateName());
        return indexingService.performMedicalUnderwriting(medicalUnderwritingReq);
    }

    private void UpdateEmsiTiffRecords(EMSITIFF emTif, Boolean jetFlg, String policyId, ICRFile icr) {
        emTif.setXRefFlag("0");
        if (emTif.getSendToEmsi().equals("1")) {
            emTif.setPolicyId(policyId);
            if (jetFlg) {
                emTif.setJtIssFlag1("1");
            } else {
                emTif.setJtIssFlag1("0");
            }
           
        } else if (emTif.getSendToEmsi().equals("2")) {
            emTif.setPolicyId1(policyId);
            if (jetFlg) {
                emTif.setJtIssFlag2("1");
            } else {
                emTif.setJtIssFlag2("0");
            }

        } else if (emTif.getSendToEmsi().equals("3")) {
            if (icr.getIcrBuffer().getMemberPolicyId().equals(policyId)) {
                emTif.setPolicyId(policyId);
                if (jetFlg) {
                    emTif.setJtIssFlag1("1");
                } else {
                    emTif.setJtIssFlag1("0");
                }

            } else if (icr.getIcrBuffer().getMemberPolicyId().equals(policyId)) {
                emTif.setPolicyId1(policyId);
                if (jetFlg) {
                    emTif.setJtIssFlag2("1");
                } else {
                    emTif.setJtIssFlag2("0");
                }

            }
        }
        emsitiffService.save(emTif);
    }
    private Boolean verifyLPOInfoValidation(GETLPOINFODto lpoIno) {

        if (lpoIno == null || lpoIno.getChildCoverage() == null || lpoIno.getChildCoverage() > 5) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Child unit greater then 5 :" + lpoIno.getChildCoverage());
        }

        if (lpoIno == null || lpoIno.getFirstName() == null || lpoIno.getFirstName().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "First name is empty : " + lpoIno.getFirstName());
        }

        if (lpoIno == null || lpoIno.getLastName() == null || lpoIno.getLastName().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404", "Last name is empty : " + lpoIno.getLastName());
        }

        if (lpoIno == null || lpoIno.getDob() == null || lpoIno.getDob().equals("") || lpoIno.getDob().equals("0")) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Date of birth is not valid : " + lpoIno.getDob());
        }

        if (lpoIno == null || lpoIno.getSexCode() == null
                || !(lpoIno.getSexCode().equals("M") || lpoIno.getSexCode().equals("F"))) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Sex code is not valid : " + lpoIno.getSexCode());
        }

        if (lpoIno == null || lpoIno.getAdress1() == null || lpoIno.getAdress1().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404", "Address not valid : " + lpoIno.getAdress1());
        }

        if (lpoIno == null || lpoIno.getCountryCode() == null || lpoIno.getCountryCode().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Country code is not valid : " + lpoIno.getCountryCode());
        }

        if (lpoIno == null || lpoIno.getStateCode() == null || lpoIno.getStateCode().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "State code is not valid : " + lpoIno.getStateCode());
        }

        if (lpoIno == null || lpoIno.getZipcode() == null || lpoIno.getZipcode().equals("")
                || lpoIno.getZipcode().equals("0")) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Zip code is not valid : " + lpoIno.getZipcode());
        }
        if (lpoIno == null || lpoIno.getContractCountry() == null || lpoIno.getContractCountry().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Contract country is not valid : " + lpoIno.getContractCountry());
        }
        if (lpoIno == null || lpoIno.getContractState() == null || lpoIno.getContractState().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Contract state is not valid : " + lpoIno.getContractState());
        }

        if (lpoIno == null || lpoIno.getIsValidLic() == null || !lpoIno.getIsValidLic()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "License is not valid : " + lpoIno.getIsValidLic());
        }

        if (lpoIno == null || lpoIno.getPhoneNo() == null || lpoIno.getPhoneNo().isEmpty()
                || lpoIno.getPhoneNo().chars().allMatch(x -> x == 0)) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Phone Number is not valid : " + lpoIno.getPhoneNo());
        }

        if (lpoIno == null || lpoIno.getCoverageAmout() == null || lpoIno.getCoverageAmout() <= 0
                || lpoIno.getCoverageAmout() < 50000) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Coverage :" + lpoIno.getChildCoverage());
        }

        if (lpoIno == null || lpoIno.getApplicationDate() == null || lpoIno.getApplicationDate().equals("")
                || lpoIno.getApplicationDate().equals("0")) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Application Date is not valid : " + lpoIno.getApplicationDate());
        }

        int age = Period.between(lpoIno.getDob(), lpoIno.getApplicationDate()).getYears();
        if (age <= 0) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Application Date is not valid : " + lpoIno.getApplicationDate());
        }

        if (lpoIno == null || lpoIno.getSmoker() == null || lpoIno.getSmoker().isEmpty()
                || !List.of("U", "P", "S", "T").contains(lpoIno.getSmoker())) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Smoker flag Invalid : " + lpoIno.getSmoker());
        }

        if (lpoIno == null || lpoIno.getAgentId() == null || lpoIno.getAgentId().isEmpty()) {
            throw new DomainException(HttpStatus.CONFLICT, "EKD116404",
                    "Agent Id is not valid : " + lpoIno.getAgentId());
        }

        return true;
    }
    private boolean verifyTargetQueueIdGOTOEMSIQ(EKD0350Case ekd0350Case, String userId) {
        var req = new PendCaseReq();
        req.setUserId(userId);
        req.setTargetQueue(GOTOEMSIQ);
        findCaseAndPendIt(ekd0350Case.getCaseId(), req);
        return true;
    }

    private boolean verifyTargetQueueIdFinalElseCondition(Ekd0116Req req, EKD0350Case ekd0350Case) {
        if (req.getTargetQueue().equals(APPSEMSIBA) || req.getTargetQueue().equals(APPSEMSICA)
                || req.getTargetQueue().equals(FSROCAEMSI) || req.getTargetQueue().equals(APPSEMSILT)
                || req.getTargetQueue().equals(FSROEMSIQ)) {
            if (verifyEmsEligible(ekd0350Case)) {
                if ((req.getTargetQueue().equals(FSROCAEMSI) || req.getTargetQueue().equals(APPSEMSICA))
                        && !verifyConsolidateAgent(req)) {
                    return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350473);
                }
            } else {
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350453);
            }
        }
        return true;
    }

    private boolean checkCurrentQueueIdValidForEKD0116(String currentQueueId, Ekd0116Req request,
            EKD0350Case ekd0350Case) {
        switch (currentQueueId) {
        case IMAGLPHLDQ:
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350434, currentQueueId);
        case IMAGCHEKHQ:
            return verifyCurrentQueueIdIMAGCHEKHQ(request);
        case FSROGFQ:
            return verifyCurrentQueueIdFSROGFQ(request);
        case APPSGFQ:
            return verifyCurrentQueueIdAPPSGFQ(request);
        case APPSREPLQ:
            return verifyCurrentQueueIdAPPSREPLQ(ekd0350Case, request);
        case APPSBAADM:
            return verifyCurrentQueueIdAPPSBAADM(request, ekd0350Case);
        case APPSLTADM:
            return verifyCurrentQueueIdAPPSLTADM(request, ekd0350Case);
        case APPSNGADM:
            return verifyCurrentQueueIdAPPSNGADM(request);
        case APPSMSADM:
            return verifyCurrentQueueIdAPPSMSADM(request);
        case FSRONGADM:
            return verifyCurrentQueueIdFSRONGADM(request);
        case FSROBAADM:
            return verifyCurrentQueueIdFSROBAADM(request, ekd0350Case);
        case FSROLTADM:
            return verifyCurrentQueueIdFSROLTADM(request, ekd0350Case);
        case APPSCAADM:
            return verifyCurrentQueueIdAPPSCAADM(request, ekd0350Case);
        case FSROCAADM:
            return verifyCurrentQueueIdFSROCAADM(request, ekd0350Case);
        case FSROPRADM:
            return verifyCurrentQueueIdFSROPRADM(request, ekd0350Case);
        case APPSEMSIBA:
            return verifyCurrentQueueIdAPPSEMSIBA(request, ekd0350Case);
        case APPSEMSILT:
            return verifyCurrentQueueIdAPPSEMSILT(request, ekd0350Case);
        case FSROEMSIQ:
            return verifyCurrentQueueIdFSROEMSIQ(request, ekd0350Case);
        case FSROCAEMSI:
            return verifyCurrentQueueIdFSROCAEMSI(request, ekd0350Case);
        case APPSEMSICA:
            return verifyCurrentQueueIdAPPSEMSICA(request, ekd0350Case);
        default:
            return false;

        }

    }

    private boolean checkTargetQueueValidForEKD0116(Ekd0116Req req, String userId, EKD0350Case ekd0350Case) {
        switch (req.getTargetQueue()) {
        case EMSIDONEQ:
            return verifyTargetQueueEMSIDONEQ(req, ekd0350Case);
        case APPSQC:
        case PERMCREDQ:
        case APPSINQ:
        case MEDIINQ:
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350430, req.getTargetQueue());
        case APPSLT121:
            return verifyTargetQueueIdAPPSLT121(ekd0350Case);
        case DELETE:
        case DELETES:
            return verifyTargetQueueIdDELETEOrDELETES(req, userId);
        case MOVE:
        case GOTOEMSIQ:
            return true;
        case IMAGHLDQ:
            return verifyTargetQueueIdIMAGHLDQ(ekd0350Case, req);
        case IMAGLPHLDQ:
            return verifyTargetQueueIdIMAGLPHLDQ(ekd0350Case, req);
        case APPSVERIQ:
            return verifyTargetQueueIdAPPSVERIQ(ekd0350Case);
        case APPSEMSIBA:
        case APPSEMSICA:
        case FSROCAEMSI:
        case APPSEMSILT:
        case FSROEMSIQ:
            return verifyTargetQueueIdFinalElseCondition(req, ekd0350Case);
        default:
            return false;
        }

    }

    public boolean targetQueueValidationEKD0116(EKD0150Queue queue, String userId) {
        var ekd0360User = userProfileService.findById(userId);
        if (ekd0360User.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404);
        }
        if (ekd0360User.get().getRepDep().equals("3025")) {
            return true;
        }
        if (ekd0360User.get().getRepDep().equals(queue.getADepartmentId())) {
            return true;
        }
        return List.of("1100", "6000").contains(ekd0360User.get().getRepDep())
                && List.of("1100", "6000").contains(queue.getADepartmentId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public EKD0116Res processEkd0116Program(Ekd0116Req req, String userId) {
        var ekd0350Case = ekd0350CaseRepository.findById(req.getCaseId());
        if (ekd0350Case.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }

        var ekd0150Queue = queueService.findById(req.getTargetQueue());

        if (ekd0150Queue.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404, req.getTargetQueue());
        }

        if (!targetQueueValidationEKD0116(ekd0150Queue.get(), userId)) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350486);
        }

        if((ekd0150Queue.get().getQueueType()!=null && ekd0150Queue.get().getQueueType().equals("Y")) &&
                (ekd0150Queue.get().getAlternateQueueId()!=null ||
                        !ekd0150Queue.get().getAlternateQueueId().equals(""))) {
            ekd0150Queue=queueService.findById(ekd0150Queue.get().getAlternateQueueId());
            if (ekd0150Queue.isEmpty()) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404, req.getTargetQueue());
            }
            req.setTargetQueue(ekd0150Queue.get().getQueueId());
        }

        var currentQueueId = ekd0350Case.get().getCurrentQueueId();
        if (currentQueueId.equals(req.getTargetQueue())) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350431);
        }

        req.setPolicyId(ekd0350Case.get().getCmAccountNumber());


        checkTargetQueueValidForEKD0116(req, userId, ekd0350Case.get());
        checkCurrentQueueIdValidForEKD0116(currentQueueId, req, ekd0350Case.get());

        if (currentQueueId.startsWith("APPSAU")) {
            return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD350448);
        }

        if (req.getTargetQueue().equals(MOVE)) {
            var queue=verifyTargetQueueIdMOVE(ekd0350Case.get(), req);
            if(!queue.equals("")){
                req.setTargetQueue(queue);
            }
        }

        return saveEkd0116Program(req, userId, currentQueueId, ekd0350Case.get());
    }

    @Override
    public EKD0350Case getLockedCaseByUserId(String userId) {
        var ekd0850CaseInUse = caseInUseService.getCaseInUseByQRepIdOrElseThrow(userId);
        if (ekd0850CaseInUse.getEkd0350Case() == null) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404);
        }
        return ekd0850CaseInUse.getEkd0350Case();
    }

    @Override
    @Transactional
    public Integer releaseSpecifiedDaysOldCasesInUse(Integer daysAgo) {
        var caseInUseList = caseInUseService.getSpecifiedDaysOldCaseInUse(daysAgo);
        caseInUseList.forEach(x -> releaseCase(x.getCaseId(), BATCH_JOB_USER));
        return caseInUseList.size();
    }

    @Override
    public Page<EKD0350Case> workWithUserOptions(Pageable pageable) {
        return ekd0350CaseRepository.findAll(pageable);
    }

    @Override
    public Page<CaseOptionsDto> caseOptions(Page<CaseOptionsDto> caseOptionsDto, boolean isAlwvwc, boolean isAlwadc,
            boolean isAlwwkc, boolean isAdmin, String repDep, String userId) {
        caseOptionsDto.get().forEach(caseOptions -> {
            caseOptions.setAddCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwadc));
            caseOptions.setViewCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwvwc));
            caseOptions.setWorkWithCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwwkc));
            caseOptions.setImportCaseComments(caseOptionsImportCaseComment(caseOptions.getCaseId()));
            caseOptions.setReAssign(caseOptionReAssign(caseOptions.getCurrentQueueId(), isAdmin, repDep));
            caseOptions.setProposedTerminations(caseOptionsProposedTerminations(caseOptions.getCmAccountNumber()));
            caseOptions.setReassignCaseOutOfEmsiHotQueue(
                    caseOptionsReassignCaseOutOfEmsiHotQueue(caseOptions.getCurrentQueueId()));
            caseOptions.setEnterQcReview(caseOptionsEnterQcReview(caseOptions.getCurrentQueueId()));
            caseOptions.setPendACase(caseOptionsPendACase(caseOptions.getStatus()));
            caseOptions.setUnpendACase(caseOptionsUnpendACase(caseOptions.getStatus()));
            caseOptions.setEndorsement(true);
            caseOptions.setInWorking(caseOptionsInWorking(caseOptions.getCaseInUse()));
            caseOptions.setCurrentlyWorking(caseOptionsCurrentlyWorking(userId, caseOptions.getCaseInUse()));
            caseOptions.setRelease(caseOptionsRelease(userId, caseOptions.getCaseInUse(), isAdmin, repDep));
            caseOptions.setCaseInQueue(caseOptionsCaseInQueue(caseOptions.getQueuedCase()));
            caseOptions.setRepId(caseOptionsRepId(caseOptions.getCaseInUse()));

        });
        return caseOptionsDto;
    }

    @Override
    public Page<EKD0350Case> findAllByCaseInUseExists(Pageable pageable) {
        return ekd0350CaseRepository.findByCaseInUseIsNotNull(pageable);
    }

    @Override
    public Page<EKD0350Case> findAllByCaseInUseQRepId(Pageable pageable, String repId) {
        var ekd0850 = caseInUseService.findAllByQRepId(pageable, repId);
        List<EKD0350Case> ekd0350Cases = new ArrayList<>();
        ekd0850.get().forEach(ekd0850CaseInUse -> ekd0350Cases.add(ekd0850CaseInUse.getEkd0350Case()));

        return new PageImpl<>(ekd0350Cases, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                ekd0350Cases.size());
    }

    @Override
    public CaseOptionsDto findAllCaseOptionsByCaseId(String caseId, boolean isAlwvwc, boolean isAlwadc,
            boolean isAlwwkc, boolean isAdmin, String repDep, String userId) {
        var ekd0350Case = findByIdOrElseThrow(caseId);
        var caseOptions = new CaseOptionsDto();
        caseOptions.setCaseId(ekd0350Case.getCaseId());
        caseOptions.setCaseCloseDateTime(ekd0350Case.getCaseCloseDateTime());
        caseOptions.setInitialQueueId(ekd0350Case.getInitialQueueId());
        caseOptions.setCurrentQueueId(ekd0350Case.getCurrentQueueId());
        caseOptions.setInitialRepId(ekd0350Case.getInitialRepId());
        caseOptions.setLastRepId(ekd0350Case.getLastRepId());
        caseOptions.setScanningDateTime(ekd0350Case.getScanningDateTime());
        caseOptions.setCmAccountNumber(ekd0350Case.getCmAccountNumber());
        caseOptions.setCmFormattedName(ekd0350Case.getCmFormattedName());
        caseOptions.setLastUpdateDateTime(ekd0350Case.getLastUpdateDateTime());
        caseOptions.setChargeBackFlag(ekd0350Case.getChargeBackFlag());
        caseOptions.setStatus(ekd0350Case.getStatus());
        caseOptions.setFiller(ekd0350Case.getFiller());
        caseOptions.setCaseInUse(ekd0350Case.getCaseInUse());
        caseOptions.setQueuedCase(ekd0350Case.getQueuedCase());
        caseOptions.setAddCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwadc));
        caseOptions.setViewCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwvwc));
        caseOptions.setWorkWithCaseComments(caseOptionsCaseCommentsOptions(isAdmin, repDep, isAlwwkc));
        caseOptions.setImportCaseComments(caseOptionsImportCaseComment(ekd0350Case.getCaseId()));
        caseOptions.setReAssign(caseOptionReAssign(ekd0350Case.getCurrentQueueId(), isAdmin, repDep));
        caseOptions.setProposedTerminations(caseOptionsProposedTerminations(ekd0350Case.getCmAccountNumber()));
        caseOptions.setReassignCaseOutOfEmsiHotQueue(
                caseOptionsReassignCaseOutOfEmsiHotQueue(ekd0350Case.getCurrentQueueId()));
        caseOptions.setEnterQcReview(caseOptionsEnterQcReview(ekd0350Case.getCurrentQueueId()));
        caseOptions.setPendACase(caseOptionsPendACase(ekd0350Case.getStatus()));
        caseOptions.setUnpendACase(caseOptionsUnpendACase(ekd0350Case.getStatus()));
        caseOptions.setEndorsement(true);
        caseOptions.setInWorking(caseOptionsInWorking(ekd0350Case.getCaseInUse()));
        caseOptions.setCurrentlyWorking(caseOptionsCurrentlyWorking(userId, ekd0350Case.getCaseInUse()));
        caseOptions.setRelease(caseOptionsRelease(userId, ekd0350Case.getCaseInUse(), isAdmin, repDep));
        caseOptions.setCaseInQueue(caseOptionsCaseInQueue(ekd0350Case.getQueuedCase()));
        caseOptions.setRepId(caseOptionsRepId(ekd0350Case.getCaseInUse()));
        return caseOptions;
    }

    private boolean caseOptionsCaseCommentsOptions(boolean isAdmin, String repDept, boolean caseOption) {
        if (isAdmin && repDept.equals("3025")) {
            return true;
        }
        return caseOption;
    }

    private String caseOptionsRepId(EKD0850CaseInUse caseInUse) {
        if (caseInUse != null) {
            return caseInUse.getQRepId();
        }
        return null;
    }

    private boolean caseOptionsCaseInQueue(EKD0250CaseQueue caseQueue) {
        return caseQueue != null;
    }

    private boolean caseOptionsRelease(String userId, EKD0850CaseInUse caseInUse, boolean isAdmin, String repDep) {
        if (!caseOptionsInWorking(caseInUse)) {
            return false;
        }
        if (caseInUse.getQRepId().equals(userId)) {
            return true;
        }
        return isAdmin && repDep.equals("3025");
    }

    private boolean caseOptionsCurrentlyWorking(String userId, EKD0850CaseInUse caseInUse) {
        if (!caseOptionsInWorking(caseInUse)) {
            return false;
        }
        return caseInUse.getQRepId().equals(userId);
    }

    private boolean caseOptionsInWorking(EKD0850CaseInUse caseInUse) {
        return caseInUse != null;
    }

    private boolean caseOptionsUnpendACase(CaseStatus caseStatus) {
        return caseStatus.equals(CaseStatus.P);
    }

    private boolean caseOptionsPendACase(CaseStatus caseStatus) {
        return caseStatus.equals(CaseStatus.A) || caseStatus.equals(CaseStatus.N);
    }

    private boolean caseOptionsEnterQcReview(String currentQueue) {
        return currentQueue != null && currentQueue.equals(APPSQC);
    }

    private boolean caseOptionsReassignCaseOutOfEmsiHotQueue(String currentQueue) {
        if (currentQueue == null) {
            return false;
        }
        return getHotQueues().contains(currentQueue);
    }

    private boolean caseOptionsProposedTerminations(String cmAccountNumber) {
        var ekdUser = ekdUserService.findById(cmAccountNumber);
        if (ekdUser.isPresent()) {
            var ssn = ekdUser.get().getIndices().substring(0, 9);
            var policies = ekdUserService.getBySsn(ssn);
            return policies.size() > 1;

        }
        return false;
    }

    private boolean caseOptionsImportCaseComment(String caseId) {
        return caseCommentService.existsByCaseId(caseId);
    }

    private boolean caseOptionReAssign(String currentQueueId, boolean isAdmin, String repDep) {
        if (currentQueueId == null) {
            return false;
        }
        var queue = queueService.findById(currentQueueId);
        if (queue.isEmpty()) {
            return false;
        }
        if (repDep.equals("3025") && isAdmin) {
            return true;
        }
        if (repDep.equals(queue.get().getADepartmentId())) {
            return true;
        }
        var canAccessBothDepart = Arrays.asList("1100", "6000");
        return canAccessBothDepart.contains(repDep) && canAccessBothDepart.contains(queue.get().getADepartmentId());
    }

    @Override
    public CaseOptions findAllCaseOptionsByCase(EKD0350Case ekd0350Case, CaseOptionsReq caseOptionsReq,
            List<EKDUser> ekdUsersList) {
        var caseOptions = new CaseOptions();

        caseOptions.setAddCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwadc()));
        caseOptions.setViewCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwvwc()));
        caseOptions.setWorkWithCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwwkc()));
        caseOptions.setImportCaseComments(caseOptionsImportCaseComment(ekd0350Case.getCaseId()));
        caseOptions.setReAssign(caseOptionReAssign(ekd0350Case.getCurrentQueueId(), caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep()));
        caseOptions.setProposedTerminations(ekdUsersList.size() > 1);
        caseOptions.setReassignCaseOutOfEmsiHotQueue(
                caseOptionsReassignCaseOutOfEmsiHotQueue(ekd0350Case.getCurrentQueueId()));
        caseOptions.setEnterQcReview(caseOptionsEnterQcReview(ekd0350Case.getCurrentQueueId()));
        caseOptions.setPendACase(caseOptionsPendACase(ekd0350Case.getStatus()));
        caseOptions.setUnpendACase(caseOptionsUnpendACase(ekd0350Case.getStatus()));
        caseOptions.setEndorsement(true);
        caseOptions.setInWorking(caseOptionsInWorking(ekd0350Case.getCaseInUse()));
        caseOptions.setCurrentlyWorking(
                caseOptionsCurrentlyWorking(caseOptionsReq.getUserId(), ekd0350Case.getCaseInUse()));
        caseOptions.setRelease(caseOptionsRelease(caseOptionsReq.getUserId(), ekd0350Case.getCaseInUse(),
                caseOptionsReq.isAdmin(), caseOptionsReq.getRepDep()));
        caseOptions.setCaseInQueue(caseOptionsCaseInQueue(ekd0350Case.getQueuedCase()));
        caseOptions.setRepId(caseOptionsRepId(ekd0350Case.getCaseInUse()));

        return caseOptions;

    }

    @Override
    public CaseOptions findAllCaseOptionsByCase(EKD0350Case ekd0350Case, CaseOptionsReq caseOptionsReq) {
        var caseOptions = new CaseOptions();

        caseOptions.setAddCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwadc()));
        caseOptions.setViewCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwvwc()));
        caseOptions.setWorkWithCaseComments(caseOptionsCaseCommentsOptions(caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep(), caseOptionsReq.isAlwwkc()));
        caseOptions.setImportCaseComments(caseOptionsImportCaseComment(ekd0350Case.getCaseId()));
        caseOptions.setReAssign(caseOptionReAssign(ekd0350Case.getCurrentQueueId(), caseOptionsReq.isAdmin(),
                caseOptionsReq.getRepDep()));
        caseOptions.setProposedTerminations(caseOptionsProposedTerminations(ekd0350Case.getCmAccountNumber()));
        caseOptions.setReassignCaseOutOfEmsiHotQueue(
                caseOptionsReassignCaseOutOfEmsiHotQueue(ekd0350Case.getCurrentQueueId()));
        caseOptions.setEnterQcReview(caseOptionsEnterQcReview(ekd0350Case.getCurrentQueueId()));
        caseOptions.setPendACase(caseOptionsPendACase(ekd0350Case.getStatus()));
        caseOptions.setUnpendACase(caseOptionsUnpendACase(ekd0350Case.getStatus()));
        caseOptions.setEndorsement(true);
        caseOptions.setInWorking(caseOptionsInWorking(ekd0350Case.getCaseInUse()));
        caseOptions.setCurrentlyWorking(
                caseOptionsCurrentlyWorking(caseOptionsReq.getUserId(), ekd0350Case.getCaseInUse()));
        caseOptions.setRelease(caseOptionsRelease(caseOptionsReq.getUserId(), ekd0350Case.getCaseInUse(),
                caseOptionsReq.isAdmin(), caseOptionsReq.getRepDep()));
        caseOptions.setCaseInQueue(caseOptionsCaseInQueue(ekd0350Case.getQueuedCase()));
        caseOptions.setRepId(caseOptionsRepId(ekd0350Case.getCaseInUse()));

        return caseOptions;
    }
}
