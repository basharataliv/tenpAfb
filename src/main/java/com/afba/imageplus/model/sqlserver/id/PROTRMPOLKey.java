package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PROTRMPOLKey implements Serializable {
    private String newPolId;
    private String extPolId;
    private String extPolType;
}
