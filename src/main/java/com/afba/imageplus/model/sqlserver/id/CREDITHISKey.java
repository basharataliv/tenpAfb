package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
//TODO ASK AFBA ABOUT THIS TABLE KEY
public class CREDITHISKey implements Serializable {
    private Long ssn;
    private String policyId;
}
