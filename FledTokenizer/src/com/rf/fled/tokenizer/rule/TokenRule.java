/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

import com.rf.fled.tokenizer.TokenRuleSyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class TokenRule
{
    public final RuleType ruletype;
    
    public final SetType chartype;
    
    public final char[][] groupchars;
    
    public final char[] singlechars;
    
    public final char[] rangechars;

    public TokenRule(RuleType ruletype,
                     SetType chartype,
                     char[] singlechars,
                     char[] rangechars)
            throws TokenRuleSyntaxException
    {
        this.chartype       = Objects.requireNonNull(chartype, "chartype");
        this.ruletype       = Objects.requireNonNull(ruletype, "ruletype");
        
        if (rangechars != null && rangechars.length % 2 == 1)
        {
            throw new TokenRuleSyntaxException(
                    String.format("illegal amount of characters in range array [%s]", 
                                    Arrays.toString(singlechars)));
        }

        switch(chartype)
        {
            case SINGLE:
            case SET:
            {
                StringBuilder singles = getSingles(singlechars);
                StringBuilder ranges = new StringBuilder();
                if (rangechars != null)
                {
                    ranges.append(rangechars);    
                }
                reduceSingles(singles, ranges);
                reduceRanges(ranges);
                consumeSingles(singles, ranges);

                this.singlechars    = singles.toString().toCharArray();
                this.rangechars     = ranges.toString().toCharArray();
                
                this.groupchars = null;
                break;
            }
                
            case GROUP:
            {
                this.singlechars = null;
                this.rangechars = null;
                this.groupchars = parseGroupChars(singlechars);
                break;
            }
                
            default:
                throw new TokenRuleSyntaxException(
                        String.format("illegal character type [%s]", 
                                      chartype));
        }
    }
    
    private StringBuilder getSingles(char[] singlechars)
    {
        if (singlechars == null)
        {
            return new StringBuilder();
        }
        char[] _singlechars = Arrays.copyOf(singlechars, singlechars.length);
        Arrays.sort(_singlechars);
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < _singlechars.length; i++)
        {
            boolean place = true;
            for(int j = i + 1; j < _singlechars.length && place; j++)
            {
                if (_singlechars[i] == _singlechars[j])
                {
                    place = false;
                }
            }
            if (place)
            {
                result.append(_singlechars[i]);
            }
        }
        return result;
    }
    
    private void reduceSingles(StringBuilder singles, StringBuilder ranges)
    {
        for(int i = 0; i < singles.length() - 1; i++)
        {
            char left = singles.charAt(i);
            char right = singles.charAt(i + 1);
            
            if (left + 1 == right)
            {
                int goesto = i + 1;
                if (i + 2 < singles.length())
                {
                    for(int j = i+2; j < singles.length(); j++)
                    {
                        char curr = singles.charAt(j);
                        if (right + 1 == curr)
                        {
                            right = curr;
                            goesto = j;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                
                int deletecount = goesto - i + 1;
                
                for(int j = 0; j < deletecount; j++)
                {
                    singles.deleteCharAt(i);
                }
                
                ranges.append(left);
                ranges.append(right);
                
                // this will basically start the loop over
                i--;
            }
        }
    }

    private void reduceRanges(StringBuilder ranges)
    {
        for(int i = 0; i < ranges.length() - 1; i += 2)
        {
            char left1 = ranges.charAt(i);
            char right1 = ranges.charAt(i + 1);
            
            boolean found = false;
            for(int j = i+2; j < ranges.length() && !found; j+=2)
            {
                char left2 = ranges.charAt(j);
                char right2 = ranges.charAt(j + 1);
                
                if ((left1 <= left2 && left2 <= right1) ||
                    (left1 <= right2 && right2 <= right1) ||
                    (left2 <= left1 && left1 <= right2) ||
                    (left2 <= right1 && right1 <= right2) ||
                    (right1 + 1 == left2) ||
                    (right2 + 1 == left1))
                {
                    found = true;
                    ranges.setCharAt(i,   (left1 < left2)   ? left1  : left2);
                    ranges.setCharAt(i+1, (right1 < right2) ? right2 : right1);
                    ranges.deleteCharAt(j);
                    ranges.deleteCharAt(j);
                    j-=2;
                }
            }
            
            if (found)
            {
                i = -2;
            }
        }
    }

    private void consumeSingles(StringBuilder singles, StringBuilder ranges)
    {
        for(int i = 0; i < ranges.length(); i+=2)
        {
            char left = ranges.charAt(i);
            char right = ranges.charAt(i+1);
            
            for(int j = 0; j < singles.length(); j++)
            {
                char single = singles.charAt(j);
                if (left <= single && single <= right)
                {
                    singles.deleteCharAt(j);
                    j--;
                }
            }
        }
    }
    
    private char[][] parseGroupChars(char[] characters)
            throws TokenRuleSyntaxException
    {
        ArrayList<char[]> buffer = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for(int i = 0; i < characters.length; i++)
        {
            char character = characters[i];
            if (character == '\\')
            {
                i++;
                if (i >= characters.length)
                {
                    throw new TokenRuleSyntaxException(
                            String.format("illegal escape of a character [%s]", 
                                          Arrays.toString(characters)));
                }
                current.append(characters[i]);
            }
            else if (character == '|')
            {
                char[] group = current.toString().toCharArray();
                current = new StringBuilder();
                buffer.add(group);
            }
            else
            {
                current.append(character);
            }
        }
        
        buffer.add(current.toString().toCharArray());
        
        char[][] result = new char[buffer.size()][];
        result = buffer.toArray(result);
        return result;
    }
    
    public int matches(String characters, int pos)
    {
        switch(ruletype)
        {
            case LITERAL:
            {
                return findLiteral(characters, pos);
            }
            case MANY:
            {
                return findMany(characters, pos);
            }
            case NOT:
            {
                return findNot(characters, pos);
            }
            default:
                throw new IllegalStateException();
        }
    }
    
    private int findLiteral(String characters, int pos)
    {
        switch(chartype)
        {
            case SET:
            case SINGLE:
            {
                return hasCharInList(characters.charAt(pos)) ? 1 : 0;
            }
            case GROUP:
            {
                return matchesGroup(characters, pos);
            }
            default:
                throw new IllegalStateException();
        }
    }
    
    private int findMany(String characters, int pos)
    {
        int count = 0;
        boolean found = true;
        while(found)
        {
            if (pos + count >= characters.length())
            {
                break;
            }
            switch(chartype)
            {
                case SET:
                case SINGLE:
                {
                    found = hasCharInList(characters.charAt(pos + count));
                    if (found)
                    {
                        count++;
                    }
                    break;
                }
                case GROUP:
                {
                    int foundcount = matchesGroup(characters, pos);
                    if (foundcount > 0)
                    {
                        found = true;
                        count += singlechars.length;
                    }
                    else
                    {
                        found = false;
                    }
                    break;
                }
                default:
                    throw new IllegalStateException();
            }
        }
        return count;
    }
    
    private int findNot(String characters, int pos)
    {
        switch(chartype)
        {
            case SET:
            case SINGLE:
            {
                return !hasCharInList(characters.charAt(pos)) ? 1 : 0;
            }
            case GROUP:
            default:
                throw new IllegalStateException();
        }
    }
    
    private boolean hasCharInList(char character)
    {
        for(char single : singlechars)
        {
            if (single == character)
            {
                return true;
            }
        }
        
        for(int i = 0; i < rangechars.length; i += 2)
        {
            if (rangechars[i] <= character && character <= rangechars[i + 1])
            {
                return true;
            }
        }
        
        return false;
    }
    
    private int matchesGroup(String expression, int start)
    {
        for(int i = 0; i < groupchars.length; i++)
        {
            char[] thisgroup = groupchars[i];
            boolean found = true;
            for(int j = 0; j < thisgroup.length && found; j++)
            {
                if (thisgroup[j] != expression.charAt(start + j))
                {
                    found = false;
                }
            }
            if (found)
            {
                return thisgroup.length;
            }
        }
        return 0;
    }
    
    @Override
    public String toString()
    {
        return String.format("[chartype: %s, ruletype: %s, singes: %s, ranges %s]", 
                             chartype, 
                             ruletype, 
                             Arrays.toString(singlechars), 
                             Arrays.toString(rangechars));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TokenRule other = (TokenRule) obj;
        if (this.chartype != other.chartype)
        {
            return false;
        }
        if (this.ruletype != other.ruletype)
        {
            return false;
        }
        if (!Arrays.equals(this.singlechars, other.singlechars))
        {
            return false;
        }
        if (!Arrays.equals(this.rangechars, other.rangechars))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + (this.chartype != null ? this.chartype.hashCode() : 0);
        hash = 19 * hash + (this.ruletype != null ? this.ruletype.hashCode() : 0);
        hash = 19 * hash + Arrays.hashCode(this.singlechars);
        hash = 19 * hash + Arrays.hashCode(this.rangechars);
        return hash;
    }
    
    
}
