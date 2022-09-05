package com.afba.imageplus.model.sqlserver;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@DynamicInsert
@Table(name = "LPISDIMGPF")
public class LPISDIMGPF extends BaseEntity {

    @Id
    @Column(name = "ACCTNO")
    private String policyNo;

    @Column(name = "FILENAME")
    private String fileName;

    @Column(name = "PAGENO")
    private Integer pageNo;

    @Column(name = "POLTYPE")
    private String polType;

    @Column(name = "PTYPE")
    private String pType;

    @Column(name = "ISSDT")
    private LocalDate issueDate;

}
