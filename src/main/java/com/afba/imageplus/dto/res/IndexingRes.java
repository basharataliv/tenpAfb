package com.afba.imageplus.dto.res;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndexingRes {
    @Size(min=1, max = 8, message = "documentType cannot be empty")
    String documentType;

    @Size(min=12, max = 12, message = "documentId must contain 12 characters")
    String documentId;

    @Convert(converter= BooleanToYNConverter.class)
    Boolean indexFlag = false;

    LocalDate scanDate;

    LocalTime scanTime;

    String scanRepId;

    LocalDateTime scanningDateTime;
}
