
package com.rf.bytescript.tokenizer;

import com.rf.bytescript.environment.IFunction;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.OrderedNamedValueArray;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * this tokenizer attempts to break things up into the smallest meaningful 
 * string segment. to make this work for general cases, we have to define
 * the fundamentals of what this tokenizer assumes about the language it
 * is tokenizing for. 
 * 
 * first, we are assuming it is some sort of sql, functional, or something
 * that uses relational algebra. 
 * 
 * there are a few things that always initiate a new token, and therefore
 * making itself a single token as well.. '(', ')', ','. this is helpful when
 * defining functions as well, because we can state that if we have a string,
 * starting with a alphabetic character and is followed by a '(' that is
 * is a function, and everything up to the appropriate ')' will be function
 * parameters (separated by commas). the last one of these is ';', which 
 * splits statements (if not in a string).
 * 
 * there are 5 base datatypes as well...
 * 1 - string: an array of characters wrapped with ' or ".
 * 2 - boolean: a string that is either true or false
 * 4 - number: 0-9 and the decimal point. 
 * 5 - null: a string token string that denotes null
 * 
 * there is also the operators. operators are automatically assumed to be
 * strings of any of these: %^&|?*=+-/<>!
 * but while tokenizing, the programmer can add specific strings that will
 * be seen as any of the avaliable ValueType enums.
 * 
 * there can also be a variable. this is a token that starts with a alphabetic 
 * character, but doesnt not meet any of the above conditions. 
 * 
 * 
 * @author REx
 */
public class Tokenizer implements Serializable
{
    /**
     * holds programmer specified tokens. if a token 
     */
    private OrderedNamedValueArray<IFunction> specific;
    
    public Tokenizer(OrderedNamedValueArray<IFunction> specific)
    {
        this.specific = specific;
    }
    
    public Tokenizer()
    {
        this(null);
    }
    
    public ArrayList<Value> tokenize(String expression) 
            throws ByteScriptSyntaxException
    {
        expression = expression.toLowerCase();
        ArrayList<Value> result = new ArrayList<Value>();
        
        /**
         * for finding strings
         */
        boolean onQuote = false;
        char matchingQuote = 0x00;
        
        /**
         * for brackets
         */
        int constructDepth = 0;
        
        /**
         * marks where 'this' token starts
         */
        int mark = 0; // start at beginning
        
        /**
         * tells the program whether it has started a token or not
         */
        boolean dirty = false;
        
        /**
         * the current type of token that it is on (not always used)
         */
        ValueType curr = null;
        
        for(int i = 0; i < expression.length(); i++)
        {
            char character = expression.charAt(i);
            if (onQuote)
            {
                if (character == matchingQuote)
                {
                    flush(result, ValueType.STRING, expression, mark, i);
                    dirty = false;
                    mark = i + 1;
                    onQuote = false;
                    curr = null;
                }
            }
            else if (isFlusher(character))
            {
                if (dirty)
                {
                    flush(result, curr, expression, mark, i);
                    dirty = false;
                }
                mark = i;
                onQuote = true;
                matchingQuote = character;
            }
            else if (isWhitespace(character))
            {
                if (dirty)
                {
                    flush(result, curr, expression, mark, i);
                    dirty = false;
                }
                mark = i + 1;
                curr = null;
            }
            else if (isDigit(character))
            {
                /**
                 * we check for variable too because variable names
                 * can have numbers in them
                 */
                if (curr != ValueType.INTEGER && 
                    curr != ValueType.DOUBLE &&
                    curr != ValueType.VARIABLE)
                {
                    /**
                     * check to see if it is just a negative number
                     */
                    ValueType last = result.size() > 0 ?
                            result.get(result.size() - 1).type : null;
                    
                    if (curr == ValueType.OPERATOR &&
                       ( last == ValueType.OPERATOR ||
                         last == ValueType.SEPERATOR ||
                         last == ValueType.COMMA ||
                         last == null) &&
                        expression.charAt(mark) == '-')
                    {
                        /**
                         * negative numbers take precedence over operators
                         */
                        curr = ValueType.INTEGER;
                    }
                    else
                    {
                        if (dirty)
                        {
                            flush(result, curr, expression, mark, i);
                        }
                        dirty = true; // keep it dirty
                        curr = ValueType.INTEGER;
                        mark = i;
                    }
                }
            }
            else if (isAlphabetic(character))
            {
                if (curr == ValueType.INTEGER)
                {
                    throw new ByteScriptSyntaxException(
                            "cannot start variable names with a number");
                }
                if (curr != ValueType.VARIABLE)
                {
                    if (dirty)
                    {
                        flush(result, curr, expression, mark, i);
                    }
                    dirty = true; // keep it dirty
                    mark = i;
                    curr = ValueType.VARIABLE;
                }
            }
            else if (isOperator(character))
            {
                if (curr != ValueType.OPERATOR)
                {
                    if (dirty)
                    {
                        flush(result, curr, expression, mark, i);
                    }
                    dirty = true; // keep it dirty
                    mark = i;
                    curr = ValueType.OPERATOR;
                }
                else
                {
                    /**
                     * if we get here, then that means we are on the second
                     * operator in a row. but we want to check a few things.
                     * the only operators that can have two operator characters
                     * end with '=', '&', or '|'.
                     */
                    if (character == '=' || character == '&' || character == '|')
                    {
                        /**
                         * in here means we will flush with both characters
                         */
                        flush(result, curr, expression, mark, mark + 2); // get 2 characters
                        dirty = false;
                        mark = i + 1;
                        curr = null;
                    }
                    else
                    {
                        /**
                         * in here means we flush the first character, and then
                         * move the mark up keeping it dirty and the curr
                         * ValueType state an OPERATOR
                         */
                        flush(result, curr, expression, mark, mark + 1);
                        mark = i;
                    }
                }
            }
            else if (isParathesis(character))
            {
                if (dirty)
                {
                    if (character == '(')
                    {
                        if (curr == ValueType.VARIABLE)
                        {
                            curr = ValueType.FUNCTION;
                        }
                    }
                    flush(result, curr, expression, mark, i);
                }
                else if (!result.isEmpty() &&
                        result.get(result.size() - 1).type == ValueType.VARIABLE)
                {
                    Value token = result.get(result.size() - 1);
                    IFunction function = (specific != null) ?
                        specific.getVariableSafe(token.getValueAsString()) : null;

                    if (function != null)
                    {
                        token.type = function.getType();
                    }
                    else
                    {
                        token.type = ValueType.FUNCTION;
                    }
                }
                flush(result, ValueType.PARATHESIS, expression, i, i);
                dirty = false;
                mark = i + 1;
                curr = null;
            }
            else if (isBracket(character))
            {
                if (character == '{')
                {
                    constructDepth++;
                }
                else
                {
                    constructDepth--;
                }
                if (dirty)
                {
                    flush(result, curr, expression, mark, i);
                }
                flush(result, ValueType.BRACKET, expression, i, i);
                dirty = false;
                mark = i + 1;
                curr = null;
            }
            else if (isComma(character))
            {
                if (dirty)
                {
                    flush(result, curr, expression, mark, i);
                }
                flush(result, ValueType.COMMA, expression, i, i);
                dirty = false;
                mark = i + 1;
                curr = null;
            }
            else if (isSeperator(character))
            {
                if (dirty)
                {
                    flush(result, curr, expression, mark, i);
                }
                flush(result, ValueType.SEPERATOR, expression, i, i);
                dirty = false;
                mark = i + 1;
                curr = null;
            }
            else
            {
                throw new ByteScriptSyntaxException("illegal character");
            }
        }
        
        if (dirty)
        {
            flush(result, curr, expression, mark, expression.length());
        }
        
        if (constructDepth != 0)
        {
            throw new ByteScriptSyntaxException("bracket mismatch");
        }
        
        return result;
    }
    
    private void flush(
            ArrayList<Value> result, 
            ValueType type, 
            String expression, 
            int start, 
            int end)
    {
        switch(type)
        {
            /**
             * these all flush the character at start
             */
            case PARATHESIS:
            case BRACKET:
            case COMMA:
            case SEPERATOR:
            {
                String character = expression.substring(start, start + 1);
                result.add(new Value(type, character));
                break;
            }
                
            /**
             * takes out the quotes, and does not trim
             */
            case STRING:
            {
                String characters = expression.substring(start + 1, end);
                result.add(new Value(type, characters));
                break;
            }
                
            case INTEGER:
            {
                String characters = expression.substring(start, end);
                Object value = null;
                if (characters.contains("."))
                {
                    value = Double.parseDouble(characters);
                    type = ValueType.DOUBLE;
                }
                else
                {
                    value = Integer.parseInt(characters);
                }
                result.add(new Value(type, value));
                break;
            }
                
            case OPERATOR:
            case FUNCTION:
            {
                String characters = expression.substring(start, end);
                IFunction function = (specific != null) ?
                        specific.getVariableSafe(characters) : null;
                if (function != null)
                {
                    type = function.getType();
                }
                result.add(new Value(type, characters));
                break;
            }
                
            case VARIABLE:
            {
                String characters = expression.substring(start, end);
                if (characters.equals("true") || characters.equals("false"))
                {
                    Boolean value = Boolean.parseBoolean(characters);
                    result.add(new Value(ValueType.BOOLEAN, value));
                }
                else if (characters.equals("null"))
                {
                    result.add(new Value(ValueType.NULL, null));
                }
                else
                {
                    IFunction function = (specific != null) ?
                            specific.getVariableSafe(characters) : null;
                    
                    if (function != null)
                    {
                        result.add(new Value(function.getType(), characters));
                    }
                    else
                    {
                        result.add(new Value(type, characters));
                    }
                }
            }
            break;
                
            case NULL:
            case BOOLEAN:
            default:
                throw new IllegalArgumentException("type");
        }
    }
    
    private static boolean isWhitespace(char character)
    {
        return Character.isWhitespace(character);
    }
    
    private static boolean isAlphabetic(char character)
    {
        return ('a' <= character && character <= 'z') ||
               (character == '_') || (character == '.');
    }
    
    private static boolean isOperator(char character)
    {
        return (character == '=') || (character == '!') || 
               (character == '<') || (character == '>') ||
               (character == '+') || (character == '-') || 
               (character == '*') || (character == '/') ||
               (character == '&') || (character == '|') ||
               (character == '?') || (character == '%') ||
               (character == '^');
    }
    
    private static boolean isDigit(char character)
    {
        return ('0' <= character && character <= '9') || (character == '.');
    }
    
    private static boolean isBracket(char character)
    {
        return (character == '{') || (character == '}');
    }
    
    private static boolean isParathesis(char character)
    {
        return (character == '(') || (character == ')');
    }
    
    private static boolean isComma(char character)
    {
        return (character == ',');
    }
    
    private static boolean isSeperator(char character)
    {
        return (character == ';');
    }
    
    private static boolean isFlusher(char character)
    {
        return (character == '\'') || (character == '"');
    }
}
