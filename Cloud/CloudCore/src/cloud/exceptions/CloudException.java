/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.exceptions;

import cloud.language.LanguageStatements;
import cloud.language.Languages;

/**
 * the base exception for this app. basically, if an exception is thrown
 * from this app, it will be an extention of this class
 * @author REx
 */
public class CloudException extends Exception
{

    public CloudException(LanguageStatements message, Throwable cause) 
    {
        super(Languages.sts(message), cause);
    }

    public CloudException(LanguageStatements message) 
    {
        super(Languages.sts(message));
    }

    public CloudException() 
    {
    }
    
}
