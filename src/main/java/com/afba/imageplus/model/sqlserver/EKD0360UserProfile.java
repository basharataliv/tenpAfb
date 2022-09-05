package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "EKD0360")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@FieldNameConstants
@SQLDelete(sql = "UPDATE EKD0360 SET IS_DELETED = 1 WHERE REPID=?")
@Where(clause = "IS_DELETED=0")
public class EKD0360UserProfile extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "REPID", nullable = false, columnDefinition = "varchar(10) default ''", length = 10)
    private String repId;

    @Column(name = "REPNAM", columnDefinition = "varchar(30) default ' '", length = 30)
    private String repName;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "IS_ADMIN", columnDefinition = "varchar(1) default 'N'", nullable = false, length = 1)
    private Boolean isAdmin;

    @Column(name = "RHTXID", columnDefinition = "varchar(4) default ' '", length = 4)
    private String rhtxId;

    @Column(name = "REPCL1", columnDefinition = "INTEGER default 0", length = 3)
    private Integer repCl1;

    @Column(name = "REPCL2", columnDefinition = "INTEGER default 0", length = 3)
    private Integer repCl2;

    @Column(name = "REPCL3", columnDefinition = "INTEGER default 0", length = 3)
    private Integer repCl3;

    @Column(name = "REPCL4", columnDefinition = "INTEGER default 0", length = 3)
    private Integer repCl4;

    @Column(name = "REPCL5", columnDefinition = "INTEGER default 0", length = 3)
    private Integer repCl5;

    @Column(name = "REPCLR", columnDefinition = "INTEGER default 0", length = 6)
    private Integer repClr;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "RIDXFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean ridxfl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "RSCNFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean rscnfl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "RCASFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean rcasfl;

    @Column(name = "REPDEP", columnDefinition = "varchar(4) default ' '", length = 4)
    private String repDep;

    @Column(name = "REPRGN", columnDefinition = "varchar(4) default ' '", length = 4)
    private String repRgn;

    @Column(name = "RPSTAT", columnDefinition = "varchar(1) default ' '", length = 1)
    private String rpStat;

    @Column(name = "DATELU", columnDefinition = "Date default ''", length = 8)
    private LocalDate dateLu;

    @Column(name = "TIMELU", columnDefinition = "time default ' '", length = 6)
    private LocalTime timeLu;

    @Column(name = "USERLU", columnDefinition = "varchar(10) default ' '", length = 10)
    private String userLu;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EXITCWFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean exitCwfl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "JUMPCWFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean jumpCwfl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "REINDEXFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean reindExfl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "DELETEFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean deleteFl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "MOVEFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean moveFl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "COPYFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean copyFl;

    @Column(name = "SECRANGE", columnDefinition = "INTEGER default '0'", length = 6)
    private Long secRange;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALLOWIMP_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean allowImpA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALLOWFAX", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean allowFax;

    @Column(name = "EKD0360_FC_SEC_LOW", columnDefinition = "INTEGER default 0", length = 3)
    private Long fcSecLow;

    @Column(name = "EKD0360_FC_SEC_HIGH", columnDefinition = "INTEGER default 0", length = 3)
    private Long fcSecHigh;

    @Column(name = "PRTQUEUE", columnDefinition = "varchar(10) default ' '", length = 10)
    private String prtQueue;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWCRANN_A", columnDefinition = "varchar(1) default 'Y'", length = 1)
    private Boolean alwCrannA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWCRRED_A", columnDefinition = "varchar(1) default 'Y'", length = 1)
    private Boolean alwCrredA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWMODANN_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwModAnnA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWMODRED_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwModRedA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWRMVANN_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwRmvAnnA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWRMVRED_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwRmvRedA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWPRSANN_A", columnDefinition = "varchar(1) default 'Y'", length = 1)
    private Boolean alwPrsAnnA;

    @Column(name = "EKD0360_ANNSECLVL_A", columnDefinition = "INTEGER default 0", length = 3)
    private Long annSecLvlA;

    @Column(name = "EKD0360_REDSECLVL_A", columnDefinition = "INTEGER default 0", length = 3)
    private Long redSecLvlA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_PREVERFL_A", columnDefinition = "varchar(1) default 'Y'", length = 1)
    private Boolean preVerFlA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_SEPARATOR_PAGE", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean separatorPage;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_CEFLAG_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean cefLagA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_CFRPFL_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean cfrPflA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_OCRFL_A", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean ocRflA;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWMODDOC_A", columnDefinition = "varchar(1) default 'Y'", length = 1)
    private Boolean alwModDocA;

    @Column(name = "EKD0360_WB_SEC_LOW", columnDefinition = "INTEGER default 0", length = 3)
    private Long wbSecLow;

    @Column(name = "EKD0360_WB_SEC_HIGH", columnDefinition = "INTEGER default 0", length = 3)
    private Long wbSecHigh;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWPMG", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwPmg;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWPMT", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwPmt;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWWBO", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwWbo;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWADC", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwAdc;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWVWC", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwVwc;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EKD0360_ALWWKC", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean alwWkc;

    @Column(name = "EKD0360_DFTCT1PNL", columnDefinition = "varchar(1) default '2'", length = 1)
    private String dftCt1Pnl;

    @Column(name = "EKD0360_RTV_LANATT", columnDefinition = "varchar(1) default '1'", length = 1)
    private String rtvLanatt;

    @Column(name = "EKD0360_DASD_SYSID", columnDefinition = "varchar(1) default '1'", length = 1)
    private String dasdSysId;

    @Column(name = "EKD0360_PRIV_NAME", columnDefinition = "varchar(10) default ' '", length = 10)
    private String privName;

    @Column(name = "EKD0360_ALLOWLOG", columnDefinition = "varchar(1) default '1'", length = 1)
    private String allowLog;

    @Column(name = "FILLER", columnDefinition = "varchar(20) default ' '", length = 20)
    private String filler;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "CLOSEFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean closeFl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "EMSIUSER", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean emsiUser;

    @Column(name = "EKD0360_WQBAPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqBaPrfCont;

    @Column(name = "EKD0360_WQLTPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqLtPrfCont;

    @Column(name = "EKD0360_WQIPPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqIpPrfCont;

    @Column(name = "EKD0360_WQDRPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqDrPrfCont;

    @Column(name = "EKD0360_WQGFPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqGfPrfCont;

    @Column(name = "EKD0360_WQBEPRFCNT", columnDefinition = "INTEGER default 0", length = 3)
    private Integer wqBePrfCont;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "MOVQUEFL", columnDefinition = "varchar(1) default 'N'", length = 1)
    private Boolean moveQueueFl;

    @Column(name = "LU_DATETIME")
    private LocalDateTime luDateTime;

    public LocalDateTime getLuDateTime() {
        if (this.luDateTime == null) {
            if (this.dateLu != null && this.timeLu != null) {
                this.luDateTime = LocalDateTime.of(this.dateLu, this.timeLu);
            }
        }
        return this.luDateTime;
    }

    public void setLuDateTime(LocalDateTime luDateTime) {
        if (luDateTime == null)
            luDateTime = LocalDateTime.now();

        this.luDateTime = luDateTime;
        this.dateLu = luDateTime.toLocalDate();
        this.timeLu = luDateTime.toLocalTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EKD0360UserProfile that = (EKD0360UserProfile) o;
        return repId != null && Objects.equals(repId, that.repId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
