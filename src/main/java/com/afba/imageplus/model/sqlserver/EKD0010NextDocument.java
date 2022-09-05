package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "EKD0010")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@DynamicInsert
@DynamicUpdate
public class EKD0010NextDocument extends BaseEntity {

    @Id
    @Column(name = " EKD0010_JULIAN_DATE")
    private String julianDate;

    @NonNull
    @Column(name = "EKD0010_DOCUMENT_ID")
    private String documentId;

    @Column(name = "EKD0010_BATCH_SEQ")
    private Integer batchSequence = 0;

    @Column(name = "FILLER", nullable = true)
    private String filler;

    public EKD0010NextDocument(String julianDate, String initialDocumentName) {
        this.julianDate=julianDate;
        this.documentId=initialDocumentName;
    }

    @Column(name = "EKD0010_JULIAN_DATETIME")
    private LocalDateTime julianDateTime;

    public LocalDateTime getJulianDateTime() {
        if (this.julianDateTime == null) {
            if (this.julianDate != null) {
                DateTimeFormatter dayOfYearFormatter
                        = DateTimeFormatter.ofPattern("yyyyDDD");
                LocalDate date = LocalDate.parse(this.julianDate, dayOfYearFormatter);
                this.julianDateTime = LocalDateTime.of(date, LocalTime.now());
            }
        }
        return julianDateTime;
    }

    public void setJulianDateTime(LocalDateTime julianDateTime) {
        if (this.julianDate != null) {
            DateTimeFormatter dayOfYearFormatter
                    = DateTimeFormatter.ofPattern("yyyyDDD");
            LocalDate date = LocalDate.parse(this.julianDate, dayOfYearFormatter);
            this.julianDateTime = LocalDateTime.of(date, LocalTime.now());
        } else {
            this.julianDateTime = julianDateTime;
        }
    }
}
