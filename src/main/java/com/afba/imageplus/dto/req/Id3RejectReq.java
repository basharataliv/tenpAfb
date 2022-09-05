package com.afba.imageplus.dto.req;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Id3RejectReq {
    @NotBlank(message = "SSN cannot be blank")
    private Long ssnNo;
    @Size(max = 4, message = "policy code size less then 4 character", groups = { Insert.class, Update.class })
    @NotBlank(message = "policy code cannot be blank")
    private String policyCode;
    @Size(max = 8, message = "reject cause code size less then 8 character", groups = { Insert.class, Update.class })
    @NotBlank(message = "reject cause code cannot be blank")
    private String rejectCauseCode;
    @NotBlank(message = "reject date cannot be blank")
    private LocalDate rejectDate;
    private LocalDate reappDate;
    private LocalDate audtDate;
    @Size(max = 10, message = "reference Cont No size less then 8 character", groups = { Insert.class, Update.class })
    private String referenceContNo;
    @Size(max = 10, message = "reference user id size less then 8 character", groups = { Insert.class, Update.class })
    private String referenceUserId;
    @Size(max = 30, message = "ref comment size less then 8 character", groups = { Insert.class, Update.class })
    @NotBlank(message = "ref comment cannot be blank")
    private String refComment;
    private String referenceComment2;

}
