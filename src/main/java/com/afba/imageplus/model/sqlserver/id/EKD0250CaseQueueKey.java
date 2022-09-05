package com.afba.imageplus.model.sqlserver.id;

import com.afba.imageplus.model.sqlserver.converter.NumericToLocalDateConverter;
import com.afba.imageplus.model.sqlserver.converter.NumericToLocalTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EKD0250CaseQueueKey implements Serializable {
    @Column(name = "EKD0250_QUEUE_ID")
    private String queueId;

    @Column(name = "EKD0250_CASE_ID")
    private String caseId;

    @Column(name = "EKD0250_PRIORITY")
    private String priority;

    @Column(name = "EKD0250_SCAN_DATE")
    @Convert(converter = NumericToLocalDateConverter.class)
    private LocalDate scanDate;

    @Column(name = "EKD0250_SCAN_TIME")
    @Convert(converter= NumericToLocalTimeConverter.class)
    private LocalTime scanTime;

}
