package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.Template;
import com.afba.imageplus.dto.PersonDto;
import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.LPAPPLifeProApplication;
import com.afba.imageplus.repository.sqlserver.LPAPPLifeProApplicationRepository;
import com.afba.imageplus.service.EditingService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.LifeProApplicationService;
import com.afba.imageplus.utilities.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static com.afba.imageplus.constants.Template.APPSCT1013;
import static com.afba.imageplus.constants.Template.APPSLT0116;
import static com.afba.imageplus.constants.Template.APPSLT0322;

@Service
public class LifeProApplicationServiceImpl extends BaseServiceImpl<LPAPPLifeProApplication, Long> implements LifeProApplicationService {

    private final DateTimeFormatter dateFormatter;

    private final LPAPPLifeProApplicationRepository lpappLifeProApplicationRepository;
    private final ICRFileService icrFileService;
    @Autowired
    protected LifeProApplicationServiceImpl(LPAPPLifeProApplicationRepository repository,
                                            @Lazy  ICRFileService icrFileService) {
        super(repository);
        this.lpappLifeProApplicationRepository = repository;
        this.icrFileService = icrFileService;
        dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    @Override
    protected Long getNewId(LPAPPLifeProApplication entity) {
         return entity.getId();
    }

    public LPAPPLifeProApplication insertMember(ICRFile icrFile) {
        var lpApp = new LPAPPLifeProApplication();
        var icrBuffer = icrFile.getIcrBuffer();

        lpApp.setSsn(String.valueOf(icrBuffer.getDdApps().getMemberSSN()));
        lpApp.setPolicyId(icrBuffer.getMemberPolicyId());

        lpApp.setProdcode(icrBuffer.getProductCode());
        lpApp.setPoltype(icrBuffer.getPolicyType());
        lpApp.setProctype(icrBuffer.getProcType());
        lpApp.setReqtype(icrBuffer.getMemberAutoIssue());
        lpApp.setDocpages(String.valueOf(icrBuffer.getDdApps().getNoOfPages()));
        lpApp.setLpfiller1("");
        lpApp.setEmptaxid(String.valueOf(icrBuffer.getDdApps().getEmploymentTaxId()));
        lpApp.setIoptype(getIOPType(
                icrBuffer.getDdApps().getMemberSSN(),
                icrBuffer.getDdApps().getPayorSSN(),
                icrBuffer.getDdApps().getOwnerSSN(),
                EditingService.EditingSubject.MEMBER));

        // Filling persons
        var personFillers = getPersonFillers();
        LinkedList<PersonDto> persons;
        // for child app
        if (APPSCT1013.toString().equals(icrBuffer.getDdApps().getTemplateName())) {
            persons = getPersonsForChild(icrBuffer);
        } else {
            persons = getPersonsForMember(icrBuffer);
        }
        for (var person : persons) {
            var personFiller = personFillers.removeFirst();
            personFiller.fill(lpApp, person);
        }

        lpApp.setCov1seq("1");
        lpApp.setCov1Plan("");
        lpApp.setCov1unit(String.valueOf(icrBuffer.getDdApps().getMemberCoverUnit() * 1000));
        lpApp.setCov1dob(DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getMemberDateOfBirth()));
        lpApp.setCov1sex(icrBuffer.getDdApps().getMemberSex());
        lpApp.setCov1smk("1".equals(icrBuffer.getDdApps().getMemberSmoker()) ? "S" : "N");
        lpApp.setCov1occup(icrBuffer.getMemberOccupationClass());

        lpApp.setCov2seq("0");
        lpApp.setCov2plan("");
        lpApp.setCov2unit(String.valueOf(icrBuffer.getDdApps().getChildUnit() == null ? 0 : icrBuffer.getDdApps().getChildUnit()));
        lpApp.setCov2dob("0");
        lpApp.setCov2sex("");
        lpApp.setCov2smk("");
        lpApp.setCov2occup("");


        // Filling remaining fields.
        lpApp.setApl("");
        lpApp.setNonfo("");
        lpApp.setDthbenOpt("");
        lpApp.setPlnAnnPrm("0");
        lpApp.setPayMode(icrBuffer.getPayMode());
        lpApp.setPayFreq(icrBuffer.getPayModeFrequency());
        lpApp.setNumPmtsDue("1");

        // to be updated from credit/check
        lpApp.setBillDay(icrBuffer.getBillDay());
        lpApp.setEPayType(icrBuffer.getEPayType());


        lpApp.setLstBilId(icrBuffer.getListBillId());

        // To be updated by credit/check processing later
        lpApp.setESsn("");
        lpApp.setEFName("");
        lpApp.setELName("");
        lpApp.setERoutNum("");
        lpApp.setEAcctNum("");
        lpApp.setEExpDte("");
        lpApp.setERecurFlg("");
        lpApp.setSignDate(DateHelper.localDateToProvidedFormat(dateFormatter,icrFileService.
                getSAVSIGNDTE(icrBuffer.getMemberPolicyId(),icrFile)));
        lpApp.setSignCity("");
        lpApp.setSignState(icrBuffer.getDdApps().getContractState());
        lpApp.setSignCntry("US");

        lpApp.setAgentNum(icrBuffer.getDdApps().getAgentCode());
        lpApp.setReplType("");
        lpApp.setSourceCode("");
        if(icrBuffer.getMemberAmountPaid()==null){
            icrBuffer.setMemberAmountPaid(BigDecimal.ZERO);
        }
        lpApp.setAmountPaid(String.format("%08d", icrBuffer.getMemberAmountPaid().intValue()));
        lpApp.setCshRecType("");
        lpApp.setBatchId("");
        lpApp.setNbsCode("");
        lpApp.setAflCode("0".equals(icrBuffer.getDdApps().getAffCode()) ? "" : icrBuffer.getDdApps().getAffCode());
        lpApp.setSubmitDate(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now()));
        lpApp.setProcessedFlag("N");
        lpApp.setTimeStamp("");
        lpApp.setMaritalStatus("1".equals(icrBuffer.getDdApps().getMarryInd()) ? "Y" : "N");
        lpApp.setPlaceOfBirth(icrBuffer.getDdApps().getMemberPlaceOfBirthState());
        lpApp.setCountryOfBirth(icrBuffer.getDdApps().getMemberPlaceOfBirthCountry());
        // todo: PRODID
        lpApp.setProdId(icrBuffer.getPolicyType());
        lpApp.setLogonName(authorizationHelper.getRequestRepId());
        lpApp.setTransDate(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now()));
        lpApp.setIssueDate("");
        lpApp.setDocId(icrFile.getDocumentId());
        lpApp.setSavSignDate("");
        lpApp.setClientId(icrBuffer.getAffiliateClientId());
        lpApp.setLpFiller2("");
        lpApp.setAgentId2(icrBuffer.getDdApps().getAgentCode2());
        lpApp.setField1(icrBuffer.getAgent1CommissionPercentage() == null ? "" : String.valueOf(icrBuffer.getAgent1CommissionPercentage()));
        lpApp.setField2(icrBuffer.getAgent2CommissionPercentage() == null ? "" : String.valueOf(icrBuffer.getAgent2CommissionPercentage()));
        // todo: FIELD3
        lpApp.setField3(icrBuffer.getDdApps().getDepartmentCode());
        lpApp.setPsn1empcl(icrBuffer.getMemberEmployeeClassCode());

        if (List.of(APPSLT0116, APPSLT0322).contains(Template.valueOf(icrBuffer.getDdApps().getTemplateName()))) {
            lpApp.setField4(icrBuffer.getDdApps().getMemberEligibility());
            lpApp.setField5("");
        }

        return super.insert(lpApp);
    }

    public LPAPPLifeProApplication insertSpouse(ICRFile icrFile) {
        var lpApp = new LPAPPLifeProApplication();
        var icrBuffer = icrFile.getIcrBuffer();

        lpApp.setSsn(String.valueOf(icrBuffer.getDdApps().getSpouseSSN()));
        lpApp.setPolicyId(icrBuffer.getSpousePolicyId());
        lpApp.setProdcode(icrBuffer.getProductCode());
        lpApp.setPoltype(icrBuffer.getPolicyType());
        lpApp.setProctype(icrBuffer.getProcType());
        lpApp.setReqtype(icrBuffer.getSpouseAutoIssue());
        lpApp.setDocpages(String.valueOf(icrBuffer.getDdApps().getNoOfPages()));
        lpApp.setLpfiller1("");
        lpApp.setEmptaxid(String.valueOf(icrBuffer.getDdApps().getEmploymentTaxId()));
        lpApp.setIoptype(getIOPType(
                icrBuffer.getDdApps().getSpouseSSN(),
                icrBuffer.getDdApps().getPayorSSN(),
                icrBuffer.getDdApps().getOwnerSSN(),
                EditingService.EditingSubject.SPOUSE));

        // Filling persons
        var personFillers = getPersonFillers();
        var persons = getPersonsForSpouse(icrBuffer);
        for (var person : persons) {
            var personFiller = personFillers.removeFirst();
            personFiller.fill(lpApp, person);
        }

        lpApp.setCov1seq("1");
        lpApp.setCov1Plan("");
        lpApp.setCov1unit(String.valueOf(icrBuffer.getDdApps().getSpouseCoverUnit() * 1000));
        lpApp.setCov1dob(DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getSpouseDateOfBirth()));
        lpApp.setCov1sex(icrBuffer.getDdApps().getSpouseSex());
        lpApp.setCov1smk("1".equals(icrBuffer.getDdApps().getSpouseSmoker()) ? "S" : "N");
        lpApp.setCov1occup("S");

        lpApp.setCov2seq("0");
        lpApp.setCov2plan("");
        if (StringUtils.isEmpty(icrBuffer.getMemberPolicyId())) {
            lpApp.setCov2unit(String.valueOf(icrBuffer.getDdApps().getChildUnit() == null ? 0 : icrBuffer.getDdApps().getChildUnit()));
        } else {
            lpApp.setCov2unit("0");
        }
        lpApp.setCov2dob("0");
        lpApp.setCov2sex("");
        lpApp.setCov2smk("");
        lpApp.setCov2occup("");

        // Filling remaining fields.
        lpApp.setApl("");
        lpApp.setNonfo("");
        lpApp.setDthbenOpt("");
        lpApp.setPlnAnnPrm("0");
        lpApp.setPayMode(icrBuffer.getPayMode());
        lpApp.setPayFreq(icrBuffer.getPayModeFrequency());
        lpApp.setNumPmtsDue("1");

        // Will be updated from credit/check
        lpApp.setBillDay(icrBuffer.getBillDay());
        lpApp.setEPayType(icrBuffer.getEPayType());

        lpApp.setLstBilId(icrBuffer.getListBillId());

        // Will be updated by credit/check processing later
        lpApp.setESsn("");
        lpApp.setEFName("");
        lpApp.setELName("");
        lpApp.setERoutNum("");
        lpApp.setEAcctNum("");
        lpApp.setEExpDte("");
        lpApp.setERecurFlg("");
        lpApp.setSignDate(DateHelper.localDateToProvidedFormat(dateFormatter,icrFileService.
                getSAVSIGNDTE(icrBuffer.getSpousePolicyId(),icrFile)));
        lpApp.setSignCity("");
        lpApp.setSignState(icrBuffer.getDdApps().getContractState());
        lpApp.setSignCntry("US");

        lpApp.setAgentNum(icrBuffer.getDdApps().getAgentCode());
        lpApp.setReplType("");
        lpApp.setSourceCode("");
        lpApp.setAmountPaid(String.format("%08d", icrBuffer.getSpouseAmountPaid().intValue()));
        lpApp.setCshRecType("");
        lpApp.setBatchId("");
        lpApp.setNbsCode("");
        lpApp.setAflCode("0".equals(icrBuffer.getDdApps().getAffCode()) ? "" : icrBuffer.getDdApps().getAffCode());
        lpApp.setSubmitDate(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now()));
        lpApp.setProcessedFlag("N");
        lpApp.setTimeStamp("");
        lpApp.setMaritalStatus("1".equals(icrBuffer.getDdApps().getMarryInd()) ? "Y" : "N");
        lpApp.setPlaceOfBirth("");
        lpApp.setCountryOfBirth("");
        // todo: PRODID
        lpApp.setProdId("");
        lpApp.setLogonName(authorizationHelper.getRequestRepId());
        lpApp.setTransDate(DateHelper.localDateToProvidedFormat(dateFormatter, LocalDate.now()));
        lpApp.setIssueDate("");
        lpApp.setDocId(icrFile.getDocumentId());
        lpApp.setSavSignDate("");
        lpApp.setClientId(icrBuffer.getAffiliateClientId());
        lpApp.setLpFiller2("");
        lpApp.setAgentId2(icrBuffer.getDdApps().getAgentCode2());
        lpApp.setField1(icrBuffer.getAgent1CommissionPercentage() == null ? "" : String.valueOf(icrBuffer.getAgent1CommissionPercentage()));
        lpApp.setField2(icrBuffer.getAgent2CommissionPercentage() == null ? "" : String.valueOf(icrBuffer.getAgent2CommissionPercentage()));
        // todo: FIELD3
        lpApp.setField3(icrBuffer.getDdApps().getDepartmentCode());
        lpApp.setPsn1empcl(icrBuffer.getSpouseEmployeeClassCode());

        if (List.of(APPSLT0116, APPSLT0322).contains(Template.valueOf(icrBuffer.getDdApps().getTemplateName()))) {
            lpApp.setField4(icrBuffer.getDdApps().getMemberEligibility());
            lpApp.setField5("1".equals(icrBuffer.getDdApps().getSpouseEmploymentFlag()) ? "1" : "0");
        }

        return super.insert(lpApp);
    }

    private String getIOPType(Long insuredSsn, Long payorSsn, Long ownerSsn, EditingService.EditingSubject subject) {
        if(insuredSsn==null) insuredSsn = 0L;
        if(payorSsn==null || payorSsn.equals(0L)){
            if(EditingService.EditingSubject.MEMBER.equals(subject)){
                payorSsn=insuredSsn;
            }else {
                payorSsn=0L;
            }

        }
        if(ownerSsn==null || ownerSsn.equals(0L)) {
            ownerSsn=insuredSsn;
        }
        if (insuredSsn.equals(payorSsn) &&  insuredSsn.equals(ownerSsn)) {
            return "A";
        } else if (insuredSsn.equals(ownerSsn)) {
            return "B";
        } else if (insuredSsn.equals(payorSsn)) {
            return "C";
        } else if (ownerSsn.equals(payorSsn)) {
            return "D";
        } else {
            return "E";
        }
    }

    private PersonDto getMember(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getMemberSSN()))
                .type("I")  //Insured
                .relationCode("")
                .relationSymbol("INS")
                .prefix("F".equals(icrBuffer.getDdApps().getMemberSex()) ? "MRS" : "MR")
                .firstName(icrBuffer.getDdApps().getMemberFirstName())
                .middleName(icrBuffer.getDdApps().getMemberMiddleInitial())
                .lastName(icrBuffer.getDdApps().getMemberLastName())
                .dateOfBirth(icrBuffer.getDdApps().getMemberDateOfBirth() != null
                        ? DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getMemberDateOfBirth())
                        : "")
                .sex(icrBuffer.getDdApps().getMemberSex())
                .rank(icrBuffer.getRank())
                .grade(icrBuffer.getPayGrade())
                .service(icrBuffer.getDdApps().getMemberEligibility())
                .duty(getDutyStatus(icrBuffer.getDdApps().getMemberDutyStatus()))
                .rs(getRs(icrBuffer.getDdApps().getMemberDutyStatus()))
                .heightFeet(getHightInFeet(icrBuffer.getDdApps().getMemberHeight()))
                .heightInches(getHeightInInches(icrBuffer.getDdApps().getMemberHeight()))
                .weight(String.valueOf(icrBuffer.getDdApps().getMemberWeight()))
                .addressLine1(icrBuffer.getDdApps().getMemberAddress1())
                .addressLine2(icrBuffer.getDdApps().getMemberAddress2())
                .addressCity(icrBuffer.getDdApps().getMemberCity())
                .addressState(icrBuffer.getDdApps().getMemberState())
                .addressCountry("US")
                .addressZip(String.valueOf(icrBuffer.getDdApps().getMemberZip()))
                .email(icrBuffer.getDdApps().getMemberEmail())
                .homePhone(icrBuffer.getDdApps().getMemberDPhone())
                .workPhone(icrBuffer.getDdApps().getMemberEPhone())
                .beneficiary("")
                // todo: logic to be confirmed
                .employmentClass("")
                .build();
    }

    private PersonDto getSpouse(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getSpouseSSN()))
                .type("I")  //Insured
                .relationCode("")
                .relationSymbol("INS")
                .prefix("F".equals(icrBuffer.getDdApps().getSpouseSex()) ? "MRS" : "MR")
                .firstName(icrBuffer.getDdApps().getSpouseFirstName())
                .middleName(icrBuffer.getDdApps().getSpouseMiddleInitial())
                .lastName(icrBuffer.getDdApps().getSpouseLastName())
                .dateOfBirth(icrBuffer.getDdApps().getSpouseDateOfBirth() != null
                        ? DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getSpouseDateOfBirth())
                        : "")
                .sex(icrBuffer.getDdApps().getSpouseSex())
                .rank("")
                .grade("")
                .service("")
                .duty(isSeniorOrFedProtect(icrBuffer) ? getDutyStatus(icrBuffer.getDdApps().getMemberDutyStatus()) : "")
                .rs(isSeniorOrFedProtect(icrBuffer) ? getRs(icrBuffer.getDdApps().getMemberDutyStatus()) : "")
                .heightFeet(getHightInFeet(icrBuffer.getDdApps().getSpouseHeight()))
                .heightInches(getHeightInInches(icrBuffer.getDdApps().getSpouseHeight()))
                .weight(String.valueOf(icrBuffer.getDdApps().getSpouseWeight()))
                .addressLine1(icrBuffer.getDdApps().getMemberAddress1())
                .addressLine2(icrBuffer.getDdApps().getMemberAddress2())
                .addressCity(icrBuffer.getDdApps().getMemberCity())
                .addressState(icrBuffer.getDdApps().getMemberState())
                .addressCountry("US")
                .addressZip(String.valueOf(icrBuffer.getDdApps().getMemberZip()))
                .email("")
                .homePhone("")
                .workPhone("")
                .beneficiary("")
                // todo: logic to be confirmed for spouse as well (dependent)
                .employmentClass("")
                .build();
    }

    private PersonDto getPayor(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getPayorSSN()))
                .type("P")  //Payor
                .relationCode(icrBuffer.getDdApps().getPayorRelation())
                .relationSymbol("")
                .prefix("")
                .firstName(icrBuffer.getDdApps().getPayorFirstName())
                .middleName("")
                .lastName(icrBuffer.getDdApps().getPayorLastName())
                .dateOfBirth("")
                .sex("")
                .rank("")
                .grade("")
                .service("")
                .duty("")
                .rs("")
                .heightFeet("")
                .heightInches("")
                .weight("")
                .addressLine1(icrBuffer.getDdApps().getPayorAddress1())
                .addressLine2("")
                .addressCity(icrBuffer.getDdApps().getPayorCity())
                .addressState(icrBuffer.getDdApps().getPayorState())
                .addressCountry("US")
                .addressZip(String.valueOf(icrBuffer.getDdApps().getPayorZip()))
                .email("")
                .homePhone(icrBuffer.getDdApps().getPayorPhone())
                .workPhone("")
                .beneficiary("")
                .employmentClass("")
                .build();
    }

    private PersonDto getOwner(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getOwnerSSN()))
                .type("O")  //Owner
                .relationCode(icrBuffer.getDdApps().getOwnerRelation())
                .relationSymbol("")
                .prefix("")
                .firstName(icrBuffer.getDdApps().getOwnerFirstName())
                .middleName("")
                .lastName(icrBuffer.getDdApps().getOwnerLastName())
                .dateOfBirth("")
                .sex("")
                .rank("")
                .grade("")
                .service("")
                .duty("")
                .rs("")
                .heightFeet("")
                .heightInches("")
                .weight("")
                .addressLine1(icrBuffer.getDdApps().getOwnerAddress1())
                .addressLine2("")
                .addressCity(icrBuffer.getDdApps().getOwnerCity())
                .addressState(icrBuffer.getDdApps().getOwnerState())
                .addressCountry("US")
                .addressZip(String.valueOf(icrBuffer.getDdApps().getOwnerZip()))
                .email(icrBuffer.getDdApps().getOwnerEmail())
                .homePhone(icrBuffer.getDdApps().getOwnerPhone())
                .workPhone("")
                .beneficiary("")
                .employmentClass("")
                .build();
    }

    private PersonDto getMemberBeneficiary(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getMemberBeneficiary1SSN()))
                .type("B")  //Beneficiary
                .relationCode(icrBuffer.getDdApps().getMemberBeneficiary1Relation())
                .relationSymbol("")
                .prefix("F".equals(icrBuffer.getDdApps().getMemberBeneficiary1Sex()) ? "MRS" : "MR")
                .firstName(icrBuffer.getDdApps().getMemberBeneficiary1FirstName())
                .middleName("")
                .lastName(icrBuffer.getDdApps().getMemberBeneficiary1LastName())
                .dateOfBirth(icrBuffer.getDdApps().getMemberBeneficiary1DateOfBirth() != null
                        ? DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getMemberBeneficiary1DateOfBirth())
                        : "")
                .sex(icrBuffer.getDdApps().getMemberBeneficiary1Sex())
                .rank("")
                .grade("")
                .service("")
                .duty("")
                .rs("")
                .heightFeet("")
                .heightInches("")
                .weight("")
                .addressLine1("")
                .addressLine2("")
                .addressCity("")
                .addressState("")
                .addressCountry("")
                .addressZip("")
                .email("")
                .homePhone("")
                .workPhone("")
                .beneficiary("Y")
                .employmentClass("")
                .build();
    }

    private PersonDto getSpouseBeneficiary(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getSpouseBeneficiarySSN()))
                .type("B")  //Beneficiary
                .relationCode(icrBuffer.getDdApps().getSpouseBeneficiaryRelation())
                .relationSymbol("")
                .prefix("")
                .firstName(icrBuffer.getDdApps().getSpouseBeneficiaryFirstName())
                .middleName("")
                .lastName(icrBuffer.getDdApps().getSpouseBeneficiaryLastName())
                .dateOfBirth(icrBuffer.getDdApps().getSpouseBeneficiaryDateOfBirth() != null
                        ? DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getSpouseBeneficiaryDateOfBirth())
                        : "")
                .sex("")
                .rank("")
                .grade("")
                .service("")
                .duty("")
                .rs("")
                .heightFeet("")
                .heightInches("")
                .weight("")
                .addressLine1("")
                .addressLine2("")
                .addressCity("")
                .addressState("")
                .addressCountry("")
                .addressZip("")
                .email("")
                .homePhone("")
                .workPhone("")
                .beneficiary("Y")
                .employmentClass("")
                .build();
    }

    private PersonDto getChildBeneficiary(ICRBuffer icrBuffer) {
        return PersonDto.builder()
                .ssn(String.valueOf(icrBuffer.getDdApps().getChildBeneficiarySSN()))
                .type("B")  //Beneficiary
                .relationCode(icrBuffer.getDdApps().getChildBeneficiaryRelation())
                .relationSymbol("")
                .prefix("")
                .firstName(icrBuffer.getDdApps().getChildBeneficiaryFirstName())
                .middleName("")
                .lastName(icrBuffer.getDdApps().getChildBeneficiaryLastName())
                .dateOfBirth(icrBuffer.getDdApps().getChildBeneficiaryDateOfBirth() != null
                        ? DateHelper.localDateToProvidedFormat(dateFormatter, icrBuffer.getDdApps().getChildBeneficiaryDateOfBirth())
                        : "")
                .sex("")
                .rank("")
                .grade("")
                .service("")
                .duty("")
                .rs("")
                .heightFeet("")
                .heightInches("")
                .weight("")
                .addressLine1("")
                .addressLine2("")
                .addressCity("")
                .addressState("")
                .addressCountry("")
                .addressZip("")
                .email("")
                .homePhone("")
                .workPhone("")
                .beneficiary("Y")
                .employmentClass("")
                .build();
    }

    private LinkedList<PersonDto> getPersonsForMember(ICRBuffer icrBuffer) {
        var persons = new LinkedList<PersonDto>();
        var member = getMember(icrBuffer);
        persons.add(member);
        var payor = getPayor(icrBuffer);
        if (payor.getSsn() != null && !"0".equals(payor.getSsn()) && !payor.getSsn().equals(member.getSsn())) {
            persons.add(payor);
        }
        if (!icrBuffer.isMemberBeneficiaryInvalid()) {
            var beneficiary = getMemberBeneficiary(icrBuffer);
            persons.add(beneficiary);
        }
        return persons;
    }

    private LinkedList<PersonDto> getPersonsForSpouse(ICRBuffer icrBuffer) {
        var persons = new LinkedList<PersonDto>();
        var spouse = getSpouse(icrBuffer);
        persons.add(spouse);
        var payor = getPayor(icrBuffer);
        var member = getMember(icrBuffer);
        if (payor.getSsn() != null && !"0".equals(payor.getSsn()) && !payor.getSsn().equals(member.getSsn())) {
            persons.add(payor);
        } else {
            persons.add(member.copyTo(payor));
        }
        var beneficiary = getSpouseBeneficiary(icrBuffer);
        if (!icrBuffer.isSpouseBeneficiaryInvalid()) {
            member.copyTo(beneficiary);
            persons.add(beneficiary);
        }
        return persons;
    }

    private LinkedList<PersonDto> getPersonsForChild(ICRBuffer icrBuffer) {
        var persons = new LinkedList<PersonDto>();
        var child = getMember(icrBuffer);
        persons.add(child);
        var owner = getOwner(icrBuffer);
        persons.add(owner);
        var beneficiary = getChildBeneficiary(icrBuffer);
        persons.add(beneficiary);
        return persons;
    }

    private interface PersonFiller {

        void fill(LPAPPLifeProApplication lpApp, PersonDto personDto);

    }

    private LinkedList<PersonFiller> getPersonFillers() {
        var personFillers = new LinkedList<PersonFiller>();
        personFillers.add(LifeProApplicationServiceImpl::person1Filler);
        personFillers.add(LifeProApplicationServiceImpl::person2Filler);
        personFillers.add(LifeProApplicationServiceImpl::person3Filler);
        return personFillers;
    }

    private static void person1Filler(LPAPPLifeProApplication lpApp, PersonDto person) {
        lpApp.setPsn1Ssn(person.getSsn());
        lpApp.setPsn1Type(person.getType());
        lpApp.setPsn1relcd(person.getRelationCode());
        lpApp.setPsn1relsy(person.getRelationSymbol());
        lpApp.setPsn1prefx(person.getPrefix());
        lpApp.setPsn1FirstName(person.getFirstName());
        lpApp.setPsn1MiddleName(person.getMiddleName());
        lpApp.setPsn1LastName(person.getLastName());
        lpApp.setPsn1dob(person.getDateOfBirth());
        lpApp.setPsn1sex(person.getSex());
        lpApp.setPsn1rank(person.getRank());
        lpApp.setPsn1grade(person.getGrade());
        lpApp.setPsn1srvc(person.getService());
        lpApp.setPsn1duty(person.getDuty());
        lpApp.setPsn1rs(person.getRs());
        lpApp.setPsn1hgtft(person.getHeightFeet());
        lpApp.setPsn1hgin(person.getHeightInches());
        lpApp.setPsn1wght(person.getWeight());
        lpApp.setPsn1ad1l1(person.getAddressLine1());
        lpApp.setPsn1ad1l2(person.getAddressLine2());
        lpApp.setPsn1ad1ct(person.getAddressCity());
        lpApp.setPsn1ad1st(person.getAddressState());
        lpApp.setPsn1ad1cy(person.getAddressCountry());
        lpApp.setPsn1ad1zp(person.getAddressZip());
        lpApp.setPsn1email(person.getEmail());
        lpApp.setPsn1hphon(person.getHomePhone());
        lpApp.setPsn1wphon(person.getWorkPhone());
        lpApp.setPsn1bene(person.getBeneficiary());
        lpApp.setPsn1empcl(person.getEmploymentClass());
    }

    private static void person2Filler(LPAPPLifeProApplication lpApp, PersonDto person) {
        lpApp.setPsn2ssn(person.getSsn());
        lpApp.setPsn2type(person.getType());
        lpApp.setPsn2relcd(person.getRelationCode());
        lpApp.setPsn2relsy(person.getRelationSymbol());
        lpApp.setPsn2prefx(person.getPrefix());
        lpApp.setPsn2fname(person.getFirstName());
        lpApp.setPsn2mname(person.getMiddleName());
        lpApp.setPsn2lname(person.getLastName());
        lpApp.setPsn2dob(person.getDateOfBirth());
        lpApp.setPsn2sex(person.getSex());
        lpApp.setPsn2rank(person.getRank());
        lpApp.setPsn2grade(person.getGrade());
        lpApp.setPsn2srvc(person.getService());
        lpApp.setPsn2duty(person.getDuty());
        lpApp.setPsn2rs(person.getRs());
        lpApp.setPsn2hgtft(person.getHeightFeet());
        lpApp.setPsn2hgin(person.getHeightInches());
        lpApp.setPsn2wght(person.getWeight());
        lpApp.setPsn2ad1l1(person.getAddressLine1());
        lpApp.setPsn2ad1l2(person.getAddressLine2());
        lpApp.setPsn2ad1ct(person.getAddressCity());
        lpApp.setPsn2ad1st(person.getAddressState());
        lpApp.setPsn2ad1cy(person.getAddressCountry());
        lpApp.setPsn2ad1zp(person.getAddressZip());
        lpApp.setPsn2email(person.getEmail());
        lpApp.setPsn2hphon(person.getHomePhone());
        lpApp.setPsn2wphon(person.getWorkPhone());
        lpApp.setPsn2bene(person.getBeneficiary());
        lpApp.setPsn2empcl(person.getEmploymentClass());
    }

    private static void person3Filler(LPAPPLifeProApplication lpApp, PersonDto person) {
        lpApp.setPsn3ssn(person.getSsn());
        lpApp.setPsn3type(person.getType());
        lpApp.setPsn3relcd(person.getRelationCode());
        lpApp.setPsn3relsy(person.getRelationSymbol());
        lpApp.setPsn3prefx(person.getPrefix());
        lpApp.setPsn3fname(person.getFirstName());
        lpApp.setPsn3mname(person.getMiddleName());
        lpApp.setPsn3lname(person.getLastName());
        lpApp.setPsn3dob(person.getDateOfBirth());
        lpApp.setPsn3sex(person.getSex());
        lpApp.setPsn3rank(person.getRank());
        lpApp.setPsn3grade(person.getGrade());
        lpApp.setPsn3srvc(person.getService());
        lpApp.setPsn3duty(person.getDuty());
        lpApp.setPsn3rs(person.getRs());
        lpApp.setPsn3hgtft(person.getHeightFeet());
        lpApp.setPsn3hgin(person.getHeightInches());
        lpApp.setPsn3wght(person.getWeight());
        lpApp.setPsn3ad1l1(person.getAddressLine1());
        lpApp.setPsn3ad1l2(person.getAddressLine2());
        lpApp.setPsn3ad1ct(person.getAddressCity());
        lpApp.setPsn3ad1st(person.getAddressState());
        lpApp.setPsn3ad1cy(person.getAddressCountry());
        lpApp.setPsn3ad1zp(person.getAddressZip());
        lpApp.setPsn3email(person.getEmail());
        lpApp.setPsn3hphon(person.getHomePhone());
        lpApp.setPsn3wphon(person.getWorkPhone());
        lpApp.setPsn3bene(person.getBeneficiary());
        lpApp.setPsn3empcl(person.getEmploymentClass());
    }

    private boolean isSeniorOrFedProtect(ICRBuffer icrBuffer) {
        return List.of(APPSLT0116, APPSLT0322).contains(Template.valueOf(icrBuffer.getDdApps().getTemplateName()));
    }

    private String getDutyStatus(String dutyStatus) {
        switch (dutyStatus) {
            case "S":
                return "3";
            case "I":
                return "7";
            default:
                return dutyStatus;
        }
    }

    private String getRs(String dutyStatus) {
        switch (dutyStatus) {
            case "S":
                return "19";
            case "3":
                return "11";
            default:
                return "";
        }
    }
    private String getHightInFeet(Integer height){
        if(height.toString().length()<3){
            return String.valueOf((int)Math.floor(height / 12.0));
        }else{
            return height.toString().substring(0,1);
        }
    }
    private String getHeightInInches(Integer height){
        if(height.toString().length()<3){
            return String.valueOf(height % 12);
        }else{
            return height.toString().substring(1,height.toString().length());
        }
    }
}
