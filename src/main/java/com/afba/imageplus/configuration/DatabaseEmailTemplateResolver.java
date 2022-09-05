package com.afba.imageplus.configuration;

import com.afba.imageplus.model.sqlserver.EmailTemplate;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class DatabaseEmailTemplateResolver extends StringTemplateResolver {

    final private EmailTemplateRepository emailTemplateRepository;

    public DatabaseEmailTemplateResolver(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.setResolvablePatterns(Collections.singleton("EMAIL_*"));
        this.setCacheTTLMs(5 * 60 * 1000L);
        this.setCacheable(true);
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
            String templateName, Map<String, Object> templateResolutionAttributes) {
        Optional<EmailTemplate> template = emailTemplateRepository.findByTemplateNameAndIsActive(templateName, true);
        if (!template.isPresent()) {
            return null;
        }
        return super.computeTemplateResource(configuration, ownerTemplate, template.get().getTemplateContent(),
                templateResolutionAttributes);
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(DatabaseEmailTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

}