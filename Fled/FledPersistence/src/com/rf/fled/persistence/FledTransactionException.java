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
public class FledTransactionException extends FledException
{
    public FledTransactionException(LanguageStatements statement, Throwable cause) {
        super(statement, cause);
    }

    public FledTransactionException(LanguageStatements statement) {
        super(statement);
    }

    public FledTransactionException() {
    }
}
