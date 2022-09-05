package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RMVOBJReq {
    @NotEmpty(message = "documentId cannot be empty")
    String documentId;
    @NotBlank(message = "CaseId can not be empty")
    String caseId;
    @Pattern(regexp = "^[N,Y]{1}", message = "ReindexFlag Length can only be 1 and value must only be N or Y")
    String reindexFlag;
    String identifier;
    @NotNull(message = "userId must not be null")
    String userId;
//    @NotNull(message = "jobRep Id must not be null")
//    String jobRepId;
}
