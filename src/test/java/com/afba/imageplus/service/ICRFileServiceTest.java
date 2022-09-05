package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.ACHDIRECT;
import com.afba.imageplus.model.sqlserver.DDAPPS;
import com.afba.imageplus.model.sqlserver.DDCHECK;
import com.afba.imageplus.model.sqlserver.DDCREDIT;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.ID3REJECT;
import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.ICRFileRepository;
import com.afba.imageplus.repository.sqlserver.TRINDPOLRRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;

import com.afba.imageplus.service.impl.ICRFileServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;


@SpringBootTest(classes = {
        ICRFileService.class,
        ErrorServiceImp.class,
        TRNIDPOLRService.class,
        DocumentService.class,
        CaseDocumentService.class,
        BPAYRCService.class,
        CaseCommentService.class,
        LifeProApplicationService.class,
        LPAUTOISSService.class,
        FINTRGTQService.class,
        CCSSNRELService.class,
        ACHDIRECTService.class,
        CREDITHISService.class,
        ICRFileServiceImpl.class,
        AuthorizationHelper.class,
        RangeHelper.class
})
public class ICRFileServiceTest {

    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private ICRFileRepository repository;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private  TRNIDPOLRService trnidpolrService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private  CaseDocumentService caseDocumentService;
    @MockBean
    private BPAYRCService bpayrcService;
    @MockBean
    private CaseCommentService caseCommentService;
    @MockBean
    private  LifeProApplicationService lifeProApplicationService;
    @MockBean
    private LPAUTOISSService lpautoissService;
    @MockBean
    private FINTRGTQService fintrgtqService;
    @MockBean
    private CCSSNRELService ccssnrelService;
    @MockBean
    private ACHDIRECTService achdirectService;
    @MockBean
    private CREDITHISService credithisService;
    @MockBean
    private PolicyService policyService;


    @Autowired
    private ICRFileService icrFileService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void CreditCardRecord_Succuss() {

        var ddCredit= new DDCREDIT("A0027655","FORMCC0902",1,false,686800000L,
                0L,0L,0L,0L,0L,0L,686800000L, LocalDate.now(),"TENPEARLS A                 ",
                "AARON               ","",686800000L,"7","101264930033",
                true,122099,LocalDate.now(), LocalTime.now(),LocalDate.now(),LocalTime.now());
        var ddApps=new DDAPPS();
        ddApps.setPayorSSN(686800000L);
        ddApps.setMemberSignDate(LocalDate.now());
        ddApps.setPremiumPaid(BigDecimal.ZERO);
        var icrBuffer= ICRBuffer.builder().ddCredit(ddCredit).ddApps(ddApps).memberPolicyId("2209800013");
        var icrFile=ICRFile.builder().documentId("documentId").icrBuffer(icrBuffer.build());
        var trindpolr= TRNIDPOLR.builder().tpTransactionId("A0027655").tpCaseId("024512733").tpPolicyId("2209800013");
        var trinpolrList=new ArrayList<TRNIDPOLR>();
        var caseDocument= EKD0315CaseDocument.builder().documentId("APPSDOC").caseId("024512733");
        var caseDocumentList=new ArrayList<EKD0315CaseDocument>();
        var document=new EKD0310Document();
        var appIcrfile=ICRFile.builder().documentId("APPSDOC").icrBuffer(icrBuffer.build());
        document.setDocumentType("APPSBA");
        document.setDocumentId("APPSDOC");
        caseDocumentList.add(caseDocument.build());
        trinpolrList.add(trindpolr.build());
        Mockito.when(repository.findById("documentId")).thenReturn(Optional.of(icrFile.build()));
        Mockito.when(trnidpolrService.findByTransitionId("A0027655")).thenReturn(trinpolrList);
        Mockito.when(caseDocumentService.getDocumentsByCaseId("024512733")).thenReturn(caseDocumentList);
        Mockito.when( documentService.findById("APPSDOC")).thenReturn(Optional.of(document));
        Mockito.when(repository.findById("APPSDOC")).thenReturn(Optional.of(appIcrfile.build()));
        icrFileService.processCreditCardForm("documentId", icrFile.build());
    }

    @Test
    void CreditCardRecord_failure() {

        var ddCredit= new DDCREDIT("A0027655","FORMCC0902",1,false,686800000L,
                0L,0L,0L,0L,0L,0L,686800000L, LocalDate.now(),"TENPEARLS A                 ",
                "AARON               ","",686800000L,"7","101264930033",
                true,122099,LocalDate.now(), LocalTime.now(),LocalDate.now(),LocalTime.now());
        var ddApps=new DDAPPS();
        ddApps.setPayorSSN(68680000L);
        ddApps.setMemberSignDate(LocalDate.now());
        ddApps.setPremiumPaid(BigDecimal.ZERO);
        var icrBuffer= ICRBuffer.builder().ddCredit(ddCredit).ddApps(ddApps).memberPolicyId("2209800013")
                .casAgentFound(true);
        var icrFile=ICRFile.builder().documentId("documentId").icrBuffer(icrBuffer.build()).accountId("LT22031613271088");
        var trindpolr= TRNIDPOLR.builder().tpTransactionId("A0027655").tpCaseId("024512733").tpPolicyId("2209800013");
        var trinpolrList=new ArrayList<TRNIDPOLR>();
        var caseDocument= EKD0315CaseDocument.builder().documentId("APPSDOC").caseId("024512733");
        var caseDocumentList=new ArrayList<EKD0315CaseDocument>();
        var document=new EKD0310Document();
        var appIcrfile=ICRFile.builder().documentId("APPSDOC").icrBuffer(icrBuffer.build()).accountId("LT22031613271088");
        document.setDocumentType("APPSBA");
        document.setDocumentId("APPSDOC");
        caseDocumentList.add(caseDocument.build());
        trinpolrList.add(trindpolr.build());
        Mockito.when(repository.findById("documentId")).thenReturn(Optional.of(icrFile.build()));
        Mockito.when(trnidpolrService.findByTransitionId("A0027655")).thenReturn(trinpolrList);
        Mockito.when(caseDocumentService.getDocumentsByCaseId("024512733")).thenReturn(caseDocumentList);
        Mockito.when( documentService.findById("APPSDOC")).thenReturn(Optional.of(document));
        Mockito.when(repository.findById("APPSDOC")).thenReturn(Optional.of(appIcrfile.build()));
        icrFileService.processCreditCardForm("documentId", icrFile.build());

    }

    @Test
    void CheckMaticRecord_Succuss() {

        var ddCheck=new DDCHECK("A0027663","FORMCK0505",1,true,686800018L,0L,0L,0L,0L,0L,0L,0L,LocalDate.now(),"TENPEARLS J",
                "WENDY","",686800018L,"14 MAR QA FAKE CIR J","","BATH","NC",278080000L,256078446L,"","C","654321",true,LocalDate.now(),LocalDate.now(),
                LocalTime.now(),LocalDate.now(),LocalTime.now());
        var ddApps=new DDAPPS();
        ddApps.setPayorSSN(686800018L);
        ddApps.setMemberSignDate(LocalDate.now());
        ddApps.setPremiumPaid(BigDecimal.ZERO);
        var icrBuffer= ICRBuffer.builder().ddCheck(ddCheck).ddApps(ddApps).memberPolicyId("2209800013");
        var icrFile=ICRFile.builder().documentId("documentId").icrBuffer(icrBuffer.build());
        var trindpolr= TRNIDPOLR.builder().tpTransactionId("A0027663").tpCaseId("024512733").tpPolicyId("2209800013");
        var trinpolrList=new ArrayList<TRNIDPOLR>();
        var caseDocument= EKD0315CaseDocument.builder().documentId("APPSDOC").caseId("024512733");
        var caseDocumentList=new ArrayList<EKD0315CaseDocument>();
        var document=new EKD0310Document();
        var appIcrfile=ICRFile.builder().documentId("APPSDOC").icrBuffer(icrBuffer.build());
        document.setDocumentType("APPSBA");
        document.setDocumentId("APPSDOC");
        caseDocumentList.add(caseDocument.build());
        trinpolrList.add(trindpolr.build());
        var achdirect= ACHDIRECT.builder().routingNumber(256078446L);
        Mockito.when(achdirectService.findById(256078446L)).thenReturn(Optional.of(achdirect.build()));
        Mockito.when(repository.findById("documentId")).thenReturn(Optional.of(icrFile.build()));
        Mockito.when(trnidpolrService.findByTransitionId("A0027663")).thenReturn(trinpolrList);
        Mockito.when(caseDocumentService.getDocumentsByCaseId("024512733")).thenReturn(caseDocumentList);
        Mockito.when( documentService.findById("APPSDOC")).thenReturn(Optional.of(document));
        Mockito.when(repository.findById("APPSDOC")).thenReturn(Optional.of(appIcrfile.build()));
        icrFileService.processCheckMaticForm("documentId", icrFile.build());
    }

    @Test
    void CheckMaticRecord_faliure() {

        var ddCheck=new DDCHECK("A0027663","FORMCK0505",1,true,686800018L,0L,0L,0L,0L,0L,0L,0L,LocalDate.now(),"TENPEARLS J",
                "WENDY","",686800018L,"14 MAR QA FAKE CIR J","","BATH","NC",278080000L,256078446L,"","C","654321",true,LocalDate.now(),LocalDate.now(),
                LocalTime.now(),LocalDate.now(),LocalTime.now());
        var ddApps=new DDAPPS();
        ddApps.setPayorSSN(686800018L);
        ddApps.setMemberSignDate(LocalDate.now());
        ddApps.setPremiumPaid(BigDecimal.ZERO);
        var icrBuffer= ICRBuffer.builder().ddCheck(ddCheck).ddApps(ddApps).memberPolicyId("2209800013");
        var icrFile=ICRFile.builder().documentId("documentId").icrBuffer(icrBuffer.build()).accountId("APPSEP1009");
        var trindpolr= TRNIDPOLR.builder().tpTransactionId("A0027663").tpCaseId("024512733").tpPolicyId("2209800013");
        var trinpolrList=new ArrayList<TRNIDPOLR>();
        var caseDocument= EKD0315CaseDocument.builder().documentId("APPSDOC").caseId("024512733");
        var caseDocumentList=new ArrayList<EKD0315CaseDocument>();
        var document=new EKD0310Document();
        var appIcrfile=ICRFile.builder().documentId("APPSDOC").icrBuffer(icrBuffer.build());
        document.setDocumentType("APPSBA");
        document.setDocumentId("APPSDOC");
        caseDocumentList.add(caseDocument.build());
        trinpolrList.add(trindpolr.build());
        Mockito.when(repository.findById("documentId")).thenReturn(Optional.of(icrFile.build()));
        Mockito.when(trnidpolrService.findByTransitionId("A0027663")).thenReturn(trinpolrList);
        Mockito.when(caseDocumentService.getDocumentsByCaseId("024512733")).thenReturn(caseDocumentList);
        Mockito.when( documentService.findById("APPSDOC")).thenReturn(Optional.of(document));
        Mockito.when(repository.findById("APPSDOC")).thenReturn(Optional.of(appIcrfile.build()));
        icrFileService.processCheckMaticForm("documentId", icrFile.build());
    }


}
