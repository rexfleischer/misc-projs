/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.exceptions.FledException;
import com.rf.fled.language.LanguageStatements;

/**
 *
 * @author REx
 */
public class FledPresistanceException extends FledException
{

    public FledPresistanceException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledPresistanceException(LanguageStatements statement) {
        super(statement);
    }

    public FledPresistanceException() {
    }
    
}
