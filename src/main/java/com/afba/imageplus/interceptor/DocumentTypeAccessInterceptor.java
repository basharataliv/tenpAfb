
package com.afba.imageplus.interceptor;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.utilities.RangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Expects document-type's value at exact 4th position, in the request URL, see
 * usage in WebMvcConfig
 */
@Component
public class DocumentTypeAccessInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(DocumentTypeAccessInterceptor.class);
    @Autowired
    ErrorService errorService;

    private final DocumentTypeService documentTypeService;
    private final RangeHelper rangeHelper;

    public DocumentTypeAccessInterceptor(DocumentTypeService documentTypeService, RangeHelper rangeHelper) {
        this.documentTypeService = documentTypeService;
        this.rangeHelper = rangeHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // If it is admin, let him pass with access: admin can access any documentType
        // regardless of the security range
        if ("true".equals(request.getAttribute(EKD0360UserProfile.Fields.isAdmin))
                && "3025".equals(request.getAttribute(EKD0360UserProfile.Fields.repDep))) {
            return true;
        }

        var urlTokens = request.getRequestURI().split("/");
        if (urlTokens.length > 4 && urlTokens[4] != null) {

            String documentType = urlTokens[4];

            var documentTypeOpt = documentTypeService.findByDocumentTypeAndIsDeleted(documentType, 0);

            if (documentTypeOpt.isPresent()) {
                // If document type security class is greater than user security range, throw
                // unauthorized
                // Since database removes zeros, manually attaching them
                final String userSecurityClass = String.format("%06d",
                        Integer.parseInt(request.getAttribute(EKD0360UserProfile.Fields.secRange).toString()));
                final int documentSecurityClass = documentTypeOpt.get().getSecurityClass();

                int fromRange = 0;
                int toRange = 0;
                var securityRange = rangeHelper.lowHighRange(Integer.valueOf(userSecurityClass));
                fromRange = securityRange.get(0);
                toRange = securityRange.get(1);

                if (fromRange <= documentSecurityClass && documentSecurityClass <= toRange) {
                    return true;
                }

                return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD110401);
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD110404);
            }
        } else {
            return errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.EKD350005);
        }
    }

}