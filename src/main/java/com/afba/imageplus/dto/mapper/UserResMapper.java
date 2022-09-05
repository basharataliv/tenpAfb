package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.dto.res.UserRes;
import com.afba.imageplus.model.sqlserver.EKDUser;
import org.springframework.stereotype.Component;

@Component
public class UserResMapper extends BaseMapper<EKDUser, UserRes> { }
