package com.afba.imageplus;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.LPPOLNUM;
import com.afba.imageplus.repository.sqlserver.LPPOLNUMRepository;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.impl.PolicyServiceImpl;
import com.afba.imageplus.utilities.DateHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { PolicyTests.class })
public class PolicyTests {

    private final LPPOLNUMRepository mockLppolnumRepository = Mockito.mock(LPPOLNUMRepository.class);

    private final DateHelper dateHelper = Mockito.mock(DateHelper.class);

    private final PolicyService policyService = new PolicyServiceImpl(mockLppolnumRepository, dateHelper);

    @Test
    void assertThat_OnGeneratingPolicyId_recordShouldBeCreatedIfNotFound() {

        when(dateHelper.getCurrentJulianDate())
                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));

        LPPOLNUM lppolnum = new LPPOLNUM(dateHelper.getCurrentJulianDate(), "00001");

        when(mockLppolnumRepository.findFirstBy()).thenReturn(Optional.empty());
        when(mockLppolnumRepository.save(lppolnum)).thenReturn(lppolnum);

        policyService.createOrUpdateUniquePolicyAsPerCurrentJulianDate();

        verify(mockLppolnumRepository).findFirstBy();
        verify(mockLppolnumRepository).save(lppolnum);
    }

    @Test
    void assertThat_onGeneratingPolicyId_oldRecordShouldBeUpdatedWithCurrentJulianDateAndSequenceNumberIsIncrementedByOne() {
        LPPOLNUM lppolnum = new LPPOLNUM("21309", "00015");

        when(dateHelper.getCurrentJulianDate())
                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));
        when(mockLppolnumRepository.findFirstBy()).thenReturn(Optional.of(lppolnum));

        final LPPOLNUM lppolnumExpectedTobeSaved = new LPPOLNUM(
                        new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()),
                "00001");

        when(mockLppolnumRepository.save(lppolnum)).thenReturn(lppolnumExpectedTobeSaved);

        policyService.createOrUpdateUniquePolicyAsPerCurrentJulianDate();

        verify(mockLppolnumRepository).findFirstBy();
        verify(mockLppolnumRepository).save(lppolnumExpectedTobeSaved);
    }

    @Test
    void assertThat_onGeneratingPolicyId_onlySequenceNumberIsIncrementedByOne_whenRecordForTodayJulianDateIsFound() {
        LPPOLNUM lppolnum = new LPPOLNUM(
                        new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()),
                "00015");

        when(dateHelper.getCurrentJulianDate())
                .thenReturn(new SimpleDateFormat(ApplicationConstants.JULIAN_DATE_FORMAT_PATTERN).format(new Date()));
        when(mockLppolnumRepository.findFirstBy()).thenReturn(Optional.of(lppolnum));

        final LPPOLNUM lppolnumExpectedTobeSaved = new LPPOLNUM(lppolnum.getId(), lppolnum.getJulianDate(), "00016");

        when(mockLppolnumRepository.save(lppolnum)).thenReturn(lppolnumExpectedTobeSaved);

        policyService.createOrUpdateUniquePolicyAsPerCurrentJulianDate();

        verify(mockLppolnumRepository).findFirstBy();
        verify(mockLppolnumRepository).save(lppolnumExpectedTobeSaved);
    }
}
