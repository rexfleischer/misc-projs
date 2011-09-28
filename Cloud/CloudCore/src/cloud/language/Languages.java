/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.language;

/**
 *
 * @author REx
 */
public enum Languages 
{
    ENGLISH
    {
        @Override
        protected LanguageTranslator getInstance()
        {
            return new LanguageTranslator_English();
        }
    };
    
    // the reason i do this is so not all of the language objects
    // get made at once every time. that would create larger start 
    // ups and cause more of a memory footprint.
    protected abstract LanguageTranslator getInstance();
    
    // the singleton object.
    private LanguageTranslator instance;
    
    private Languages()
    {
        instance = null;
    }
    
    // the one really important method that causes all of the 
    // enum translator objects to be singlton
    protected LanguageTranslator get()
    {
        if (instance == null)
        {
            instance = getInstance();
        }
        return instance;
    }
    
    /**
     * 
     * @param statement
     * @return 
     */
    public static String sts(LanguageStatements statement)
    {
        return null;
    }
    
    public static String sts(Languages lang, LanguageStatements statement)
    {
        if (lang == null)
        {
            throw new NullPointerException("lang");
        }
        if (statement == null)
        {
            throw new NullPointerException("statements");
        }
        return lang.get().translate(statement);
    }
}
