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
public interface Requirement
{
    public static class Method
    {
        @Retention(RetentionPolicy.RUNTIME)
        public @interface PUT   { }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface POST  { }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface GET   { }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface DELETE{ }
        
        public enum MethodReverse
        {
            PUT,
            POST,
            GET,
            DELETE;
        }
    }
    
    public static class Path
    {
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Variable  { String value(); }
        
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Index     { }
    }
}
