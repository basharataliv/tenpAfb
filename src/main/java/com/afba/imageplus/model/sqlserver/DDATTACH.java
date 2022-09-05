package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.converter.Numeric8ByteToLocalTimeConverter;
import com.afba.imageplus.model.sqlserver.id.DDATTACHTransIdTemplateNameKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@IdClass(DDATTACHTransIdTemplateNameKey.class)
@Table(name = "DDATTACH")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
@FieldNameConstants
public class DDATTACH extends BaseEntity {

    @Id
    @Column(name = "TRANSID", columnDefinition = "varchar(8) default ' '", length = 8)
    private String transactionId;

    @Id
    @Column(name = "TEMPLNAME", columnDefinition = "varchar(10) default ' '", length = 10)
    private String templateName;

    @Column(name = "NBROFPAGES", columnDefinition = "varchar(2) default ' '", length = 2)
    private Integer noOfPages;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFLG", columnDefinition = "varchar(1) default ' '", length = 1)
    private Boolean processFlag;

    @Column(name = "APPLFLAG", columnDefinition = "varchar(1) default ' '", length = 1)
    private String applyFlag;

    @Column(name = "ENTRYDATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate entryDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "ENTRYTIME", length = 8)
    private LocalTime entryTime;

    @Column(name = "LSTCHGDATE", columnDefinition = "date default ' '", length = 8)
    private LocalDate lastChangeDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "LSTCHGTIME", length = 8)
    private LocalTime lastChangeTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DDATTACH ddattach = (DDATTACH) o;
        return transactionId != null && Objects.equals(transactionId, ddattach.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName != null ? templateName.trim() : "";
    }

    public String getTemplateName() {
        return templateName != null ? templateName.trim() : "";
    }

}
