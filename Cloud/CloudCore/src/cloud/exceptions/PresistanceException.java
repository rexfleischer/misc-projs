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
public class PresistanceException extends CloudException
{

    public PresistanceException() {
    }

    public PresistanceException(LanguageStatements message) {
        super(message);
    }

    public PresistanceException(LanguageStatements message, Throwable cause) {
        super(message, cause);
    }
    
}
