package com.afba.imageplus;

import com.afba.imageplus.controller.CodesFlController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.CodesFlService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RangeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = {CodesFlController.class, BaseService.class, BaseMapper.class, ErrorServiceImp.class, ObjectMapper.class, DateHelper.class, RangeHelper.class, RestResponseEntityExceptionHandler.class})
public class CodesFlControllerTest {

    @Autowired
    private CodesFlController controller;

    @MockBean
    private CodesFlService codesFlService;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private MockMvc mockMvc;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }
}
