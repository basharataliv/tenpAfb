package com.afba.imageplus.dto;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodesFlDto {

    @NotBlank(groups = { Insert.class }, message = "Sys code type is a required field")
    private String sysCodeType;

    @NotBlank(groups = { Insert.class }, message = "Sys code is a required field")
    private String sysCode;

    @NotBlank(groups = { Insert.class, Update.class }, message = "Sys code description is a required field")
    private String sysCodeDescription;

    @NotBlank(groups = { Insert.class }, message = "Sys code buff is a required field")
    private String sysCodeBuff;

}
