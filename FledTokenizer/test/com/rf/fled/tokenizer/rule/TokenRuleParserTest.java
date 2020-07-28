/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

import com.rf.fled.tokenizer.TokenRuleSyntaxException;
import java.util.List;
import org.junit.*;

/**
 *
 * @author REx
 */
public class TokenRuleParserTest
{
    
    public TokenRuleParserTest()
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
    public void test_reducing1() throws Exception
    {
        System.out.println("test_reducing1");
        
        TokenRule rule1 = new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'1', '2', '3'}, null);
        TokenRule rule2 = new TokenRule(RuleType.LITERAL, SetType.SET, null, new char[]{'1', '3'});
        System.out.println(rule1);
        System.out.println(rule2);
        Assert.assertEquals(rule1, rule2);
    }
    
    @Test
    public void test_reducing2() throws Exception
    {
        System.out.println("test_reducing2");
        
        TokenRule rule1 = new TokenRule(RuleType.LITERAL, SetType.SET, null, new char[]{'1', '4', '3', '7'});
        TokenRule rule2 = new TokenRule(RuleType.LITERAL, SetType.SET, null, new char[]{'1', '7'});
        System.out.println(rule1);
        System.out.println(rule2);
        Assert.assertEquals(rule1, rule2);
    }
    
    @Test
    public void test_reducing3() throws Exception
    {
        System.out.println("test_reducing3");
        
        TokenRule rule1 = new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'2', '5'}, new char[]{'1', '4', '3', '7'});
        TokenRule rule2 = new TokenRule(RuleType.LITERAL, SetType.SET, null, new char[]{'1', '7'});
        System.out.println(rule1);
        System.out.println(rule2);
        Assert.assertEquals(rule1, rule2);
    }
    
    @Test
    public void test_reducing4() throws Exception
    {
        System.out.println("test_reducing4");
        
        TokenRule rule1 = new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'2', '5', '9'}, new char[]{'1', '4', '3', '7'});
        TokenRule rule2 = new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'9'}, new char[]{'1', '7'});
        System.out.println(rule1);
        System.out.println(rule2);
        Assert.assertEquals(rule1, rule2);
    }
    
    @Test
    public void test_reducing5() throws Exception
    {
        System.out.println("test_reducing5");
        
        TokenRule rule1 = new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'2', '5', '9', '8'}, new char[]{'1', '4', '3', '7'});
        TokenRule rule2 = new TokenRule(RuleType.LITERAL, SetType.SET, null, new char[]{'1', '9'});
        System.out.println(rule1);
        System.out.println(rule2);
        Assert.assertEquals(rule1, rule2);
    }

    @Test
    public void test_leterals() throws Exception
    {
        System.out.println("test_leterals");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-2-3").rules;
        Assert.assertEquals(3, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'2'}, null), rules.get(1));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'3'}, null), rules.get(2));
    }

    @Test
    public void test_leterals2() throws Exception
    {
        System.out.println("test_leterals2");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-2-a- ").rules;
        Assert.assertEquals(4, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'2'}, null), rules.get(1));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'a'}, null), rules.get(2));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{' '}, null), rules.get(3));
    }
    
    @Test
    public void test_group() throws Exception
    {
        System.out.println("test_group");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-(asfd)").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.GROUP, new char[]{'a', 's', 'f', 'd'}, null), rules.get(1));
    }
    
    @Test
    public void test_charsets() throws Exception
    {
        System.out.println("test_charsets");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-[asfd]").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'a', 's', 'f', 'd'}, null), rules.get(1));
    }
    
    @Test
    public void test_charsets2() throws Exception
    {
        System.out.println("test_charsets2");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-[asd-z]").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'a', 's'}, new char[]{'d', 'z'}), rules.get(1));
    }
    
    @Test
    public void test_charsets3() throws Exception
    {
        System.out.println("test_charsets3");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-[asd\\-z]").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SET, new char[]{'a', 's', 'd', '-', 'z'}, null), rules.get(1));
    }
    
    @Test
    public void test_charsgroup1() throws Exception
    {
        System.out.println("test_charsgroup1");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-(asfd)").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.GROUP, new char[]{'a', 's', 'f', 'd'}, null), rules.get(1));
    }
    
    @Test
    public void test_charsgroup2() throws Exception
    {
        System.out.println("test_charsgroup2");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1-(asd-z)").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.GROUP, new char[]{'a', 's', 'd', '-', 'z'}, null), rules.get(1));
    }
    
    @Test
    public void test_charsgroup3() throws Exception
    {
        System.out.println("test_charsgroup3");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-(asd\\\\-z)").rules;
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.GROUP, new char[]{'a', 's', 'd', '\\', '-', 'z'}, null), rules.get(0));
    }
    
    @Test
    public void test_charsgroup4() throws Exception
    {
        System.out.println("test_charsgroup4");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-(true|false)").rules;
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.GROUP, 
                new char[]{'t', 'r', 'u', 'e', '|', 'f', 'a', 'l', 's', 'e'}, null), rules.get(0));
    }
    
    @Test
    public void test_astricts() throws Exception
    {
        System.out.println("test_astricts");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("-1*[asd\\-z]").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'1'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, new char[]{'a', 's', 'd', '-', 'z'}, null), rules.get(1));
    }
    
    @Test
    public void test_double() throws Exception
    {
        System.out.println("test_double");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("*[0-9]-.*[0-9]").rules;
        Assert.assertEquals(3, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'.'}, null), rules.get(1));
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(2));
    }
    
    @Test
    public void test_negativedouble() throws Exception
    {
        System.out.println("test_negativedouble");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("--*[0-9]-.*[0-9]").rules;
        Assert.assertEquals(4, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'-'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(1));
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'.'}, null), rules.get(2));
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(3));
    }
    
    @Test
    public void test_int() throws Exception
    {
        System.out.println("test_int");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("*[0-9]").rules;
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(0));
    }
    
    @Test
    public void test_negativeint() throws Exception
    {
        System.out.println("test_negativeint");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("--*[0-9]").rules;
        Assert.assertEquals(2, rules.size());
        Assert.assertEquals(new TokenRule(RuleType.LITERAL, SetType.SINGLE, new char[]{'-'}, null), rules.get(0));
        Assert.assertEquals(new TokenRule(RuleType.MANY, SetType.SET, null, new char[]{'0', '9'}), rules.get(1));
    }
    
    @Test(expected=TokenRuleSyntaxException.class)
    public void test_fail() throws Exception
    {
        System.out.println("test_fail");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("--[qwer]").rules;
    }
    
    @Test(expected=TokenRuleSyntaxException.class)
    public void test_fail2() throws Exception
    {
        System.out.println("test_fail2");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("--*[qwer-]").rules;
    }
    
    
    @Test(expected=TokenRuleSyntaxException.class)
    public void test_fail3() throws Exception
    {
        System.out.println("test_fail3");
        
        List<TokenRule> rules = TokenRuleParser.factoryRules("--*[qwer").rules;
    }
}
