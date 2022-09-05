package com.afba.imageplus.utilities;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;

import java.lang.reflect.InvocationTargetException;

public class BeanUtilsWrapper extends BeanUtilsBean {

    private static final ContextClassLoaderLocal beansByClassLoader = new ContextClassLoaderLocal() {
        @Override
        protected Object initialValue() {
            return new BeanUtilsWrapper();
        }
    };

    public static BeanUtilsWrapper getInstance() {
        return (BeanUtilsWrapper)beansByClassLoader.get();
    }

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        super.copyProperty(dest, name, value);
    }
}
