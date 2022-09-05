package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.DtoMapper;
import com.afba.imageplus.dto.req.GetPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPoliciesPROTRMPOLReq;
import com.afba.imageplus.dto.req.PostPolicyPROTRMPOLReq;
import com.afba.imageplus.dto.res.GetPoliciesPROTRMPOLRes;
import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.model.sqlserver.id.PROTRMPOLKey;
import com.afba.imageplus.repository.sqlserver.PROTRMPOLRepository;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.PROTRMPOLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PROTRMPOLServiceImpl extends BaseServiceImpl<PROTRMPOL, PROTRMPOLKey> implements PROTRMPOLService {

    private final PROTRMPOLRepository protrmpolRepository;
    private final IndexingService indexingService;
    private final EKDUserService ekdUserService;

    protected PROTRMPOLServiceImpl(PROTRMPOLRepository protrmpolRepository, @Lazy IndexingService indexingService,
            EKDUserService ekdUserService) {
        super(protrmpolRepository);
        this.protrmpolRepository = protrmpolRepository;
        this.indexingService = indexingService;
        this.ekdUserService = ekdUserService;
    }

    @Override
    protected PROTRMPOLKey getNewId(PROTRMPOL entity) {
        return new PROTRMPOLKey(entity.getNewPolId(), entity.getExtPolId(), entity.getExtPolType());
    }

    @Override
    public List<PROTRMPOL> getbyNewPolicyId(String policyId) {

        return protrmpolRepository.findByNewPolId(policyId);
    }

    @Transactional
    public List<PROTRMPOL> removebyNewPolicyId(String policyId) {

        return protrmpolRepository.deleteByNewPolId(policyId);
    }

    public List<GetPoliciesPROTRMPOLRes> fetchPoliciesForProposedTermination(GetPoliciesPROTRMPOLReq request) {

        String ssn = request.getSsn() != null ? request.getSsn()
                : ekdUserService.getByAccountNo(request.getPolicyId()).getSsn();

        List<PartyRelationshipsRes> partyRelationshipsRes = indexingService.getPoliciesForCoverageAmount(ssn);
        if (partyRelationshipsRes != null && !partyRelationshipsRes.isEmpty()) {

            DtoMapper<PartyRelationshipsRes, GetPoliciesPROTRMPOLRes> dtoMapper = new DtoMapper<>();
            List<GetPoliciesPROTRMPOLRes> policies = partyRelationshipsRes.stream()
                    .map(e -> dtoMapper.convert(e, GetPoliciesPROTRMPOLRes.class)).collect(Collectors.toList());

            // Set isTerminated as false or true based on record in PROTRMPOL table
            policies = policies.stream().map(e -> {
                e.setIsTerminated(!protrmpolRepository.findByExtPolId(e.getPolicyNumber()).isEmpty());
                return e;
            }).collect(Collectors.toList());

            return policies;
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.PRTMPL001, ssn);
        }
    }

    @Transactional
    public String postPoliciesForProposedTermination(PostPoliciesPROTRMPOLReq request) {

        BaseMapper<PROTRMPOL, PostPolicyPROTRMPOLReq> entityMapper = new BaseMapper<>();

        for (PostPolicyPROTRMPOLReq requestPolicy : request.getPolicies()) {

            // Verify is passed EXTPOLID is valid
            ekdUserService.getByAccountNo(requestPolicy.getExtPolId());

            PROTRMPOL mappedProtrmpol = entityMapper.convert(requestPolicy, getEntityClass());
            Optional<PROTRMPOL> dbProtrmpolOpt = protrmpolRepository.findById(new PROTRMPOLKey(
                    mappedProtrmpol.getNewPolId(), mappedProtrmpol.getExtPolId(), mappedProtrmpol.getExtPolType()));
            if (dbProtrmpolOpt.isPresent()) {
                PROTRMPOL dbProtrmpol = dbProtrmpolOpt.get();

                // Remove if record exists and termination flag is false
                if (!requestPolicy.getIsTerminated()) {
                    protrmpolRepository.delete(dbProtrmpol);
                }

            } else {
                // Add if record not exists and termination flag is true
                if (requestPolicy.getIsTerminated()) {
                    protrmpolRepository.save(mappedProtrmpol);
                }
            }
        }
        return "Proposed terminations recorded successfully";
    }
}
