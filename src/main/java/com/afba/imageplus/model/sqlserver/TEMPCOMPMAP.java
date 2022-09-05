package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEMPCOMPMAP")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TEMPCOMPMAP extends BaseEntity {

    @Id
    @Column(name = "TEMPLNAME")
    private String templateName;
    @Column(name = "COMPCODE")
    private String companyCode;
    @Column(name = "PRODCODE")
    private String productCode;
    @Column(name = "ExcludeMedicalUnderWriting")
    private String excludeMedicalUnderWriting;

}
