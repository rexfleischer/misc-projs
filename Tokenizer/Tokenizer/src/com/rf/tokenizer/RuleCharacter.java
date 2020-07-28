/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

/**
 *
 * @author REx
 */
class RuleCharacter
{
    static final char SEPARATOR = ':';
    
    static final char ESCAPE = '\\';
    
    static final char DEFINITION_START = '(';
    
    static final char DEFINITION_END = ')';
    
    
    // token attributes
    
    static final char ATTRIBUTE = '@';
    
    static final char ATTRIBUTE_KEEP = 'K';
    
    static final char ATTRIBUTE_KEEP_N = 'k';
    
    
    // token context characters
    
    static final char CONTEXT = '?';
    
    static final char CONTEXT_POSITION = '%';
    
    static final String CONTEXT_POSITION_S = new String(new char[]{CONTEXT_POSITION});
    
    static final char CONTEXT_SPLIT = '-';
    
    static final String CONTEXT_SPLIT_S = new String(new char[]{CONTEXT_SPLIT});
    
    static final char CONTEXT_NOT = '~';
    
    static final String CONTEXT_NOT_S = new String(new char[]{CONTEXT_NOT});
    
    
    // token rule characters
    
    static final char RULE_SPLIT = '|';
    
    static final char RULE_CHAR_ANY = '.';
    
    static final char RULE_GROUP_START = '(';
    
    static final char RULE_GROUP_END = ')';
    
    static final char RULE_SET_START = '[';
    
    static final char RULE_SET_END = ']';
    
    static final char RULE_SET_NOT = '^';
    
    static final char RULE_SET_RANGE = '-';
    
    static final char RULE_COUNT_RANGE_START = '{';
    
    static final char RULE_COUNT_RANGE_END = '}';
    
    static final char RULE_COUNT_RANGE_DELIMITER = ',';
    
    static final char RULE_COUNT_ZERO_OR_ONE = '?';
    
    static final char RULE_COUNT_ZERO_OR_MORE = '*';
    
    static final char RULE_COUNT_ONE_OR_MORE = '+';
    
    static final char RULE_COUNT_NOT_GREEDY = '?';
}
