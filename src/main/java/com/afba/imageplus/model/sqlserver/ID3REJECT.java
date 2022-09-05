package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ID3REJECT")
@FieldNameConstants
public class ID3REJECT extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SSN_NO", columnDefinition = "varchar(9) default ' '", length = 9)
    private Long ssnNo;

    @Column(name = "POL_CODE", columnDefinition = "varchar(4) default ' '", length = 4)
    private String policyCode;

    @Column(name = "REJ_CAUSE_CODE", columnDefinition = "varchar(8) default ' '", length = 8)
    private String rejectCauseCode;

    @Column(name = "REJ_DATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate rejectDate;

    @Column(name = "REAPP_DATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate reappDate;

    @Column(name = "AUDT_DATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate audtDate;

    @Column(name = "REF_CONT_NO", columnDefinition = "varchar(10) default ' '", length = 10)
    private String referenceContNo;

    @Column(name = "REF_USER_ID", columnDefinition = "varchar(10) default ' '", length = 10)
    private String referenceUserId;

    @Column(name = "REF_COMENT", columnDefinition = "varchar(30) default ' '", length = 30)
    private String refComment;

    @Column(name = "REF_COMENT2", columnDefinition = "varchar(80) default ' '", length = 80)
    private String referenceComment2;

}
