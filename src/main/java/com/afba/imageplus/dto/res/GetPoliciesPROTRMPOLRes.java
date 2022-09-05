package com.afba.imageplus.dto.res;

import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPoliciesPROTRMPOLRes extends PartyRelationshipsRes {

    private Boolean isTerminated;

}
