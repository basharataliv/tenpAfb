package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

@Entity
@Table(name = "APPSWTA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
public class APPSWTA extends BaseEntity {
    @Id
    @Column(name = "A_HEIGHT")
    private String height;
    @Column(name = "A_HIGH_WEIGHT")
    private Integer highWeight;
    @Column(name = "A_LOW_WEIGHT")
    private Integer lowWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        APPSWTA appswta = (APPSWTA) o;
        return height != null && Objects.equals(height, appswta.height);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getHeight() {
        return  height != null ? height.trim() : "";
    }

    public void setHeight(String height) {
        this.height = height != null ? height.trim() : "";
    }
}
