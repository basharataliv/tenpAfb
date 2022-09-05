package com.afba.imageplus.service;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.QCRUNHIS;
import com.afba.imageplus.model.sqlserver.WRKQCRUN;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.QCRUNHISRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.QCRUNHISServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { QCRUNHISServiceImpl.class, ErrorServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class })

class QCRUNHISServiceTest {
    @Autowired
    QCRUNHISService qcrunhisService;

    @MockBean
    QCRUNHISRepository qcrunhisRepository;

    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    WRKQCRUNService wrkQcRunService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void onGettingUpdateQcHisRequest_recordShouldBeUpdated() {

        final String caseId = "123456";
        final String userId = "TestUser";

        QCRUNHIS entity = new QCRUNHIS(caseId, null, null, null, userId, "Y", LocalDate.now(), LocalTime.now());

        Mockito.when(qcrunhisRepository.findById(caseId)).thenReturn(Optional.of(entity));
        Mockito.when(qcrunhisService.save(entity)).thenReturn(entity);

        var actual = qcrunhisService.updateQcFlag(caseId, userId, "Y");

        Assertions.assertEquals(entity.getQcQcPass(), actual.getQcQcPass());
    }

    @Test
    void onGettingUpdateQcHisRequest_exceptionShouldBeThrown_whenRecordDoesNotExists() {

        final String caseId = "123456";
        final String userId = "TestUser";
        Mockito.when(qcrunhisRepository.findById(caseId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> qcrunhisService.updateQcFlag(caseId, userId, "Y"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void onGettingQcRuntimeCheckRequest_exceptionShouldBeThrown_whenUserDoesNotExistsInEKD0360() {

        final String userId = "TestUser";
        final String caseId = "123456";
        final String policyId = "12345678940";
        final String documentType = "TestType";

        Mockito.when(userProfileService.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> qcrunhisService.qcRunTimeCheck(userId, caseId, policyId, documentType));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.EKD360404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingQcRuntimeCheckRequest_exceptionShouldBeThrown_whenUserDoesNotExistsInWRKQCRUN() {

        final String userId = "TestUser";
        final String caseId = "123456";
        final String policyId = "12345678940";
        final String documentType = "TestType";

        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TESTU");
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");

        Mockito.when(userProfileService.findById(userId)).thenReturn(Optional.of(userProfileTest));
        Mockito.when(wrkQcRunService.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> qcrunhisService.qcRunTimeCheck(userId, caseId, policyId, documentType));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.WKQCRN404.code(), exception.getStatusCode());
    }

    @Test
    void onGettingQcRuntimeCheckRequest_qcFlagShouldBeN_whenRunCountIsLess() {

        final String userId = "TestUser";
        final String caseId = "123456";
        final String policyId = "12345678940";
        final String documentType = "APPSBA";

        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId(userId);
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setWqBaPrfCont(10);

        var wrkRun = WRKQCRUN.builder().wrUserId(userId).wqBaRunCont(1).build();

        Mockito.when(userProfileService.findById(userId)).thenReturn(Optional.of(userProfileTest));
        Mockito.when(wrkQcRunService.findById(userId)).thenReturn(Optional.of(wrkRun));

        var response = qcrunhisService.qcRunTimeCheck(userId, caseId, policyId, documentType);
        assertEquals("N", response.getQcFlag());
    }

    @Test
    void onGettingQcRuntimeCheckRequest_qcFlagShouldBeY_whenRunCountIsGreaterOrEqual() {

        final String userId = "TestUser";
        final String caseId = "123456";
        final String policyId = "12345678940";
        final String documentType = "APPSBA";

        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId(userId);
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setWqBaPrfCont(1);

        QCRUNHIS qcrunhis = QCRUNHIS.builder().qcCaseId(caseId).qcPolId(policyId).qcDocType(documentType)
                .qcUserId(userId).qcReviewer("").qcQcPass("Y").qcDate(LocalDate.now()).qcTime(LocalTime.now()).build();
        var wrkRun = WRKQCRUN.builder().wrUserId(userId).wqBaRunCont(1).build();

        Mockito.when(userProfileService.findById(userId)).thenReturn(Optional.of(userProfileTest));
        Mockito.when(wrkQcRunService.findById(userId)).thenReturn(Optional.of(wrkRun));
        Mockito.when(qcrunhisRepository.save(qcrunhis)).thenReturn(qcrunhis);

        var response = qcrunhisService.qcRunTimeCheck(userId, caseId, policyId, documentType);
        assertEquals("Y", response.getQcFlag());
    }
}
