package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class CurrentQueueIdValidationInterceptor implements HandlerInterceptor {
    private final CaseService caseService;
    private final QueueService queueService;
    Logger logger = LoggerFactory.getLogger(CurrentQueueIdValidationInterceptor.class);
    @Autowired
    ErrorService errorService;

    public CurrentQueueIdValidationInterceptor(CaseService caseService, QueueService queueService) {
        this.caseService = caseService;
        this.queueService = queueService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        var urlTokens = request.getRequestURI().split("/");
        String caseId = urlTokens[2];

        var ekd0350Case = caseService.findById(caseId);

        if (ekd0350Case.isPresent()) {
            if (ekd0350Case.get().getCurrentQueueId() != null && !ekd0350Case.get().getCurrentQueueId().equals("")) {
                String repDep = (String) request.getAttribute(EKD0360UserProfile.Fields.repDep);
                Integer repClass = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl3);
                var queue = queueService.findById(ekd0350Case.get().getCurrentQueueId());
                if (queue.isPresent()) {
                    if ("3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep))) {
                        return true;
                    }
                    if (repDep == null) {
                        repDep = "0";
                    }
                    if (!repDep.equals(queue.get().getADepartmentId())) {
                        var canAccessBothDepart = Arrays.asList("1100", "6000");
                        if (!canAccessBothDepart.contains(repDep)
                                || !canAccessBothDepart.contains(queue.get().getADepartmentId())) {
                            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350456);
                        }
                    }
                    if (!repClass.equals(queue.get().getQueueClass())) {
                        return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350456);
                    }
                } else {
                    return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350460);
                }

            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350455);
            }
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD350404, caseId);
        }

        return true;
    }

}
