package com.afba.imageplus;

import com.afba.imageplus.controller.CaseQueueController;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.CaseQueueMapper;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = { CaseQueueController.class, CaseQueueMapper.class, BaseMapper.class, ErrorServiceImp.class })
class CaseQueueControllerTest {

    @MockBean
    private CaseQueueService caseQueueService;

    @Autowired
    private CaseQueueController caseQueueController;

    @MockBean
    private ErrorRepository errorRepository;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void findAll() throws Exception {

        Mockito.when(caseQueueService.findAll(any(), any()))
                .thenAnswer(invocation -> new PageImpl<>(getList(), invocation.getArgument(0), 1));

        var result = caseQueueController.findAll(PageRequest.of(1, 1), new HashMap<>(), Map.of("queueId", "TESTQUEUE"));
        Assertions.assertEquals("EKD000000", result.getStatusCode());
        Assertions.assertEquals(1, result.getResponseData().getContent().size());
        Assertions.assertNotNull(result.getResponseData().getContent().get(0).getEkdCase());
        Assertions.assertNotNull(result.getResponseData().getContent().get(0).getQueueDateTime());
        Assertions.assertNotNull(result.getResponseData().getContent().get(0).getScanDateTime());
    }

    private List<EKD0250CaseQueue> getList() {
        var caseQueue = new EKD0250CaseQueue();
        caseQueue.setQueueId("TESTQUEUE");
        caseQueue.setQueueDateTime(LocalDateTime.now());
        caseQueue.setScanDateTime(LocalDateTime.now());
        caseQueue.setCases(new EKD0350Case());
        return List.of(caseQueue);
    }
}
