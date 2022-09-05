package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.STETBLPKey;
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
@Table(name = "STETBLP")
@IdClass(STETBLPKey.class)
public class STETBLP extends BaseEntity{
    @Id
    @Column(name = "CRCPCD")
    private String crcpCd;

    @Id
    @Column(name = "CRCOCD")
    private String crcoCd;

    @Column(name = "CRB6NA")
    private String crb6na;

    @Column(name = "CRWRST")
    private String crwrst;

    public String getCrcpCd() {
        return crcpCd != null ? crcpCd.trim() : "";
    }

    public void setCrcpCd(String crcpCd) {
        this.crcpCd = crcpCd != null ? crcpCd.trim() : "";
    }

    public String getCrcoCd() {
        return crcoCd != null ? crcoCd.trim() : "";
    }

    public void setCrcoCd(String crcoCd) {
        this.crcoCd = crcoCd != null ? crcoCd.trim() : "";
    }
}
