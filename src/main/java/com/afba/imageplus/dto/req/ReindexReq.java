package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReindexReq {

    @NotBlank(message = "Document ID is a required field")
    private String documentId;

    @NotNull(message = "Policy ID cannot be null")
    private String policyId;

    @NotNull(message = "Status code cannot be null")
    private String statusCode;

    @Pattern(regexp = "^[N,Y]{1}", message = "ReindexFlag Length can only be 1 and value must only be N or Y")
    private String reindexFlag;
}
