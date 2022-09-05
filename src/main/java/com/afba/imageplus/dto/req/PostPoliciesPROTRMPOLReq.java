package com.afba.imageplus.dto.req;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class PostPoliciesPROTRMPOLReq {

    @Valid
    List<PostPolicyPROTRMPOLReq> policies;
}
