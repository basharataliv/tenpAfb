package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.afba.imageplus.constants.ApplicationConstants.BATCH_JOB_USER;

@Component
public class IsBatchJobUserValidationInterceptor implements HandlerInterceptor {
    @Autowired
    ErrorService errorService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("POST") ) {
            if (BATCH_JOB_USER.compareTo(request.getAttribute(EKD0360UserProfile.Fields.repId).toString()) != 0) {
                return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
            }
        }
        return true;
    }
}
