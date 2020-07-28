/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

import com.rf.fled.tokenizer.Tokenizer;
import com.rf.fled.tokenizer.TokenType;
import org.junit.*;

/**
 *
 * @author REx
 */
public class TokenTypeManagerTest
{
    
    public TokenTypeManagerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void test_literal_single_1() throws Exception
    {
        System.out.println("test_literal_single_1");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(new TokenType("count", "-1-2-3"));
        
        TokenRuleNode _3 = new TokenRuleNode();
        _3.tokentype = new TokenType("count", "-1-2-3");
        TokenRuleNode _2 = new TokenRuleNode();
        _2.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'3'}, null), _3);
        TokenRuleNode _1 = new TokenRuleNode();
        _1.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'2'}, null), _2);
        TokenRuleNode root = new TokenRuleNode();
        root.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), _1);
        
        Assert.assertTrue(root.equals(instance.getRootRule()));
    }

    @Test
    public void test_literal_single_2() throws Exception
    {
        System.out.println("test_literal_single_2");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(new TokenType("count1", "-1-2-3"));
        instance.addTokenType(new TokenType("count2", "-1-2-4"));
        
        TokenRuleNode _4 = new TokenRuleNode();
        _4.tokentype = new TokenType("count2", "-1-2-4");
        TokenRuleNode _3 = new TokenRuleNode();
        _3.tokentype = new TokenType("count1", "-1-2-3");
        TokenRuleNode _2 = new TokenRuleNode();
        _2.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'4'}, null), _4);
        _2.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'3'}, null), _3);
        TokenRuleNode _1 = new TokenRuleNode();
        _1.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'2'}, null), _2);
        TokenRuleNode root = new TokenRuleNode();
        root.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), _1);
        
        Assert.assertTrue(root.equals(instance.getRootRule()));
    }

    @Test
    public void test_literal_single_3() throws Exception
    {
        System.out.println("test_literal_single_3");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(new TokenType("count1", "-1-2-3"));
        instance.addTokenType(new TokenType("count2", "-1-2-4"));
        instance.addTokenType(new TokenType("count3", "-1-4-4"));
        
        TokenRuleNode _1_4_4 = new TokenRuleNode();
        _1_4_4.tokentype = new TokenType("count3", "-1-4-4");
        TokenRuleNode _1_2_4 = new TokenRuleNode();
        _1_2_4.tokentype = new TokenType("count2", "-1-2-4");
        TokenRuleNode _1_2_3 = new TokenRuleNode();
        _1_2_3.tokentype = new TokenType("count1", "-1-2-3");
        TokenRuleNode _1_2 = new TokenRuleNode();
        _1_2.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'4'}, null), _1_2_4);
        _1_2.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'3'}, null), _1_2_3);
        TokenRuleNode _1_4 = new TokenRuleNode();
        _1_4.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'4'}, null), _1_4_4);
        TokenRuleNode _1 = new TokenRuleNode();
        _1.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'2'}, null), _1_2);
        _1.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'4'}, null), _1_4);
        TokenRuleNode root = new TokenRuleNode();
        root.children.put(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), _1);
        
        Assert.assertTrue(root.equals(instance.getRootRule()));
    }

    @Test
    public void test_visual() throws Exception
    {
        System.out.println("test_visual");
        
        Tokenizer instance = new Tokenizer();
        instance.addTokenType(new TokenType("count1", "*1-2-3"));
        instance.addTokenType(new TokenType("count2", "-1-2-4"));
        instance.addTokenType(new TokenType("count3", "-1-[4657]-4"));
        instance.addTokenType(new TokenType("count5", "-1-4-4"));
        System.out.println(instance);
    }
}
