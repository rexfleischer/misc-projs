/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.exceptions;

import cloud.language.LanguageStatements;

/**
 * 
 * @author REx
 */
public class DataTypeException extends CloudException
{

    public DataTypeException() 
    {
    }

    public DataTypeException(LanguageStatements message) 
    {
        super(message);
    }

    public DataTypeException(LanguageStatements message, Throwable cause) 
    {
        super(message, cause);
    }
    
}
