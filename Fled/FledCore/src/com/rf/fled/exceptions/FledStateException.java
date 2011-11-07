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
public class FledStateException extends FledException
{
    public FledStateException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledStateException(LanguageStatements statement) {
        super(statement);
    }

    public FledStateException() {
    }
}
