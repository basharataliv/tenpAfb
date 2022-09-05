package com.afba.imageplus;

import com.afba.imageplus.controller.PolicyController;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.LPPOLNUM;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.LPPOLNUMRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.PolicyServiceImpl;
import com.afba.imageplus.utilities.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {PolicyController.class, PolicyService.class, PolicyServiceImpl.class, BaseMapper.class, ErrorServiceImp.class,  DateHelper.class})
@AutoConfigureMockMvc
@EnableWebMvc
class PolicyControllerTest {

    private final DateHelper dateHelper = Mockito.mock(DateHelper.class);

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private LPPOLNUM lppolnum;

    @MockBean
    private LPPOLNUMRepository lppolnumRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private PolicyController controller;

    @Autowired
    private PolicyServiceImpl policyServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }

    @Test
    void getPolicyIdAPISuccess() throws Exception {
        Mockito.when(dateHelper.getCurrentJulianDate()).thenReturn("2225");
        LPPOLNUM lppolnum1 = new LPPOLNUM(dateHelper.getCurrentJulianDate(), "00001");
        Mockito.when(policyServiceImpl.createOrUpdateUniquePolicyAsPerCurrentJulianDate()).thenReturn(lppolnum1);
        this.mockMvc.perform(get("/policy/get/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("222500001"));
    }

}
