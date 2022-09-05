package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.res.GetPartyRelationshipsResultRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.api.dto.res.PartySearchResultRes;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.SSNDIF;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.SSNDIFRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.SSNDIFServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { SSNDIFServiceImpl.class, RangeHelper.class, AuthorizationHelper.class,
        ErrorServiceImp.class, })
@TestInstance(Lifecycle.PER_CLASS)

public class SSNDIFTestService {
    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private SSNDIFRepository repo;
    @MockBean
    private LifeProApiService lifeProApiService;
    @MockBean
    private EKDUserService eKDUserService;
    @Autowired
    private SSNDIFService service;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void SSNDIFServiceTest() throws FileNotFoundException, IOException {
        PartySearchRes res = new PartySearchRes();
        res.setName_id("123");
        res.setSsn("12345");
        res.setIndividual_first("a");
        res.setIndividual_last("c");
        res.setIndividual_middle("b");
        PartyRelationshipsRes party = new PartyRelationshipsRes();
        party.setRelateCode("IN");
        party.setPolicyNumber("12345678");
        Mockito.when(repo.findByProcessFlag(false))
                .thenReturn(List.of(new SSNDIF(1L, "12345", LocalDateTime.now(), LocalDateTime.now(), false)));
        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(List.of(res), "", 1, "")));

        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(
                new PartyRelationshipsBaseRes(new GetPartyRelationshipsResultRes(List.of(party), "", 1, "")));

        Mockito.when(eKDUserService.findById("12345678")).thenReturn(
                Optional.of(new EKDUser("12345678", "123456789First               Last                          A")));
        service.ssnDifProcessing();
        Assertions.assertEquals("12345c                             a                   b",
                eKDUserService.findById("12345678").get().getIndices());
        Assertions.assertEquals(true, repo.findByProcessFlag(false).get(0).getProcessFlag());

    }

}
