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
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType
{
    String value();
}
