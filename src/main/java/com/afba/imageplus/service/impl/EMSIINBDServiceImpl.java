package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.EMSIINBND;
import com.afba.imageplus.model.sqlserver.EmailTemplate;
import com.afba.imageplus.repository.sqlserver.EMSIINBDRepository;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.service.EMSIINBDService;
import com.afba.imageplus.utilities.EmailUtility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EMSIINBDServiceImpl extends BaseServiceImpl<EMSIINBND, Integer> implements EMSIINBDService {

    private final Logger logger = LoggerFactory.getLogger(EMSIINBDServiceImpl.class);
    private final EMSIINBDRepository emsiInbdRepository;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtility emailUtility;

    protected EMSIINBDServiceImpl(EMSIINBDRepository emsiInbdRepository, SpringTemplateEngine thymeleafTemplateEngine,
            EmailTemplateRepository emailTemplateRepository, EmailUtility emailUtility) {
        super(emsiInbdRepository);
        this.emsiInbdRepository = emsiInbdRepository;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailUtility = emailUtility;
    }

    protected Integer getNewId(EMSIINBND entity) {
        return entity.getId();
    }

    public List<String> getDistinctPoliciesByProcessFlagFalse() {

        return emsiInbdRepository.findDistinctPoliciesByProcessFlagFalse();
    }

    public List<EMSIINBND> getByPolicyId(String policyId) {
        return emsiInbdRepository.findByEiPolIdAndEiProcessFlgFalse(policyId);
    }

    /**
     * @param records
     * @return returns true if validation is successful. False if validation fails.
     *         Email is sent to relevant stake holders in that case
     */
    public Boolean performValidationForEmsiInbndRecords(EMSIINBND emsiInbnd) {

        if (emsiInbnd.getEiPageNo() == null || emsiInbnd.getEiPageNo() < 1 || emsiInbnd.getEiPageNo() > 999
                || StringUtils.isBlank(emsiInbnd.getEiFileExt()) || !emsiInbnd.getEiFileExt()
                        .matches("^(APS|ATH|LAB|MVR|LTR|CLS|UW2|PT2|UWD|MSC|MB1|MB2|CSN|HIP|CHG|EKG|TIK)$")) {

            Optional<EmailTemplate> emailTemplateOpt = emailTemplateRepository.findByTemplateNameAndIsActive(
                    ApplicationConstants.EMSIINBND_VALIDATION_FAILURE_EMAIL_TEMPLATE_NAME, true);
            if (emailTemplateOpt.isPresent()) {

                EmailTemplate emailTemplate = emailTemplateOpt.get();
                String ccRecepients = emailTemplate.getEmailCC() == null ? "" : emailTemplate.getEmailCC();
                String bccRecepients = emailTemplate.getEmailBcc() == null ? "" : emailTemplate.getEmailBcc();

                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("policyId", emsiInbnd.getEiPolId());
                Context thymeleafContext = new Context();
                thymeleafContext.setVariables(templateModel);
                String htmlBody = thymeleafTemplateEngine.process(
                        ApplicationConstants.EMSIINBND_VALIDATION_FAILURE_EMAIL_TEMPLATE_NAME, thymeleafContext);

                // Send Email With Highest Priority
                emailUtility.sendHtmlMessage(emailTemplate.getEmailSubject(), emailTemplate.getEmailTo(), ccRecepients,
                        bccRecepients, htmlBody, 1);
                return false;
            } else {
                logger.error("Email template {} not found ",
                        ApplicationConstants.EMSIINBND_VALIDATION_FAILURE_EMAIL_TEMPLATE_NAME);
            }
        }
        return true;
    }
}
