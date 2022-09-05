package com.afba.imageplus.annotation.validator;

import com.afba.imageplus.annotation.RLSEMSIWTConstraint;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RLSEMSIWTValidator implements
        ConstraintValidator<RLSEMSIWTConstraint, Object> {

    private String callingProgFN;
    private String fsroOverrideFN;
    private String policyIdFN;

    private String specifyFSROOverrideWithPolicyIdMessage;
    private String requiredFieldsWithProcessMessage;
    private String specifyPolicyIdWithFSROFlagMessage;

    @Override
    public void initialize(RLSEMSIWTConstraint constraintAnnotation) {
        callingProgFN = constraintAnnotation.callingProgram();
        fsroOverrideFN = constraintAnnotation.doFsroOverride();
        policyIdFN = constraintAnnotation.policyId();

        specifyFSROOverrideWithPolicyIdMessage = constraintAnnotation.
                specifyFSROOverrideWithPolicyIdMessage();
        requiredFieldsWithProcessMessage = constraintAnnotation.
                requiredFieldsWithProcessMessage();
        specifyPolicyIdWithFSROFlagMessage = constraintAnnotation.
                specifyPolicyIdWithFSROFlagMessage();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();

        BeanWrapper releaseBeanWrapper  = new BeanWrapperImpl(o);

        String callingProgram = (String) releaseBeanWrapper.
                getPropertyValue(callingProgFN);

        String policyId = (String) releaseBeanWrapper.
                getPropertyValue(policyIdFN);

        Boolean doFSROOverride = (Boolean) releaseBeanWrapper.
                getPropertyValue(fsroOverrideFN);

        if (callingProgram != null &&
                callingProgram.startsWith(UnPendCallingProgram.RLSEMSIWT.name())) {

            if (doFSROOverride == null && (policyId == null || policyId.isEmpty())) {
                ctx.buildConstraintViolationWithTemplate(requiredFieldsWithProcessMessage).
                        addPropertyNode(callingProgFN).addConstraintViolation();
                return false;
            }

            if (policyId == null || policyId.isEmpty()) {
                ctx.buildConstraintViolationWithTemplate(specifyPolicyIdWithFSROFlagMessage).
                        addPropertyNode(policyIdFN).addConstraintViolation();
                return false;
            }

            if (doFSROOverride == null) {
                ctx.buildConstraintViolationWithTemplate(specifyFSROOverrideWithPolicyIdMessage).
                        addPropertyNode(fsroOverrideFN).addConstraintViolation();
                return false;
            }


        }
        return true;
    }
}
