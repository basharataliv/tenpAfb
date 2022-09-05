package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CaseOptionsReq {
    boolean isAlwvwc;
    boolean isAlwadc;
    boolean isAlwwkc;
    boolean isAdmin;
    String repDep;
    String userId;
}
