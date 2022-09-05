package com.afba.imageplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueueAuthorization {

    boolean findById() default true;

    boolean findAll() default true;

    boolean insert() default true;

    boolean update() default true;

    boolean delete() default true;

    boolean allowIfNull() default true;

}
