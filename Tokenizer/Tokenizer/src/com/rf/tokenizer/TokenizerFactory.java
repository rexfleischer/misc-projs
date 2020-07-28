/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class TokenizerFactory implements Serializable
{
    
    TokenRuleNode rootrule;
    
    public TokenizerFactory()
    {
        rootrule = new TokenRuleNode.TokenRuleNode_Start();
    }
    
    public void addTokenType(String definition)
            throws TokenizerSyntaxException
    {
        TokenRuleNode parsedroot = TokenRuleParser.parse(definition);
        
        rootrule.addNodeTree(parsedroot);
    }
    
    /**
     * we will parse everything up front to make sure certain things can happen.
     * for instance, if we parsed one token at a time it would become 
     * a bit convoluted when multiple tokens get returned because of 
     * look aheads. also, it wold make hasNext() difficult to make accurate
     * when some tokens can be ignored, and if the last token is 
     * ignored then we would have to perform some sort of 'peek' functionality 
     * which would mean we should have to be able to throw a exception.
     * 
     * its just better this way... just let it happen...
     * 
     * @param rootrule
     * @param parsing
     * @throws IllegalTokenException
     * @throws TokenRuleOverlapException 
     */
    public Tokenizer getTokenizer(String parsing)
            throws TokenizerParseException
    {
        return new Tokenizer(rootrule, parsing);
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        rootrule.recursiveToString(builder, (TokenRuleNode)null, 0);
        return builder.toString();
    }
}
