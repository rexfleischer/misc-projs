/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.language;

/**
 *
 * @author REx
 */
public class LanguageTranslator_English implements LanguageTranslator
{

    @Override
    public String translate(LanguageStatements statement) 
    {
        String result = "";
        switch(statement)
        {
            case NONE:
            default:
                // nothing to do
                break;
        }
        return result;
    }
    
}
