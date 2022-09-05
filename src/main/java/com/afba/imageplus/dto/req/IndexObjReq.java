package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexObjReq {
    @NotBlank(message = "Document type is required.")
    @Size(message = "Document type must not exceed 8 bytes.", max = 8)
    private String documentType;
    @Size(message = "Document description must not exceed 26 bytes.", max = 26)
    private String documentDescription;
}
