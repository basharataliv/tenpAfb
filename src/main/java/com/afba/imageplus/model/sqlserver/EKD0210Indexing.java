package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.id.EKD0210DocTypeDocIdKey;
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
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EKD0210")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EKD0210DocTypeDocIdKey.class)
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0210Indexing extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKD0210_DOCUMENT_TYPE", nullable = false)
    @DocumentTypeAuthorization
    private String documentType;

    @Id
    @Column(name = "EKD0210_DOCUMENT_ID", nullable = false)
    private String documentId;

    @Id
    @Column(name = "EKD0210_SCANNING_DATE")
    private LocalDate scanDate;

    @Id
    @Column(name = "EKD0210_SCANNING_TIME")
    private LocalTime scanTime;


    @Column(name = "EKD0210_SCANNING_REP_ID")
    private String scanRepId;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0210_INDEX_FLAG")
    private Boolean indexFlag;

    @Column(name = "FILLER")
    private String filler;

    @Column(name = "EKD0210_SCANNING_DATETIME")
    private LocalDateTime scanningDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKD0210Indexing that = (EKD0210Indexing) o;
        return documentType != null && Objects.equals(documentType, that.documentType)
                && documentId != null && Objects.equals(documentId, that.documentId)
                && scanDate != null && Objects.equals(scanDate, that.scanDate)
                && scanTime != null && Objects.equals(scanTime, that.scanTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentType, documentId,scanDate,scanTime);
    }

    public String getDocumentType() {
        return documentType != null ? documentType.trim() : "";
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType != null ? documentType.trim() : "";
    }

    public LocalDateTime getScanningDateTime() {
        if (this.scanningDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        }
        return this.scanningDateTime;
    }

    public void setScanningDateTime(LocalDateTime scanningDateTime) {
        if (scanningDateTime == null) {
            if (this.scanDate != null && this.scanTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanDate, this.scanTime);
            }
        } else {
            this.scanningDateTime = scanningDateTime;
            this.scanDate = scanningDateTime.toLocalDate();
            this.scanTime = scanningDateTime.toLocalTime();
        }
    }
}
