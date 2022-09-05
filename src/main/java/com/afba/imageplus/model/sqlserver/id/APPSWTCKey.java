package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APPSWTCKey implements Serializable {
    private Integer age;
    private Integer month;
    private String sex;
}
