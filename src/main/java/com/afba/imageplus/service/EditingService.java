package com.afba.imageplus.service;

import com.afba.imageplus.dto.EditingResponse;
import com.afba.imageplus.model.sqlserver.ICRBuffer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EditingService {

    enum EditingSubject {
        MEMBER,
        SPOUSE,
        SPONSOR
    }

    EditingResponse editAgentCode(ICRBuffer icrBuffer);

    EditingResponse editPolicyType(ICRBuffer icrBuffer);

    EditingResponse editPayMode(ICRBuffer icrBuffer);

    EditingResponse editProcType(ICRBuffer icrBuffer);

    EditingResponse editAgentSignFlag(ICRBuffer icrBuffer);

    EditingResponse editAgentSignDate(ICRBuffer icrBuffer);

    EditingResponse editAgentCode2(ICRBuffer icrBuffer);

    EditingResponse editAgentLevel(ICRBuffer icrBuffer);

    EditingResponse editMedicalRejection(ICRBuffer icrBuffer, Long ssn, EditingSubject subject);

    EditingResponse editMedicalUnderwriting(ICRBuffer icrBuffer
            , Long ssn, LocalDate dateOfBirth, LocalDate signDate, Integer coverageUnit
            , EditingSubject subject);

    EditingResponse editHeightWeight(ICRBuffer icrBuffer, Integer height, Integer weight, String sex, LocalDate dateOfBirth, LocalDate signDate, EditingSubject subject);

    EditingResponse editSSN(ICRBuffer icrBuffer, Long ssn, EditingSubject subject);

    EditingResponse editPreviousPoliciesStatus(ICRBuffer icrBuffer, EditingSubject subject);

    EditingResponse editAffiliationCode(ICRBuffer icrBuffer);

    EditingResponse editPremiumPaid(ICRBuffer icrBuffer);

    EditingResponse editInsurerEligibility(ICRBuffer icrBuffer);

    EditingResponse editInsurerDutyStatus(ICRBuffer icrBuffer);

    EditingResponse editInsurerRank(ICRBuffer icrBuffer);

    EditingResponse editLastName(ICRBuffer icrBuffer, String lastName, EditingSubject subject);

    EditingResponse editFirstName(ICRBuffer icrBuffer, String firstName, EditingSubject subject);

    EditingResponse editMiddleName(ICRBuffer icrBuffer, String middleName, EditingSubject subject);

    EditingResponse editDateOfBirth(ICRBuffer icrBuffer, LocalDate dateOfBirth, LocalDate signDate, EditingSubject subject);

    EditingResponse editSex(ICRBuffer icrBuffer, String sex, EditingSubject subject);

    EditingResponse editSmokerFlag(ICRBuffer icrBuffer, String smokerFlag, EditingSubject subject);

    EditingResponse editAddressLine1(ICRBuffer icrBuffer, String addressLine1, EditingSubject subject);

    EditingResponse editAddressLine2(ICRBuffer icrBuffer, String addressLine2, EditingSubject subject);

    EditingResponse editCity(ICRBuffer icrBuffer, String city, EditingSubject subject);

    EditingResponse editState(ICRBuffer icrBuffer, String state, EditingSubject subject);

    EditingResponse editZipCode(ICRBuffer icrBuffer, Long zipCode, String city, EditingSubject subject);

    EditingResponse editEmailAddress(ICRBuffer icrBuffer, String emailAddress, EditingSubject subject);

    EditingResponse editDayPhoneSecondPhone(ICRBuffer icrBuffer, String dayPhone, String secondPhone, EditingSubject subject);

    EditingResponse editCoverageUnit(ICRBuffer icrBuffer, Integer coverageAmount, EditingSubject subject);

    EditingResponse editChildUnits(ICRBuffer icrBuffer);

    EditingResponse editEmployerTaxId(ICRBuffer icrBuffer);

    EditingResponse editPremiumAmount(ICRBuffer icrBuffer);

    EditingResponse editChildPremium(ICRBuffer icrBuffer);

    EditingResponse editTotalPremiumAmount(ICRBuffer icrBuffer);

    EditingResponse editOwnerSSN(ICRBuffer icrBuffer);

    EditingResponse editPayorSSN(ICRBuffer icrBuffer);

    EditingResponse editApplicationSpecifiedState(ICRBuffer icrBuffer);

    EditingResponse editBeneficialFirstName(ICRBuffer icrBuffer, String firstName, Long ssn, EditingSubject subject);

    EditingResponse editBeneficialLastName(ICRBuffer icrBuffer, String lastName, Long ssn, EditingSubject subject);

    EditingResponse editBeneficialSSN(ICRBuffer icrBuffer, Long ssn, EditingSubject subject);

    EditingResponse editBeneficialRelationCode(ICRBuffer icrBuffer, String relationCode, EditingSubject subject);

    EditingResponse editApplicantReplacementIndicator(ICRBuffer icrBuffer);

    EditingResponse editStatementOfHealth(ICRBuffer icrBuffer, String hq, EditingSubject subject);

    EditingResponse editChildStatementOfHealth(ICRBuffer icrBuffer);

    EditingResponse editGeneralNote(ICRBuffer icrBuffer);

    EditingResponse editApplicantSignFlag(ICRBuffer icrBuffer, Boolean signFlag, EditingSubject subject);

    EditingResponse editApplicantSignDate(ICRBuffer icrBuffer, LocalDate signDate, EditingSubject subject);

    EditingResponse editContractState(ICRBuffer icrBuffer);

    EditingResponse editParamediOrderFlag(ICRBuffer icrBuffer);

    EditingResponse editSOHInitialFlag(ICRBuffer icrBuffer);

    EditingResponse editNumberOfPages(ICRBuffer icrBuffer, String documentId);

    EditingResponse editMemberOccupationClass(ICRBuffer icrBuffer);

    Map<String, Integer> getQueuePriorities();

    String determineFinalQueue(List<EditingResponse> editingResponses, String templateName, ICRBuffer icrBuffer);

    EditingResponse editMemberEmployeeClassCode(ICRBuffer icrBuffer);

    EditingResponse editSpouseEmployeeClassCode(ICRBuffer icrBuffer);

    EditingResponse editBillDayEpayType(ICRBuffer icrBuffer);

}
