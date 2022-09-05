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

@Component
public class IsCopyFlValidationInterceptor implements HandlerInterceptor {
    @Autowired
    ErrorService errorService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("POST")) {
            if ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.copyFl))
                    || ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.isAdmin))
                            && "3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep)))) {
                return true;
            }
        }

        return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
    }
}
