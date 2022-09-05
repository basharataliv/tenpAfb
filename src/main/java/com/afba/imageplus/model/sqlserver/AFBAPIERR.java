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
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "AFBAPIERR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AFBAPIERR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AFBPGM", columnDefinition = "varchar(10) default ' '", length = 10, nullable = false)
    private String afbProgram;

    @Column(name = "USERID", columnDefinition = "varchar(10) default ' '", length = 10, nullable = false)
    private String userId;

    @Column(name = "DATEIN", columnDefinition = "date default ' '", length = 8, nullable = false)
    private Date dateIn;

    @Column(name = "TIMEIN", columnDefinition = "integer default 0", length =8 )
    private Long timeIn;

    @Column(name = "WAFPGM", columnDefinition = "varchar(10) default ' '", length = 10, nullable = false)
    private String wafProgram;

    @Column(name = "RETCOD", columnDefinition = "integer default 0", length = 6, nullable = false)
    private Integer returnCode;

    @Column(name = "STATUS", columnDefinition = "varchar(6) default ' '", length = 6, nullable = false)
    private String status;

    @Column(name = "TEXT", columnDefinition = "varchar(100) default ' '", length = 100, nullable = false)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AFBAPIERR that = (AFBAPIERR) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
         return getClass().hashCode();
    }
}
