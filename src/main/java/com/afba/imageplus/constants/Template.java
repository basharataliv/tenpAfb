package com.afba.imageplus.constants;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.afba.imageplus.constants.TransactionSource.DATA_DIMENSION;
import static com.afba.imageplus.constants.TransactionSource.STAUNTON;

public enum Template {
    APPSNG0702("BA", true, STAUNTON),
    APPSSS0810("BA", true, STAUNTON),
    APPSCO0810("BA", true, STAUNTON),
    APPSSS0818("BA", true, DATA_DIMENSION, STAUNTON),
    APPSBA0217("BA", false, DATA_DIMENSION),
    APPSGF0901("GF", false, DATA_DIMENSION),
    APPSGF0317("GF", false, DATA_DIMENSION),
    APPSCT1013("CT", false, DATA_DIMENSION), // Child app
    APPSEP1009("LT", false, DATA_DIMENSION),
    APPSPR1009("LT", false, DATA_DIMENSION),
    APPSLT0116("LT", false, DATA_DIMENSION), // Senior Protect

    APPSLT0322("LT", false, DATA_DIMENSION), // FED Protect

    FORMCC0902(null, false),
    FORMCK0505(null, false);


    @Getter
    @Setter
    private List<TransactionSource> sources;

    @Getter
    @Setter
    private String productType;

    @Getter
    @Setter
    private boolean isTermedtdta;

    Template(String productType, boolean isTermedtdta, TransactionSource... sources) {
        this.productType = productType;
        this.setTermedtdta(isTermedtdta);
        this.sources = List.of(sources);
    }
}