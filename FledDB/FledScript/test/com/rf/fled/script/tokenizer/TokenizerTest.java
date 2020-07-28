/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.tokenizer;

import com.rf.fled.script.rpn.MathFunctionSet;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class TokenizerTest {
    
    public TokenizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
//    @Test
    public void testTokenizer() throws Exception
    {
        String test = "(hello='blah and stuff')";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE,     null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR,     null));
        comp.add(new TokenPair("'",     TokenType.STRING,       "blah and stuff"));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Comma() throws Exception
    {
        String test = "(hello='blah and stuff') function(stuff, something, coo)";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        
        System.out.println(tokens);
        
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("'",     TokenType.STRING    ,   "blah and stuff"));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("stuff", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(",",     TokenType.COMMA     ,   null));
        comp.add(new TokenPair("something",TokenType.VARIABLE,  null));
        comp.add(new TokenPair(",",     TokenType.COMMA     ,   null));
        comp.add(new TokenPair("coo",   TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
                    
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Question() throws Exception
    {
        String test = "(hello=?) function(stuff, something, coo)";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("?",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("stuff", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(",",     TokenType.COMMA     ,   null));
        comp.add(new TokenPair("something",TokenType.VARIABLE,  null));
        comp.add(new TokenPair(",",     TokenType.COMMA     ,   null));
        comp.add(new TokenPair("coo",   TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
                    
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Negative() throws Exception
    {
        String test = "(hello=-'blah and stuff')";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("'",     TokenType.STRING    ,   "blah and stuff"));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "(hello=-12345678)";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("-12345678",TokenType.INTEGER,   -12345678));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "1 - 2";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("1",  TokenType.INTEGER  ,   1));
        comp.add(new TokenPair("-",  TokenType.OPERATOR ,   null));
        comp.add(new TokenPair("2",  TokenType.INTEGER  ,   2));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        
        test = "1-2";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("1",  TokenType.INTEGER  ,   1));
        comp.add(new TokenPair("-",  TokenType.OPERATOR ,   null));
        comp.add(new TokenPair("2",  TokenType.INTEGER  ,   2));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "1+ -1";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("1",     TokenType.INTEGER   ,   1));
        comp.add(new TokenPair("+",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("-1",    TokenType.INTEGER   ,   -1));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "1+-1";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("1",     TokenType.INTEGER   ,   1));
        comp.add(new TokenPair("+",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("-1",    TokenType.INTEGER   ,   -1));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Whitespace() throws Exception
    {
        String test = "(hello=-              'blah and                         stuff                 ') &&";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("hello", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("'",     TokenType.STRING    ,   "blah and                         stuff                 "));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("&&",    TokenType.OPERATOR  ,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "&& && &&           == ==<<";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("&&",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("&&",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("&&",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("==",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("==",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("<",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("<",     TokenType.OPERATOR  ,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
        
        test = "function()   -function () function() - function()";
        tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("function",TokenType.FUNCTION,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Parathesis() throws Exception
    {
        String test = "(((())(thingy - skfdjl && stuff )))";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("thingy",TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("skfdjl",TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("&&",    TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("stuff", TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_FunctionThenMinus() throws Exception
    {
        String test = ")-5";
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("-",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("5",     TokenType.INTEGER   ,   5));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
//    @Test
    public void testTokenizer_Seperator() throws Exception
    {
        ArrayList<TokenPair> tokens = (new Tokenizer()).tokenize(
                "var = 'something'; "+ 
                "var = var + 1;");
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("var",   TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("'",     TokenType.STRING    ,   "something"));
        comp.add(new TokenPair(";",     TokenType.SEPERATOR ,   null));
        comp.add(new TokenPair("var",   TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("=",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("var",   TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair("+",     TokenType.OPERATOR  ,   null));
        comp.add(new TokenPair("1",     TokenType.INTEGER   ,   1));
        comp.add(new TokenPair(";",     TokenType.SEPERATOR ,   null));
        
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_Construct() throws Exception
    {
        String test = "if (thingy)";
        ArrayList<TokenPair> tokens = (new Tokenizer(new MathFunctionSet())).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("if",    TokenType.CONSTRUCT ,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("thingy",TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
    
    @Test
    public void testTokenizer_ConstructChaining() throws Exception
    {
        String test = "if (thingy){}else{}";
        ArrayList<TokenPair> tokens = (new Tokenizer(new MathFunctionSet())).tokenize(test);
        System.out.println(tokens);
        ArrayList<TokenPair> comp = new ArrayList<TokenPair>();
        comp.add(new TokenPair("if",    TokenType.CONSTRUCT ,   null));
        comp.add(new TokenPair("(",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("thingy",TokenType.VARIABLE  ,   null));
        comp.add(new TokenPair(")",     TokenType.PARATHESIS,   null));
        comp.add(new TokenPair("{",     TokenType.BRACKET   ,   null));
        comp.add(new TokenPair("}",     TokenType.BRACKET   ,   null));
        comp.add(new TokenPair("else",  TokenType.CONSTRUCT ,   null));
        comp.add(new TokenPair("{",     TokenType.BRACKET   ,   null));
        comp.add(new TokenPair("}",     TokenType.BRACKET   ,   null));
        System.out.println(comp);
        Assert.assertEquals(comp.toString(), tokens.toString());
    }
}
