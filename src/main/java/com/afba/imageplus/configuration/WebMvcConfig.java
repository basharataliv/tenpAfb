package com.afba.imageplus.configuration;

import com.afba.imageplus.interceptor.CurrentQueueIdValidationInterceptor;
import com.afba.imageplus.interceptor.DocumentTypeAccessInterceptor;
import com.afba.imageplus.interceptor.IsAdminValidationInterceptor;
import com.afba.imageplus.interceptor.IsAllowImpAValidationInterceptor;
import com.afba.imageplus.interceptor.IsAlwAdcValidationInterceptor;
import com.afba.imageplus.interceptor.IsAlwVwcValidationInterceptor;
import com.afba.imageplus.interceptor.IsAlwWkcValidationInterceptor;
import com.afba.imageplus.interceptor.IsBatchJobUserValidationInterceptor;
import com.afba.imageplus.interceptor.IsCloseFlVlalidationInterceptor;
import com.afba.imageplus.interceptor.IsCopyFlValidationInterceptor;
import com.afba.imageplus.interceptor.IsDeleteFlValidationInterceptor;
import com.afba.imageplus.interceptor.IsMoveFlValidationInterceptor;
import com.afba.imageplus.interceptor.IsRcasFlValidationInterceptor;
import com.afba.imageplus.interceptor.IsReindexFlagValidationInterceptor;
import com.afba.imageplus.interceptor.IsRidxFlValidationInterceptor;
import com.afba.imageplus.interceptor.IsRscnFlValidationInterceptor;
import com.afba.imageplus.interceptor.JwtValidationInterceptor;
import com.afba.imageplus.interceptor.MoveQueueFlValidationInterceptor;
import com.afba.imageplus.interceptor.QueueRangeValidationInterceptor;
import com.afba.imageplus.interceptor.SecRangeValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig class to define interceptors to handle authorization before
 * request reaching to controller layer
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    JwtValidationInterceptor jwtValidationInterceptor;

    @Autowired
    SecRangeValidationInterceptor secRangeValidationInterceptor;

    @Autowired
    QueueRangeValidationInterceptor queueRangeValidationInterceptor;

    @Autowired
    IsAdminValidationInterceptor isAdminValidationInterceptor;

    @Autowired
    IsReindexFlagValidationInterceptor isReindexFlagValidationInterceptor;

    @Autowired
    IsCloseFlVlalidationInterceptor isCloseFlVlalidationInterceptor;

    @Autowired
    IsDeleteFlValidationInterceptor isDeleteFlValidationInterceptor;

    @Autowired
    IsAllowImpAValidationInterceptor isAllowImpAValidationInterceptor;

    @Autowired
    IsAlwAdcValidationInterceptor isAlwAdcValidationInterceptor;

    @Autowired
    IsAlwVwcValidationInterceptor isAlwVwcValidationInterceptor;

    @Autowired
    IsAlwWkcValidationInterceptor isAlwWkcValidationInterceptor;

    @Autowired
    IsCopyFlValidationInterceptor isCopyFlValidationInterceptor;

    @Autowired
    IsMoveFlValidationInterceptor isMoveFlValidationInterceptor;

    @Autowired
    IsRcasFlValidationInterceptor isRcasFlValidationInterceptor;

    @Autowired
    IsRidxFlValidationInterceptor isRidxFlValidationInterceptor;

    @Autowired
    IsRscnFlValidationInterceptor isRscnFlValidationInterceptor;

    @Autowired
    DocumentTypeAccessInterceptor documentTypeAccessInterceptor;

    @Autowired
    MoveQueueFlValidationInterceptor moveQueueFlValidationInterceptor;

    @Autowired
    CurrentQueueIdValidationInterceptor currentQueueIdValidationInterceptor;

    @Autowired
    IsBatchJobUserValidationInterceptor isBatchJobUserValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Excluded /token and /health endpoint from jwt token validation
        registry.addInterceptor(jwtValidationInterceptor).addPathPatterns("/**").excludePathPatterns("/token/**",
                "/health", "/swagger**/**", "/v2/api-docs/**");
        registry.addInterceptor(secRangeValidationInterceptor).addPathPatterns("/document-types/**");

        registry.addInterceptor(isAdminValidationInterceptor).addPathPatterns("/users/**");

        registry.addInterceptor(queueRangeValidationInterceptor).addPathPatterns("/queues/**")
                .excludePathPatterns("/queues/users/all","/queues/count");

        registry.addInterceptor(isReindexFlagValidationInterceptor)
                .addPathPatterns("/case/document/rmvobj", "/re-index/documents/**")
                .excludePathPatterns("/re-index/documents/document-types/*");

        registry.addInterceptor(documentTypeAccessInterceptor).addPathPatterns("/index/documents/document-types/*",
                "/re-index/documents/document-types/*");

        registry.addInterceptor(isRidxFlValidationInterceptor).addPathPatterns("/index/**")
                .excludePathPatterns("/index/documents/document-types/*");

        registry.addInterceptor(isCloseFlVlalidationInterceptor).addPathPatterns("/cases/{caseId}/closeflr");

        registry.addInterceptor(isDeleteFlValidationInterceptor).addPathPatterns("/cases/**");

        registry.addInterceptor(isAllowImpAValidationInterceptor).addPathPatterns("/documents");

        registry.addInterceptor(isAlwAdcValidationInterceptor).addPathPatterns("/cases/{caseId}/comments");

        registry.addInterceptor(isAlwVwcValidationInterceptor).addPathPatterns("/cases/{caseId}/comments/{commentId}",
                "/cases/{caseId}/comments");

        registry.addInterceptor(isAlwWkcValidationInterceptor).addPathPatterns("/cases/{caseId}/comments/{commentId}");

        registry.addInterceptor(isCopyFlValidationInterceptor).addPathPatterns("/case/document");

        registry.addInterceptor(isMoveFlValidationInterceptor).addPathPatterns("/case/document/move");

        registry.addInterceptor(isRcasFlValidationInterceptor).
                addPathPatterns("/cases/{caseId}/work").
                addPathPatterns("/cases/{caseId}/release").
                addPathPatterns("/cases/{queueId}/lock");

        registry.addInterceptor(isRscnFlValidationInterceptor).addPathPatterns("/case/document/rscnfl");

        registry.addInterceptor(moveQueueFlValidationInterceptor).addPathPatterns("/cases/{caseId}/movque");

        registry.addInterceptor(currentQueueIdValidationInterceptor).addPathPatterns("/cases/{caseId}/reassign");

        registry.addInterceptor(isBatchJobUserValidationInterceptor).addPathPatterns("/cases/release/job");
        /**
         * Interceptor for each access flag column to be created and registered here
         * once AFBA confirms the required columns e.g. AdminAccessInterceptor.
         */
    }
}