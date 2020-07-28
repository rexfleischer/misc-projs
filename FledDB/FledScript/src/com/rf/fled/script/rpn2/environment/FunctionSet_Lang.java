/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.language.FunctionSet.FunctionPair;
import com.rf.fled.script.rpn2.lang.Assignment_Equal;
import com.rf.fled.script.rpn2.lang.Compare_Equals;
import com.rf.fled.script.rpn2.lang.Compare_GreaterThan;
import com.rf.fled.script.rpn2.lang.Compare_GreaterThanEqual;
import com.rf.fled.script.rpn2.lang.Compare_LessThan;
import com.rf.fled.script.rpn2.lang.Compare_LessThanEqual;
import com.rf.fled.script.rpn2.lang.Compare_Negate;
import com.rf.fled.script.rpn2.lang.Compare_NotEquals;
import com.rf.fled.script.rpn2.lang.Construct_If;
import com.rf.fled.script.rpn2.lang.Declare_Boolean;
import com.rf.fled.script.rpn2.lang.Declare_Double;
import com.rf.fled.script.rpn2.lang.Declare_Integer;
import com.rf.fled.script.rpn2.lang.Declare_String;
import com.rf.fled.script.rpn2.lang.Declare_Unknown;
import com.rf.fled.script.rpn2.lang.Function_Return;
import com.rf.fled.script.rpn2.lang.Math_Add;
import com.rf.fled.script.rpn2.lang.Math_Div;
import com.rf.fled.script.rpn2.lang.Math_Mod;
import com.rf.fled.script.rpn2.lang.Math_Mul;
import com.rf.fled.script.rpn2.lang.Math_Sub;
import java.util.List;

/**
 *
 * @author REx
 */
public class FunctionSet_Lang extends FunctionSet
{

    @Override
    protected void getFunctionList (List<FunctionPair> buffer)
    {
        buffer.add(new FunctionPair("*", new Math_Mul()));
        buffer.add(new FunctionPair("/", new Math_Div()));
        buffer.add(new FunctionPair("+", new Math_Add()));
        buffer.add(new FunctionPair("-", new Math_Sub()));
        buffer.add(new FunctionPair("%", new Math_Mod()));
        
        buffer.add(new FunctionPair("!",    new Compare_Negate()));
        buffer.add(new FunctionPair("==",   new Compare_Equals()));
        buffer.add(new FunctionPair("!=",   new Compare_NotEquals()));
        buffer.add(new FunctionPair("<",    new Compare_LessThan()));
        buffer.add(new FunctionPair(">",    new Compare_GreaterThan()));
        buffer.add(new FunctionPair("<=",   new Compare_LessThanEqual()));
        buffer.add(new FunctionPair(">=",   new Compare_GreaterThanEqual()));
        
        buffer.add(new FunctionPair("boolean",  new Declare_Boolean()));
        buffer.add(new FunctionPair("int",      new Declare_Integer()));
        buffer.add(new FunctionPair("double",   new Declare_Double()));
        buffer.add(new FunctionPair("string",   new Declare_String()));
        buffer.add(new FunctionPair("var",      new Declare_Unknown()));
        
//        buffer.add(new FunctionPair("++", new Operator_PlusEquals()));
//        buffer.add(new FunctionPair("--", new Operator_PlusEquals()));
//        buffer.add(new FunctionPair("+=", new Operator_PlusEquals()));
        buffer.add(new FunctionPair("=",        new Assignment_Equal()));
        
        buffer.add(new FunctionPair("return",   new Function_Return()));
        buffer.add(new FunctionPair("if",       new Construct_If()));
//        buffer.add(new FunctionPair("for", new Construct_For()));
    }
}
