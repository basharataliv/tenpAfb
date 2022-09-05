package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "QCRUNHIS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class QCRUNHIS extends BaseEntity {

    @Id
    @Column(name = "QCCASEID", columnDefinition = "varchar(9) default ' '", length = 9)
    private String qcCaseId;

    @Column(name = "QCPOLID", columnDefinition = "varchar(11) default ' '", length = 11)
    private String qcPolId;

    @Column(name = "QCDOCTYPE", columnDefinition = "varchar(8) default ' '", length = 8)
    private String qcDocType;

    @Column(name = "QCUSERID", columnDefinition = "varchar(8) default ' '", length = 8)
    private String qcUserId;

    @Column(name = "QCREVIEWER", columnDefinition = "varchar(8) default ' '", length = 8)
    private String qcReviewer;

    @Column(name = "QCQCPASS", columnDefinition = "varchar(1) default ' '", length = 1)
    private String qcQcPass;

    @Column(name = "QCDATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate qcDate;

    @Column(name = "QCTIME", columnDefinition = "time default ' '", length = 8)
    private LocalTime qcTime;
}
