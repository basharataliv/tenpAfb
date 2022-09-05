package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "AFFCLIENT")
public class AFFCLIENT extends BaseEntity{


    @Id
    @Column(name = "ACAFFCODE", columnDefinition = "varchar(8) default ' '", length = 8)
    private String acAffCode;

    @Column(name = "ACCLIENTID", columnDefinition = "varchar(15) default ' '", length = 15)
    private String acClientId;

    public String getAcAffCode() {
      return  acAffCode != null ? acAffCode.trim() : "";
    }

    public void setAcAffCode(String acAffCode) {
        this.acAffCode = acAffCode != null ? acAffCode.trim() : "";
    }


}
