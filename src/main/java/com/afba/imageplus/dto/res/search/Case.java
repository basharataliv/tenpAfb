package com.afba.imageplus.dto.res.search;

import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Case {
    String caseId;
    String initialQueueId;
    String currentQueueId;
    String cmFormattedName;
    String cmAccountNumber;
    CaseStatus status;
    String initialRepId;
    String lastRepId;
    LocalDate scanningDate;
    LocalTime scanningTime;
    LocalDate dateLastUpdate;
    LocalTime timeLastUpdate;
    Integer documentCount;
    LocalDateTime scanningDateTime;
    LocalDateTime caseCloseDateTime;
    LocalDateTime lastUpdateDateTime;

    CaseOptions caseOptions;
    List<Document> documents;
}
