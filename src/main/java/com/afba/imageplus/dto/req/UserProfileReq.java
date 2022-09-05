package com.afba.imageplus.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserProfileReq {

    @NotBlank(message = "repId cannot be blank")
    @Size(min = 1, max = 10, message = "Length of repId must be between 1-10")
    private String repId;

    @NotBlank(message = "repName cannot be blank")
    @Size(min = 1, max = 30, message = "Length of repName must be between 1-30")
    private String repName;

    @NotNull(message = "isAdmin cannot be null")
    private Boolean isAdmin;

    // Not planning to use
    @Size(max = 4, message = "Length of rhtxId must be between 1-4")
    private String rhtxId;

    @Max(value = 999, message = "Length of repCl1 cannot exceed more than 3 digits")
    private Integer repCl1;

    @Max(value = 999, message = "Length of repCl2 cannot exceed more than 3 digits")
    private Integer repCl2;

    @Max(value = 999, message = "Length of repCl3 cannot exceed more than 3 digits")
    private Integer repCl3;

    @Max(value = 999, message = "Length of repCl4 cannot exceed more than 3 digits")
    private Integer repCl4;

    @Max(value = 999, message = "Length of repCl5 cannot exceed more than 3 digits")
    private Integer repCl5;

    @NotNull(message = "repClr cannot be null")
    @Min(value = 0, message = "Length of repClr cannot be less than 0")
    @Max(value = 999999, message = "Length of repClr cannot exceed more than 6 digits")
    private Integer repClr;

    @NotNull(message = "ridxfl cannot be null")
    private Boolean ridxfl;

    @NotNull(message = "rscnfl cannot be null")
    private Boolean rscnfl;

    @NotNull(message = "rcasfl cannot be null")
    private Boolean rcasfl;

    @NotNull(message = "repDep cannot be null")
    @Size(min = 1, max = 4, message = "Length of repDep must be between 1-4")
    private String repDep;

    @NotNull(message = "repRgn cannot be null")
    @Size(min = 1, max = 4, message = "Length of repRgn must be between 1-4")
    private String repRgn;

    @NotNull(message = "rpStat cannot be null")
    @Size(min = 1, max = 1, message = "Length of rpStat must exactly be 1")
    private String rpStat;

    @JsonIgnore
    private LocalDate dateLu = LocalDate.now();

    @JsonIgnore
    private LocalTime timeLu = LocalTime.now();

    @JsonIgnore
    private String userLu;

    // Not planning to use
    private Boolean exitCwfl;

    // Not planning to use
    private Boolean jumpCwfl;

    @NotNull(message = "reindExfl cannot be null")
    private Boolean reindExfl;

    @NotNull(message = "deleteFl cannot be null")
    private Boolean deleteFl;

    @NotNull(message = "moveFl cannot be null")
    private Boolean moveFl;

    @NotNull(message = "copyFl cannot be null")
    private Boolean copyFl;

    @NotNull(message = "secRange cannot be null")
    @Min(value = 0, message = "Length of secRange cannot be less than 0")
    @Max(value = 999999, message = "Length of secRange cannot exceed more than 6 digits")
    private Long secRange;

    @NotNull(message = "allowImpA cannot be null")
    private Boolean allowImpA;

    // Not planning to use
    private Boolean allowFax;

    // Not planning to use
    @Max(value = 999, message = "Length of fcSecLow cannot exceed more than 3 digits")
    private Long fcSecLow;

    // Not planning to use
    @Max(value = 999, message = "Length of fcSecHigh cannot exceed more than 3 digits")
    private Long fcSecHigh;

    // Not planning to use
    private String prtQueue;

    // Not planning to use
    private Boolean alwCrannA;

    // Not planning to use
    private Boolean alwCrredA;

    // Not planning to use
    private Boolean alwModAnnA;

    // Not planning to use
    private Boolean alwModRedA;

    // Not planning to use
    private Boolean alwRmvAnnA;

    // Not planning to use
    private Boolean alwRmvRedA;

    // Not planning to use
    private Boolean alwPrsAnnA;

    // Not planning to use
    @Max(value = 999, message = "Length of annSecLvlA cannot exceed more than 3 digits")
    private Long annSecLvlA;

    // Not planning to use
    @Max(value = 999, message = "Length of redSecLvlA cannot exceed more than 3 digits")
    private Long redSecLvlA;

    // Not planning to use
    private Boolean preVerFlA;

    // Not planning to use
    private Boolean separatorPage;

    // Not planning to use
    private Boolean cefLagA;

    // Not planning to use
    private Boolean cfrPflA;

    // Not planning to use
    private Boolean ocRflA;

    // Not planning to use
    private Boolean alwModDocA;

    // Not planning to use
    @Max(value = 999, message = "Length of wbSecLow cannot exceed more than 3 digits")
    private Long wbSecLow;

    // Not planning to use
    @Max(value = 999, message = "Length of wbSecHigh cannot exceed more than 3 digits")
    private Long wbSecHigh;

    // Not planning to use
    private Boolean alwPmg;

    // Not planning to use
    private Boolean alwPmt;

    // Not planning to use
    private Boolean alwWbo;

    @NotNull(message = "alwAdc cannot be null")
    private Boolean alwAdc;

    @NotNull(message = "alwVwc cannot be null")
    private Boolean alwVwc;

    @NotNull(message = "alwWkc cannot be null")
    private Boolean alwWkc;

    @NotNull(message = "dftCt1Pnl cannot be null")
    @Size(min = 1, max = 1, message = "Length of dftCt1Pnl must exactly be 1")
    private String dftCt1Pnl;

    // Not planning to use
    @Size(min = 1, max = 1, message = "Length of rtvLanatt must exactly be 1")
    private String rtvLanatt;

    // Not planning to use
    @Size(min = 1, max = 1, message = "Length of dasdSysId must exactly be 1")
    private String dasdSysId;

    // Not planning to use
    @Size(min = 1, max = 10, message = "Length of privName must be between 1-10")
    private String privName;

    // Not planning to use
    @Size(min = 1, max = 1, message = "Length of allowLog must exactly be 1")
    private String allowLog;

    // Not planning to use
    @Size(min = 1, max = 20, message = "Length of filler must be between 1-20")
    private String filler;

    @NotNull(message = "closeFl cannot be null")
    private Boolean closeFl;

    @Max(value = 2147483647, message = "Length of wqBaPrfCont cannot exceed more than 2147483647")
    private Integer wqBaPrfCont;

    @Max(value = 2147483647, message = "Length of wqLtPrfCont cannot exceed more than 2147483647")
    private Integer wqLtPrfCont;

    @Max(value = 2147483647, message = "Length of wqIpPrfCont cannot exceed more than 2147483647")
    private Integer wqIpPrfCont;

    @Max(value = 2147483647, message = "Length of wqDrPrfCont cannot exceed more than 2147483647")
    private Integer wqDrPrfCont;

    @Max(value = 2147483647, message = "Length of wqGfPrfCont cannot exceed more than 2147483647")
    private Integer wqGfPrfCont;

    @Max(value = 2147483647, message = "Length of wqBePrfCont cannot exceed more than 2147483647")
    private Integer wqBePrfCont;

    @NotNull(message = "emsiUser cannot be null")
    private Boolean emsiUser;

    @NotNull(message = "moveQueueFl cannot be null")
    private Boolean moveQueueFl;

    @JsonIgnore
    private LocalDateTime luDateTime = LocalDateTime.now();
}
