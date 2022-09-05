package com.afba.imageplus.annotation;

import com.afba.imageplus.annotation.validator.CasePendingValidator;

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
@Constraint(validatedBy = CasePendingValidator.class)
@Documented
public @interface CasePendingConstraint {
    String pendReleaseDateFieldName();
    String docTypeFieldName();
    String pendForDaysFieldName();
    String returnQueueIdFieldName();

    String pendDateMessage() default "PEND RELEASE DATE should be greater than CURRENT DATE";
    String pendDateFormatMessage() default "PEND RELEASE DATE is not in valid format [YYYYMMDD]";
    String message() default  "Request is malformed!";


    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CasePendingConstraint[] value();
    }
}
