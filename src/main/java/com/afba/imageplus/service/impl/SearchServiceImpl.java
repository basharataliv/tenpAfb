package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.mapper.SearchDocumentsByCaseByPolicyResMapper;
import com.afba.imageplus.dto.mapper.SearchDocumentsByQueueResMapper;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.UserDetailsRes;
import com.afba.imageplus.dto.res.search.Case;
import com.afba.imageplus.dto.res.search.Policy;
import com.afba.imageplus.dto.res.search.SSN;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    SearchDocumentsByCaseByPolicyResMapper policyResMapper;
    CaseService caseService;
    EKDUserService eKDUserService;
    ErrorService errorService;
    SearchDocumentsByQueueResMapper queueResMapper;
    CaseQueueService caseQueueService;
    QueueService queueService;

    @Autowired
    public SearchServiceImpl(CaseService caseService, CaseQueueService caseQueueService, EKDUserService eKDUserService,
            SearchDocumentsByCaseByPolicyResMapper policyResMapper, ErrorService errorService,
            SearchDocumentsByQueueResMapper queueResMapper,QueueService queueService) {
        this.caseService = caseService;
        this.eKDUserService = eKDUserService;
        this.policyResMapper = policyResMapper;
        this.errorService = errorService;
        this.queueResMapper = queueResMapper;
        this.caseQueueService = caseQueueService;
        this.queueService=queueService;
    }

    @Override
    @Transactional
    public SSN getDocumentBySnn(String ssn, CaseOptionsReq caseOptionsReq) {
        List<EKDUser> users = eKDUserService.getBySsn(ssn);
        if (!users.isEmpty()) {
            var policies = users.stream()
                    .map(policy -> new Policy(policy.getAccountNumber(), policyResMapper
                            .convert(caseService.getCaseDocumentsByPolicy(
                                    policy.getAccountNumber()), Case.class, caseOptionsReq, users)))
                    .collect(Collectors.toList());

            try {
                String lastName = users.get(0).getIndices().substring(9, ApplicationConstants.USER_LAST_NAME_SIZE);
                String firstName = users.get(0).getIndices().substring(ApplicationConstants.USER_LAST_NAME_SIZE,
                        ApplicationConstants.USER_LAST_NAME_SIZE + ApplicationConstants.USER_FIRST_NAME_SIZE);
                String ss = users.get(0).getIndices().substring(0, 9);
                return new SSN(ss, lastName.trim(), firstName.trim(), policies);
            } catch (Exception e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR406);
            }

        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, ssn);
        }
    }

    @Override
    public SSN getDocumentByFirstLastName(String firstName, String lastName, CaseOptionsReq caseOptionsReq) {
        String name;
        if (!firstName.isEmpty()) {
            name = StringUtils.rightPad(lastName, ApplicationConstants.USER_LAST_NAME_SIZE)
                    + StringUtils.rightPad(firstName, ApplicationConstants.USER_FIRST_NAME_SIZE);
        } else {
            name = lastName;
        }

        var ekdUsers = eKDUserService.getByLastFirstNme(name);
        if (!ekdUsers.isEmpty()) {
            var policies = ekdUsers.stream()
                    .map(policy -> new Policy(policy.getAccountNumber(), policyResMapper
                            .convert(caseService.getCaseDocumentsByPolicy(
                                    policy.getAccountNumber()), Case.class, caseOptionsReq, ekdUsers)))
                    .collect(Collectors.toList());

            try {
                String lName = ekdUsers.get(0).getIndices().substring(9, ApplicationConstants.USER_LAST_NAME_SIZE);
                String fName = ekdUsers.get(0).getIndices().substring(ApplicationConstants.USER_LAST_NAME_SIZE,
                        ApplicationConstants.USER_LAST_NAME_SIZE + ApplicationConstants.USER_FIRST_NAME_SIZE);
                String ss = ekdUsers.get(0).getIndices().substring(0, 9);
                return new SSN(ss, lName.trim(), fName.trim(), policies);
            } catch (Exception e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR406);
            }
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404);
        }

    }

    @Override
    public List<SSN> getByQueueId(String queueId, CaseOptionsReq caseOptionsReq) {
        if(queueId!=null){
            var ekd0150Queue=queueService.findById(queueId);
            if(ekd0150Queue.isPresent() && (ekd0150Queue.get().getQueueType().equals("Y") &&
                    (ekd0150Queue.get().getAlternateQueueId()!=null
                    || !ekd0150Queue.get().getAlternateQueueId().equals("")))){
                queueId=ekd0150Queue.get().getAlternateQueueId();
            }
        }
        var caseDocuments = queueResMapper.convert(
                caseQueueService.getCaseDocumentsFromQueue(queueId), Case.class, caseOptionsReq);
        List<SSN> ssnList = new ArrayList<>();
        if (!caseDocuments.isEmpty()) {
            Set<String> uniquePolicy = new LinkedHashSet<>();
            for (Case cas : caseDocuments) {
                uniquePolicy.add(cas.getCmAccountNumber());
            }
            for (String policy : uniquePolicy) {
                UserDetailsRes user = eKDUserService.getByAccountNo(policy);
                List<Case> caseList = new ArrayList<>();
                for (Case cas : caseDocuments) {
                    if (cas.getCmAccountNumber().equals(policy)) {
                        caseList.add(cas);
                    }
                }

                ssnList.add(new SSN(user.getSsn(), user.getLastName(), user.getFirstName(),
                        List.of(new Policy(policy, caseList))));
            }

        }
        return ssnList;
    }

    public SSN getQueuedCaseDocumentsBySSN(String ssn, CaseOptionsReq caseOptionsReq) {
        List<EKDUser> users = eKDUserService.getBySsn(ssn);
        if (!users.isEmpty()) {
            var policies = users.stream()
                    .map(policy -> new Policy(policy.getAccountNumber(),
                            policyResMapper.convert(
                                    caseService.getQueuedCaseDocumentsByPolicy(
                                            policy.getAccountNumber()), Case.class, caseOptionsReq, users)))
                    .collect(Collectors.toList());

            try {
                String lastName = users.get(0).getIndices().substring(9, ApplicationConstants.USER_LAST_NAME_SIZE);
                String firstName = users.get(0).getIndices().substring(ApplicationConstants.USER_LAST_NAME_SIZE,
                        ApplicationConstants.USER_LAST_NAME_SIZE + ApplicationConstants.USER_FIRST_NAME_SIZE);
                String ss = users.get(0).getIndices().substring(0, 9);
                return new SSN(ss, lastName.trim(), firstName.trim(), policies);
            } catch (Exception e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR406);
            }

        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, ssn);
        }
    }
}
