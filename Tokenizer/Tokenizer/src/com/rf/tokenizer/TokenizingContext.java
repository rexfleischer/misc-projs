/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import java.util.LinkedList;

/**
 *
 * @author REx
 */
class TokenizingContext
{
    int start;
    
    int winningSize;
    
    String winningname;
    
    String parsing;
    
    LinkedList<Token> tokens;
    
    TokenRuleNode rootrule;

    TokenizingContext(String parsing, TokenRuleNode rootrule)
    {
        this.winningSize = 0;
        this.winningname = null;
        this.parsing = parsing;
        this.tokens = new LinkedList<>();
        this.rootrule = rootrule;
    }
}
