package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.ICRBufferConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ICRFILE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class ICRFile extends BaseEntity  {
    @Id
    @Column(name = "DOC_ID")
    private String documentId;

    @Column(name = "TEMPL_NAME")
    private String templateName;

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Convert(converter = ICRBufferConverter.class)
    @Column(name = "ICR_BUFFER", columnDefinition = "varchar(max)")
    private ICRBuffer icrBuffer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ICRFile icrFile = (ICRFile) o;
        return documentId != null && Objects.equals(documentId, icrFile.documentId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
