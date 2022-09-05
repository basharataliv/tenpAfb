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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EMSIINBND")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EMSIINBND extends BaseEntity {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EIPOLID")
    private String eiPolId;

    @Column(name = "EIFILEXT")
    private String eiFileExt;

    @Column(name = "EIDOCID")
    private String eiDocId;

    @Column(name = "EIPAGENO")
    private Integer eiPageNo;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EIPROCFLG")
    private Boolean eiProcessFlg;

    @Column(name = "EIRETCODE")
    private String eiRetCode;

    @Column(name = "EIPROCDAT")
    private LocalDate eiProcessDate;

    @Column(name = "EIPROCTIM")
    private LocalTime eiProcessTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EMSIINBND that = (EMSIINBND) o;
        return eiPolId != null && Objects.equals(eiPolId, that.eiPolId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
