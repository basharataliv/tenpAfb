package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.EmailTemplate;
import com.afba.imageplus.model.sqlserver.SharepointControl;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.repository.sqlserver.SharepointControlRepository;
import com.afba.imageplus.service.SharepointControlService;
import com.afba.imageplus.utilities.EmailUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SharepointControlServiceImpl implements SharepointControlService {

    private final Logger logger = LoggerFactory.getLogger(SharepointControlService.class);
    private final SharepointControlRepository sharepointControlRepository;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtility emailUtility;

    @Autowired
    protected SharepointControlServiceImpl(SharepointControlRepository sharepointControlRepository,
            EmailUtility emailUtility, EmailTemplateRepository emailTemplateRepository,
            SpringTemplateEngine thymeleafTemplateEngine) {
        this.sharepointControlRepository = sharepointControlRepository;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailUtility = emailUtility;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateLibraryCounterAndStatus() {
        sharepointControlRepository.updateLibraryCounterAndStatus(ApplicationConstants.MAX_DOCUMENT_PER_LIBRARY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void makeNewLibraryAvailable() {
        sharepointControlRepository.updateNewLibraryToAvailable(ApplicationConstants.MAX_DOCUMENT_PER_LIBRARY);
    }

    @Override
    public Optional<SharepointControl> findFirstByIsAvailableTrue() {
        return sharepointControlRepository.findFirstByIsAvailableTrueOrderById();

    }

    @Override
    public void checkSharepointSitesAndGenerateEmail() {

        String ccRecepients = "";
        String bccRecepients = "";

        Integer libraryCount = sharepointControlRepository
                .countByFilesCountLessThan(ApplicationConstants.MAX_DOCUMENT_PER_LIBRARY);
        // If count less then generate email based on email template table
        if (libraryCount <= ApplicationConstants.SHAREPOINT_MIN_AVAILABLE_LIBRARY_THRESHOLD) {

            Optional<EmailTemplate> emailTemplateOpt = emailTemplateRepository
                    .findByTemplateNameAndIsActive(ApplicationConstants.SHAREPOINT_CONTROL_EMAIL_TEMPLATE_NAME, true);
            if (emailTemplateOpt.isPresent()) {

                EmailTemplate emailTemplate = emailTemplateOpt.get();
                ccRecepients = emailTemplate.getEmailCC() == null ? "" : emailTemplate.getEmailCC();
                bccRecepients = emailTemplate.getEmailBcc() == null ? "" : emailTemplate.getEmailBcc();
                List<SharepointControl> records = sharepointControlRepository
                        .findByFilesCountLessThanOrderById(ApplicationConstants.MAX_DOCUMENT_PER_LIBRARY);

                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("sharepointControlRecords", records);
                Context thymeleafContext = new Context();
                thymeleafContext.setVariables(templateModel);
                String htmlBody = thymeleafTemplateEngine
                        .process(ApplicationConstants.SHAREPOINT_CONTROL_EMAIL_TEMPLATE_NAME, thymeleafContext);

                // Send Email With Highest Priority
                emailUtility.sendHtmlMessage(emailTemplate.getEmailSubject(), emailTemplate.getEmailTo(), ccRecepients,
                        bccRecepients, htmlBody, 1);
            } else {
                logger.info("Email template not found");
            }
            logger.info("Email send successfully");
        } else {
            logger.info("Available libraries are above threshhold");
        }
    }
}
