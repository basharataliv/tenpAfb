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
public class EKD0210DocTypeDocIdKey implements Serializable {
    @Column(name = "EKD0210_DOCUMENT_TYPE")
    private String documentType;

    @Column(name = "EKD0210_DOCUMENT_ID")
    private String documentId;

    @Column(name = "EKD0210_SCANNING_DATE")
    @Convert(converter = NumericToLocalDateConverter.class)
    private LocalDate scanDate;

    @Column(name = "EKD0210_SCANNING_TIME")
    @Convert(converter= NumericToLocalTimeConverter.class)
    private LocalTime scanTime;

}
