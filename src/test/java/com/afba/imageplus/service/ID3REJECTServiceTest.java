package com.afba.imageplus.service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.ID3REJECT;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.ID3REJECTRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.ID3REJECTServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { ID3REJECTServiceImpl.class, ErrorServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class })
public class ID3REJECTServiceTest {

    @MockBean
    private ID3REJECTRepository repository;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @Autowired
    private ID3REJECTService iD3REJECTService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void getBySsnTest() {
        var expectedList = List.of(ID3REJECT.builder().id(1l).ssnNo(1l).policyCode("af").refComment("abc").build(),
                ID3REJECT.builder().id(2l).ssnNo(1l).policyCode("ad").refComment("ac").build());
        Mockito.when(repository.findBySsnNo(1l)).thenReturn(expectedList);

        var data = iD3REJECTService.getBySsn(1l);
        Assertions.assertEquals(2, data.size());
        Assertions.assertIterableEquals(expectedList, data);
    }

    @Test
    void getBySsnTest_WhenDataNotFound() {

        Mockito.when(repository.findBySsnNo(2l)).thenReturn(List.of());
        var res = assertThrows(DomainException.class, () -> {
            iD3REJECTService.getBySsn(2l);
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

}
