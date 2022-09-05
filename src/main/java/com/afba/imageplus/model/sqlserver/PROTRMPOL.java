package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.PROTRMPOLKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "PROTRMPOL")
@IdClass(PROTRMPOLKey.class)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PROTRMPOL extends BaseEntity {

    @Id
    @Column(name = "NEWPOLID")
    private String newPolId;

    @Id
    @Column(name = "EXTPOLID")
    private String extPolId;

    @Id
    @Column(name = "EXTPOLTYPE")
    private String extPolType;

    @Column(name = "EXTPOLSTAT")
    private String extPolStat;

    @Column(name = "COVAMT")
    private Double covAmt;
}
