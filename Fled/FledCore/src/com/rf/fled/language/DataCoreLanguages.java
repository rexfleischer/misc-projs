/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.language;

import com.rf.fled.interfaces.LanguageInterpreter;

/**
 *
 * @author REx
 */
public enum DataCoreLanguages 
{
    ENGLISH()
    {
        @Override
        public LanguageInterpreter get()
        {
            return new EnglishStatements();
        }
    };
    
    
    public abstract LanguageInterpreter get();
}
