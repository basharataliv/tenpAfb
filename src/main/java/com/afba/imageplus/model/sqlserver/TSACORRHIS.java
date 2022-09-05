package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TSACORRHIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class TSACORRHIS {
    @Id
    @Column(name = "TCDOCID")
    private String documentId;

    @Column(name = "TCSSN")
    private String ssn;

    @Column(name = "TCDOCTYP")
    private String docType;

    @Column(name = "TCSCANDATE")
    private LocalDate scanningDate;
}
