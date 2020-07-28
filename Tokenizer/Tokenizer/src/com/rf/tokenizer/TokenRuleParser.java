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
class TokenRuleParser
{

    private static class ParsingContext
    {
        String name;
        
        String definition;
        
        int pos = 0;
        
        LinkedList<TokenRuleNode> leafs = new LinkedList<>();
        
        TokenRuleNode root = new TokenRuleNode.TokenRuleNode_Iterate();
        
        TokenRuleNode curr = null;

        public ParsingContext(String name, String definition)
        {
            this.definition = definition;
            this.name = name;
            leafs.add(root);
        }
        
        boolean end()
        {
            return pos >= definition.length();
        }
        
        char curr()
        {
            return definition.charAt(pos);
        }
        
        char peek()
        {
            return definition.charAt(pos + 1);
        }
        
        boolean canPeek()
        {
            return StringUtil.canPeek(definition, pos);
        }
        
        boolean isDefinition()
        {
            return curr() == RuleCharacter.DEFINITION_START;
        }
        
        boolean isLoopCount()
        {
            char character = curr();
            return (character == RuleCharacter.RULE_COUNT_ZERO_OR_ONE) ||
                   (character == RuleCharacter.RULE_COUNT_ZERO_OR_MORE) ||
                   (character == RuleCharacter.RULE_COUNT_ONE_OR_MORE) ||
                   (character == RuleCharacter.RULE_COUNT_RANGE_START);
        }
        
        boolean isDefAttribute()
        {
            if (!canPeek() || !isDefinition())
            {
                return false;
            }
            return peek() == RuleCharacter.ATTRIBUTE;
        }
        
        boolean isDefContext()
        {
            if (!canPeek() || !isDefinition())
            {
                return false;
            }
            return peek() == RuleCharacter.CONTEXT;
        }
    }
    
    /**
     * returns the root node of the parsed tree for the 
     * specific rule
     * @param definition
     * @return 
     */
    static TokenRuleNode parse(String definition)
            throws TokenizerSyntaxException
    {
        // first we get the name
        int index = definition.indexOf(RuleCharacter.SEPARATOR);
        if (index < 0)
        {
            throw new TokenizerSyntaxException(
                    String.format("could not find separator for name [%s]", 
                                  definition));
        }
        else if(index == 0)
        {
            throw new TokenizerSyntaxException(
                    String.format("must have a string representing the name "
                    + "before the first separator [%s]", definition));
        }
        
        String name = definition.substring(0, index);
        String subdef = definition.substring(index + 1);
        
        ParsingContext context = new ParsingContext(name, subdef);
        
        parseEntry(context);
        
        return context.root;
    }
    
    private static void parseEntry(ParsingContext context)
            throws TokenizerSyntaxException
    {
        
        for( ; context.pos < context.definition.length(); context.pos++)
        {
            char character = context.definition.charAt(context.pos);
            
            switch(character)
            {
                case RuleCharacter.RULE_SPLIT:
                {
                    doHighLevelSplit(context);
                    break;
                }
                    
                case RuleCharacter.RULE_CHAR_ANY:
                {
                    parseAny(context);
                    break;
                }
    
                case RuleCharacter.RULE_GROUP_START:
                {
                    parseGroup(context);
                    break;
                }
    
                case RuleCharacter.RULE_SET_START:
                {
                    parseSet(context);
                    break;
                }
                    
                case RuleCharacter.RULE_GROUP_END:
                case RuleCharacter.RULE_SET_END:
                case RuleCharacter.RULE_COUNT_RANGE_START:
                case RuleCharacter.RULE_COUNT_RANGE_END:
                case RuleCharacter.RULE_COUNT_ZERO_OR_ONE:
                case RuleCharacter.RULE_COUNT_ZERO_OR_MORE:
                case RuleCharacter.RULE_COUNT_ONE_OR_MORE:
                {
                    throw new TokenizerSyntaxException(
                            String.format("illegal floating character at index %s", 
                                          context.pos));
                }
                    
                case RuleCharacter.ESCAPE:
                {
                    if (!StringUtil.canPeek(context.definition, context.pos))
                    {
                        throw new TokenizerSyntaxException(
                                String.format("illegal ending character, cannot "
                                              + "have the last char be the escape char [%s]", 
                                              context.definition));
                    }
                }
                default:
                {
                    parseSingle(context);
                    break;
                }
            }
        }
    }
    
    private static void doHighLevelSplit(ParsingContext context)
            throws TokenizerSyntaxException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void parseAny(ParsingContext context)
            throws TokenizerSyntaxException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void parseGroup(ParsingContext context)
            throws TokenizerSyntaxException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void parseSet(ParsingContext context)
            throws TokenizerSyntaxException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void parseSingle(ParsingContext context)
            throws TokenizerSyntaxException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private static void parseSafeMeta(ParsingContext context)
            throws TokenizerSyntaxException
    {
        if (!context.end())
        {
            return;
        }
        
        TokenRuleNode loop = null;
        TokenRuleNode cotx = null;
        char[] attr = null;
        
        if (context.isDefContext())
        {
            cotx = parseContext(context);
        }
        
        if (context.isDefAttribute())
        {
            attr = parseAttributes(context);
        }
        
        if (context.isLoopCount())
        {
            loop = parseLoop(context);
        }
        
        context.curr.attributes = attr;
    }
    
    private static TokenRuleNode parseLoop(ParsingContext context)
            throws TokenizerSyntaxException
    {
        return null;
    }
    
    private static TokenRuleNode parseContext(ParsingContext context)
            throws TokenizerSyntaxException
    {
        return null;
    }
    
    private static char[] parseAttributes(ParsingContext context)
            throws TokenizerSyntaxException
    {
        return null;
    }
}
