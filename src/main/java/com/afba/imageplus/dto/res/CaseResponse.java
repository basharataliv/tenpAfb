package com.afba.imageplus.dto.res;

import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseResponse {
    private String caseId;
    private LocalDate caseCloseDate;
    private LocalTime caseCloseTime;
    private String initialQueueId;
    private String initialRepId;
    private String lastRepId;
    private CaseStatus status;
    private LocalDate scanningDate;
    private LocalTime scanningTime;
    private String cmAccountNumber;
    private String cmFormattedName;
    private LocalDate dateLastUpdate;
    private LocalTime timeLastUpdate;
    private String chargeBackFlag;
    private String currentQueueId;
    private String filler;
    private LocalDateTime scanningDateTime;
    private LocalDateTime caseCloseDateTime;
    private LocalDateTime lastUpdateDateTime;

    public LocalDateTime getCaseCloseDateTime() {
        if (this.caseCloseDateTime == null) {
            if (this.caseCloseDate != null && this.caseCloseTime != null) {
                this.caseCloseDateTime = LocalDateTime.of(this.caseCloseDate, this.caseCloseTime);
            }
        }
        return this.caseCloseDateTime;
    }

    public void setCaseCloseDateTime(LocalDateTime caseCloseDateTime) {
        if (caseCloseDateTime == null) {
            if (this.caseCloseDate != null && this.caseCloseTime != null) {
                this.caseCloseDateTime = LocalDateTime.of(this.caseCloseDate, this.caseCloseTime);
            }
        } else {
            this.caseCloseDateTime = caseCloseDateTime;
            this.caseCloseDate = caseCloseDateTime.toLocalDate();
            this.caseCloseTime = caseCloseDateTime.toLocalTime();
        }
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        if (lastUpdateDateTime == null)
            lastUpdateDateTime = LocalDateTime.now();

        this.lastUpdateDateTime = lastUpdateDateTime;
        this.dateLastUpdate = lastUpdateDateTime.toLocalDate();
        this.timeLastUpdate = lastUpdateDateTime.toLocalTime();
    }

    public LocalDateTime getLastUpdateDateTime() {
        if (this.lastUpdateDateTime == null) {
            if (this.dateLastUpdate != null && this.timeLastUpdate != null) {
                this.lastUpdateDateTime = LocalDateTime.of(this.dateLastUpdate, this.timeLastUpdate);
            }
        }
        return this.lastUpdateDateTime;
    }

    public LocalDateTime getScanningDateTime() {
        if (this.scanningDateTime == null) {
            if (this.scanningDate != null && this.scanningTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanningDate, this.scanningTime);
            }
        }
        return this.scanningDateTime;
    }

    public void setScanningDateTime(LocalDateTime scanningDateTime) {
        if (scanningDateTime == null) {
            if (this.scanningDate != null && this.scanningTime != null) {
                this.scanningDateTime = LocalDateTime.of(this.scanningDate, this.scanningTime);
            }
        } else {
            this.scanningDateTime = scanningDateTime;
            this.scanningDate = scanningDateTime.toLocalDate();
            this.scanningTime = scanningDateTime.toLocalTime();
        }
    }
}
