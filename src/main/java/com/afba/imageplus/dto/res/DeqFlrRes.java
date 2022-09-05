package com.afba.imageplus.dto.res;

import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeqFlrRes {

    CaseStatus caseStatus;
    String queueId;
}
