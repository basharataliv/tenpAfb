package com.afba.imageplus.utilities;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import org.springframework.http.HttpStatus;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class PropertyUtil {

    public static void setPropertyValue(PropertyDescriptor propertyDescriptor, Object object, Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(object, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DomainException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    EKDError.EKD000500.code(),
                    String.format("Failed to set value '%s' for property %s of class %s.", value, propertyDescriptor.getName(), object.getClass())
            );
        }
    }
    public static Object getPropertyValue(PropertyDescriptor propertyDescriptor, Object object) {
        try {
            return propertyDescriptor.getReadMethod().invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DomainException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    EKDError.EKD000500.code(),
                    String.format("Failed to get value for property %s of class %s.", propertyDescriptor.getName(), object.getClass())
            );
        }
    }

}
