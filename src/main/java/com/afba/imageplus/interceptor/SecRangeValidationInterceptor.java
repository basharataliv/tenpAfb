
package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.utilities.RangeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class SecRangeValidationInterceptor implements HandlerInterceptor {

    private final DocumentTypeService documentTypeService;
    private final RangeHelper rangeHelper;

    @Autowired
    ErrorService errorService;

    public SecRangeValidationInterceptor(DocumentTypeService documentTypeService, RangeHelper rangeHelper) {
        this.documentTypeService = documentTypeService;
        this.rangeHelper = rangeHelper;
    }

    /**
     * Intercept every call and validate user security range for document type
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        if (!request.getMethod().equals("POST")) {
            // If it is admin, let him pass with access: admin can access any documentType
            // regardless of the security range
            if ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.isAdmin))
                    && "3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep))) {
                return true;
            }
            var documentTypeId = (request.getRequestURI().split("/")[2]);
            Optional<EKD0110DocumentType> optDocumentType = documentTypeService.findById(documentTypeId);
            if (optDocumentType.isEmpty()) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD110404);
            }
            var secRange = rangeHelper.lowHighRange(Integer.parseInt((String) request.getAttribute("secRange")));
            var secRangeFrom = secRange.get(0);
            var secRangeTo = secRange.get(1);
            EKD0110DocumentType documentType = optDocumentType.get();
            // If document type security class is greater than user security range, throw
            // unauthorized
            if (!(documentType.getSecurityClass() >= secRangeFrom && documentType.getSecurityClass() <= secRangeTo)) {
                return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD110401);
            }
        }

        return true;
    }

}