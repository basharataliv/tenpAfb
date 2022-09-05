package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class STETBLPKey implements Serializable {
    private String crcpCd;
    private String crcoCd;
}
