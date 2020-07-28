/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author REx
 */
public class Hook
{
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Security { public Class<? extends ClassSecurity> value(); }
    
    public interface ClassSecurity
    {
        /**
         * 
         * @param pojo this object instance that contains the method that
         * is attempting to be ran
         * @return false if it is not allowed and therefore will automatically
         * return a 403, true otherwise and the method will be called.
         */
        public boolean allowed(Object pojo);
    }
}
