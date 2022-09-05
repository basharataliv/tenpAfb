package com.afba.imageplus.annotation.validator;

import com.afba.imageplus.annotation.CasePendingConstraint;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CasePendingValidator implements
        ConstraintValidator<CasePendingConstraint, Object> {

    private String dateFieldName;
    private String docTypeFieldName;
    private String daysFieldName;
    private String returnQueueFieldName;

    private String pendDateMessage;
    private String pendDateFormatMessage;

    @Override
    public void initialize(CasePendingConstraint annotation) {
        dateFieldName = annotation.pendReleaseDateFieldName();
        docTypeFieldName = annotation.docTypeFieldName();
        daysFieldName = annotation.pendForDaysFieldName();
        returnQueueFieldName = annotation.returnQueueIdFieldName();

        pendDateMessage = annotation.pendDateMessage();
        pendDateFormatMessage = annotation.pendDateFormatMessage();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();
        BeanWrapper pendBeanWrapper  = new BeanWrapperImpl(value);

        String date = (String) pendBeanWrapper.getPropertyValue(dateFieldName);
        String docType = (String) pendBeanWrapper.getPropertyValue(docTypeFieldName);
        Integer days = (Integer) pendBeanWrapper.getPropertyValue(daysFieldName);
        String returnQueue = (String) pendBeanWrapper.getPropertyValue(returnQueueFieldName);

        boolean docTypeIsEmpty = (docType == null || docType.isEmpty());
        boolean dateIsEmpty = (date == null || date.isEmpty() || date.equals("0"));
        boolean daysAreEmpty = (days == null);
        boolean returnQueueIsEmpty = (returnQueue == null || returnQueue.isEmpty());

        if (!docTypeIsEmpty && !dateIsEmpty && !daysAreEmpty) {
            ctx.buildConstraintViolationWithTemplate("Please provide either DOCTYPE, RELEASE DATE or PENDFORDAYS")
                    .addPropertyNode(docTypeFieldName)
                    .addConstraintViolation();
            return false;
        }

        if (!docTypeIsEmpty && !dateIsEmpty) {
            ctx.buildConstraintViolationWithTemplate("Please provide either DOCTYPE or RELEASE DATE")
                    .addPropertyNode(docTypeFieldName)
                    .addConstraintViolation();
            return false;
        }

        if (!docTypeIsEmpty && !daysAreEmpty) {
            ctx.buildConstraintViolationWithTemplate("Please provide either DOCTYPE or PENDFORDAYS")
                    .addPropertyNode(docTypeFieldName)
                    .addConstraintViolation();
            return false;
        }

        if (!dateIsEmpty && !daysAreEmpty) {
            ctx.buildConstraintViolationWithTemplate("Please provide either RELEASE DATE or PENDFORDAYS")
                    .addPropertyNode(dateFieldName)
                    .addConstraintViolation();
            return false;
        }
        // For DOCTYPE, RETURN QUEUE ID is optional
        if (returnQueueIsEmpty && (!daysAreEmpty || ! dateIsEmpty)) {
                ctx.buildConstraintViolationWithTemplate("RETURN QUEUE ID is required")
                        .addPropertyNode(returnQueueFieldName)
                        .addConstraintViolation();
                return false;
        }

        if (!validDate(date, ctx, dateIsEmpty))
            return false;

        if (!validDays(days, ctx, daysAreEmpty))
            return false;

        return true;
    }

    private boolean isDateValidAndGreaterThanCurrent(String releaseDate, ConstraintValidatorContext ctx) {
        try {
            LocalDate date = LocalDate.parse(releaseDate, DateTimeFormatter.BASIC_ISO_DATE);
            if (!date.isAfter(LocalDate.now())) {
                ctx.buildConstraintViolationWithTemplate(pendDateMessage)
                        .addPropertyNode(dateFieldName)
                        .addConstraintViolation();
                return false;
            }
        } catch (DateTimeParseException e) {
            ctx.buildConstraintViolationWithTemplate(pendDateFormatMessage)
                    .addPropertyNode(dateFieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validDate(String date, ConstraintValidatorContext ctx, boolean dateIsEmpty) {
        if (!dateIsEmpty) {
            boolean isDateValid = isDateValidAndGreaterThanCurrent(date, ctx);
            if (!isDateValid) return false;
        }
        return true;
    }

    private boolean areDaysValidAndGreaterThanCurrent(Integer days, ConstraintValidatorContext ctx) {
        LocalDate now = LocalDate.now();
        if (!now.plusDays(days).isAfter(now)) {
            ctx.buildConstraintViolationWithTemplate("PEND RELEASE DATE cannot be less than CURRENT DATE")
                    .addPropertyNode(daysFieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validDays(Integer days, ConstraintValidatorContext ctx,
                                          boolean daysAreEmpty) {
        if (!daysAreEmpty) {
            boolean areDaysValid = areDaysValidAndGreaterThanCurrent(days, ctx);
            if (!areDaysValid) return false;
        }
        return true;
    }
}
