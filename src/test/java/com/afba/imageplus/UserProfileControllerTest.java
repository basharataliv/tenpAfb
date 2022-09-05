package com.afba.imageplus;

import com.afba.imageplus.controller.UserProfileController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.repository.sqlserver.EKD0360UserProfileRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.UserProfileServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { UserProfileController.class, UserProfileServiceImpl.class, UserProfileService.class,
        BaseService.class, BaseMapper.class, ErrorServiceImp.class, ObjectMapper.class, DateHelper.class,
        RestResponseEntityExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
public class UserProfileControllerTest {

    final String id = "test";

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private EKD0360UserProfileRepository ekd0360UserProfileRepository;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @Autowired
    private ErrorServiceImp errorServiceImp;

     @Autowired
     private UserProfileController controller;

    @Autowired
    private UserProfileServiceImpl userProfileServiceImpl;

    @MockBean
    private QueueService queueService;

    @MockBean
    private EKD0360UserProfile ekd0360UserProfile;

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

    @Test
    public void insertUserAPISuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TEST");
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setIsAdmin(false);
        userProfileTest.setRidxfl(false);
        userProfileTest.setRscnfl(false);
        userProfileTest.setRcasfl(false);
        userProfileTest.setExitCwfl(false);
        userProfileTest.setJumpCwfl(false);
        userProfileTest.setReindExfl(false);
        userProfileTest.setDeleteFl(false);
        userProfileTest.setMoveFl(false);
        userProfileTest.setAllowFax(false);
        userProfileTest.setAlwCrannA(false);
        userProfileTest.setAlwCrredA(false);
        userProfileTest.setAlwModAnnA(false);
        userProfileTest.setAlwRmvAnnA(false);
        userProfileTest.setAlwRmvRedA(true);
        userProfileTest.setPreVerFlA(true);
        userProfileTest.setSeparatorPage(false);
        userProfileTest.setCefLagA(false);
        userProfileTest.setOcRflA(false);
        userProfileTest.setAlwModDocA(true);
        userProfileTest.setAlwPmg(false);
        userProfileTest.setAlwPmt(false);
        userProfileTest.setAlwWbo(false);
        userProfileTest.setAlwAdc(false);
        userProfileTest.setAlwVwc(false);
        userProfileTest.setAlwWkc(false);
        userProfileTest.setCloseFl(false);
        userProfileTest.setCfrPflA(false);
        userProfileTest.setAlwPrsAnnA(false);
        userProfileTest.setAllowImpA(false);
        userProfileTest.setAlwModRedA(false);
        userProfileTest.setCopyFl(false);
        userProfileTest.setWqBaPrfCont(10);
        userProfileTest.setWqLtPrfCont(10);
        userProfileTest.setWqIpPrfCont(10);
        userProfileTest.setWqDrPrfCont(10);
        userProfileTest.setWqGfPrfCont(10);
        userProfileTest.setWqBePrfCont(10);
        userProfileTest.setEmsiUser(false);
        userProfileTest.setSecRange(12L);
        userProfileTest.setRpStat("1");
        userProfileTest.setRepRgn("1");
        userProfileTest.setMoveQueueFl(true);
        userProfileTest.setRepClr(123355);
        userProfileTest.setRepDep("1");
        userProfileTest.setDftCt1Pnl("1");
        userProfileTest.setLuDateTime(LocalDateTime.now());

        Mockito.when(ekd0360UserProfileRepository.save(any())).thenReturn(userProfileTest);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.mockMvc
                .perform(post("/users").requestAttr("repId", "TEST").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userProfileTest)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.repName").value("TEST USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.userLu").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.luDateTime").isNotEmpty());
    }

    @Test
    public void insertUserAPIFailureRepIdAlreadyExists() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TEST");
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setIsAdmin(false);
        userProfileTest.setRidxfl(false);
        userProfileTest.setRscnfl(false);
        userProfileTest.setRcasfl(false);
        userProfileTest.setExitCwfl(false);
        userProfileTest.setJumpCwfl(false);
        userProfileTest.setReindExfl(false);
        userProfileTest.setDeleteFl(false);
        userProfileTest.setMoveFl(false);
        userProfileTest.setAllowFax(false);
        userProfileTest.setAlwCrannA(false);
        userProfileTest.setAlwCrredA(false);
        userProfileTest.setAlwModAnnA(false);
        userProfileTest.setAlwRmvAnnA(false);
        userProfileTest.setAlwRmvRedA(true);
        userProfileTest.setPreVerFlA(true);
        userProfileTest.setSeparatorPage(false);
        userProfileTest.setCefLagA(false);
        userProfileTest.setOcRflA(false);
        userProfileTest.setAlwModDocA(true);
        userProfileTest.setAlwPmg(false);
        userProfileTest.setAlwPmt(false);
        userProfileTest.setAlwWbo(false);
        userProfileTest.setAlwAdc(false);
        userProfileTest.setAlwVwc(false);
        userProfileTest.setAlwWkc(false);
        userProfileTest.setCloseFl(false);
        userProfileTest.setCfrPflA(false);
        userProfileTest.setAlwPrsAnnA(false);
        userProfileTest.setAllowImpA(false);
        userProfileTest.setAlwModRedA(false);
        userProfileTest.setCopyFl(false);
        userProfileTest.setWqBaPrfCont(10);
        userProfileTest.setWqLtPrfCont(10);
        userProfileTest.setWqIpPrfCont(10);
        userProfileTest.setWqDrPrfCont(10);
        userProfileTest.setWqGfPrfCont(10);
        userProfileTest.setWqBePrfCont(10);
        userProfileTest.setEmsiUser(false);
        userProfileTest.setSecRange(12L);
        userProfileTest.setRpStat("1");
        userProfileTest.setRepRgn("1");
        userProfileTest.setMoveQueueFl(true);
        userProfileTest.setRepClr(123355);
        userProfileTest.setRepDep("1");
        userProfileTest.setDftCt1Pnl("1");

        Mockito.when(userProfileServiceImpl.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        this.mockMvc
                .perform(post("/users").requestAttr("repId", "TEST").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userProfileTest)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD360400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Invalid request: Provided repId TEST already exists."));
    }

    @Test
    public void insertUserAPIFailureMissingFieldsInRequestBody() throws Exception {
        this.mockMvc
                .perform(post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("closeFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("allowImpA cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("isAdmin cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repDep cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repName cannot be blank")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("moveFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("emsiUser cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("rpStat cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repId cannot be blank")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("copyFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repClr cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("dftCt1Pnl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("rscnfl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("reindExfl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("emsiUser cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repRgn cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("alwAdc cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("ridxfl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("alwWkc cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("moveFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("alwVwc cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("rcasfl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("deleteFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("moveQueueFl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("secRange cannot be null")));
    }

    @Test
    public void insertUserAPIFailureInsufficientLengthOfFieldsInRequestBody() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("");
        userProfileTest.setRepName("1111111111111111111111111111111111111111111111111111111111111111111111111");
        userProfileTest.setRhtxId("111111");
        userProfileTest.setRepCl1(1111);
        userProfileTest.setRepCl2(1111);
        userProfileTest.setRepCl3(1111);
        userProfileTest.setRepCl4(1111);
        userProfileTest.setRepCl5(1111);
        userProfileTest.setRepClr(1234567);
        userProfileTest.setRepDep("11111");
        userProfileTest.setRpStat("11");
        userProfileTest.setRepRgn("11111");
        userProfileTest.setUserLu("11111111111");
        userProfileTest.setSecRange(1234567L);
        userProfileTest.setFcSecHigh(1234567L);
        userProfileTest.setFcSecLow(1234567L);
        userProfileTest.setAnnSecLvlA(1234567L);
        userProfileTest.setRedSecLvlA(1234567L);
        userProfileTest.setWbSecHigh(1234567L);
        userProfileTest.setWbSecLow(1234567L);
        userProfileTest.setDftCt1Pnl("11");
        userProfileTest.setRtvLanatt("11");
        userProfileTest.setDasdSysId("11");
        userProfileTest.setPrivName("");
        userProfileTest.setAllowLog("");
        userProfileTest.setFiller("");
        this.mockMvc
                .perform(post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userProfileTest)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of repId must be between 1-10")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repName must be between 1-30")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of rhtxId must be between 1-4")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repCl1 cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repCl2 cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repCl3 cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repCl4 cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repCl5 cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of repClr cannot exceed more than 6 digits")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of repDep must be between 1-4")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of repRgn must be between 1-4")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Length of rpStat must exactly be 1")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of secRange cannot exceed more than 6 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of fcSecLow cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of fcSecHigh cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of annSecLvlA cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of redSecLvlA cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of wbSecLow cannot exceed more than 3 digits")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of wbSecHigh cannot exceed more than 3 digits")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of dftCt1Pnl must exactly be 1")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of rtvLanatt must exactly be 1")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of dasdSysId must exactly be 1")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of privName must be between 1-10")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of allowLog must exactly be 1")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of filler must be between 1-20")));
    }

    @Test
    public void updateUserAPISuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TEST");
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setIsAdmin(false);
        userProfileTest.setRidxfl(false);
        userProfileTest.setRscnfl(false);
        userProfileTest.setRcasfl(false);
        userProfileTest.setExitCwfl(false);
        userProfileTest.setJumpCwfl(false);
        userProfileTest.setReindExfl(false);
        userProfileTest.setDeleteFl(false);
        userProfileTest.setMoveFl(false);
        userProfileTest.setAllowFax(false);
        userProfileTest.setAlwCrannA(false);
        userProfileTest.setAlwCrredA(false);
        userProfileTest.setAlwModAnnA(false);
        userProfileTest.setAlwRmvAnnA(false);
        userProfileTest.setAlwRmvRedA(true);
        userProfileTest.setPreVerFlA(true);
        userProfileTest.setSeparatorPage(false);
        userProfileTest.setCefLagA(false);
        userProfileTest.setOcRflA(false);
        userProfileTest.setAlwModDocA(true);
        userProfileTest.setAlwPmg(false);
        userProfileTest.setAlwPmt(false);
        userProfileTest.setAlwWbo(false);
        userProfileTest.setAlwAdc(false);
        userProfileTest.setAlwVwc(false);
        userProfileTest.setAlwWkc(false);
        userProfileTest.setCloseFl(false);
        userProfileTest.setCfrPflA(false);
        userProfileTest.setAlwPrsAnnA(false);
        userProfileTest.setAllowImpA(false);
        userProfileTest.setAlwModRedA(false);
        userProfileTest.setCopyFl(false);
        userProfileTest.setWqBaPrfCont(10);
        userProfileTest.setWqLtPrfCont(10);
        userProfileTest.setWqIpPrfCont(10);
        userProfileTest.setWqDrPrfCont(10);
        userProfileTest.setWqGfPrfCont(10);
        userProfileTest.setWqBePrfCont(10);
        userProfileTest.setEmsiUser(false);
        userProfileTest.setSecRange(12L);
        userProfileTest.setRpStat("1");
        userProfileTest.setRepRgn("1");
        userProfileTest.setMoveQueueFl(true);
        userProfileTest.setRepClr(123355);
        userProfileTest.setRepDep("1");
        userProfileTest.setDftCt1Pnl("1");
        userProfileTest.setLuDateTime(LocalDateTime.now());

        Mockito.when(ekd0360UserProfileRepository.save(any())).thenReturn(userProfileTest);
        Mockito.when(ekd0360UserProfile.getRepId()).thenReturn("test");
        Mockito.when(userProfileServiceImpl.findById(any())).thenReturn(Optional.of(userProfileTest));
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.mockMvc
                .perform(put("/users/{id}", id).requestAttr("repId", "TEST").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userProfileTest)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.luDateTime").isNotEmpty());
    }

    @Test
    public void updateUserAPIFailureRepIDNotFound() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TEST");
        userProfileTest.setRepName("TEST USER UPDATED");
        userProfileTest.setUserLu("ADMIN");
        userProfileTest.setIsAdmin(false);
        userProfileTest.setRidxfl(false);
        userProfileTest.setRscnfl(false);
        userProfileTest.setRcasfl(false);
        userProfileTest.setExitCwfl(false);
        userProfileTest.setJumpCwfl(false);
        userProfileTest.setReindExfl(false);
        userProfileTest.setDeleteFl(false);
        userProfileTest.setMoveFl(false);
        userProfileTest.setAllowFax(false);
        userProfileTest.setAlwCrannA(false);
        userProfileTest.setAlwCrredA(false);
        userProfileTest.setAlwModAnnA(false);
        userProfileTest.setAlwRmvAnnA(false);
        userProfileTest.setAlwRmvRedA(true);
        userProfileTest.setPreVerFlA(true);
        userProfileTest.setSeparatorPage(false);
        userProfileTest.setCefLagA(false);
        userProfileTest.setOcRflA(false);
        userProfileTest.setAlwModDocA(true);
        userProfileTest.setAlwPmg(false);
        userProfileTest.setAlwPmt(false);
        userProfileTest.setAlwWbo(false);
        userProfileTest.setAlwAdc(false);
        userProfileTest.setAlwVwc(false);
        userProfileTest.setAlwWkc(false);
        userProfileTest.setCloseFl(false);
        userProfileTest.setCfrPflA(false);
        userProfileTest.setAlwPrsAnnA(false);
        userProfileTest.setAllowImpA(false);
        userProfileTest.setAlwModRedA(false);
        userProfileTest.setCopyFl(false);
        userProfileTest.setWqBaPrfCont(10);
        userProfileTest.setWqLtPrfCont(10);
        userProfileTest.setWqIpPrfCont(10);
        userProfileTest.setWqDrPrfCont(10);
        userProfileTest.setWqGfPrfCont(10);
        userProfileTest.setWqBePrfCont(10);
        userProfileTest.setEmsiUser(false);
        userProfileTest.setSecRange(12L);
        userProfileTest.setRpStat("1");
        userProfileTest.setRepRgn("1");
        userProfileTest.setMoveQueueFl(true);
        userProfileTest.setRepClr(123355);
        userProfileTest.setRepDep("1");
        userProfileTest.setDftCt1Pnl("1");

        Mockito.when(ekd0360UserProfileRepository.save(any())).thenReturn(userProfileTest);
        Mockito.when(userProfileServiceImpl.findById(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(put("/users/{id}", id)
                        .requestAttr("repId", "TEST")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userProfileTest)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD360404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("User Profile not found against REPID: test"));
    }

    @Test
    public void updateUserAPIFailureJSONMismatch() throws Exception {
        String requestBody = "{" +
//            "\"repId\": \"test\"," +
                "\"repName\": \"Zuha Ahmad\"," + "\"isAdmin\": \"false\"," + "\"repClr\": 123456," + "\"ridxfl\": true,"
                + "\"rscnfl\": false," + "\"rcasfl\": false," + "\"userLu\": \"Gondal\"," + "\"exitCwfl\": false,"
                + "\"jumpCwfl\": false," + "\"reindExfl\": true," + "\"deleteFl\": true," + "\"moveFl\": true,"
                + "\"copyFl\": false," + "\"allowImpA\": false," + "\"allowFax\": false," + "\"alwCrannA\": true,"
                + "\"alwCrredA\": true," + "\"alwModAnnA\": false," + "\"alwModRedA\": false,"
                + "\"alwRmvAnnA\": false," + "\"alwRmvRedA\": false," + "\"alwPrsAnnA\": true," + "\"preVerFlA\": true,"
                + "\"separatorPage\": false," + "\"cefLagA\": false," + "\"cfrPflA\": false," + "\"ocRflA\": false,"
                + "\"alwModDocA\": true," + "\"alwPmg\": false," + "\"alwPmt\": false," + "\"alwWbo\": false,"
                + "\"alwAdc\": false," + "\"alwVwc\": false," + "\"alwWkc\": false," + "\"closeFl\": true,"+ "\"emsiUser\": false" + "}";
        Mockito.when(userProfileServiceImpl.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        this.mockMvc
                .perform(put("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repId cannot be blank")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("rpStat cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("dftCt1Pnl cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repRgn cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("repDep cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("secRange cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("moveQueueFl cannot be null")))
        ;
    }

}
