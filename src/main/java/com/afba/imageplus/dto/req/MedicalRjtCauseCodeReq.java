package com.afba.imageplus.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRjtCauseCodeReq {
    @Size(max = 15, message = "Cause code size less then 15 character", groups = { Insert.class, Update.class })
    @NotBlank(groups = { Insert.class }, message = "Cause code cannot be blank")
    private String causeCode;
    @Size(max = 3, message = "code Type size less then 3 character", groups = { Insert.class, Update.class })
    @NotBlank(groups = { Insert.class }, message = "code Type cannot be blank")
    private String codeType;
    @Size(max = 300, message = "code description size less then 300 character", groups = { Insert.class, Update.class })
    @NotBlank(groups = { Insert.class }, message = "code description cannot be blank")
    private String codeDes;

}
