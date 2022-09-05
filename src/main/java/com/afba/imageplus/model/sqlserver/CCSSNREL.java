package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CCSSNREL")
@DynamicInsert
@DynamicUpdate
public class CCSSNREL extends BaseEntity{
    @Id
    @Column(name = "CSCASEID", columnDefinition = "varchar(9) default ' '", length =9 )
    private String csCaseId;

    @Column(name = "CSDOCID", columnDefinition = "varchar(12) default ' '", length = 12)
    private String csDocumentId;

    @Column(name = "CSPAYSSN", columnDefinition = "integer default ' '", length = 9)
    private Long csPaySsn;

    @Column(name = "CSPRODSEL", columnDefinition = "varchar(1) default ' '", length =1 )
    private String csProdSel;

    @Column(name = "CSPOLID", columnDefinition = "varchar(11) default ' '", length =11 )
    private String csPolicyId;

    @Column(name = "CSPOLTYPE", columnDefinition = "varchar(4) default ' '", length = 4)
    private String csPolicyType;

    @Column(name = "CSTEMPLNAM", columnDefinition = "varchar(10) default ' '", length = 10)
    private String csTemplateName;

    @Column(name = "CSRECUR", columnDefinition = "varchar(1) default ' '", length = 1)
    private String csRecur;

    @Column(name = "CSPREMAMT", columnDefinition = "integer default ' '", length = 7)
    private Long csPremamt;

    @Column(name = "CSRETCODE", columnDefinition = "varchar(1) default ' '", length = 1)
    private String csRetCode;

    @Column(name = "CSTRANSID", columnDefinition = "varchar(8) default ' '", length = 8)
    private String csTransitionId;

}
