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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name = "FINTRGTQ")
public class FINTRGTQ extends BaseEntity{

    @Id
    @Column(name = "POLID", columnDefinition = "varchar(11) default ' '", length = 11)
    private String policyId;

    @Column(name = "QUEID", columnDefinition = "varchar(10) default ' '", length = 10)
    private String queueId;
}
