/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.lang.Assignment_Equal;
import com.rf.bytescript.lang.Compare_Equals;
import com.rf.bytescript.lang.Compare_GreaterThan;
import com.rf.bytescript.lang.Compare_GreaterThanEqual;
import com.rf.bytescript.lang.Compare_LessThan;
import com.rf.bytescript.lang.Compare_LessThanEqual;
import com.rf.bytescript.lang.Compare_Negate;
import com.rf.bytescript.lang.Compare_NotEquals;
import com.rf.bytescript.lang.Construct_If;
import com.rf.bytescript.lang.Declare_Boolean;
import com.rf.bytescript.lang.Declare_Double;
import com.rf.bytescript.lang.Declare_Integer;
import com.rf.bytescript.lang.Declare_String;
import com.rf.bytescript.lang.Declare_Unknown;
import com.rf.bytescript.lang.Function_Return;
import com.rf.bytescript.lang.Math_Add;
import com.rf.bytescript.lang.Math_Div;
import com.rf.bytescript.lang.Math_Mod;
import com.rf.bytescript.lang.Math_Mul;
import com.rf.bytescript.lang.Math_Sub;
import com.rf.bytescript.util.OrderedNamedValueArray;

/**
 *
 * @author REx
 */
public class FunctionSet_Lang extends OrderedNamedValueArray<IFunction>
{

    public FunctionSet_Lang ()
    {
        super(10);
        
        /**
         * add lang functions here
         */
        super.setVariable("*", new Math_Mul());
        super.setVariable("/", new Math_Div());
        super.setVariable("+", new Math_Add());
        super.setVariable("-", new Math_Sub());
        super.setVariable("%", new Math_Mod());
        
        super.setVariable("!",    new Compare_Negate());
        super.setVariable("==",   new Compare_Equals());
        super.setVariable("!=",   new Compare_NotEquals());
        super.setVariable("<",    new Compare_LessThan());
        super.setVariable(">",    new Compare_GreaterThan());
        super.setVariable("<=",   new Compare_LessThanEqual());
        super.setVariable(">=",   new Compare_GreaterThanEqual());
        
        super.setVariable("boolean",  new Declare_Boolean());
        super.setVariable("int",      new Declare_Integer());
        super.setVariable("double",   new Declare_Double());
        super.setVariable("string",   new Declare_String());
        super.setVariable("var",      new Declare_Unknown());
        
//        super.setVariable("++", new Operator_PlusEquals());
//        super.setVariable("--", new Operator_PlusEquals());
//        super.setVariable("+=", new Operator_PlusEquals());
        super.setVariable("=",        new Assignment_Equal());
        
        super.setVariable("return",   new Function_Return());
        super.setVariable("if",       new Construct_If());
//        super.setVariable("for", new Construct_For());
    }
    
    @Override
    public boolean setVariable(String name, IFunction value) 
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void removeVariable(String name) throws UndefinedVariableException
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addNamedValueSet(OrderedNamedValueArray<IFunction> adding)
    {
        throw new UnsupportedOperationException();
    }

}
