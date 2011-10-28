/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.engine;

import com.rf.fled.language.Languages;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.exceptions.FledException;
import com.rf.fled.interfaces.Configurable;

/**
 *
 * @author REx
 */
public class FledEngine
{
    private static FledEngine instance;
    
    public static void init(String masterFile)
            throws FledException
    {
        
    }
    
    public static void close()
    {
        
    }
    
    public static FledEngine get()
    {
        return instance;
    }
    
    
    
    
    
    protected Configurable config;
    
    private Languages language;
    
    private FledEngine(Configurable config)
            throws FledException
    {
        this.config = config;
        
        // language
        // /////////////////////
        // check if there is a language option
        if (!config.configStringExists(
                FledConfigOption.LANGUAGE.name()))
        {
            // if nothing is there, then make it english and
            // put it in the config object
            language = Languages.ENGLISH;
            config.configSetString(
                    FledConfigOption.LANGUAGE.name(), 
                    Languages.ENGLISH.name());
        }
        else
        {
            // if there is a config option for language, then get the
            // string and try to convert it.
            String languageStr = config.configGetString(
                    FledConfigOption.LANGUAGE.name());
            try
            {
                language = Languages.valueOf(languageStr);
            }
            catch(Exception ex)
            {
                throw new FledException(
                        LanguageStatements.INVALID_LANGUAGE, ex);
            }
        }
    }
    
    public String configDirectory()
    {
        return config.configGetString(
                FledConfigOption.CONFIG_DIR.name());
    }
    
    public Languages getDefaultLanguage()
    {
        return language;
    }
}
