package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**TODO To be removed, once LifePro services get integrated*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MAST002Req {

    @NotBlank(message = "CLIENT ID is required")
    String clientId;

    @NotBlank(message = "CONTRACT CODE is required")
    @Size(min=1, max = 1, message = "CONTRACT CODE is not valid")
    String contractCode;

    @NotBlank(message = "CONTRACT REASON is required")
    String contractReason;
}
