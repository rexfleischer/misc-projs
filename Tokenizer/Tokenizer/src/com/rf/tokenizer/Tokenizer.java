/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import java.util.List;

/**
 *
 * @author REx
 */
public class Tokenizer 
{
    int position;
    
    Token[] result;
    
    Tokenizer(TokenRuleNode rootrule, String parsing)
             throws TokenizerParseException
    {
        List<Token> buffer = parse(rootrule, parsing);
        result = new Token[buffer.size()];
        result = buffer.toArray(result);
        position = 0;
    }
    
    public boolean hasNext()
    {
        return position < result.length;
    }
    
    public Token next()
    {
        return result[position++];
    }
    
    private List<Token> parse(TokenRuleNode rootrule, String parsing)
             throws TokenizerParseException
    {
        int at = 0;
        TokenizingContext context = new TokenizingContext(parsing, rootrule);
        while(context.parsing.length() > at)
        {
            context.start = at;
            context.winningSize = -1;
            context.winningname = null;
            if (!context.rootrule.matches(context))
            {
                throw new TokenizerParseException(
                        String.format("no valid token at index %s", at));
            }
            Token token = new Token(
                    context.winningname, 
                    parsing.substring(context.start, context.start + context.winningSize), 
                    context.start);
            context.tokens.add(token);
        }
        return context.tokens;
    }
}
