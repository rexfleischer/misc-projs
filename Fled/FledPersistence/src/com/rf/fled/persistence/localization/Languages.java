/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.localization;

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
    
    public LanguageTranslator getTranslator()
    {
        if (instance == null)
        {
            instance = getInstance();
        }
        return instance;
    }
    
    public static Languages language;
    
    static
    {
        // i dont have a way to figure this out yet, but
        // i will put it in place for ease
        language = ENGLISH;
    }
    
    /**
     * @param statement
     * @return 
     */
    public static String sts(LanguageStatements statement) 
    {
        return language.getTranslator().interpret(statement);
    }
    
    /**
     * get a statement in a specific language
     * @param language
     * @param statement
     * @return 
     */
    public static String sts(Languages language, LanguageStatements statement)
    {
        return language.getTranslator().interpret(statement);
    }
}
