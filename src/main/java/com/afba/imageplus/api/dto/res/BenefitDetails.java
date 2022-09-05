package com.afba.imageplus.api.dto.res;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDetails {
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("PolicyNumber")
    private String policyNumber;
    @JsonProperty("BenefitSeq")
    private Integer benefitSeq;
    @JsonProperty("BenefitType")
    private String benefitType;
    @JsonProperty("ProcessedToDate")
    private Integer processedToDate;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("StatusReason")
    private String statusReason;
    @JsonProperty("StatusDate")
    private Integer statusDate;
    @JsonProperty("PlanCode")
    private String planCode;
    @JsonProperty("IssueDate")
    private Integer issueDate;
    @JsonProperty("ParentBenefitSeq")
    private Integer parentBenefitSeq;
    @JsonProperty("PayUpDate")
    private Integer payUpDate;
    @JsonProperty("MatureExpireDate")
    private Integer matureExpireDate;
    @JsonProperty("ProcessingCycle")
    private String processingCycle;
    @JsonProperty("InfRiderSeq")
    private Integer infRiderSeq;
    @JsonProperty("LastChgDate")
    private Integer lastChgDate;
    @JsonProperty("LastChgTime")
    private Integer lastChgTime;
    @JsonProperty("DateOfBirth")
    private Integer dateOfBirth;
    @JsonProperty("IssueAge")
    private Integer issueAge;
    @JsonProperty("UnderwritingClass")
    private String underwritingClass;
    @JsonProperty("SexCode")
    private String sexCode;
    @JsonProperty("InsuredDeathInd")
    private String insuredDeathInd;
    @JsonProperty("SecondInsuredDob")
    private Integer secondInsuredDob;
    @JsonProperty("SecInsIssAge")
    private Integer secInsIssAge;
    @JsonProperty("SecInsUwcls")
    private String secInsUwcls;
    @JsonProperty("SecInsSexCode")
    private String secInsSexCode;
    @JsonProperty("SecInsDeathInd")
    private String secInsDeathInd;
    @JsonProperty("ValuePerUnit")
    private Double valuePerUnit;
    @JsonProperty("NumberOfUnits")
    private Double numberOfUnits;
    @JsonProperty("AnnPremPerUnit")
    private Double annPremPerUnit;
    @JsonProperty("ModePremium")
    private Double modePremium;
    @JsonProperty("CommMdlFctrsFl")
    private String commMdlFctrsFl;
    @JsonProperty("BenefitFee")
    private Double benefitFee;
    @JsonProperty("NonCommHelthFlg")
    private String nonCommHelthFlg;
    @JsonProperty("CommBasedOnDate")
    private String commBasedOnDate;
    @JsonProperty("PremBasedOnDate")
    private String premBasedOnDate;
    @JsonProperty("UnitsNarFlag")
    private String unitsNarFlag;
    @JsonProperty("CategoryCode")
    private String categoryCode;
    @JsonProperty("RenewalType")
    private String renewalType;
    @JsonProperty("AaModelFlag")
    private String aaModelFlag;
    @JsonProperty("EtiMx")
    private Double etiMx;
    @JsonProperty("PolLineOfBusnss")
    private String polLineOfBusnss;
    @JsonProperty("ProductType")
    private String productType;
    @JsonProperty("AgentSplitCtl")
    private Integer agentSplitCtl;
    @JsonProperty("CommissionClass")
    private String commissionClass;
    @JsonProperty("IssueState")
    private String issueState;
    @JsonProperty("ResidentState")
    private String residentState;
    @JsonProperty("AuxAge")
    private Integer auxAge;
    @JsonProperty("BasicBillDate")
    private Integer basicBillDate;
    @JsonProperty("EffPremDate")
    private Integer effPremDate;
    @JsonProperty("MecDate")
    private Integer mecDate;
    @JsonProperty("UnderwriterCode")
    private String underwriterCode;
    @JsonProperty("UnderwriterType")
    private String underwriterType;
    @JsonProperty("ReinCode")
    private Integer reinCode;
    @JsonProperty("PremTaxBasisFl")
    private String premTaxBasisFl;
    @JsonProperty("JoIntegerFlag")
    private String joIntegerFlag;
    @JsonProperty("AllctdTargetPrem")
    private Double allctdTargetPrem;
    @JsonProperty("AllctdMinPrem")
    private Double allctdMinPrem;
    @JsonProperty("TypeCode")
    private String typeCode;
    @JsonProperty("ParType")
    private String parType;
    @JsonProperty("NonForfeiture")
    private String nonForfeiture;
    @JsonProperty("Dividend")
    private String dividend;
    @JsonProperty("OtherInfo")
    private String otherInfo;
    @JsonProperty("AccumDividends")
    private Double accumDividends;
    @JsonProperty("PremiumsPaid")
    private Double premiumsPaid;
    @JsonProperty("DividendsCredited")
    private Double dividendsCredited;
    @JsonProperty("TaxBasis")
    private Double taxBasis;
    @JsonProperty("ExcessDividend")
    private String excessDividend;
    @JsonProperty("UlRiderFvSeq")
    private Integer ulRiderFvSeq;
    @JsonProperty("CapOytOption")
    private String capOytOption;
    @JsonProperty("MaximizeOytFlag")
    private String maximizeOytFlag;
    @JsonProperty("OriginalUnits")
    private Double originalUnits;
    @JsonProperty("OrLPOverride")
    private String orLPOverride;
    @JsonProperty("BillLoanNetDiv")
    private String billLoanNetDiv;
    @JsonProperty("Opt8TarFlag")
    private String opt8TarFlag;
    @JsonProperty("EtiRpuEndowment")
    private Double etiRpuEndowment;
    @JsonProperty("EtiRpuPattern")
    private String etiRpuPattern;
    @JsonProperty("PpPtOytDivs")
    private Double ppPtOytDivs;
    @JsonProperty("FundValue")
    private FundValue fundValue;
    @JsonProperty("UnitValue")
    private UnitValue unitValue;
    @JsonProperty("OtherRider")
    private OtherRider otherRider;
    @JsonProperty("Base")
    private BenefitDetailsBase benefitDetailsBase;
    @JsonProperty("ShadowBenefit")
    private ShadowBenefit shadowBenefit;
    @JsonProperty("SurrenderLoad")
    private SurrenderLoad surrenderLoad;
    @JsonProperty("PaidUp")
    private PaidUp paidUp;
    @JsonProperty("AnnuityRider")
    private AnnuityRider annuityRider;
    @JsonProperty("BaseFund")
    private BaseFund baseFund;
    @JsonProperty("SpecifiedAmountIncrease")
    private SpecifiedAmountIncrease specifiedAmountIncrease;
}
