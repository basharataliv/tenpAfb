package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.EKDUserRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.EKDUserServiceImpl;
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
import java.util.List;

@SpringBootTest(classes = {
        EKDUserServiceImpl.class,
        ErrorServiceImp.class,
        RangeHelper.class,
        AuthorizationHelper.class
})
class EKDUserServiceTest {

    @MockBean
    private EKDUserRepository ekdUserRepository;
    @Autowired
    private EKDUserService ekdUserService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void getBySsnTest() {
        var ekdUsersExpected = List.of(
                new EKDUser("2012456710", "123456789First               Last                          A"),
                new EKDUser("2022987960", "123456789First               Last                          A")
        );
        Mockito.when(ekdUserRepository.findByIndicesStartsWith("123456789")).thenReturn(ekdUsersExpected);

        var ekdUsers = ekdUserService.getBySsn("123456789");
        Assertions.assertEquals(2, ekdUsers.size());
        Assertions.assertIterableEquals(ekdUsersExpected, ekdUsers);
    }

}
