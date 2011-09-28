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
public class FledIOException extends FledException
{

    public FledIOException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledIOException(LanguageStatements statement) {
        super(statement);
    }

    public FledIOException() {
    }
    
}
