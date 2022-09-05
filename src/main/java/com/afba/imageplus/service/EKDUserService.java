package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.UserDetailsRes;
import com.afba.imageplus.model.sqlserver.EKDUser;

import java.util.List;

public interface EKDUserService extends BaseService<EKDUser, String> {

    List<EKDUser> getBySsn(String ssn);

    UserDetailsRes getByAccountNo(String acct);

    List<EKDUser> getByLastFirstNme(String lastFirstName);

    EKDUser insert(String policyId, Long ssn, String lastName, String firstName, String middleName, boolean imageCase);
}
