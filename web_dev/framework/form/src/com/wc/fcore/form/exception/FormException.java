/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.form.exception;

/**
 *
 * @author REx
 */
public class FormException extends Exception {

    public FormException(String message, Exception ex){
        super(message, ex);
    }

}
