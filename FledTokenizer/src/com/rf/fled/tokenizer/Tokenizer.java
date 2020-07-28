/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

import com.rf.fled.tokenizer.rule.TokenRuleNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * this class analyzes the rules that are passed in and makes decisions
 * based on the rules. the rules are expressed with strings. for instance,
 * if a rule is that that a token 'if' is a token only if it doesnt have 
 * a alphanumeric character after the characters 'i' then 'f'... then 
 * the expression for the rule would be:
 * -i-f
 * its important to note that the largest token will always when. so if there 
 * are two rules as '-i-f' and '-i-f-f', and a token is 'iff', then the 
 * resulting token will be according to the '-i-f-f' rule.
 * 
 * basically, there is a rule tag, then the characters the rule applies to.
 * here is a list of rules:
 * 1 - the '-' character means literal, so the character that is after the 
 *     '-' must be the next character in order to be the token
 * 2 - the '~' means that the character is true only if it is not the next character
 * 3 - the '*' means that there is as many of those characters in a row.
 *     this is good for catching tokens like numbers where it could be
 *     written like this: *[0-9] (for int) and *[0-9]-.*[0-9] (for double or float)
 * 
 * the characters that the rule applies too is after the rule. here is 
 * a list of types of characters:
 * 1 - if there is just a character (other than '['), then that is the 
 *     character that the rule applies to.
 * 2 - the '[]' means character sets... but the example expression above 
 *     could not have been '-[if] because that would mean that
 *     if there was an 'i' or an 'f', then a non-alphanumeric character, 
 *     both of those would match.
 * 3 - the '()' is for taking characters as groups. these do not recognize
 *     ranges or things like that. this means that the rule '-(if)' would
 *     only trigger when the characters 'i' then 'f' happen with no
 *     other rules that can be triggered deeper as well.
 * 
 * there are attributes that can tell the parser to do a variety of different
 * things with the data. attributes are denoted with '@' then the attribute
 * option. there can be multiple attributes.
 * 1 - we can tell the parser not to keep a token (for instance, whitespace
 *     tokens can be found, but doesnt have to be saved). '@k' means the
 *     parser shouldnt keep the token, and '@K' means to keep the token.
 *     the default is @K.
 * 
 * the tokenizer also allows hooks for more complicated logic. these 
 * hooks are only triggered when there is a successful match.
 * {@see com.rf.fled.tokenizer.TokenType}
 * 
 * @author REx
 */
public class Tokenizer implements Serializable
{
    protected ArrayList<String> tokennames;
    
    protected TokenRuleNode rootrule;
    
    protected boolean startedTokens;
    
    public Tokenizer()
    {
        tokennames  = new ArrayList<>();
        rootrule    = new TokenRuleNode();
        startedTokens = false;
    }
    
    /**
     * generally for testing... 
     * 
     * you better really know what you're doing if you manipulate
     * any node directly.
     * 
     * @return 
     */
    public TokenRuleNode getRootRule()
    {
        return rootrule;
    }
    
    public void addTokenType(TokenType tokentype)
            throws TokenRuleOverlapException,
                   TokenizerException
    {
        if (startedTokens)
        {
            throw new TokenizerException(
                    "cannot add token types while tokenizing a string");
        }
        if (tokennames.contains(tokentype.type))
        {
            throw new TokenRuleOverlapException(
                    String.format("cannot place the same rule twice [%s]", 
                                  tokentype));
        }
        tokennames.add(tokentype.type);
        rootrule.setChild(tokentype);
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        rootrule.recursiveToString(builder, null, 0);
        return builder.toString();
    }
    
    public List<Token> parseAsList(String expression)
             throws IllegalTokenException, TokenRuleOverlapException
    {
        return parse(expression);
    }

    public Iterator<Token> parseAsIterator(String expression)
             throws IllegalTokenException, TokenRuleOverlapException
    {
        return new ThisTokenParseIterator(parse(expression));
    }
    
    private ArrayList<Token> parse(String expression)
             throws IllegalTokenException, TokenRuleOverlapException
    {
        startedTokens = true;
        ArrayList<Token> result = new ArrayList<>();
        int position = 0;
        while(position < expression.length())
        {
            Token token = rootrule.getToken(expression, position);
            if (token == null)
            {
                throw new IllegalTokenException(
                        String.format("no token was found after character index %s", 
                                    position));
            }
            position += token.matched.length();
            if (token.type.keep)
            {
                result.add(token);
            }
            token.type.postWork(expression, position, expression, result);
        }
        return result;
    }
    
    public class ThisTokenParseIterator implements Iterator<Token>
    {
        Iterator<Token> tokens;

        private ThisTokenParseIterator(ArrayList<Token> parse)
        {
            tokens = parse.iterator();
        }

        @Override
        public boolean hasNext()
        {
            return tokens.hasNext();
        }

        @Override
        public Token next()
        {
            return tokens.next();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
