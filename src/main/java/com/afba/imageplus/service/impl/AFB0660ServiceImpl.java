package com.afba.imageplus.service.impl;
import com.afba.imageplus.model.sqlserver.AFB0660;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.id.AFB0660CaseIdFQueueTQueueKey;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.AFB0660Service;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.afba.imageplus.constants.ApplicationConstants.LP_IMAGE_HOLD_QUEUE;

@Service
public class AFB0660ServiceImpl extends BaseServiceImpl<AFB0660, AFB0660CaseIdFQueueTQueueKey>
        implements AFB0660Service {
    protected AFB0660ServiceImpl(BaseRepository<AFB0660, AFB0660CaseIdFQueueTQueueKey> repository) {
        super(repository);
    }

    @Override
    protected AFB0660CaseIdFQueueTQueueKey getNewId(AFB0660 entity) {
        return null;
    }

    @Override
    public void addHisInAFBA0660(EKD0350Case ekd0350Case, String policyId, String userId)  {
        var addHisRecord = repository.findById(
                new AFB0660CaseIdFQueueTQueueKey(
                        ekd0350Case.getCaseId(), LP_IMAGE_HOLD_QUEUE, ekd0350Case.getCurrentQueueId()));

        if (addHisRecord.isPresent() &&
                addHisRecord.get().getCaseType().equals("11") &&
                addHisRecord.get().getPolicyId().equals(policyId)) {

            addHisRecord.get().setToQueue(ekd0350Case.getCurrentQueueId());
            addHisRecord.get().setCurrentDate(LocalDate.now());
            addHisRecord.get().setCurrentTime(LocalTime.now());
        } else {
            var afba0660Record = AFB0660.builder().
                    currentDate(LocalDate.now()).currentTime(LocalTime.now()).
                    policyId(policyId).caseId(ekd0350Case.getCaseId()).
                    arrivalDate(LocalDate.now()).fromQueue(LP_IMAGE_HOLD_QUEUE).
                    toQueue(ekd0350Case.getCurrentQueueId()).caseType("11").
                    userId(userId).build();
            repository.save(afba0660Record);
        }
    }
}
