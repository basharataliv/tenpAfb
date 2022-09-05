package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingRes {

    private String overallFlag;
    private String productFlag;
    private String homeOfficeFlag;
    private String paramediFlag;
    private String ekgFlag;
}
