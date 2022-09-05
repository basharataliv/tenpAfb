package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.converter.Numeric8ByteToLocalTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "DDAPPS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
@FieldNameConstants
public class DDAPPS extends BaseEntity {

    @Id
    @Column(name = "TRANSID")
    private String transactionId;

    @Column(name = "TEMPLNAME")
    private String templateName;

    @Column(name = "NBROFPAGES")
    private Integer noOfPages;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFLG")
    private Boolean processFlag;

    @Column(name = "PRODTYPE")
    private String productType;

    @Column(name = "MBRSSN")
    private Long memberSSN;

    @Column(name = "SPONRANK")
    private String sponRank;

    @Column(name = "SPONLNAME")
    private String sponLastName;

    @Column(name = "SPONFNAME")
    private String sponFirstName;

    @Column(name = "SPONMNAME")
    private String sponMiddleName;

    @Column(name = "SPONSSN")
    private Long sponSSN;

    @Column(name = "COVEFFDATE")
    private LocalDate coverFFDate;

    @Column(name = "AGTCODE")
    private String agentCode;

    @Column(name = "AGTCODE2")
    private String agentCode2;

    @Column(name = "AGTPCT1")
    private Integer agentPercentage1;

    @Column(name = "AGTPCT2")
    private Integer agentPercentage2;

    @Column(name = "AGTMKTCODE")
    private String agentMarketCode;

    @Column(name = "AGTLVL")
    private String agentLevel;

    @Column(name = "AFFCODE")
    private String affCode;

    @Column(name = "DEPLFLAG")
    private String deplFlag;

    @Column(name = "PAYROLLNBR")
    private String payrollNumber;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "ATTCHFLAG")
    private Boolean attachmentFlag;

    @Column(name = "PREMPAID")
    private BigDecimal premiumPaid;

    @Column(name = "NBRATTCH")
    private Integer numberOfAttachment;

    @Column(name = "PLANTYPE")
    private String planType;

    @Column(name = "POSCASENBR")
    private String posCaseNumber;

    @Column(name = "RPLTYPE")
    private String rplType;

    @Column(name = "RANK")
    private String rank;

    @Column(name = "MBRLNAME")
    private String memberLastName;

    @Column(name = "MBRFNAME")
    private String memberFirstName;

    @Column(name = "MBRMINIT")
    private String memberMiddleInitial;

    @Column(name = "MBRSPFLAG")
    private String memberSpFlag;

    @Column(name = "MBRDOB")
    private LocalDate memberDateOfBirth;

    @Column(name = "MBRHEIGHT")
    private Integer memberHeight;

    @Column(name = "MBRWEIGHT")
    private Integer memberWeight;

    @Column(name = "MBRELIGIB")
    private String memberEligibility;

    @Column(name = "MBRDUTYSTT")
    private String memberDutyStatus;

    @Column(name = "ACTIVATEFG")
    private String activateFlag;

    @Column(name = "MARRYIND")
    private String marryInd;

    @Column(name = "ESPGOVIND")
    private String espGovInd;

    @Column(name = "MBRSEX")
    private String memberSex;

    @Column(name = "MBRSMOKER")
    private String memberSmoker;

    @Column(name = "MBRADDR1")
    private String memberAddress1;

    @Column(name = "MBRADDR2")
    private String memberAddress2;

    @Column(name = "MBRCITY")
    private String memberCity;

    @Column(name = "MBRSTATE")
    private String memberState;

    @Column(name = "MBRZIP")
    private Long memberZip;

    @Column(name = "MBREMAIL")
    private String memberEmail;

    @Column(name = "MBRDPHONE")
    private String memberDPhone;

    @Column(name = "MBREPHONE")
    private String memberEPhone;

    @Column(name = "MBRPOBST")
    private String memberPlaceOfBirthState;

    @Column(name = "MBRPOBCNTR")
    private String memberPlaceOfBirthCountry;

    @Column(name = "MBRCITIZEN")
    private String memberCitizen;

    @Column(name = "SPSSN")
    private Long spouseSSN;

    @Column(name = "SPLNAME")
    private String spouseLastName;

    @Column(name = "SPFNAME")
    private String spouseFirstName;

    @Column(name = "SPMINIT")
    private String spouseMiddleInitial;

    @Column(name = "SPDOB")
    private LocalDate spouseDateOfBirth;

    @Column(name = "SPHEIGHT")
    private Integer spouseHeight;

    @Column(name = "SPWEIGHT")
    private Integer spouseWeight;

    @Column(name = "SPSEX")
    private String spouseSex;

    @Column(name = "SPSMOKER")
    private String spouseSmoker;

    @Column(name = "APPSTATE")
    private String appState;

    @Column(name = "SSLICOVTYP")
    private String ssliCoverType;

    @Column(name = "SSLICOVAMT")
    private Integer ssliCoverAmount;

    @Column(name = "SSLIFAMTYP")
    private String ssliFamilyType;

    @Column(name = "EMPTAXID")
    private Long employmentTaxId;

    @Column(name = "OWNERSSN")
    private Long ownerSSN;

    @Column(name = "OWNERFNAME")
    private String ownerFirstName;

    @Column(name = "OWNERLNAME")
    private String ownerLastName;

    @Column(name = "OWNERADDR1")
    private String ownerAddress1;

    @Column(name = "OWNERCITY")
    private String ownerCity;

    @Column(name = "OWNERSTATE")
    private String ownerState;

    @Column(name = "OWNERZIP")
    private Long ownerZip;

    @Column(name = "OWNERREL")
    private String ownerRelation;

    @Column(name = "OWNERPHONE")
    private String ownerPhone;

    @Column(name = "OWNEREMAIL")
    private String ownerEmail;

    @Column(name = "PAYORIND")
    private String payorInd;

    @Column(name = "PAYORSSN")
    private Long payorSSN;

    @Column(name = "PAYORFNAME")
    private String payorFirstName;

    @Column(name = "PAYORLNAME")
    private String payorLastName;

    @Column(name = "PAYORADDR1")
    private String payorAddress1;

    @Column(name = "PAYORCITY")
    private String payorCity;

    @Column(name = "PAYORSTATE")
    private String payorState;

    @Column(name = "PAYORZIP")
    private Long payorZip;

    @Column(name = "PAYORREL")
    private String payorRelation;

    @Column(name = "PAYORPHONE")
    private String payorPhone;

    @Column(name = "CONTOWNER")
    private String contOwner;

    @Column(name = "EXRBENELTR")
    private String exrbeneltr;

    @Column(name = "AGTRPLIND")
    private String agtRplInd;

    @Column(name = "RPLIND")
    private String replacementIndicator;

    @Column(name = "INITPAYMNT")
    private String initPayment;

    @Column(name = "PAYMODE")
    private String payMode;

    @Column(name = "PAYMODEEXT")
    private String payModeExt;

    @Column(name = "LSTBILLFRQ")
    private String lstBillFrq;

    @Column(name = "MBRPREM")
    private BigDecimal memberPremium;

    @Column(name = "SPPREM")
    private BigDecimal spousePremium;

    @Column(name = "CHILDPREM")
    private BigDecimal childPremium;

    @Column(name = "MBRTOTPREM")
    private BigDecimal memberTotalPremium;

    @Column(name = "MBRCOVUNIT")
    private Integer memberCoverUnit;

    @Column(name = "IPCOVUNIT")
    private BigDecimal ipCoverUnit;

    @Column(name = "SPCOVUNIT")
    private Integer spouseCoverUnit;

    @Column(name = "LOANPREFLG")
    private String loanPreFlag;

    @Column(name = "IPPREM")
    private BigDecimal ipPremium;

    @Column(name = "CHILDIND")
    private String childInd;

    @Column(name = "CHILDUNIT")
    private Integer childUnit;

    @Column(name = "ELECISSFLG")
    private String elecIssFlag;

    @Column(name = "MBRB1FNAME")
    private String memberBeneficiary1FirstName;

    @Column(name = "MBRB1LNAME")
    private String memberBeneficiary1LastName;

    @Column(name = "MBRB1REL")
    private String memberBeneficiary1Relation;

    @Column(name = "MBRB1SSN")
    private Long memberBeneficiary1SSN;

    @Column(name = "MBRB1DOB")
    private LocalDate memberBeneficiary1DateOfBirth;

    @Column(name = "MBRB1SEX")
    private String memberBeneficiary1Sex;

    @Column(name = "MBRBPCT1")
    private Integer memberBeneficiaryPercentage1;

    @Column(name = "MBRB2FNAME")
    private String memberBeneficiary2FirstName;

    @Column(name = "MBRB2LNAME")
    private String memberBeneficiary2LastName;

    @Column(name = "MBRB2REL")
    private String memberBeneficiary2Relation;

    @Column(name = "MBRB2SSN")
    private Long memberBeneficiary2SSN;

    @Column(name = "MBRB2DOB")
    private LocalDate memberBeneficiary2DateOfBirth;

    @Column(name = "MBRBPCT2")
    private Integer memberBeneficiaryPercentage2;

    @Column(name = "SPBFNAME")
    private String spouseBeneficiaryFirstName;

    @Column(name = "SPBLNAME")
    private String spouseBeneficiaryLastName;

    @Column(name = "SPBREL")
    private String spouseBeneficiaryRelation;

    @Column(name = "SPBSSN")
    private Long spouseBeneficiarySSN;

    @Column(name = "SPBDOB")
    private LocalDate spouseBeneficiaryDateOfBirth;

    @Column(name = "CHDBFNAME")
    private String childBeneficiaryFirstName;

    @Column(name = "CHDBLNAME")
    private String childBeneficiaryLastName;

    @Column(name = "CHDBREL")
    private String childBeneficiaryRelation;

    @Column(name = "CHDBSSN")
    private Long childBeneficiarySSN;

    @Column(name = "CHDBDOB")
    private LocalDate childBeneficiaryDateOfBirth;

    @Column(name = "MBRHQ")
    private String memberHq;

    @Column(name = "CHILDHQ")
    private String childHq;

    @Column(name = "SPHQ")
    private String spouseHq;

    @Column(name = "MBRHQINIT")
    private String memberHqInit;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "GNALNOTE")
    private Boolean generalNote;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "GNALNOTE1")
    private Boolean generalNote1;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "MBRSIGNFLG")
    private Boolean memberSignFlag;

    @Column(name = "MBRSIGNDAT")
    private LocalDate memberSignDate;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "SPSIGNFLG")
    private Boolean spouseSignFlag;

    @Column(name = "SPSIGNDAT")
    private LocalDate spouseSignDate;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PAYSIGNFLG")
    private Boolean paySignFlag;

    @Column(name = "PAYSIGNDAT")
    private LocalDate paySignDate;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "OWNSIGNFLG")
    private Boolean ownerSignFlag;

    @Column(name = "OWNSIGNDAT")
    private LocalDate ownerSignDate;

    @Column(name = "CNTRCITY")
    private String centerCity;

    @Column(name = "CNTRSTATE")
    private String contractState;

    @Column(name = "XREFSSN")
    private Long xRefSSN;

    @Column(name = "BTMFROM")
    private String btmFrom;

    @Column(name = "BTMFRMAMPM")
    private String btmFromAMPM;

    @Column(name = "BTMTO")
    private String btmTo;

    @Column(name = "BTMTOAMPM")
    private String btmToAMPM;

    @Column(name = "BDAY")
    private String bDay;

    @Column(name = "BDAYAMPM")
    private String bDayAMPM;

    @Column(name = "PARAMEDI")
    private String paraMedi;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "AGTSIGNFLG")
    private Boolean agentSignFlag;

    @Column(name = "AGTSIGNDAT")
    private LocalDate agentSignDate;

    @Column(name = "AGENTNOTE")
    private String agentNote;

    @Column(name = "ENTRYDATE")
    private LocalDate entryDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "ENTRYTIME")
    private LocalTime entryTime;

    @Column(name = "LSTCHGDATE")
    private LocalDate lastChangeDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "LSTCHGTIME")
    private LocalTime lastChangeTime;

    @Column(name = "MKTCAMPGN")
    private String marketingCampaign;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "ELCISSUE")
    private Boolean elcIssue;

    @Column(name = "DUMMYFLD")
    private String dummyField;

    @Column(name = "LT121IND")
    private String lt121Ind;

    @Column(name = "DEPTCODE")
    private String departmentCode;

    @Column(name = "SPEMPFLAG")
    private String spouseEmploymentFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "SUVBENFLG")
    private Boolean suvBenFlag;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "CBSCHFLG")
    private Boolean cbSchFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DDAPPS ddapps = (DDAPPS) o;
        return transactionId != null && Objects.equals(transactionId, ddapps.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
