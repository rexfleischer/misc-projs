/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.exceptions;

import com.rf.fled.language.LanguageStatements;

/**
 *
 * @author REx
 */
public class FledLockException extends FledException
{

    public FledLockException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledLockException(LanguageStatements statement) {
        super(statement);
    }

    public FledLockException() {
    }
    
}
