
package com.rf.fled.script.tokenizer;

/**
 *
 * @author REx
 */
public enum TokenType 
{
    HOOK        (0),
    CONSTRUCT   (0),
    
    BRACKET     (0),
    PARATHESIS  (0),
    COMMA       (0),
    SEPERATOR   (0),
    
    STRING      (0),
    BOOLEAN     (0),
    INTEGER     (0),
    DOUBLE      (0),
    NULL        (0),
    
    DECLARATION (0),
    VARIABLE    (0),
    FUNCTION    (2),
    OPERATOR    (1),
    ASSIGNMENT  (0);
    
    public final int globalPrecedence;
    
    private TokenType(int precedence)
    {
        globalPrecedence = precedence;
    }
}
