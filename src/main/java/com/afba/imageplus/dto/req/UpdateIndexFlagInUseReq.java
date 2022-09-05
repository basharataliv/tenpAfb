package com.afba.imageplus.dto.req;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Convert;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndexFlagInUseReq {

    @NonNull
    @Convert(converter= BooleanToYNConverter.class)
    private Boolean indexFlag;
}