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

@Entity
@Table(name = "WRKQCRUN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class WRKQCRUN extends BaseEntity {

    @Id
    @Column(name = "WRUSRID", columnDefinition = "varchar(8) default ' '", length = 8)
    private String wrUserId;

    @Column(name = "WRBARUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqBaRunCont;

    @Column(name = "WRLTRUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqLtRunCont;

    @Column(name = "WRIPRUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqIpRunCont;

    @Column(name = "WRDRRUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqDrRunCont;

    @Column(name = "WRGFRUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqGfRunCont;

    @Column(name = "WRBERUNCNT", nullable = false, columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqBeRunCont;
}
