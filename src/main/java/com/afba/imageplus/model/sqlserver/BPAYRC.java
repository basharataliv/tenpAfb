package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BPAYRC")
public class BPAYRC extends BaseEntity{
    @Id
    @Column(name = "RC", columnDefinition = "varchar(1) default ' '", length =1 )
    private String rc;

    @Column(name = "RCTEXT", columnDefinition = "varchar(75) default ' '", length =75 )
    private String rcText;

    public String getRc() {
        return rc != null ? rc.trim() : "";

    }

    public void setRc(String rc) {
        this.rc = rc != null ? rc.trim() : "";
    }
}
