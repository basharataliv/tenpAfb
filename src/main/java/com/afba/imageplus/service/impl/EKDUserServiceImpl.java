package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.res.UserDetailsRes;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.EKDUserRepository;
import com.afba.imageplus.service.EKDUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EKDUserServiceImpl extends BaseServiceImpl<EKDUser, String> implements EKDUserService {

    private final EKDUserRepository ekdUserRepository;

    protected EKDUserServiceImpl(EKDUserRepository repository) {
        super(repository);
        this.ekdUserRepository = repository;
    }

    @Override
    protected String getNewId(EKDUser entity) {
        return entity.getAccountNumber();
    }

    @Override
    public List<EKDUser> getBySsn(String ssn) {
        return ekdUserRepository.findByIndicesStartsWith(ssn);
    }

    @Override
    public List<EKDUser> getByLastFirstNme(String lastFirstName) {
        Optional<List<EKDUser>> user = ekdUserRepository.findByIndicesLike("%" + lastFirstName + "%");
        if (user.isEmpty() || user.get().isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, lastFirstName);
        }
        return user.get();
    }

    @Override
    public UserDetailsRes getByAccountNo(String acct) {
        Optional<EKDUser> user = ekdUserRepository.findByAccountNumber(acct);
        if (user.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404, acct);
        }
        try {
            String lastName = user.get().getIndices().substring(9, ApplicationConstants.USER_LAST_NAME_SIZE);
            String firstName = user.get().getIndices().substring(ApplicationConstants.USER_LAST_NAME_SIZE,
                    ApplicationConstants.USER_LAST_NAME_SIZE + ApplicationConstants.USER_FIRST_NAME_SIZE);
            String ssn = user.get().getIndices().substring(0, 9);

            return new UserDetailsRes(acct, ssn, firstName.trim(), lastName.trim());
        } catch (Exception e) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR406);
        }
    }

    @Override
    public EKDUser insert(String policyId, Long ssn, String lastName, String firstName, String middleInitial, boolean imageCase) {
        return insert(EKDUser.builder()
                .accountNumber(policyId)
                .indices(String.format("%09d%30s%20s%1s%1s", ssn, lastName, firstName, middleInitial, imageCase ? "Y" : "N"))
                .build());
    }
}
