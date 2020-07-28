/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2;

import com.rf.fled.script.ExecutorFactory;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class RPN2CompilerTest
{
    
    public RPN2CompilerTest ()
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
    
    public RPN2Compiler getCompiler(String type) throws Exception
    {
        Properties props = new Properties();
        props.setProperty("executor", type);
        return (RPN2Compiler) ExecutorFactory.RPN2.getInstance(null, props);
    }
    
    @Test
    public void test_bench () throws Exception
    {
        RPN2Compiler compiler = null;
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(1 - 2 * 12);");
        
        long start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i += 1)
        {
            result = executor.execute(null, null, true);
            Assert.assertEquals(-23, result.value);
        }
        System.out.println("first time: " + (System.currentTimeMillis() - start));
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(1 - 2 * 12);");
        
        start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i += 1)
        {
            result = executor.execute(null, null, true);
            Assert.assertEquals(-23, result.value);
        }
        System.out.println("second time: " + (System.currentTimeMillis() - start));
    }

    /**
     * Test of init method, of class RPN2Compiler.
     */
    @Test
    public void test_mathops_ints () throws Exception
    {
        RPN2Compiler compiler = null;
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(1 - 2);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(-1, result.value);
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(2 - 1);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(1, result.value);
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(2 / 1);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(2, result.value);
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(1 / 2);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(0, result.value);
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return(1 - 2 * 12);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(-23, result.value);
    }
    
    
    @Test
    public void test_mathops_double () throws Exception
    {
        RPN2Compiler compiler = null;
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(1.0 - 2.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(-1.0, result.value);
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(2.0 - 1.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(1.0, result.value);
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(2.0 / 1.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(2.0, result.value);
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(1.0 / 2.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(0.5, result.value);
        
        compiler = getCompiler("ARRAY");
        executor = compiler.compile("return(1.0 - 2.0 * 12.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(-23.0, result.value);
    }
    
    
    @Test
    public void test_mathops_mixed () throws Exception
    {
        RPN2Compiler compiler = null;
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        compiler = getCompiler("SWITCH");
        executor = compiler.compile("return 1 - 2.0 * 12;");
        result = executor.execute(null, null, true);
        Assert.assertEquals(-23.0, result.value);
    }
    
    @Test
    public void test_equals () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(true);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(true == false);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1 == 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1 == 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1.0 == 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return('hello' == 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
    @Test
    public void test_notequals () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(true != false);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1 != 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1 != 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1.0 != 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return('hello' != 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
    }
    
    @Test
    public void test_greater () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 > 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1 > 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1.0 > 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return('hello' > 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
    @Test
    public void test_greaterequal () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 >= 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1 >= 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return(1.0 >= 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        executor = compiler.compile("return('hello' >= 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
    @Test
    public void test_less () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 < 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1 < 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1.0 < 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return('hello' < 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
    }
    
    @Test
    public void test_lessequal () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 <= 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1 <= 3.0);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(1.0 <= 3);");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return('hello' <= 'world');");
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        executor = compiler.compile("return(!('hello' <= 'world'));");
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
    @Test
    public void test_declare_and_assign () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "boolean hello = true;"
                + "hello = false;"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        test = ""
                + "int hello = 123;"
                + "hello = 12;"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(12, result.value);
        
        test = ""
                + "double hello = 123;"
                + "hello = 2.5;"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(2.5, result.value);
        
        test = ""
                + "string hello = \"123\";"
                + "hello = 'something';"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals("something", result.value);
        
        test = ""
                + "var hello = \"123\";"
                + "hello = false;"
                + "hello = true;"
                + "hello = 123;"
                + "hello = 123.0;"
                + "hello = 'something';"
                + "hello = '124';"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals("124", result.value);
    }
    
    @Test
    public void test_bool_assign () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "boolean hello = true;"
                + "hello = false;"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
    @Test
    public void test_ifstatement () throws Exception
    {
        RPN2Compiler compiler = getCompiler("ARRAY");
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "boolean hello = true;"
                + "if (hello)"
                + "{"
                + " hello = false;"
                + "}"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        test = ""
                + "boolean hello = true;"
                + "if (!hello)"
                + "{"
                + " hello = false;"
                + "}"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(true, result.value);
        
        test = ""
                + "boolean hello = true;"
                + "if (!hello)"
                + "{"
                + " hello = true;"
                + "}"
                + "else"
                + "{"
                + " hello = false;"
                + "}"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
        
        test = ""
                + "var hello = 12345678;"
                + "if (!(hello > 123))"
                + "{"
                + " hello = true;"
                + "}"
                + "else if (hello > 123)"
                + "{"
                + " hello = false;"
                + "}"
                + "return(hello);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, true);
        Assert.assertEquals(false, result.value);
    }
    
//    @Test
//    public void test_fibinacci_whatever() throws Exception
//    {
//        RPN2Compiler compiler = getCompiler("ARRAY");
//        ExpressionExecutor executor = null;
//        TokenPair result = null;
//        String test = null;
//        
//        test = ""
//                + "total = 0;"
//                + "minus1 = 1;"
//                + "minus2 = 1;"
//                + "for(i = 0; i < 15; i += 1)"
//                + "{"
//                + " total = minus1 + minus2;"
//                + " minus2 = minus1;"
//                + " minus1 = total;"
//                + " echoln('count so far: ' + total);"
//                + "}"
//                + "return(total);";
//        executor = compiler.compile(test);
//        result = executor.execute(null, null, false);
//        Assert.assertNotNull(result);
//        Assert.assertEquals(1597, result.value);
//    }
}
