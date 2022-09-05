package com.afba.imageplus;

import com.afba.imageplus.controller.FTPController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.FTPService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.FTPServiceImpl;
import com.afba.imageplus.utilities.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {FTPController.class, FTPService.class, FTPServiceImpl.class, ErrorServiceImp.class, DateHelper.class, RestResponseEntityExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class FTPControllerTest {

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private FTPService ftpService;

    @Autowired
    private FTPController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

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

//    @Test
//    public void getFTPZipAPISuccess() throws Exception {
//        Resource fileResource = new ClassPathResource("assets/big.tif");
//        MockMultipartFile file = new MockMultipartFile(
//                "doc",fileResource.getFilename(),
//                "image/tiff",
//                fileResource.getInputStream());
//        Mockito.when(ftpService.download()).thenReturn(file.getBytes());
//        this.mockMvc.perform(get("/ftp/zip")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk()).andExpect(content().contentType("application/zip"))
//                .andDo(print()).andReturn();
//    }

//    @Test
//    public void getFTPZipAPIFailure() throws Exception {
//        Mockito.when(ftpService.download()).thenThrow(new Exception());
//        this.mockMvc.perform(get("/ftp/zip")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is5xxServerError());
//    }

}
