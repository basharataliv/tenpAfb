package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPolicyRes {

    @JsonProperty("Company_Code")
    private String company_Code;
    @JsonProperty("Policy_Number")
    private String policy_Number;
    @JsonProperty("Coupon_Policy_Flag")
    private String coupon_Policy_Flag;
    @JsonProperty("Coupon_ANN_Flag")
    private String coupon_ANN_Flag;
    @JsonProperty("Coupon_Loan_Flag")
    private String coupon_Loan_Flag;
    @JsonProperty("Policy_Code")
    private String policy_Code;
    @JsonProperty("Line_Of_Business")
    private String line_Of_Business;
    @JsonProperty("Product_Code")
    private String product_Code;
    @JsonProperty("Total_Ben_Count")
    private String total_Ben_Count;
    @JsonProperty("Total_Loan_Count")
    private String total_Loan_Count;
    @JsonProperty("Last_Change_Date")
    private String last_Change_Date;
    @JsonProperty("Last_Change_Time")
    private String last_Change_Time;
    @JsonProperty("Last_Change_Oper")
    private String last_Change_Oper;
    @JsonProperty("Misc_Billing_IND")
    private String misc_Billing_IND;
    @JsonProperty("UL_Premium_Type")
    private String uL_Premium_Type;
    @JsonProperty("Last_Sys_Act_Date")
    private String last_Sys_Act_Date;
    @JsonProperty("Last_Sys_Act_Code")
    private String last_Sys_Act_Code;
    @JsonProperty("Contract_Code")
    private String contract_Code;
    @JsonProperty("Contract_Reason")
    private String contract_Reason;
    @JsonProperty("Contract_Date")
    private String contract_Date;
    @JsonProperty("Billing_Code")
    private String billing_Code;
    @JsonProperty("Billing_Reason")
    private String billing_Reason;
    @JsonProperty("Billing_Date")
    private String billing_Date;
    @JsonProperty("Payment_Code")
    private String payment_Code;
    @JsonProperty("Payment_Reason")
    private String payment_Reason;
    @JsonProperty("Payment_Date")
    private String payment_Date;
    @JsonProperty("Policy_Bill_Day")
    private String policy_Bill_Day;
    @JsonProperty("Loan_Bill_Day")
    private String loan_Bill_Day;
    @JsonProperty("AR_Bill_Day")
    private String aR_Bill_Day;
    @JsonProperty("Second_Bill_Day")
    private String second_Bill_Day;
    @JsonProperty("AR_Benefit_Seq")
    private String aR_Benefit_Seq;
    @JsonProperty("PMT_Since_Last_MPR")
    private String pMT_Since_Last_MPR;
    @JsonProperty("TAMRA_MEC_Flag")
    private String tAMRA_MEC_Flag;
    @JsonProperty("Bill_Code")
    private String bill_Code;
    @JsonProperty("Issue_Date")
    private String issue_Date;
    @JsonProperty("Application_Date")
    private String application_Date;
    @JsonProperty("APP_Received_Date")
    private String aPP_Received_Date;
    @JsonProperty("Issue_State")
    private String issue_State;
    @JsonProperty("Res_State")
    private String res_State;
    @JsonProperty("County_Code")
    private String county_Code;
    @JsonProperty("Zip")
    private String zip;
    @JsonProperty("Zip_Ext")
    private String zip_Ext;
    @JsonProperty("Actual_Bill_Date")
    private String actual_Bill_Date;
    @JsonProperty("Billing_Mode")
    private String billing_Mode;
    @JsonProperty("Billing_Form")
    private String billing_Form;
    @JsonProperty("Billed_To_Date")
    private String billed_To_Date;
    @JsonProperty("Paid_To_Date")
    private String paid_To_Date;
    @JsonProperty("Group_Number")
    private String group_Number;
    @JsonProperty("Mode_Premium")
    private String mode_Premium;
    @JsonProperty("Annual_Premium")
    private String annual_Premium;
    @JsonProperty("Loan_Repmt_Date")
    private String loan_Repmt_Date;
    @JsonProperty("Loan_Repmt_Mode")
    private String loan_Repmt_Mode;
    @JsonProperty("Loan_Repmt_Form")
    private String loan_Repmt_Form;
    @JsonProperty("Loan_Repmt_Amount")
    private String loan_Repmt_Amount;
    @JsonProperty("ACTG_Block_IND")
    private String aCTG_Block_IND;
    @JsonProperty("IBA_Code")
    private String iBA_Code;
    @JsonProperty("Conversion_Flag")
    private String conversion_Flag;
    @JsonProperty("Poliy_Fee")
    private String poliy_Fee;
    @JsonProperty("Polc_Special_Mode")
    private String polc_Special_Mode;
    @JsonProperty("Tax_Qualify_Code")
    private String tax_Qualify_Code;
    @JsonProperty("FED_Withhold_Code")
    private String fED_Withhold_Code;
    @JsonProperty("FED_Certify_Code")
    private String fED_Certify_Code;
    @JsonProperty("ADJ_Bill_Flag")
    private String aDJ_Bill_Flag;
    @JsonProperty("Delivery_Date")
    private String delivery_Date;
    @JsonProperty("Withdraw_INT_Flag")
    private String withdraw_INT_Flag;
    @JsonProperty("Override_Flag")
    private String override_Flag;
    @JsonProperty("FED_Certify_Date")
    private String fED_Certify_Date;
    @JsonProperty("STA_Withhold_Code")
    private String sTA_Withhold_Code;
    @JsonProperty("Allctd_CVRG_Code")
    private String allctd_CVRG_Code;
    @JsonProperty("Premium_Commitment")
    private String premium_Commitment;
    @JsonProperty("Group_Proc_Date")
    private String group_Proc_Date;
    @JsonProperty("State_Tax_Person")
    private String state_Tax_Person;
    @JsonProperty("Coll_Advance_PTD")
    private String coll_Advance_PTD;
    @JsonProperty("Multiple_Insur")
    private String multiple_Insur;
    @JsonProperty("Policy_Currency")
    private String policy_Currency;
    @JsonProperty("Primary_Person")
    private String primary_Person;
    @JsonProperty("PREM_PCT_Mult")
    private String pREM_PCT_Mult;
    @JsonProperty("Paid_UP_Type")
    private String paid_UP_Type;
    @JsonProperty("Policy_PAC")
    private String policy_PAC;
    @JsonProperty("Annuity_PAC")
    private String annuity_PAC;
    @JsonProperty("Loan_Repay_PAC")
    private String loan_Repay_PAC;
    @JsonProperty("ANN_Special_Mode")
    private String aNN_Special_Mode;
    @JsonProperty("Loan_Special_Mode")
    private String loan_Special_Mode;
    @JsonProperty("City_Code")
    private String city_Code;
    @JsonProperty("UL_MPR_Date")
    private String uL_MPR_Date;
    @JsonProperty("Case_Number")
    private String case_Number;
    @JsonProperty("OFF_ANNV_PD_TO_Day")
    private String oFF_ANNV_PD_TO_Day;
    @JsonProperty("ORIG_OFF_ANNV_Day")
    private String oRIG_OFF_ANNV_Day;
    @JsonProperty("Loan_PAC_Month")
    private String loan_PAC_Month;
    @JsonProperty("Loan_PAC_Year")
    private String loan_PAC_Year;
    @JsonProperty("MEC_Reason")
    private String mEC_Reason;
    @JsonProperty("MEC_EFF_Date")
    private String mEC_EFF_Date;
    @JsonProperty("Designated_ROTH")
    private String designated_ROTH;
    @JsonProperty("Client_ID")
    private String client_ID;
    @JsonProperty("Lives_Covered")
    private String lives_Covered;
    @JsonProperty("Tax_Code_Override")
    private String tax_Code_Override;
    @JsonProperty("Claim_Status")
    private String claim_Status;
    @JsonProperty("Claim_Status_Date")
    private String claim_Status_Date;
    @JsonProperty("Claim_Settlemnt_DT")
    private String claim_Settlemnt_DT;
    @JsonProperty("SubContract_Type")
    private String subContract_Type;
    @JsonProperty("Continuatin_EFF_DT")
    private String continuatin_EFF_DT;
    @JsonProperty("POLC_UPD_Count")
    private String pOLC_UPD_Count;
    @JsonProperty("Mature_Expire_Date")
    private String mature_Expire_Date;
    @JsonProperty("Issue_Age")
    private String issue_Age;
    @JsonProperty("Plan_Code")
    private String plan_Code;
    @JsonProperty("Dividend")
    private String dividend;
    @JsonProperty("BF_Last_Val_Date")
    private String bF_Last_Val_Date;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("UD_Billing_Reason")
    private String uD_Billing_Reason;
    @JsonProperty("UD_Payment_Reason")
    private String uD_Payment_Reason;
    @JsonProperty("Cancel_Reason")
    private String cancel_Reason;
}
