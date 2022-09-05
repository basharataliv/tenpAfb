package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.SSNDIF;
import com.afba.imageplus.repository.sqlserver.SSNDIFRepository;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.SSNDIFService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SSNDIFServiceImpl extends BaseServiceImpl<SSNDIF, Long> implements SSNDIFService {
    Logger logger = LoggerFactory.getLogger(SSNDIFServiceImpl.class);

    private final SSNDIFRepository repo;
    private final LifeProApiService lifeProApiService;
    private final EKDUserService eKDUserService;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    @Autowired
    protected SSNDIFServiceImpl(SSNDIFRepository repo, LifeProApiService lifeProApiService,
            EKDUserService eKDUserService) {
        super(repo);
        this.repo = repo;
        this.lifeProApiService = lifeProApiService;
        this.eKDUserService = eKDUserService;
    }

    @Override
    protected Long getNewId(SSNDIF entity) {
        return entity.getId();
    }

    @Override
    public void ssnDifProcessing() {
        try {
            List<SSNDIF> ssnList = repo.findByProcessFlag(false);
            ssnList.forEach(ssn -> {
                try {
                    PartySearchBaseRes partySearchResult = lifeProApiService
                            .partySearchDetails(new PartySearchBaseReq(new PartySearch("N", "S", ssn.getSsn(),
                                    UUID.randomUUID().toString(), "string", lifeProCoderId)));
                    boolean checkSize = !partySearchResult.getPartySearchResult().getPartySearchRes().isEmpty();
                    if (checkSize) {
                        boolean checkNotEmpty = !partySearchResult.getPartySearchResult().getPartySearchRes().get(0)
                                .getName_id().isBlank();
                        if (checkNotEmpty) {
                            PartyRelationshipsBaseRes partRelation = lifeProApiService
                                    .PartyRelationships(new PartyRelationshipBaseReq(new PartyRelationshipReq(
                                            Integer.parseInt(partySearchResult.getPartySearchResult()
                                                    .getPartySearchRes().get(0).getName_id()),
                                            UUID.randomUUID().toString(), "string", lifeProCoderId)));
                            List<PartyRelationshipsRes> partylist = partRelation.getGetPartyRelationshipsResult()
                                    .getPartyRelationshipsResp();
                            List<String> polices = partylist.stream()
                                    .filter(party -> party.getRelateCode().equalsIgnoreCase("IN"))
                                    .map(pol -> pol.getPolicyNumber()).collect(Collectors.toList());
                            polices.forEach(pol -> {
                                Optional<EKDUser> userOp = eKDUserService.findById(pol);
                                if (userOp.isPresent()) {
                                    EKDUser user = userOp.get();
                                    String sn = partySearchResult.getPartySearchResult().getPartySearchRes().get(0)
                                            .getSsn();
                                    String fname = partySearchResult.getPartySearchResult().getPartySearchRes().get(0)
                                            .getIndividual_first();
                                    String lname = partySearchResult.getPartySearchResult().getPartySearchRes().get(0)
                                            .getIndividual_last();
                                    String mname = partySearchResult.getPartySearchResult().getPartySearchRes().get(0)
                                            .getIndividual_middle();
                                    String indices = sn
                                            + StringUtils.rightPad(lname, ApplicationConstants.USER_LAST_NAME_SIZE)
                                            + StringUtils.rightPad(fname, ApplicationConstants.USER_FIRST_NAME_SIZE)
                                            + mname;

                                    user.setIndices(indices);
                                    eKDUserService.save(user);

                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    logger.error("Exception In SSNDIF", e);
                }
                ssn.setProcessFlag(true);
                ssn.setUpdatedAt(LocalDateTime.now());
                repo.save(ssn);

            });

        } catch (Exception e) {
            logger.error("Exception In SSNDIF", e);
        }
    }
}
