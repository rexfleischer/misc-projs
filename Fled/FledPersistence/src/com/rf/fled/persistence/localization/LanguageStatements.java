
package com.rf.fled.persistence.localization;

/**
 *
 * @author REx
 */
public enum LanguageStatements 
{
    NONE,
    UNEXPECTED_EXCEPTION,
    INVALID_LANGUAGE,
    FILE_DOES_NOT_EXISTS,
    FILE_ALREADY_EXISTS,
    FILE_CANNOT_OPEN,
    FILE_CANNOT_WRITE,
    CONTEXT_DOES_NOT_EXISTS,
    CONTEXT_ALREADY_EXISTS,
    INVALID_LOCK_TYPE,
    INVALID_LOCK_KEY, 
    DIRECTORY_ALREADY_IN_USE;
    
    @Override
    public String toString()
    {
        return Languages.sts(this);
    }
}
