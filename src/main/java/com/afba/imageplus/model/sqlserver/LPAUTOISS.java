package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.converter.NumericToLocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name = "LPAUTOISS")
public class LPAUTOISS extends BaseEntity {

    @Id
    @Column(name = "LPPOLID", columnDefinition = "varchar(10) default ' '", length = 10)
    private String lpPolicyId;

    @Column(name = "LPDOCID", columnDefinition = "varchar(12) default ' '", length = 12)
    private String lpDocumentId;

    @Column(name = "LPPOLTYPE", columnDefinition = "varchar(4) default ' '", length = 4)
    private String lpPolicyType;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFLG", columnDefinition = "varchar(1) default ' '", length = 1)
    private Boolean processFlag;

    @Column(name = "ENTRYDATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate entryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LPAUTOISS that = (LPAUTOISS) o;
        return lpPolicyId != null && Objects.equals(lpPolicyId, that.lpPolicyId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
