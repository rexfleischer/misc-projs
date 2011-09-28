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
public class FledDataTypeException extends FledException
{

    public FledDataTypeException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledDataTypeException(LanguageStatements statement) {
        super(statement);
    }

    public FledDataTypeException() {
    }
    
}
