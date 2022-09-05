package com.afba.imageplus.dto.req;

import com.afba.imageplus.annotation.CasePendingConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CasePendingConstraint(
        pendReleaseDateFieldName = "pendReleaseDate",
        docTypeFieldName = "pendDocType",
        pendForDaysFieldName = "pendForDays",
        returnQueueIdFieldName = "returnQueueId"
)
public class PendCaseReq {
    String userId;
    String pendReleaseDate;
    String pendDocType;
    @Range(min= 1, max= 99999999, message = "PEND FOR DAYS is out of range.")
    Integer pendForDays;
    String returnQueueId;
    String targetQueue;
}
