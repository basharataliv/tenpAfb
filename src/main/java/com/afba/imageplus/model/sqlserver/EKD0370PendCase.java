package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EKD0370")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class EKD0370PendCase extends SoftDeleteBaseEntity {
    
    @Id
    @Column(name = "EKD0370_CASE_ID", nullable = false, columnDefinition = "varchar(9) default ' '", length = 9)
    public String caseId;

    @Column(name = "EKD0370_DOCUMENT_TYPE", nullable = true, columnDefinition = "varchar(8) default ' '", length =8 )
    @DocumentTypeAuthorization
    public String documentType;
    
    @Column(name = "EKD0370_IDENTIFIER", nullable = false, columnDefinition = "varchar(40) default ' '", length =40 )
    public String identifier;
    
    @Column(name = "EKD0370_INPUT_FIELD_1", nullable = true, columnDefinition = "varchar(20) default ' '", length =20 )
    public String inputField1;
    
    @Column(name = "EKD0370_INPUT_FIELD_2", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    public String inputField2;
    
    @Column(name = "EKD0370_INPUT_FIELD_3", nullable = true, columnDefinition = "varchar(20) default ' '", length =20 )
    public String inputField3;
    
    @Column(name = "EKD0370_INPUT_FIELD_4", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    public String inputField4;
    
    @Column(name = "EKD0370_INPUT_FIELD_5", nullable = true, columnDefinition = "varchar(20) default ' '", length =20 )
    public String inputField5;
    
    @Column(name = "EKD0370_RELEASE_DATE", nullable = false, columnDefinition = "   Date default ' '", length =8 )
    public LocalDate releaseDate;
    
    @Column(name = "EKD0370_RETURN_QUEUE", nullable = false, columnDefinition = "varchar(10) default ' '", length =10 )
    public String returnQueue;
    
    @Column(name = "EKD0370_LAST_REP_ID", nullable = false, columnDefinition = "varchar(10) default ' '", length = 10)
    public String lastRepId;
    
    @Column(name = "EKD0370_PEND_DATE", nullable = false, columnDefinition = "Date default ' '", length =8 )
    public LocalDate pendDate;
    
    @Column(name = "EKD0370_PRINT_REQUEST_FLAG", nullable = true, columnDefinition = "varchar(1) default ' '", length = 1)
    public String printRequestFlag;
    
    @Column(name = "EKD0370_REQ_LIST_DESCRIPTION", nullable = true, columnDefinition = "varchar(40) default ' '", length = 40)
    public String reqListDescription;
    
    @Column(name = "EKD0370_MEDIA_MATCH_FLAG", nullable = true, columnDefinition = "varchar(1) default ' '", length = 1)
    public String mediaMatchFlag;
    
    @Column(name = "EKD0370_PEND_TIME", nullable = false, columnDefinition = "time default ' '", length = 8)
    public LocalTime pendTime;
    
    @Column(name = "FIL11", nullable = true, columnDefinition = "varchar(20) default ' '", length = 20)
    public String fil11;

    @Column(name = "EKD0370_RELEASE_DATETIME")
    public LocalDateTime releaseDateTime;

    @Column(name = "EKD0370_PEND_DATETIME")
    private LocalDateTime pendDateTime;

    public LocalDateTime getReleaseDateTime() {
        if (this.releaseDateTime == null) {
            if (this.releaseDateTime != null) {
                this.releaseDateTime = LocalDateTime.of(this.releaseDate, LocalTime.now());
            }
        }
        return this.releaseDateTime;
    }

    public void setReleaseDateTime(LocalDateTime releaseDateTime) {
        if (releaseDateTime == null) {
            if (this.releaseDate != null) {
                this.releaseDateTime = LocalDateTime.of(this.releaseDate, LocalTime.now());
            }
        } else {
            this.releaseDateTime = releaseDateTime;
            this.releaseDate = releaseDateTime.toLocalDate();
        }
    }

    public void setPendDateTime(LocalDateTime pendDateTime) {
        if (pendDateTime == null)
            pendDateTime = LocalDateTime.now();

        this.pendDateTime = pendDateTime;
        this.pendDate = pendDateTime.toLocalDate();
        this.pendTime = pendDateTime.toLocalTime();
    }

    public LocalDateTime getPendDateTime() {
        if (this.pendDateTime == null) {
            if (this.pendDate != null && this.pendTime != null) {
                this.pendDateTime = LocalDateTime.of(this.pendDate, this.pendTime);
            }
        }
        return this.pendDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKD0370PendCase that = (EKD0370PendCase) o;
        return caseId != null && Objects.equals(caseId, that.caseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
