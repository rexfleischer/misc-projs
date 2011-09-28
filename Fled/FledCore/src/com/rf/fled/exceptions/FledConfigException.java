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
public class FledConfigException extends FledException
{
    public FledConfigException() 
    {
        
    }
    
    public FledConfigException(LanguageStatements statement) 
    {
        super(statement);
    }

    public FledConfigException(LanguageStatements statement, Throwable cause) 
    {
        super(statement, cause);
    }
}
