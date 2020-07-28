/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn;

import com.rf.fled.script.tokenizer.InfixToRPNConverter;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class InfixToRPNConverterTest {
    
    public InfixToRPNConverterTest() {
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

    /**
     * Test of convert method, of class InfixToRPNConverter.
     */
//    @Test
    public void testConvert() throws Exception 
    {
        String test = "string = 'hello ' + 'world'";
        ArrayList<TokenPair> tokens = (new Tokenizer(new MathFunctionSet())).tokenize(test);
        List<TokenPair> statement = (new InfixToRPNConverter(new MathFunctionSet())).convert(tokens);
        
        System.out.println(statement.toString());
    }
            
    @Test
    public void testConvert_2() throws Exception 
    {
        String test = "stuff = 1 + 2 * 5";
        ArrayList<TokenPair> tokens = (new Tokenizer(new MathFunctionSet())).tokenize(test);
        List<TokenPair> statement = (new InfixToRPNConverter(new MathFunctionSet())).convert(tokens);
        
        System.out.println(statement.toString());
    }
}
