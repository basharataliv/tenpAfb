package com.afba.imageplus.service;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.CODESFL;
import com.afba.imageplus.repository.sqlserver.CodesFlRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.CodesFlServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { CodesFlServiceImpl.class, ErrorServiceImp.class, AuthorizationHelper.class,
        RangeHelper.class })
class CodesFlServiceTest {

    @Autowired
    private CodesFlService codesFlService;

    @MockBean
    CodesFlRepository codesFlRepository;

    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    @Rollback
    void onGettingNewSsnRequest_systemGeneratedSsnShouldBeReturned_whenRecordExistsInCodesFl() {

        CODESFL codesFlObj = CODESFL.builder().id(1L).sysCodeBuff("111111111").build();
        Mockito.when(codesFlRepository.findBySysCodeAndSysCodeType("SSN", "CUSTID"))
                .thenReturn(List.of(codesFlObj));
        Mockito.when(codesFlRepository.findById(1L)).thenReturn(Optional.of(codesFlObj));

        assertEquals("111111112", codesFlService.generateNewSsn());
    }

    @Test
    @Rollback
    void onGettingNewSsnRequest_exceptionShouldBeThrown_whenRecordNotExistsInCodesFl() {

        Mockito.when(codesFlRepository.findBySysCodeAndSysCodeType("SSN", "CUSTID")).thenReturn(List.of());

        DomainException exception = assertThrows(DomainException.class, () -> codesFlService.generateNewSsn());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(EKDError.CODSFL001.code(), exception.getStatusCode());
    }

}