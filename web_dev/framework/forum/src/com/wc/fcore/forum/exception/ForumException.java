/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum.exception;

/**
 *
 * @author REx
 */
public class ForumException extends Exception {

    public ForumException(String message, Exception ex){
        super(message, ex);
    }

}
