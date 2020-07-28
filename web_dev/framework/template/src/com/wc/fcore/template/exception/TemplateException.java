/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.template.exception;

/**
 *
 * @author REx
 */
public class TemplateException extends Exception {

    public TemplateException(String message, Exception ex){
        super(message, ex);
    }

}
