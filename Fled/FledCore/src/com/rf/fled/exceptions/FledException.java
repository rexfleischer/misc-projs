/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.exceptions;

import com.rf.fled.language.LanguageStatements;
import com.rf.fled.language.Languages;

/**
 *
 * @author REx
 */
public class FledException extends Exception
{
    public FledException() 
    {
        super();
    }
    
    public FledException(LanguageStatements statement) 
    {
        super(Languages.sts(statement));
    }

    public FledException(LanguageStatements statement, Throwable cause) 
    {
        super(Languages.sts(statement), cause);
    }
}
