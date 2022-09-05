package com.afba.imageplus.utilities;

import static com.afba.imageplus.constants.ApplicationConstants.BATCH_JOB_USER;

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.afba.imageplus.annotation.DocumentTypeAuthorization;
import com.afba.imageplus.annotation.QueueAuthorization;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.ErrorService;

import reactor.util.annotation.Nullable;

@Component
public class AuthorizationHelper {

    private final ErrorService errorService;
    private final RangeHelper rangeHelper;
    private final AuthorizationCacheService authorizationCacheService;

    public AuthorizationHelper(ErrorService errorService, RangeHelper rangeHelper,
            AuthorizationCacheService authorizationCacheService) {
        this.errorService = errorService;
        this.rangeHelper = rangeHelper;
        this.authorizationCacheService = authorizationCacheService;
    }

    public void authorizeEntity(String operation, Object entity) {
        // Authorize queue fields
        authorizeEntityForQueue(operation, entity);
        // Authorize document type fields
        authorizeEntityForDocumentType(operation, entity);
    }

    public void authorizeEntityForQueue(String operation, Object entity) {
        if (!isAdmin() && !isRequestFromBatchJob()) {
            var queueIds = getAuthorizedQueueIds();
            Arrays.stream(entity.getClass().getDeclaredFields()).filter(field -> field
                    .isAnnotationPresent(QueueAuthorization.class)
                    && ((operation.equals("I") && field.getAnnotation(QueueAuthorization.class).insert())
                            || (operation.equals("U") && field.getAnnotation(QueueAuthorization.class).update())
                            || (operation.equals("D") && field.getAnnotation(QueueAuthorization.class).delete())
                            || (operation.equals("F") && field.getAnnotation(QueueAuthorization.class).findById())))
                    .forEach(field -> {
                        var queueId = PropertyUtil.getPropertyValue(
                                BeanUtils.getPropertyDescriptor(entity.getClass(), field.getName()), entity);
                        if (queueIds != null && !queueIds.contains(queueId)) {
                            errorService.throwException(HttpStatus.FORBIDDEN, EKDError.EKD000403, "Queue", queueId);
                        }
                    });

        }

    }

    public void authorizeEntityForDocumentType(String operation, Object entity) {
        if (!isAdmin()) {
            var documentTypeIds = getAuthorizedDocumentTypeIds();
            Arrays.stream(entity.getClass().getDeclaredFields()).filter(field -> field
                    .isAnnotationPresent(DocumentTypeAuthorization.class)
                    && ((operation.equals("I") && field.getAnnotation(DocumentTypeAuthorization.class).insert())
                            || (operation.equals("U") && field.getAnnotation(DocumentTypeAuthorization.class).update())
                            || (operation.equals("D") && field.getAnnotation(DocumentTypeAuthorization.class).delete())
                            || (operation.equals("F")
                                    && field.getAnnotation(DocumentTypeAuthorization.class).findById())))
                    .forEach(field -> {
                        var documentTypeId = PropertyUtil.getPropertyValue(
                                BeanUtils.getPropertyDescriptor(entity.getClass(), field.getName()), entity);
                        if (documentTypeIds != null && !documentTypeIds.contains(documentTypeId)) {
                            errorService.throwException(HttpStatus.FORBIDDEN, EKDError.EKD000403, "Document Type",
                                    documentTypeId);
                        }
                    });
        }

    }

    public @Nullable Set<String> getAuthorizedQueueIds() {
        var request = getRequest();
        if (request == null) {
            return null;
        }
        if (isAdmin()) {
            return null;
        }
        var repId = request.getAttribute(EKD0360UserProfile.Fields.repId);
        var repClr = request.getAttribute(EKD0360UserProfile.Fields.repClr);

        Integer repCl1 = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl1);
        Integer repCl2 = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl2);
        Integer repCl3 = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl3);
        Integer repCl4 = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl4);
        Integer repCl5 = (Integer) request.getAttribute(EKD0360UserProfile.Fields.repCl5);

        if (repClr == null) {
            return Set.of(String.valueOf(repId));
        }
        var queueClassRange = rangeHelper.lowHighRange(Integer.parseInt(repClr.toString()));
        var queueIds = authorizationCacheService.getQueues(queueClassRange.get(0), queueClassRange.get(1), repCl1,
                repCl2, repCl3, repCl4, repCl5, String.valueOf(repId));
        if (queueIds != null) {
            queueIds.add(String.valueOf(repId));
        }
        return queueIds;
    }

    public @Nullable Set<String> getAuthorizedDocumentTypeIds() {
        var request = getRequest();
        if (request == null) {
            return null;
        }
        if (isAdmin()) {
            return null;
        }
        var secRange = request.getAttribute(EKD0360UserProfile.Fields.secRange);
        if (secRange == null) {
            return null;
        }
        var securityClassRange = rangeHelper.lowHighRange(Integer.parseInt(secRange.toString()));
        return authorizationCacheService.getDocumentTypes(securityClassRange.get(0), securityClassRange.get(1));
    }

    public @Nullable HttpServletRequest getRequest() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    public String getRequestRepId() {
        var request = getRequest();
        if (request == null) {
            return null;
        }
        var repId = request.getAttribute(EKD0360UserProfile.Fields.repId);
        return String.valueOf(repId);
    }

    public boolean isAdmin() {
        var request = getRequest();
        if (request == null) {
            return false;
        }
        return ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.isAdmin))
                && "3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep)));
    }

    public boolean isRequestFromBatchJob() {
        var request = getRequest();
        if (request == null) {
            return false;
        }
        return BATCH_JOB_USER.equals(request.getAttribute(EKD0360UserProfile.Fields.repId));
    }
}
