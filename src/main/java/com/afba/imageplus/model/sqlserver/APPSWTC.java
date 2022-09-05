package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.APPSWTCKey;
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
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "APPSWTC")
@IdClass(APPSWTCKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
public class APPSWTC extends BaseEntity {
    @Id
    @Column(name = "C_AGE")
    private Integer age;
    @Id
    @Column(name = "C_MONTH")
    private Integer month;
    @Id
    @Column(name = "C_SEX")
    private String sex;

    @Column(name = "C_LOW_HEIGHT")
    private Integer lowHeight;

    @Column(name = "C_HIGH_HEIGHT")
    private Integer highHeight;

    @Column(name = "C_LOW_WEIGHT")
    private Integer lowWeight;

    @Column(name = "C_HIGH_WEIGHT")
    private Integer highWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        APPSWTC appswtc = (APPSWTC) o;
        return age != null && Objects.equals(age, appswtc.age)
                && month != null && Objects.equals(month, appswtc.month)
                && sex != null && Objects.equals(sex, appswtc.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age,
                month,
                sex);
    }

    public String getSex() {
        return sex != null ? sex.trim() : "";
    }

    public void setSex(String sex) {
        this.sex = sex != null ? sex.trim() : "";
    }
}
