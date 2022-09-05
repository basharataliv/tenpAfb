package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EMSIUND")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EMSIUND extends BaseEntity {

    @Id
    @Column(name = "RECDKEY")
    private Long recordKey;

    @Column(name = "COMPCD")
    private String companyCode;

    @Column(name = "POLID")
    private String policyId;

    @Column(name = "PRODID")
    private String productId;

    @Column(name = "PAYMODE")
    private String payMode;

    @Column(name = "EMSISTAT")
    private String emsiStatus;

    @Column(name = "EMSIRSN")
    private String emsiReason;

    @Column(name = "CREATEDT")
    private LocalDate createdDate;

    @Column(name = "CREATETM")
    private LocalTime createdTime;

    @Column(name = "UPDATEDT")
    private LocalDate updatedDate;

    @Column(name = "UPDATETM")
    private LocalTime updatedTime;

    @Column(name = "PROCFLG")
    private String processFlag;

    @Column(name = "FAILRSN")
    private String failReason;

    @Column(name = "POLSTAT")
    private String policyStatus;

    @Column(name = "POLRSN")
    private String policyReason;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "ISSDOCFLG")
    private Boolean issDocFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EMSIUND that = (EMSIUND) o;
        return recordKey != null && Objects.equals(recordKey, that.recordKey);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
