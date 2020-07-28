/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.db.exception;

/**
 *
 * @author REx
 */
public class DatabaseException extends Exception {

    public DatabaseException(String message, Exception ex){
        super(message, ex);
    }

}
