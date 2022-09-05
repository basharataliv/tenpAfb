package com.afba.imageplus.constants;

import lombok.Getter;
import lombok.Setter;

public enum TransactionSource {
    DATA_DIMENSION("DATADM"),
    STAUNTON("SHUEY");

    @Getter
    @Setter
    private String value;

    TransactionSource(String value) {
        this.value = value;
    }

}
