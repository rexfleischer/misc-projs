/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.language;

import com.rf.fled.engine.FledEngine;

/**
 *
 * @author REx
 */
public enum Languages 
{
    ENGLISH()
    {
        @Override
        protected LanguageTranslator getInstance()
        {
            return new LanguageTranslator_English();
        }
    };
    
    protected abstract LanguageTranslator getInstance();
    
    protected LanguageTranslator instance;
    
    private Languages()
    {
        instance = getInstance();
    }
    
    public static String sts(LanguageStatements statement)
    {
        return FledEngine.get()
                .getDefaultLanguage()
                .instance
                .interpret(statement);
    }
}
