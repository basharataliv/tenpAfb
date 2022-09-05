package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQcPassReq {

    @NotBlank(message = "Policy ID is a required field")
    private String qcPolId;
    @Pattern(regexp = "^[Y|N]$", message = "Status Code can only be Y or N")
    private String qcQcPass;
}
