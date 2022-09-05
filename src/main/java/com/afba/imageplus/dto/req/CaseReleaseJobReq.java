package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CaseReleaseJobReq {
//    @Positive
    @Range(min= 1, max= 999, message = "DAYS AGO is out of range.")
    Integer daysAgo;
}
