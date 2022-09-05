package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SSNDIF")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SSNDIF extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "ssn")
    private String ssn;

    @Column(name = "createdAt")
    private LocalDateTime CreatedAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "processFlag", length = 1)
    private Boolean processFlag;

}
