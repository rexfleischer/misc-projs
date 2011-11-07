/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.language;

import com.rf.fled.config.FledConfigOption;
import com.rf.fled.config.FledPropertiesFactory;
import java.io.IOException;

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
    
    public static String sts(LanguageStatements statement) 
            throws IOException
    {
        String language = FledPropertiesFactory
                .getProperties()
                .getProperty(FledConfigOption.LANGUAGE.name());
        return Languages
                .valueOf(language)
                .getTranslator()
                .interpret(statement);
//        return FledEngine.get()
//                .getDefaultLanguage()
//                .instance
//                .interpret(statement);
    }
}
