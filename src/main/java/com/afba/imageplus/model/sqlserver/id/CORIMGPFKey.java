package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CORIMGPFKey implements Serializable {

    @Column(name = "ACCTNO", columnDefinition = "varchar(12) default ' '", length = 12)
    private String policyNo;
    @Column(name = "FILENAME", columnDefinition = "varchar(12) default ' '", length = 12)
    private String fileName;

}
