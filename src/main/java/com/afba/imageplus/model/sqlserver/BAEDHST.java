package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "BAEDHST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BAEDHST{

    @Id
    @Column(name = "HAFBCASENO")
    private String hAfbaCaseNo;

    @Column(name = "HLPOCASENO")
    private String hLpoCaseNo;

    @Column(name = "HPOLTYPE")
    private String hPolType;


    @Column(name = "HSSN")
    private String hSsn;


    @Column(name = "HLNAME")
    private String hLName;


    @Column(name = "HFNAME")
    private String hFName;

    @Column(name = "HMINIT")
    private String hMInit;

    @Column(name = "RECORDENTERED")
    private Date recordEntered;
}
