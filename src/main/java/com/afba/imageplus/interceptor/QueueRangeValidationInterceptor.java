package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.utilities.RangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class QueueRangeValidationInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(QueueRangeValidationInterceptor.class);
    private final QueueService queueService;
    private final RangeHelper rangeHelper;

    @Autowired
    private ErrorService errorService;

    public QueueRangeValidationInterceptor(QueueService queueService, RangeHelper rangeHelper) {
        this.queueService = queueService;
        this.rangeHelper = rangeHelper;
    }

    /**
     * Intercept every call and validate user queue class for queue access
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        var urlSplit = request.getRequestURI().split("/");
        if (urlSplit.length < 3) {
            return true;
        }
        String queueId = URLDecoder.decode(urlSplit[2], StandardCharsets.UTF_8.toString());
        queueId = queueId.stripLeading();

        Optional<EKD0150Queue> optQueue = queueService.findById(queueId);
        if (optQueue.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD150404, queueId);
        }
        // If it is admin, let him pass with access: admin can access any queue
        // regardless of the queue class range
        if ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.isAdmin))
                && "3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep))) {
            return true;
        }
        EKD0150Queue queue = optQueue.get();
        // If queue class is not in range of user's queue class range, throw
        // unauthorized
        List<Integer> queueClasses = new ArrayList<>();
        Integer queueRangeFrom = -1;
        Integer queueRangeTo = -1;
        queueClasses.add(getIntegerAttribute(request, EKD0360UserProfile.Fields.repCl1));
        queueClasses.add(getIntegerAttribute(request, EKD0360UserProfile.Fields.repCl2));
        queueClasses.add(getIntegerAttribute(request, EKD0360UserProfile.Fields.repCl3));
        queueClasses.add(getIntegerAttribute(request, EKD0360UserProfile.Fields.repCl4));
        queueClasses.add(getIntegerAttribute(request, EKD0360UserProfile.Fields.repCl5));
        if (queue.getQueueClass() != null && queueClasses.contains(queue.getQueueClass())) {
            return true;
        }
        var queueClassRange = rangeHelper
                .lowHighRange((getIntegerAttribute(request, EKD0360UserProfile.Fields.repClr)));
        queueRangeFrom = queueClassRange.get(0);
        queueRangeTo = queueClassRange.get(1);
        if (queue.getQueueClass() != null && queue.getQueueClass() >= queueRangeFrom
                && queue.getQueueClass() <= queueRangeTo) {
            return true;
        }
        return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD150401);
    }

    private Integer getIntegerAttribute(HttpServletRequest request, String attributeName) {
        int value = -1;
        if (request.getAttribute(attributeName) != null) {
            value = (Integer) request.getAttribute(attributeName);
        }
        return value;
    }

}
