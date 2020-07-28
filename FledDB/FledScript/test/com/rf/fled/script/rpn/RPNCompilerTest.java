/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn;

import com.rf.fled.script.ExecutorFactory;
import com.rf.fled.script.ExpressionCompiler;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.tokenizer.TokenPair;
import java.lang.reflect.InvocationTargetException;
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
public class RPNCompilerTest {
    
    public RPNCompilerTest() {
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
    
    public ExpressionCompiler getCompiler() 
            throws  NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    InvocationTargetException
    {
        return ExecutorFactory.RPN.getInstance(new MathFunctionSet(), new Properties());
    }
    
    @Test
    public void test_bench () throws Exception
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 - 2 * 12);");
        
        long start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i += 1)
        {
            result = executor.execute(null, null, true);
            Assert.assertEquals(-23, result.value);
        }
        System.out.println("first time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testCompile() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return(1 - 2)");
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1, result.value);
        
        executor = compiler.compile("1 - 2");
        result = executor.execute(null, null, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1, result.value);
    }

    @Test
    public void testCompile_string() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        
        executor = compiler.compile("return('hello ' + 'world')");
        result = executor.execute(null, null, false);
        Assert.assertEquals("hello world", result.value);
        
        executor = compiler.compile("'hello ' + 'world'");
        result = executor.execute(null, null, true);
        Assert.assertEquals("hello world", result.value);
        
        String test = ""
                + "string = 'hello ' + 'world';"
                + "echo(string);"
                + "return(string);";
        
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("hello world", result.value);
    }

    @Test
    public void testCompile_IfConstruct() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "string = 'not found';"
                + "if (string == 'not found')"
                + "{"
                + " string = 'found';"
                + "}"
                + "return(string);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("found", result.value);
    }

    @Test
    public void testCompile_IfConstructComplex() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "string = 'not found';"
                + "if (string == 'not found')"
                + "{"
                + " if (1 == 2)"
                + " {"
                + "     return('1 == 2 ????');"
                + " }"
                + " string = 'found';"
                + "}"
                + "return(string);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("found", result.value);
    }

    @Test
    public void testCompile_IfElseConstruct() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "if (1 == 1)"
                + "{"
                + " return('1 == 1');"
                + "}"
                + "else"
                + "{"
                + " return('1 != 1');"
                + "}";
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("1 == 1", result.value);
        
        test = ""
                + "if (1 == 2)"
                + "{"
                + " return('1 == 2');"
                + "}"
                + "else"
                + "{"
                + " return('1 != 2');"
                + "}";
        executor = compiler.compile(test);
//        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("1 != 2", result.value);
    }

    @Test
    public void testCompile_IfElseConstructComplex() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "stuff = 1 + 2 * 5;"
                + "if (stuff == 1)"
                + "{"
                + " return('stuff == 1');"
                + "}"
                + "else"
                + "{"
                + " if (stuff == 11)"
                + " {"
                + "     return('stuff == 11');"
                + " }"
                + " else"
                + " {"
                + "     return('stuff != 11');"
                + " }"
                + "}";
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("stuff == 11", result.value);
        
        test = ""
                + "stuff = 1 + 2 * 5;"
                + "if (stuff == 1)"
                + "{"
                + " stuff = 'stuff == 1';"
                + "}"
                + "else"
                + "{"
                + " if (stuff == 11)"
                + " {"
                + "     stuff = 'stuff == 11';"
                + " }"
                + " else"
                + " {"
                + "     stuff = 'stuff != 11';"
                + " }"
                + "}"
                + "return(stuff);";
        executor = compiler.compile(test);
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("stuff == 11", result.value);
    }

    @Test
    public void testCompile_ForLoop() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "counter = 0;"
                + "for(i = 0; i < 10; i = i + 1)"
                + "{"
                + " counter = counter + 1;"
                + " echoln(counter);"
                + "}"
                + "return(counter);";
        executor = compiler.compile(test);
//        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.value);
    }

    @Test
    public void testCompile_ElseIfChain() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "counter = 0;"
                + "if(counter == 1)"
                + "{"
                + " return('what the freak!');"
                + "}"
                + "else if(counter == 0)"
                + "{"
                + " return('sweet!');"
                + "}";
        executor = compiler.compile(test);
//        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("sweet!", result.value);
    }

    @Test
    public void testCompile_Scope() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "counter = 0;"
                + "{"
                + " counter += 1;"
                + " echoln(counter);"
                + "}"
                + "return(counter);";
        executor = compiler.compile(test);
//        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.value);
    }

    @Test
    public void testCompile_ForLoopComplex() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "counter = 0;"
                + "for(i = 0; i < 5; i += 1)"
                + "{"
                + " if (i % 2 == 0)"
                + " {"
                + "     counter += 1;"
                + "     for(j = 0; j < 2; j += 1)"
                + "     {"
                + "         counter += 1;"
                + "     }"
                + " }"
                + "}"
                + "return(counter);";
        executor = compiler.compile(test);
//        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(9, result.value);
    }

    @Test
    public void testCompile_FibSquence() throws Exception 
    {
        ExpressionCompiler compiler = getCompiler();
        ExpressionExecutor executor = null;
        TokenPair result = null;
        String test = null;
        
        test = ""
                + "total = 0;"
                + "minus1 = 1;"
                + "minus2 = 1;"
                + "for(i = 0; i < 15; i += 1)"
                + "{"
                + " total = minus1 + minus2;"
                + " minus2 = minus1;"
                + " minus1 = total;"
                + " echoln('count so far: ' + total);"
                + "}"
                + "return(total);";
        executor = compiler.compile(test);
        System.out.println(((RPNExecutor) executor).dumpCode());
        result = executor.execute(null, null, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(1597, result.value);
    }
}
