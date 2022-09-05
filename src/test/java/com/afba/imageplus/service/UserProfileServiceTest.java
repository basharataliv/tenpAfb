package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.repository.sqlserver.EKD0360UserProfileRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.QueueServiceImpl;
import com.afba.imageplus.service.impl.UserProfileServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { UserProfileServiceImpl.class, EKD0360UserProfile.class, ErrorServiceImp.class,
        QueueServiceImpl.class, RangeHelper.class, AuthorizationHelper.class })
class UserProfileServiceTest {

    @MockBean
    private EKD0360UserProfileRepository ekd0360UserProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private QueueService queueService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @PostConstruct
    public void mock() {
        Mockito.when(ekd0360UserProfileRepository.save(any())).thenAnswer(invocation -> {
            var userProfile = (EKD0360UserProfile) invocation.getArgument(0);
            userProfile.setRepId("TESTU");
            return userProfile;
        });
    }

    @Test
    void assertThat_onCreatingNewUserProfile_recordShouldBeCreated() {
        Mockito.when(ekd0360UserProfileRepository.existsById(any())).thenReturn(false);
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TESTU");
        userProfileTest.setRepName("TEST USER");
        userProfileTest.setUserLu("ADMIN");
        var insertResponse = userProfileService.insert(userProfileTest);
        Assertions.assertEquals("TESTU", userProfileTest.getRepId());
        Assertions.assertEquals(userProfileTest, insertResponse);
    }

    @Test
    void assertThat_onUpdatingExistingUserProfile_recordShouldBeUpdated() {
        var userProfileTest = new EKD0360UserProfile();
        userProfileTest.setRepId("TESTU");
        userProfileTest.setAllowFax(true);
        userProfileTest.setUserLu("ADMIN");
        Mockito.when(ekd0360UserProfileRepository.findById(any())).thenReturn(Optional.of(userProfileTest));
        var updateResponse = userProfileService.update("TESTU", userProfileTest);
        Assertions.assertEquals(userProfileTest.getRepId(), updateResponse.getRepId());
        Assertions.assertEquals(userProfileTest, updateResponse);
    }
}
