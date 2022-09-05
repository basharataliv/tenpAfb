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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "MOVECASEH")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class MOVECASEH extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "MCCASEID")
    private String mcCaseId;

    @Column(name = "MCFROMQUE")
    private String mcFromQueue;

    @Column(name = "MCTOQUE")
    private String mcToQueue;

    @Column(name = "MCUSERID")
    private String mcUserId;

    @Column(name = "MCDATE")
    private LocalDate mcDate;

    @Column(name = "MCTIME")
    private LocalTime mcTime;
}
