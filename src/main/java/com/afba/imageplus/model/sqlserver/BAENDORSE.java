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

@Entity
@Table(name = "BAENDORSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BAENDORSE{
    @Column(name = "AFBCASENO", nullable = false, columnDefinition = "varchar(12) default ' '", length =12 )
    private String afbaCaseNo;

    @Id
    @Column(name = "LPOCASENO", nullable = false, columnDefinition = "varchar(12) default ' '", length =12 )
    private String lpoCaseNo;

    @Column(name = "POLTYPE", nullable = false, columnDefinition = "varchar(3) default ' '", length = 3)
    private String polType;

    @Column(name = "SSN", nullable = false, columnDefinition = "varchar(9) default ' '", length = 9)
    private String ssn;

    @Column(name = "LNAME", nullable = false, columnDefinition = "varchar(30) default ' '", length = 30)
    private String lName;

    @Column(name = "FNAME", nullable = false, columnDefinition = "varchar(20) default ' '", length = 20)
    private String fName;

    @Column(name = "MINIT", nullable = false, columnDefinition = "varchar(1) default ' '", length = 1)
    private String mInit;
}
