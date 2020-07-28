/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class TokenTypeManagerTestParse
{
    
    public TokenTypeManagerTestParse()
    {
    }

    @Test
    public void test_simple1() throws Exception
    {
        System.out.println("test_simple1");
        
        TokenType counter = new TokenType("count1", "-1-2-3");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(counter);
        
        Iterator<Token> it = instance.parseAsIterator("123");
        Assert.assertTrue(it.hasNext());
        Token result = it.next();
        Assert.assertEquals(new Token(counter, "123", "123", 0), result);
        Assert.assertEquals(false, it.hasNext());
    }

    @Test
    public void test_simple2() throws Exception
    {
        System.out.println("test_simple2");
        
        TokenType counter1 = new TokenType("count1", "-1-2-3");
        TokenType counter2 = new TokenType("count2", "-3-2-1");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(counter1);
        instance.addTokenType(counter2);
        
        Iterator<Token> it = instance.parseAsIterator("123321");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(counter1, "123", "123", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(counter2, "321", "321", 3), result);
            Assert.assertEquals(false, it.hasNext());
        }
    }

    @Test
    public void test_numbers1() throws Exception
    {
        System.out.println("test_numbers1");
        
        TokenType integer = new TokenType("int", "*[0-9]");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(integer);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("123321");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(integer, "123321", "123321", 0), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_numbers2() throws Exception
    {
        System.out.println("test_numbers2");
        
        TokenType integer = new TokenType("int", "*[0-9]");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(integer);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("123321 1234");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(integer, "123321", "123321", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(whitespace, " ", " ", 6), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(integer, "1234", "1234", 7), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_numbers3() throws Exception
    {
        System.out.println("test_numbers3");
        
        TokenType _double = new TokenType("double", "*[0-9]-.*[0-9]");
        TokenType integer = new TokenType("int", "*[0-9]");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(_double);
        instance.addTokenType(integer);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("123321.123");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_double, "123321.123", "123321.123", 0), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_numbers4() throws Exception
    {
        System.out.println("test_numbers4");
        
        TokenType _ndouble = new TokenType("ndouble", "--*[0-9]-.*[0-9]");
        TokenType _double = new TokenType("double", "*[0-9]-.*[0-9]");
        TokenType integer = new TokenType("int", "*[0-9]");
        TokenType ninteger = new TokenType("nint", "--*[0-9]");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(_ndouble);
        instance.addTokenType(_double);
        instance.addTokenType(ninteger);
        instance.addTokenType(integer);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("-123321.123");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_ndouble, "-123321.123", "-123321.123", 0), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_numbers5() throws Exception
    {
        System.out.println("test_numbers5");
        
        TokenType _ndouble = new TokenType("ndouble", "--*[0-9]-.*[0-9]");
        TokenType _double = new TokenType("double", "*[0-9]-.*[0-9]");
        TokenType integer = new TokenType("int", "*[0-9]");
        TokenType ninteger = new TokenType("nint", "--*[0-9]");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(_ndouble);
        instance.addTokenType(_double);
        instance.addTokenType(ninteger);
        instance.addTokenType(integer);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("983274 -123321.123 -123 7213.32");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(integer, "983274", "983274", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(whitespace, " ", " ", 6), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_ndouble, "-123321.123", "-123321.123", 7), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(whitespace, " ", " ", 18), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(ninteger, "-123", "-123", 19), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(whitespace, " ", " ", 23), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_double, "7213.32", "7213.32", 24), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_string1() throws Exception
    {
        System.out.println("test_string1");
        
        TokenType string = new TokenType("string", "-\"*[\n !#-~]-\"");
        TokenType lstring = new TokenType("lstring", "-'*[\n -&(-~]-'");
        TokenType whitespace = new TokenType("whitespace", "*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(string);
        instance.addTokenType(lstring);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("'hello world!'   \"hello world again!\"");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(lstring, "'hello world!'", "'hello world!'", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(whitespace, "   ", "   ", 14), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(string, "\"hello world again!\"", "\"hello world again!\"", 17), result);
            Assert.assertFalse(it.hasNext());
        }
    }

    @Test
    public void test_string2() throws Exception
    {
        System.out.println("test_string2");
        
        TokenType string = new TokenType("string", "-\"*[\n !#-~]-\"");
        TokenType lstring = new TokenType("lstring", "-'*[\n -&(-~]-'");
        TokenType whitespace = new TokenType("whitespace", "@k*[ \n]");
        TokenType variable = new TokenType("variable", "-[a-zA-Z_]*[a-zA-Z0-9_]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(string);
        instance.addTokenType(lstring);
        instance.addTokenType(whitespace);
        instance.addTokenType(variable);
        
        Iterator<Token> it = instance.parseAsIterator("'hello world!'   \"hello world again!\"");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(lstring, "'hello world!'", "'hello world!'", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(string, "\"hello world again!\"", "\"hello world again!\"", 17), result);
            Assert.assertFalse(it.hasNext());
        }
    }
    
    @Test
    public void test_attribute1() throws Exception
    {
        System.out.println("test_attribute1");
        
        TokenType string = new TokenType("string", "-\"*[\n !#-~]-\"");
        TokenType lstring = new TokenType("lstring", "-'*[\n -&(-~]-'");
        TokenType whitespace = new TokenType("whitespace", "@k*[ \n]");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(string);
        instance.addTokenType(lstring);
        instance.addTokenType(whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("'hello world!'   \"hello world again!\"");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(lstring, "'hello world!'", "'hello world!'", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(string, "\"hello world again!\"", "\"hello world again!\"", 17), result);
            Assert.assertFalse(it.hasNext());
        }
    }
    
    @Test
    public void test_group1() throws Exception
    {
        System.out.println("test_group1");
        
        TokenType _boolean = new TokenType("boolean", "-(true|false)");
        TokenType _whitespace = new TokenType("whitespace", "@k*[ \n]");
        
        
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(_boolean);
        instance.addTokenType(_whitespace);
        
        Iterator<Token> it = instance.parseAsIterator("true false");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_boolean, "true", "true", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(_boolean, "false", "false", 5), result);
            Assert.assertFalse(it.hasNext());
        }
    }
    
    @Test
    public void test_workontoken1() throws Exception
    {
        System.out.println("test_workontoken1");
        
        TokenType string = new TokenType("string", "-\"*[\n !#-~]-\"");
        TokenType lstring = new TokenType("lstring", "-'*[\n -&(-~]-'");
        TokenType whitespace = new TokenType("whitespace", "@k*[ \n]");
        TokenType integer = new TokenType("int", "*[0-9]")
        {
            @Override
            public Object doWorkOnToken(String rawtoken)
            {
                return Integer.parseInt(rawtoken);
            }
        };
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(string);
        instance.addTokenType(lstring);
        instance.addTokenType(whitespace);
        instance.addTokenType(integer);
        
        Iterator<Token> it = instance.parseAsIterator("'hello world!'   \"hello world again!\" 1234");
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(lstring, "'hello world!'", "'hello world!'", 0), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(string, "\"hello world again!\"", "\"hello world again!\"", 17), result);
            Assert.assertTrue(it.hasNext());
        }
        
        {
            Assert.assertTrue(it.hasNext());
            Token result = it.next();
            Assert.assertEquals(new Token(integer, 1234, "1234", 38), result);
            Assert.assertFalse(it.hasNext());
        }
    }
}
