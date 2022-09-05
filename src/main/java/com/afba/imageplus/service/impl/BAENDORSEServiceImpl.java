package com.afba.imageplus.service.impl;

import com.afba.imageplus.dto.mapper.UserReqMapper;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.UserReq;
import com.afba.imageplus.model.sqlserver.BAEDHST;
import com.afba.imageplus.model.sqlserver.BAENDORSE;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.BAENDORSERepository;
import com.afba.imageplus.service.BAEDHSTService;
import com.afba.imageplus.service.BAENDORSEService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.EKDUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BAENDORSEServiceImpl extends BaseServiceImpl<BAENDORSE, String> implements BAENDORSEService {
    private final BAENDORSERepository baendorseRepository;
    private final EKDUserService ekdUserService;
    private final BAEDHSTService baedhstService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final UserReqMapper userReqMapper;

    @Autowired
    public BAENDORSEServiceImpl(BAENDORSERepository repository, EKDUserService ekdUserService,
                                BAEDHSTService baedhstService, CaseService caseService,
                                CaseDocumentService caseDocumentService, UserReqMapper userReqMapper) {
        super(repository);
        this.baendorseRepository = repository;
        this.ekdUserService = ekdUserService;
        this.baedhstService = baedhstService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.userReqMapper = userReqMapper;
    }

    @Override
    protected String getNewId(BAENDORSE entity) {
        return null;
    }

    public void processBAEndorse() {
        var baendorse = baendorseRepository.findAll();
        if (baendorse != null) {
            for (int j = 0; j < baendorse.size(); j++) {
                var newPolicieNo = baendorse.get(j).getLpoCaseNo();
                var oldPolicieNo = baendorse.get(j).getAfbaCaseNo();
                var ssn = baendorse.get(j).getSsn();
                var firstName = baendorse.get(j).getFName();
                var lastName = baendorse.get(j).getLName();
                var middleInitial = baendorse.get(j).getMInit();
                List<String> permanentCaseDes = List.of("01 APPLICATIONS", "02 BENEFICIARY",
                        "03 MEDICAL", "04 STATUS CHANGE", "05 CLAIMS", "06 ACCOUNTING", "07 MISCELLANEOUS");
                if (ssn == null) {
                    ssn = "";
                }
                if (lastName == null) {
                    lastName = "";
                }
                if (firstName == null) {
                    firstName = "";
                }
                if (middleInitial == null) {
                    middleInitial = "";
                }
                if (!ekdUserService.existsById(newPolicieNo)) {
                    UserReq newEkdUser = new UserReq(newPolicieNo, ssn, firstName, lastName, middleInitial, "Y");
                    ekdUserService.insert(userReqMapper.convert(newEkdUser, EKDUser.class));
                }

                BAEDHST baedhst = new BAEDHST();
                baedhst.setHAfbaCaseNo(oldPolicieNo);
                baedhst.setHLpoCaseNo(newPolicieNo);
                baedhst.setHFName(baendorse.get(j).getFName());
                baedhst.setHLName(baendorse.get(j).getLName());
                baedhst.setHMInit(baendorse.get(j).getMInit());
                baedhst.setHPolType(baendorse.get(j).getPolType());
                baedhst.setHSsn(baendorse.get(j).getSsn());
                long millis = System.currentTimeMillis();
                java.util.Date date = new java.util.Date(millis);
                baedhst.setRecordEntered(date);
                baedhstService.save(baedhst);
                baendorseRepository.deleteById(baendorse.get(j).getLpoCaseNo());

                var ekd0350cases = caseService.findByCmAccountNumber(oldPolicieNo);

                if (ekd0350cases != null) {

                    for (int i = 0; i < ekd0350cases.size(); i++) {
                        CaseCreateReq caseCreateReq = new CaseCreateReq();
                        caseCreateReq.setCmAccountNumber(newPolicieNo);
                        caseCreateReq.setCmFormattedName(ekd0350cases.get(i).getCmFormattedName());
                        caseCreateReq.setFiller(ekd0350cases.get(i).getFiller());
                        caseCreateReq.setChargeBackFlag(ekd0350cases.get(i).getChargeBackFlag());
                        caseCreateReq.setInitialRepId(ekd0350cases.get(i).getInitialRepId());
                        caseCreateReq.setScanningDateTime(ekd0350cases.get(i).getScanningDateTime());
                        caseCreateReq.setLastRepId(ekd0350cases.get(i).getLastRepId());
                        if(!permanentCaseDes.contains(ekd0350cases.get(i).getCmFormattedName())){
                           caseCreateReq.setInitialQueueId(ekd0350cases.get(i).getCurrentQueueId());
                        }
                        var ekdCase = caseService.createCase(caseCreateReq);
                        if(!permanentCaseDes.contains(ekd0350cases.get(i).getCmFormattedName())){
                            CaseUpdateReq caseUpdateReq=new CaseUpdateReq();
                            caseUpdateReq.setStatus(ekd0350cases.get(i).getStatus());
                            caseService.updateCase(caseUpdateReq,ekdCase.getCaseId());
                        }

                        var caseDocument =
                                caseDocumentService.getDocumentsByCaseId(ekd0350cases.get(i).getCaseId());
                        for (int k = 0; k < caseDocument.size(); k++) {
                            EKD0315CaseDocument ekd0315CaseDocument = new EKD0315CaseDocument();
                            ekd0315CaseDocument.setDocumentId(caseDocument.get(k).getDocumentId());
                            ekd0315CaseDocument.setCaseId(ekdCase.getCaseId());
                            ekd0315CaseDocument.setDasdFlag(caseDocument.get(k).getDasdFlag());
                            ekd0315CaseDocument.setFiller(caseDocument.get(k).getFiller());
                            ekd0315CaseDocument.setItemInit(caseDocument.get(k).getItemInit());
                            ekd0315CaseDocument.setItemType(caseDocument.get(k).getItemType());
                            ekd0315CaseDocument.setScanningDateTime(caseDocument.get(k).getDocument().getScanningDateTime());
                            caseDocumentService.insert(ekd0315CaseDocument);
                        }


                    }
                }


            }
        }

    }

}
