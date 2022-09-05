package com.afba.imageplus.model.sqlserver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMSITIFF")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EMSITIFF extends BaseEntity{
    @Id
    @Column(name = "TIFDOCID")
    private String tifDocId;

    @Column(name = "WAFDOCID")
    private String wafDocId;

    @Column(name = "POLICYID")
    private String policyId;

    @Column(name = "POLICYID1")
    private String policyId1;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFLG")
        private Boolean processFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFG1")
    private Boolean processFlag1;

    @Column(name = "PROCESSTYP")
    private String processType;

    @Column(name = "SNDTOEMSI")
    private String sendToEmsi;

    @Column(name = "XREFFLG")
    private String xRefFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "ACKNOWLED")
    private Boolean acknowledged;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "ACKNOWLED1")
    private Boolean acknowledged1;

    @Column(name = "JTISSFLG1")
    private String jtIssFlag1;

    @Column(name = "JTISSFLG2")
    private String jtIssFlag2;

    @Column(name = "PROCESSDAT")
    private LocalDate processDate;

    @Column(name = "PROCESSDT1")
    private LocalDate processDate1;

    @Column(name = "PROCESSTIM")
    private LocalTime processTime;

    @Column(name = "PROCESSTM1")
    private LocalTime processTime1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EMSITIFF that = (EMSITIFF) o;
        return tifDocId != null && Objects.equals(tifDocId, that.tifDocId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

