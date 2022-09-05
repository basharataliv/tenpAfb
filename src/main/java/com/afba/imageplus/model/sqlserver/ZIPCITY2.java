package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ZIPCITY2")
public class ZIPCITY2 extends BaseEntity{

    @Id
    @Column(name = "ZIP2")
    private Long zip2;

    @Column(name = "CITY2")
    private String city2;

    public String getCity2() {
        return  city2 != null ? city2.trim() : "";
    }

    public void setCity2(String city2) {
        this.city2 = city2 != null ? city2.trim() : "";
    }
}
