package com.afba.imageplus.dto.res;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReindexRes {

    private String documentType;
    private String documentId;
    private String indexRepId;
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean indexFlag;
    private String identifier;
    private LocalDate scanDate;
    private LocalTime scanTime;
    private LocalDateTime scanningDateTime;
    private String workRepId;
    private String statusCode;
    private String scanRepId;

}
