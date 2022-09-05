package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.CORIMGPFKey;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(CORIMGPFKey.class)
@Table(name = "CORIMGPF")
public class CORIMGPF extends BaseEntity {
    @Id
    @Column(name = "ACCTNO", columnDefinition = "varchar(12) default ' '", length = 12)
    private String policyNo;
    @Id
    @Column(name = "FILENAME", columnDefinition = "varchar(12) default ' '", length = 12)
    private String fileName;
    @Column(name = "PAGENO")
    private Integer pageNo;
    @Column(name = "DOCTYPE", columnDefinition = "varchar(4) default ' '", length = 4)
    private String docType;
    @Column(name = "LETDESC", columnDefinition = "varchar(50) default ' '", length = 50)
    private String description;

}
