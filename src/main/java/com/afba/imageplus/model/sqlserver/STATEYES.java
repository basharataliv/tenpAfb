package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.STATEEYESSysTemSysStateKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(STATEEYESSysTemSysStateKey.class)
@Table(name = "STATEYES")
public class STATEYES extends BaseEntity{
    @Id
    @Column(name = "SYTEMPLATE", columnDefinition = "varchar(10) default ' '", length = 10)
    private String systemTemplate;

    @Id
    @Column(name = "SYSTATE", columnDefinition = "varchar(2) default ' '", length = 2)
    private String systemState;
}
