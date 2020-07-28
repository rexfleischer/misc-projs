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
public class Param
{
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Path    { String value(); }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Form    { String value(); }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Request { }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Response{ }
    
    public enum ReverseParam
    {
        Path,
        Form,
        Request,
        Response
    }
}
