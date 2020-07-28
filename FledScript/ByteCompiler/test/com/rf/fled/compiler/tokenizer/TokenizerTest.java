/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.tokenizer;

import com.rf.fled.environment.util.OrderedNamedValueArray;
import junit.framework.Assert;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class TokenizerTest
{
    
    public TokenizerTest ()
    {
    }

    @BeforeClass
    public static void setUpClass () throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass () throws Exception
    {
    }
    
    @Before
    public void setUp ()
    {
    }
    
    @After
    public void tearDown ()
    {
    }

    @Test
    public void testTokenizer() throws Exception
    {
        System.out.println("testTokenizer");
        
        String test = "(hello='blah and stuff')";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE,     "hello"));
        comp.add(new Token(TokenType.OPERATOR,     "="));
        comp.add(new Token(TokenType.STRING,       "blah and stuff"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Comma() throws Exception
    {
        System.out.println("testTokenizer_Comma");
        
        String test = "(hello='blah and stuff') function(stuff, something, coo)";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        
        System.out.println(tokens);
        
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "hello"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.STRING    ,   "blah and stuff"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "stuff"));
        comp.add(new Token(TokenType.COMMA     ,   ","));
        comp.add(new Token(TokenType.VARIABLE  ,   "something"));
        comp.add(new Token(TokenType.COMMA     ,   ","));
        comp.add(new Token(TokenType.VARIABLE  ,   "coo"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
                    
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Question() throws Exception
    {
        System.out.println("testTokenizer_Question");
        
        String test = "(hello=?) function(stuff, something, coo)";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "hello"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.OPERATOR  ,   "?"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "stuff"));
        comp.add(new Token(TokenType.COMMA     ,   ","));
        comp.add(new Token(TokenType.VARIABLE  ,   "something"));
        comp.add(new Token(TokenType.COMMA     ,   ","));
        comp.add(new Token(TokenType.VARIABLE  ,   "coo"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
                    
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Negative() throws Exception
    {
        System.out.println("testTokenizer_Negative");
        
        String test = "(hello=-'blah and stuff')";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "hello"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.STRING    ,   "blah and stuff"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "(hello=-12345678)";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "hello"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.INTEGER   ,   -12345678));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "1 - 2";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.INTEGER  ,   1));
        comp.add(new Token(TokenType.OPERATOR ,   "-"));
        comp.add(new Token(TokenType.INTEGER  ,   2));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "1-2";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.INTEGER  ,   1));
        comp.add(new Token(TokenType.OPERATOR ,   "-"));
        comp.add(new Token(TokenType.INTEGER  ,   2));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "1+ -1";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.INTEGER  ,   1));
        comp.add(new Token(TokenType.OPERATOR ,   "+"));
        comp.add(new Token(TokenType.INTEGER  ,   -1));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "1+-1";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.INTEGER  ,   1));
        comp.add(new Token(TokenType.OPERATOR ,   "+"));
        comp.add(new Token(TokenType.INTEGER  ,   -1));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Whitespace() throws Exception
    {
        System.out.println("testTokenizer_Whitespace");
        
        String test = "(hello=-              'blah and                         stuff                 ') &&";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "hello"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.STRING    ,   "blah and                         stuff                 "));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.OPERATOR  ,   "&&"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "&& && &&           == ==<<";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.OPERATOR  ,   "&&"));
        comp.add(new Token(TokenType.OPERATOR  ,   "&&"));
        comp.add(new Token(TokenType.OPERATOR  ,   "&&"));
        comp.add(new Token(TokenType.OPERATOR  ,   "=="));
        comp.add(new Token(TokenType.OPERATOR  ,   "=="));
        comp.add(new Token(TokenType.OPERATOR  ,   "<<"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "function()   -function () function() - function()";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.FUNCTION  ,   "function"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Parathesis() throws Exception
    {
        System.out.println("testTokenizer_Parathesis");
        
        String test = "(((())(thingy - skfdjl && stuff )))";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "thingy"));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.VARIABLE  ,   "skfdjl"));
        comp.add(new Token(TokenType.OPERATOR  ,   "&&"));
        comp.add(new Token(TokenType.VARIABLE  ,   "stuff"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_FunctionThenMinus() throws Exception
    {
        System.out.println("testTokenizer_FunctionThenMinus");
        
        String test = ")-5";
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.OPERATOR  ,   "-"));
        comp.add(new Token(TokenType.INTEGER   ,   5));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Seperator() throws Exception
    {
        System.out.println("testTokenizer_Seperator");
        
        ArrayList<Token> tokens = (new Tokenizer()).tokenize(
                "var = 'something'; "+ 
                "var = var + 1;");
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.VARIABLE  ,   "var"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.STRING    ,   "something"));
        comp.add(new Token(TokenType.SEPERATOR ,   ";"));
        comp.add(new Token(TokenType.VARIABLE  ,   "var"));
        comp.add(new Token(TokenType.OPERATOR  ,   "="));
        comp.add(new Token(TokenType.VARIABLE  ,   "var"));
        comp.add(new Token(TokenType.OPERATOR  ,   "+"));
        comp.add(new Token(TokenType.INTEGER   ,   1));
        comp.add(new Token(TokenType.SEPERATOR ,   ";"));
        
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Construct() throws Exception
    {
        System.out.println("testTokenizer_Construct");
        
        String test = "if (thingy)";
        OrderedNamedValueArray<TypePrecedencePair> constructs = 
                TokenizerFunctionSets.get(TokenizerFunctionSets.FunctionSet.CONSTRUCTS);
        ArrayList<Token> tokens = (new Tokenizer(constructs)).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.CONSTRUCT ,   "if"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "thingy"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_ConstructChaining() throws Exception
    {
        System.out.println("testTokenizer_ConstructChaining");
        
        String test = "if (thingy){}else{}";
        OrderedNamedValueArray<TypePrecedencePair> constructs = 
                TokenizerFunctionSets.get(TokenizerFunctionSets.FunctionSet.CONSTRUCTS);
        ArrayList<Token> tokens = (new Tokenizer(constructs)).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.CONSTRUCT ,   "if"));
        comp.add(new Token(TokenType.PARATHESIS,   "("));
        comp.add(new Token(TokenType.VARIABLE  ,   "thingy"));
        comp.add(new Token(TokenType.PARATHESIS,   ")"));
        comp.add(new Token(TokenType.BRACKET   ,   "{"));
        comp.add(new Token(TokenType.BRACKET   ,   "}"));
        comp.add(new Token(TokenType.CONSTRUCT ,   "else"));
        comp.add(new Token(TokenType.BRACKET   ,   "{"));
        comp.add(new Token(TokenType.BRACKET   ,   "}"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Operators() throws Exception
    {
        System.out.println("testTokenizer_Operators");
        
        String test = "+= -= == /= ?= ? |= %= &= >> << && || ?&--++-+ =+ =- =&";
        OrderedNamedValueArray<TypePrecedencePair> constructs = 
                TokenizerFunctionSets.get(TokenizerFunctionSets.FunctionSet.CONSTRUCTS);
        ArrayList<Token> tokens = (new Tokenizer(constructs)).tokenize(test);
        System.out.println(tokens);
        ArrayList<Token> comp = new ArrayList<Token>();
        comp.add(new Token(TokenType.OPERATOR, "+="));
        comp.add(new Token(TokenType.OPERATOR, "-="));
        comp.add(new Token(TokenType.OPERATOR, "=="));
        comp.add(new Token(TokenType.OPERATOR, "/="));
        comp.add(new Token(TokenType.OPERATOR, "?="));
        comp.add(new Token(TokenType.OPERATOR, "?"));
        comp.add(new Token(TokenType.OPERATOR, "|="));
        comp.add(new Token(TokenType.OPERATOR, "%="));
        comp.add(new Token(TokenType.OPERATOR, "&="));
        comp.add(new Token(TokenType.OPERATOR, ">>"));
        comp.add(new Token(TokenType.OPERATOR, "<<"));
        comp.add(new Token(TokenType.OPERATOR, "&&"));
        comp.add(new Token(TokenType.OPERATOR, "||"));
        comp.add(new Token(TokenType.OPERATOR, "?"));
        comp.add(new Token(TokenType.OPERATOR, "&"));
        comp.add(new Token(TokenType.OPERATOR, "--"));
        comp.add(new Token(TokenType.OPERATOR, "++"));
        comp.add(new Token(TokenType.OPERATOR, "-"));
        comp.add(new Token(TokenType.OPERATOR, "+"));
        comp.add(new Token(TokenType.OPERATOR, "="));
        comp.add(new Token(TokenType.OPERATOR, "+"));
        comp.add(new Token(TokenType.OPERATOR, "="));
        comp.add(new Token(TokenType.OPERATOR, "-"));
        comp.add(new Token(TokenType.OPERATOR, "="));
        comp.add(new Token(TokenType.OPERATOR, "&"));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
}
