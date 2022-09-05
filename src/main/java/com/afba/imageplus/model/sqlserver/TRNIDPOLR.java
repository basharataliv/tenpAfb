package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.TRNIDPOLRKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "TRNIDPOLR")
@IdClass(TRNIDPOLRKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TRNIDPOLR extends BaseEntity {

    @Id
    @Column(name = "TPTRANSID", columnDefinition = "varchar(10) default ' '", length = 10)
    private String tpTransactionId;

    @Id
    @Column(name = "TPPOLID", columnDefinition = "varchar(11) default ' '", length =11 )
    private String tpPolicyId;

    @Column(name = "TPCASEID", columnDefinition = "varchar(9) default ' '", length = 9)
    private String tpCaseId;

    @Column(name = "TPSOURCE", columnDefinition = "varchar(10) default ' '", length =10 )
    private String tpSource;

    @Column(name = "TPDATEIN", columnDefinition = "date default ' '", length = 8)
    private LocalDate tpDateIn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TRNIDPOLR trnidpolr = (TRNIDPOLR) o;
        return tpTransactionId != null && Objects.equals(tpTransactionId, trnidpolr.tpTransactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
