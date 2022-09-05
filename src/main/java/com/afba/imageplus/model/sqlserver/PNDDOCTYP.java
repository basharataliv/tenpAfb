package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "PNDDOCTYP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE PNDDOCTYP SET IS_DELETED = 1 WHERE DOCTYPE=?")
@Where(clause = "(IS_DELETED IS NULL OR IS_DELETED = 0)")
@Builder
@DynamicInsert
@DynamicUpdate
public class PNDDOCTYP extends SoftDeleteBaseEntity {
    @Id
    @Column(name = "DOCTYPE", nullable = false, columnDefinition = "varchar(8) default ' '", length = 8)
    private String docType;

    @Column(name = "DOCDESC", nullable = false, columnDefinition = "varchar(40) default ' '", length = 40)
    private String docDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PNDDOCTYP that = (PNDDOCTYP) o;
        return docType != null && Objects.equals(docType, that.docType);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
