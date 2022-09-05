package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.ZIPCITY1Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ZIPCITY1")
@IdClass(ZIPCITY1Key.class)
public class ZIPCITY1 extends BaseEntity{
    @Id
    @Column(name = "ZIP1")
    private Long zip1;

    @Id
    @Column(name = "CITY1")
    private String city1;

    public String getCity1() {
        return  city1 != null ? city1.trim() : "";
    }

    public void setCity1(String city1) {
        this.city1 = city1 != null ? city1.trim() : "";
    }
}
