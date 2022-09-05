package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.AFB0660CaseIdFQueueTQueueKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(AFB0660CaseIdFQueueTQueueKey.class)
@Table(name = "AFB0660")
public class AFB0660 extends BaseEntity{

    @Id
    @Column(name = "CASEID", columnDefinition = "varchar(9) default ' '", length =9 )
    private String caseId;

    @Column(name = "CASETYPE", columnDefinition = "varchar(2) default ' '", length =2 )
    private String caseType;

    @Column(name = "POLICYID", columnDefinition = "varchar(11) default ' '", length =11 )
    private String policyId;

    @Column(name = "ARRVALDATE")
    private LocalDate arrivalDate;

    @Id
    @Column(name = "FROMQUE", columnDefinition = "varchar(10) default ' '", length =10 )
    private String fromQueue;

    @Id
    @Column(name = "TOQUE", columnDefinition = "varchar(10) default ' '", length = 10)
    private String toQueue;

    @Column(name = "USERID", columnDefinition = "varchar(10) default ' '", length = 10)
    private String userId;

    @Column(name = "CURDATE")
    private LocalDate currentDate;

    @Column(name = "CURTIME")
    private LocalTime currentTime;
}
