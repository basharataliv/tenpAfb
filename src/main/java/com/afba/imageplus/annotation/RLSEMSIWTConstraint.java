package com.afba.imageplus.annotation;

import com.afba.imageplus.annotation.validator.RLSEMSIWTValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RLSEMSIWTValidator.class)
@Documented
public @interface RLSEMSIWTConstraint {

    String policyId();
    String doFsroOverride();
    String callingProgram();

    String message() default  "Request is malformed!";
    String specifyFSROOverrideWithPolicyIdMessage() default
            "Please specify FSRO override flag [true/false]";
    String requiredFieldsWithProcessMessage() default
            "POLICY ID and FSRO override flag are required";
    String specifyPolicyIdWithFSROFlagMessage() default
            "POLICY ID is required";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        RLSEMSIWTConstraint[] value();
    }
}
