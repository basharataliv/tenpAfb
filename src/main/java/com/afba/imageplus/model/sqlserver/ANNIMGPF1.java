package com.afba.imageplus.model.sqlserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ANNIMGPF1")
public class ANNIMGPF1 extends BaseEntity {
    @Id
    @Column(name = "ACCTNO", columnDefinition = "varchar(12) default ' '", length = 12)
    private String policyNo;

    @Column(name = "FILENAME", columnDefinition = "varchar(12) default ' '", length = 12)
    private String fileName;
    @Column(name = "PAGENO")
    private Integer pageNo;
    @Column(name = "PTYPE", columnDefinition = "varchar(1) default ' '", length = 1)
    private String policyType;
    @Column(name = "COMPCODE", columnDefinition = "varchar(2) default ' '", length = 2)
    private String productCode;

    @Column(name = "GROUPBCN", columnDefinition = "varchar(10) default ' '", length = 10)
    private String groupBCn;
    @Column(name = "INDIVBCN", columnDefinition = "varchar(10) default ' '", length = 10)
    private String indivbcn;
    @Column(name = "RECKEY", columnDefinition = "varchar(20) default ' '", length = 20)
    private String recKey;
    @Column(name = "BATCHID", columnDefinition = "varchar(20) default ' '", length = 20)
    private String batchId;

    @Column(name = "DOCID", columnDefinition = "varchar(12) default ' '", length = 12)
    private String docId;

}
