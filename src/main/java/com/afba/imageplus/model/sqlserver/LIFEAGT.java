package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIFEAGT")
public class LIFEAGT extends BaseEntity{
    @Id
    @Column(name = "LAGTNUM", columnDefinition = "varchar(12) default ' '", length = 12)
    private String lAgentNum;

    @Column(name = "LSTATE", columnDefinition = "varchar(2) default ' '", length = 2)
    private String lState;

    @Column(name = "LMTATUS", columnDefinition = "varchar(1) default ' '", length = 1)
    private String lMtatus;

    @Column(name = "LSTATUS", columnDefinition = "varchar(1) default ' '", length = 1)
    private String lStatus;

    @Column(name = "LEXPDATE", columnDefinition = "date default ' '", length = 8)
    private Date lExpiryDate;

}
