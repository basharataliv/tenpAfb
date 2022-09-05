package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.EKD0250CaseQueueRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.CaseQueueServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(classes = { CaseQueueServiceImpl.class, ErrorServiceImp.class, AuthorizationHelper.class,
        RangeHelper.class })
class CaseQueueServiceTest {
    @Autowired
    private CaseQueueService caseQueueService;

    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private  EKD0250CaseQueueRepository ekd0250CaseQueueRepository;

    @MockBean
    private  UserProfileService userProfileService;

    @MockBean
    private  QueueService queueService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void getQueueIdCaseCount_withAdmin(){
        var ekd0360= EKD0360UserProfile.builder().repId("BLOHR").isAdmin(true).repDep("3025");
        Set<String> queueSet = new HashSet<String>();
        queueSet.add("BLOHR");
        String[][] queueArray={{"BLOHR"},{"BLOHR"},{"5"},{""}};
        var queueList=new ArrayList<String[][]>();
        queueList.add(queueArray);
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekd0360.build()));
        Mockito.when(authorizationHelper.getAuthorizedQueueIds()).thenReturn(null);
        Mockito.when(ekd0250CaseQueueRepository.findAllQueueIdsWithCount()).thenReturn(queueList);
       var res= caseQueueService.getQueueIdCaseCount("BLOHR");
        Assertions.assertEquals("5",res.get(0).getCount());
    }

    @Test
    void getQueueIdCaseCount_withNotAdmin(){
        var ekd0360= EKD0360UserProfile.builder().repId("BLOHR").isAdmin(true).repDep("325");
        var ekd150Queue= EKD0150Queue.builder().queueId("BLOHR").queueType("Y").queueDescription("Privatequeue");
        Set<String> queueSet = new HashSet<String>();
        queueSet.add("BLOHR");
        String[][] queueArray={{"BLOHR"},{"BLOHR"},{"5"},{""}};
        var queueList=new ArrayList<String[][]>();
        queueList.add(queueArray);
        Mockito.when(queueService.findById("BLOHR")).thenReturn(Optional.of(ekd150Queue.build()));
        Mockito.when(userProfileService.findById("BLOHR")).thenReturn(Optional.of(ekd0360.build()));
        Mockito.when(authorizationHelper.getAuthorizedQueueIds()).thenReturn(queueSet);
        Mockito.when(ekd0250CaseQueueRepository.findQueueIdsWithCount(queueSet)).thenReturn(queueList);
        var res=caseQueueService.getQueueIdCaseCount("BLOHR");
        Assertions.assertEquals("5",res.get(0).getCount());

    }
}
