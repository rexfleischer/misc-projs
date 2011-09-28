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
public class TransducerException extends CloudException
{

    public TransducerException() {
    }

    public TransducerException(LanguageStatements message) {
        super(message);
    }

    public TransducerException(LanguageStatements message, Throwable cause) {
        super(message, cause);
    }
    
}
