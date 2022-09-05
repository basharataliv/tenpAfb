package com.afba.imageplus.dto.res;

import com.afba.imageplus.dto.PolicyCaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetIndexPoliciesRes {

    String documentId;
    String ssn;
    Boolean isAppsDoc;
    String createNewCase;
    List<PolicyCaseDto> policyCases;
}
