package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.dto.req.UserReq;
import com.afba.imageplus.model.sqlserver.EKDUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserReqMapper extends BaseMapper<EKDUser, UserReq> {

    @Override
    public EKDUser convert(UserReq userReq, Class<EKDUser> to) {
        String indices = userReq.getSsn() +
                StringUtils.rightPad(userReq.getLastName(), ApplicationConstants.USER_LAST_NAME_SIZE) +
                StringUtils.rightPad(userReq.getFirstName(), ApplicationConstants.USER_FIRST_NAME_SIZE) +
                userReq.getMiddleInitial() +
                userReq.getHasImageCase();

        return EKDUser.builder()
                .accountNumber(userReq.getPolicyNumber())
                .indices(indices)
                .build();
    }

}
