package com.afba.imageplus.service;

import com.afba.imageplus.annotation.RLSEMSIWTConstraint;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.api.dto.res.PartySearchResultRes;
import com.afba.imageplus.configuration.ImageThreadingConfig;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.TransactionSource;
import com.afba.imageplus.dto.req.CaseCreateReqExtended;
import com.afba.imageplus.model.sqlserver.DDAPPS;
import com.afba.imageplus.model.sqlserver.DDCREDIT;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.TEMPCOMPMAP;
import com.afba.imageplus.repository.sqlserver.EKD0010NextDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.DDProcessServiceImpl;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.FileConvertServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.EmailUtility;
import com.afba.imageplus.utilities.ImageConverter;
import com.afba.imageplus.utilities.MSBatchConverter;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import static com.afba.imageplus.constants.TransactionSource.DATA_DIMENSION;
import static com.afba.imageplus.constants.TransactionSource.STAUNTON;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = {  DateHelper.class, ErrorServiceImp.class,  RangeHelper.class,
        AuthorizationHelper.class,DDProcessServiceImpl.class})
class DDProcessServiceTest {
    @Autowired
    private DDProcessService ddProcessService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private DDAPPSService ddappsService;

    @MockBean
    private DDATTACHService ddattachService;

    @MockBean
    private DDCREDITService ddcreditService;

    @MockBean
    private DDCHECKService ddcheckService;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private CaseService caseService;

    @MockBean
    private CaseDocumentService caseDocumentService;

    @MockBean
    private TRNIDPOLRService trnidpolrService;

    @MockBean
    private ICRFileService icrFileService;

    @MockBean
    private CaseCommentService caseCommentService;

    @MockBean
    private LPAUTOISSService lpautoissService;

    @MockBean
    private EditingService editingService;

    @MockBean
    private  QueueService queueService;

    @MockBean
    private  EKDUserService ekdUserService;

    @MockBean
    private  AFB0660Service afb0660Service;

    @MockBean
    private  PolicyService policyService;

    @MockBean
    private  EMSITIFFService emsitiffService;

    @MockBean
    private  AUTOCMTPFService autocmtpfService;

    @MockBean
    private  LifeProApiService lifeProApiService;

    @MockBean
    private  LifeProApplicationService lifeProApplicationService;

    @MockBean
    private  TEMPCOMPMAPService tempcompmapService;

    @MockBean
    private  SpringTemplateEngine thymeleafTemplateEngine;

    @MockBean
    private  EmailTemplateRepository emailTemplateRepository;

    @MockBean
    private  EmailUtility emailUtility;

    @Value("${afba.dd-process.base-dir:}")
    private String ddBaseDir;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;


    private NavigableMap<String, TransactionSource> transactionSources;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @PostConstruct
    public void loadTransactionSources() {
        transactionSources = new TreeMap<>();
        transactionSources.put("A", DATA_DIMENSION);
        transactionSources.put("C", DATA_DIMENSION);
        transactionSources.put("P", STAUNTON);
    }


    @Test
    void ddProcessWhenNoRecordFound_Success()  {
        List<DDAPPS> ddAppsList=new ArrayList<>();
        Mockito.when(ddappsService.findAll(Pageable.unpaged(), Map.of(DDAPPS.Fields.processFlag, false)))
                .thenReturn((new PageImpl<>(ddAppsList, PageRequest.of(0, 1),
                        0)));
        var res=ddProcessService.ddProcess();
        Assertions.assertEquals(null,res.getTransactionsProcessed());
    }
    @Test
    void ddProcessWhenRecordFoundDocumentFound_tempCOMPNotFound(){
        var ddApps=DDAPPS.builder().transactionId("A0027655").processFlag(false).templateName("APPSEP1009").noOfPages(2);
        List<DDAPPS> ddAppsList=new ArrayList<>();
        ddAppsList.add(ddApps.build());
        Mockito.when(ddappsService.findAll(Pageable.unpaged(), Map.of(DDAPPS.Fields.processFlag, false)))
                .thenReturn((new PageImpl<>(ddAppsList, PageRequest.of(0, 1),
        ddAppsList.size())));
        var res=ddProcessService.ddProcess();
        Assertions.assertEquals(null,res.getTransactionsProcessed());



    }

    @Test
    void ddProcessWhenPartRelationShipNullPointerException(){
        var ddApps=DDAPPS.builder().transactionId("A0027655").processFlag(false).templateName("APPSEP1009")
                .noOfPages(2).payMode("0").attachmentFlag(false).memberCoverUnit(100).spouseCoverUnit(0).memberSSN(686800000L)
                .memberLastName("TENPEARLS A").memberFirstName("AARON").memberMiddleInitial("").memberDateOfBirth(LocalDate.now())
                .memberSignDate(LocalDate.now());
        List<DDAPPS> ddAppsList=new ArrayList<>();
        ddAppsList.add(ddApps.build());
        var tempCompMap=new TEMPCOMPMAP("APPSEP1009","02","02","N");
        var ekd0310=EKD0310Document.builder().documentId("appsDoc");
        var emsitiff= EMSITIFF.builder();
        var icrBuffer= ICRBuffer.builder().ddApps(ddApps.build()).memberPolicyId("2209800013");
        var icrFile= ICRFile.builder().documentId("documentId").icrBuffer(icrBuffer.build());
        var partySearchBaseRes= new PartySearchBaseRes();
        var partySearchResult=new PartySearchResultRes();
        var partySearchRes=new PartySearchRes();
        partySearchRes.setName_id("12345");
        partySearchResult.setReturnCode(103);
        partySearchResult.setPartySearchRes(List.of(partySearchRes));
        partySearchBaseRes.setPartySearchResult(partySearchResult);
        Mockito.when(icrFileService.insert(any())).thenReturn(icrFile.build());
        Mockito.when(ddappsService.findAll(Pageable.unpaged(), Map.of(DDAPPS.Fields.processFlag, false)))
                .thenReturn((new PageImpl<>(ddAppsList, PageRequest.of(0, 1),
                        ddAppsList.size())));
        Mockito.when(tempcompmapService.findById("APPSEP1009")).thenReturn(Optional.of(tempCompMap));
        Mockito.when(documentService.insert(any())).thenReturn(ekd0310.build());
        Mockito.when(emsitiffService.save(any())).thenReturn(emsitiff.build());
        try{
            Mockito.when(ddProcessService.id3Loadata(icrFile.build(),any())).thenReturn(new CaseCreateReqExtended());
            Mockito.when(lifeProApiService.partySearchDetails(any())).thenReturn(partySearchBaseRes);
            var res=ddProcessService.ddProcess();
        }catch (Exception e){
            Assertions.assertTrue(e instanceof NullPointerException );
        }

    }

}
