package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOCTEMP")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DOCTEMP extends BaseEntity {

    @Id
    @Column(name = "TEMP_DOC_TYPE")
    private String docType;

    @Column(name = "TEMP_MONTHS")
    private Integer caseNumber;

    public String getDocType() {
        return docType != null ? docType.trim() : "";
    }

    public void setDocType(String docType) {
        this.docType = docType != null ? docType.trim() : "";
    }
}
