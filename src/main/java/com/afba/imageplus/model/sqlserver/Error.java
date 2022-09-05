package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ERROR")
@Entity
@DynamicInsert
@DynamicUpdate
public class Error extends BaseEntity {

    @Id
    @Column(name = "CODE", length = 9, nullable = false)
    private String code;
    @Column(name = "MESSAGE", length = 1000, nullable = false)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Error error = (Error) o;
        return code != null && Objects.equals(code, error.code);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
