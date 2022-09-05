package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.CREDITHISKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CREDITHIS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@IdClass(CREDITHISKey.class)
public class CREDITHIS extends BaseEntity{
    @Id
    @Column(name = "SSN" )
    private Long ssn;

    @Column(name = "COMPCODE" )
    private String compCode;

    @Id
    @Column(name = "POLID" )
    private String policyId;

    @Column(name = "DOLLARAMT" )
    private Double dollarAmount;

    @Column(name = "RECUR" )
    private String reCur;

    @Column(name = "PRODDESC" )
    private String productDescription;

    @Column(name = "rc" )
    private String rc;

    @Column(name = "DATEIN" )
    private LocalDate dateIn;

    @Column(name = "TIMEIN" )
    private LocalTime timeIn;
}
