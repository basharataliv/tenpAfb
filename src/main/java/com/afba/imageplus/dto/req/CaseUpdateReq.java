package com.afba.imageplus.dto.req;

import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CaseUpdateReq {

    private LocalDate caseCloseDate;
    private LocalTime caseCloseTime;
    private String initialRepId;
    private String lastRepId;
    private CaseStatus status;
    @JsonIgnore
    private LocalDate scanningDate;
    @JsonIgnore
    private LocalTime scanningTime;
    private String cmAccountNumber;
    private String cmFormattedName;
    @JsonIgnore
    private LocalDate dateLastUpdate;
    @JsonIgnore
    private LocalTime timeLastUpdate;
    private String chargeBackFlag;
    private String filler;
    private String currentQueue;

    @JsonIgnore
    private LocalDateTime scanningDateTime;
    @JsonIgnore
    private LocalDateTime lastUpdateDateTime = LocalDateTime.now();
    @JsonIgnore
    private LocalDateTime caseCloseDateTime;

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
}
