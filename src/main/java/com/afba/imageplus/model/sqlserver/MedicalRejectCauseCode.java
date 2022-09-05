package com.afba.imageplus.model.sqlserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "MEDRJT_CAUSE_CODES")
public class MedicalRejectCauseCode extends BaseEntity{
    @Id
    @Column(name = "CAUSE_CODE")
    private String causeCode;

    @Column(name = "CODE_TYPE", columnDefinition = "varchar(3) default ' '", length = 3)
    private String codeType;

    @Column(name = "DESCRIPTION")
    private String codeDes;


}
