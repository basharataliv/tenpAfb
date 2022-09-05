package com.afba.imageplus.service;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.ReindexReq;
import com.afba.imageplus.dto.res.ReindexRes;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.EKD0260ReindexingRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.ReindexingServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = { ReindexingServiceImpl.class, ErrorServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class ,DocumentTypeService.class})
class ReindexingServiceTest {
    @Autowired
    ReindexingService reindexingService;

    @MockBean
    EKD0260ReindexingRepository ekd0260IndexingRepository;

    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    DocumentTypeService documentTypeService;

    @MockBean
    AuthorizationCacheService authorizationCacheService;

    @MockBean
    private  IndexingService indexingService;

    @MockBean
    private  EKDUserService ekdUserService;

    @MockBean
    private  DocumentService documentService;
    @MockBean
    private  BaseMapper<EKD0260Reindexing, ReindexRes> responseMapper;
    @MockBean
    private UserProfileService userProfileService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void onGettingReindexingRequest_recordShouldBeReturned_whenExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0260 = EKD0260Reindexing.builder().documentId(documentId).documentType(documentType)
                .scanRepId("TEST").indexFlag(false).build();
        ekd0260.setScanningDateTime(LocalDateTime.now());

        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0260));

        var actual = reindexingService.getDocumentFromReIndex(documentId);

        Assertions.assertEquals(ekd0260.getDocumentId(), actual.getDocumentId());
    }

    @Test
    void onGettingReindexingRequest_recordShouldNotBeReturned_whenNotExists() {
        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> reindexingService.getDocumentFromReIndex(documentId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD260404.code(), exception.getStatusCode());
    }

    @Test
    void onUpdatingReindexingRequest_reindexFlagShouldBeUpdated_whenRecordExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0260 = EKD0260Reindexing.builder().documentId(documentId).documentType(documentType)
                .scanRepId("TEST").indexFlag(false).build();
        ekd0260.setScanningDateTime(LocalDateTime.of(today, time));
        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0260));

        var actual = reindexingService.updateDocumentForReIndexStatus(documentId, true);

        Assertions.assertEquals(ekd0260, actual);
    }

    @Test
    void onUpdatingReindexingRequest_reindexFlagShouldNotBeUpdated_whenRecordDoesNotExists() {

        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> reindexingService.updateDocumentForReIndexStatus(documentId, true));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD260404.code(), exception.getStatusCode());
    }

    @Test
    void onDeletingReindexingRequest_recordShouldBeDeleted_whenExists() {

        final String documentId = "B21336AA.AAA";
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        var ekd0260 = EKD0260Reindexing.builder().documentId(documentId).documentType(documentType).
                scanRepId("TEST").indexFlag(false).build();
        ekd0260.setScanningDateTime(LocalDateTime.of(today, time));
        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.of(ekd0260));

        reindexingService.removeDocumentFromReIndex(documentId);

        Mockito.verify(ekd0260IndexingRepository, times(1)).delete(ekd0260);
    }

    @Test
    void onDeletingReindexingRequest_recordShouldNotBeDeleted_whenDoesNotExists() {
        final String documentId = "B21336AA.AAA";

        Mockito.when(ekd0260IndexingRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> reindexingService.removeDocumentFromReIndex(documentId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD260404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingAllReindexAbleDocuments_listsOfEKD0260RecordsIsReturned() {
        final String documentType = "TestDocType";
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();
        var reindexDocA  = EKD0260Reindexing.builder().
                documentId("B21336AA.AAA").documentType(documentType).
                scanRepId("TEST").indexFlag(false).build();
        reindexDocA.setScanningDateTime(LocalDateTime.of(today, time));
        var reindexDocB = EKD0260Reindexing.builder().
                documentId("B21336AA.AAB").documentType(documentType).
                scanRepId("TEST").indexFlag(false).build();
        reindexDocB.setScanningDateTime(LocalDateTime.of(today, time));
        var list = List.of(reindexDocA, reindexDocB);

        Page<EKD0260Reindexing> ekd0260IndexingPage = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 2);
        Mockito.when(ekd0260IndexingRepository.findAllByDocumentTypeAndIndexFlag(pageable, documentType, false))
                .thenReturn(ekd0260IndexingPage);

        var actual = reindexingService.getAllDocumentFromReIndexByDocumentType(pageable, documentType);

        Assertions.assertEquals(ekd0260IndexingPage.getTotalElements(), actual.getTotalElements());
    }

    @Test
    void populateReindx_RecordEKD210NotFound_EKD260Populated(){
        var reindexReq=new ReindexReq("B21336AA.AAA","123456789","1","Y");
        var ekd0310= EKD0310Document.builder().documentId("B21336AA.AAA").documentType("APPSDOC");
        var ekdUser= EKDUser.builder().accountNumber("123456789");
        Mockito.when(ekdUserService.existsById(reindexReq.getPolicyId())).thenReturn(true);
        Mockito.when(documentService.findById(reindexReq.getDocumentId())).thenReturn(Optional.of(ekd0310.build()));
        Mockito.when(indexingService.getOptionalEKD0210ByDocumentId(reindexReq.getDocumentId())).thenReturn(Optional.empty());
        Mockito.when(ekd0260IndexingRepository.findByDocumentId(reindexReq.getDocumentId())).thenReturn(Optional.empty());
        var res=reindexingService.populateReindxRecord(reindexReq);
        Assertions.assertEquals("EKD0210 Record not present.Document Reindex successful",res.getMessage());
    }
    @Test
    void populateReindx_RecordEKD210Found_EKD260Populated(){
        var reindexReq=new ReindexReq("B21336AA.AAA","123456789","1","Y");
        var ekd0310= EKD0310Document.builder().documentId("B21336AA.AAA").documentType("APPSDOC");
        var ekd0210= EKD0210Indexing.builder().documentId("B21336AA.AAA").indexFlag(false);
        var ekdUser= EKDUser.builder().accountNumber("123456789");
        Mockito.when(ekdUserService.existsById(reindexReq.getPolicyId())).thenReturn(true);
        Mockito.when(documentService.findById(reindexReq.getDocumentId())).thenReturn(Optional.of(ekd0310.build()));
        Mockito.when(indexingService.getOptionalEKD0210ByDocumentId(reindexReq.getDocumentId())).thenReturn(Optional.of(ekd0210.build()));
        Mockito.when(ekd0260IndexingRepository.findByDocumentId(reindexReq.getDocumentId())).thenReturn(Optional.empty());
        var res=reindexingService.populateReindxRecord(reindexReq);
        Assertions.assertEquals("EKD0210 Record is deleted.Document Reindex successful",res.getMessage());
    }

    @Test
    void populateReindx_RecordEKD210Found_EKD260NotPopulated(){
        var reindexReq=new ReindexReq("B21336AA.AAA","","","N");
        var ekd0310= EKD0310Document.builder().documentId("B21336AA.AAA").documentType("APPSDOC");
        var ekd0210= EKD0210Indexing.builder().documentId("B21336AA.AAA").indexFlag(false);
        Mockito.when(documentService.findById(reindexReq.getDocumentId())).thenReturn(Optional.of(ekd0310.build()));
        Mockito.when(indexingService.getOptionalEKD0210ByDocumentId(reindexReq.getDocumentId())).thenReturn(Optional.of(ekd0210.build()));
        var res=reindexingService.populateReindxRecord(reindexReq);
        Assertions.assertEquals("EKD0210 Record is deleted.",res.getMessage());
    }
}
