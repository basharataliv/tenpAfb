package com.afba.imageplus.dto.res;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class UserProfileRes {

    private String repId;

    private String repName;

    private String rhtxId;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAdmin;

    private Integer repCl1;

    private Integer repCl2;

    private Integer repCl3;

    private Integer repCl4;

    private Integer repCl5;

    private Integer repClr;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean ridxfl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean rscnfl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean rcasfl;

    private String repDep;

    private String repRgn;

    private String rpStat;

    private LocalDate dateLu;

    private LocalTime timeLu;

    private String userLu;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean exitCwfl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean jumpCwfl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean reindExfl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean deleteFl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean moveFl;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean copyFl;

    private Long secRange;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean allowImpA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean allowFax;

    private Long fcSecLow;

    private Long fcSecHigh;

    private String prtQueue;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwCrannA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwCrredA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwModAnnA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwModRedA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwRmvAnnA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwRmvRedA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwPrsAnnA;

    private Long annSecLvlA;

    private Long redSecLvlA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean preVerFlA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean separatorPage;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean cefLagA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean cfrPelA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean ocRflA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwModDocA;

    private Long wbSecLow;

    private Long wbSecHigh;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwPmg;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwPmt;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwWbo;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwAdc;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwVwc;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwWkc;

    private String dftCt1Pnl;

    private String rtvLanatt;

    private String dasdSysId;

    private String privName;

    private String allowLog;

    private String filler;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean closeFl;

    private Integer wqBaPrfCont;

    private Integer wqLtPrfCont;

    private Integer wqIpPrfCont;

    private Integer wqDrPrfCont;

    private Integer wqGfPrfCont;

    private Integer wqBePrfCont;

    private LocalDateTime luDateTime;

}