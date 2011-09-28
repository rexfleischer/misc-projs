/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.exceptions;

import com.rf.fled.engine.DataCoreEngine;
import com.rf.fled.language.LanguageStatements;

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
        super(DataCoreEngine.get().languageInterpret(statement));
    }

    public FledException(LanguageStatements statement, Throwable cause) 
    {
        super(DataCoreEngine.get().languageInterpret(statement), cause);
    }
}
