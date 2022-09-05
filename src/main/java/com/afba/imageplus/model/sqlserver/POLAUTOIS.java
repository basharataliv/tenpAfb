package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/** Table: LifePro Policy Auto Issue - Record Keeping*/

@Entity
@Table(name = "POLAUTOIS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class POLAUTOIS extends BaseEntity {
    @Id
    @Column(name = "POLID", columnDefinition = "varchar(11) default ' '", length = 11, unique = true)
    private String policyId;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "AUTOISSFLG", columnDefinition = "varchar(1) default ' '", length = 1)
    private Boolean autoIssueFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        POLAUTOIS that = (POLAUTOIS) o;
        return policyId != null && Objects.equals(policyId, that.policyId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}