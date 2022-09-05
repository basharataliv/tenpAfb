package com.afba.imageplus.service;

import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.GetPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPolicyPROTRMPOLReq;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.repository.sqlserver.EKDUserRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.PROTRMPOLRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.EKDUserServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.PROTRMPOLServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { PROTRMPOLServiceImpl.class, ErrorServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class, EKDUserServiceImpl.class })
class PROTRMPOLServiceTest {

    @MockBean
    private PROTRMPOLRepository protrmpolRepository;

    @Autowired
    private PROTRMPOLService protrmpolService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private IndexingService indexingService;

    @MockBean
    private EKDUserRepository ekdUserRepository;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void assertThat_onFetchingPoliciesForTermination_recordShouldNotBeReturnedIfNotExist() {

        GetPoliciesPROTRMPOLReq request = new GetPoliciesPROTRMPOLReq(null, "88888888");

        Mockito.when(indexingService.getPoliciesForCoverageAmount(any()))
                .thenReturn(new ArrayList<PartyRelationshipsRes>());
        Mockito.when(protrmpolRepository.findByExtPolId(any())).thenReturn(new ArrayList<PROTRMPOL>());

        DomainException exception = assertThrows(DomainException.class,
                () -> protrmpolService.fetchPoliciesForProposedTermination(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void assertThat_onFetchingPoliciesForTermination_recordShouldBeReturnedIfExist() {

        GetPoliciesPROTRMPOLReq request = new GetPoliciesPROTRMPOLReq(null, "88888888");

        PartyRelationshipsRes partyRelationshipsRes = new PartyRelationshipsRes();
        partyRelationshipsRes.setPolicyNumber("1111111");

        Mockito.when(indexingService.getPoliciesForCoverageAmount(any())).thenReturn(List.of(partyRelationshipsRes));
        Mockito.when(protrmpolRepository.findByExtPolId(any())).thenReturn(new ArrayList<PROTRMPOL>());

        var getResponse = protrmpolService.fetchPoliciesForProposedTermination(request);
        Assertions.assertEquals("1111111", getResponse.get(0).getPolicyNumber());
        Assertions.assertEquals(false, getResponse.get(0).getIsTerminated());
    }

    @Test
    void assertThat_onFetchingPoliciesForTermination_isTerminatedTrueIfExistInPROTRMPOL() {

        GetPoliciesPROTRMPOLReq request = new GetPoliciesPROTRMPOLReq(null, "88888888");
        PROTRMPOL protrmpol = PROTRMPOL.builder().extPolId("1111111").extPolStat("S").extPolType("BA").covAmt(2000.0)
                .newPolId("2222222").build();

        PartyRelationshipsRes partyRelationshipsRes = new PartyRelationshipsRes();
        partyRelationshipsRes.setPolicyNumber("1111111");

        Mockito.when(indexingService.getPoliciesForCoverageAmount(any())).thenReturn(List.of(partyRelationshipsRes));
        Mockito.when(protrmpolRepository.findByExtPolId(any())).thenReturn(List.of(protrmpol));

        var getResponse = protrmpolService.fetchPoliciesForProposedTermination(request);
        Assertions.assertEquals("1111111", getResponse.get(0).getPolicyNumber());
        Assertions.assertEquals(true, getResponse.get(0).getIsTerminated());
    }

    @Test
    void assertThat_onPostPoliciesForTermination_exceptionIsThrown_ifPolicyNotFoundInEKDUSER() {

        PostPoliciesPROTRMPOLReq request = new PostPoliciesPROTRMPOLReq();
        PostPolicyPROTRMPOLReq policy = new PostPolicyPROTRMPOLReq();
        policy.setExtPolId("1111111");
        request.setPolicies(List.of(policy));
        Mockito.when(ekdUserRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> protrmpolService.postPoliciesForProposedTermination(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void assertThat_onPostPoliciesForTermination_policyIsTerminated_ifPolicyFoundInEKDUSER() {

        EKDUser user = EKDUser.builder().accountNumber("1111111")
                .indices("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                .build();
        PostPoliciesPROTRMPOLReq request = new PostPoliciesPROTRMPOLReq();
        PostPolicyPROTRMPOLReq policy = new PostPolicyPROTRMPOLReq();
        policy.setExtPolId("1111111");
        policy.setIsTerminated(true);
        request.setPolicies(List.of(policy));

        PROTRMPOL protrmpol = PROTRMPOL.builder().extPolId("1111111").extPolStat("S").extPolType("BA").covAmt(2000.0)
                .newPolId("2222222").build();

        Mockito.when(ekdUserRepository.findByAccountNumber(any())).thenReturn(Optional.of(user));
        Mockito.when(protrmpolRepository.findByExtPolId(any())).thenReturn(List.of(protrmpol));
        Mockito.when(protrmpolRepository.save(any())).thenReturn(protrmpol);

        var response = protrmpolService.postPoliciesForProposedTermination(request);

        assertEquals("Proposed terminations recorded successfully", response);
    }

}
