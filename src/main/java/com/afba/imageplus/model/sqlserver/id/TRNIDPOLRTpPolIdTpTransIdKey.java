package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TRNIDPOLRTpPolIdTpTransIdKey implements Serializable {
    private String tpTransitionId;
    private String tpPolicyId;
}
