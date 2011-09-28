/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.engine;

import com.rf.fled.language.DataCoreLanguages;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.exceptions.FledException;
import com.rf.fled.interfaces.Configurable;
import com.rf.fled.interfaces.LanguageInterpreter;

/**
 *
 * @author REx
 */
public class DataCoreEngine
{
    private static DataCoreEngine instance;
    
    public static void init(String masterFile)
            throws FledException
    {
        
    }
    
    public static void close()
    {
        
    }
    
    public static DataCoreEngine get()
    {
        return instance;
    }
    
    
    
    
    
    protected Configurable config;
    
    private LanguageInterpreter language;
    
    private DataCoreEngine(Configurable config)
            throws FledException
    {
        this.config = config;
        
        // language
        // /////////////////////
        // check if there is a language option
        if (!config.configStringExists(
                DataCoreConfigOption.LANGUAGE.name()))
        {
            // if nothing is there, then make it english and
            // put it in the config object
            language = DataCoreLanguages.ENGLISH.get();
            config.configSetString(
                    DataCoreConfigOption.LANGUAGE.name(), 
                    DataCoreLanguages.ENGLISH.name());
        }
        else
        {
            // if there is a config option for language, then get the
            // string and try to convert it.
            String languageStr = config.configGetString(
                    DataCoreConfigOption.LANGUAGE.name());
            try
            {
                language = DataCoreLanguages.valueOf(languageStr).get();
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
                DataCoreConfigOption.CONFIG_DIR.name());
    }
    
    public String languageInterpret(LanguageStatements statement)
    {
        return language.interpret(statement);
    }
}
