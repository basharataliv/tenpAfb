package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "CODESFL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CODESFL extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SYS_CODETYPE")
    private String sysCodeType;

    @Column(name = "SYS_CODE")
    private String sysCode;

    @Column(name = "SYS_CODEDATE", columnDefinition = "DATE DEFAULT GETDATE()", insertable = false)
    private LocalDate sysCodeDate;

    @Column(name = "SYS_CODEDESC")
    private String sysCodeDescription;

    @Column(name = "SYS_CODEBUFF")
    private String sysCodeBuff;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        CODESFL that = (CODESFL) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
