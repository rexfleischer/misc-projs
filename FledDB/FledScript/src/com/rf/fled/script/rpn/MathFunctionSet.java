
package com.rf.fled.script.rpn;

import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.rpn.lang.Construct_Else;
import com.rf.fled.script.rpn.lang.Construct_For;
import com.rf.fled.script.rpn.lang.Construct_If;
import com.rf.fled.script.rpn.lang.Function_Cos;
import com.rf.fled.script.rpn.lang.Function_Echo;
import com.rf.fled.script.rpn.lang.Function_EchoLn;
import com.rf.fled.script.rpn.lang.Function_Power;
import com.rf.fled.script.rpn.lang.Function_Return;
import com.rf.fled.script.rpn.lang.Function_Sin;
import com.rf.fled.script.rpn.lang.Function_Tan;
import com.rf.fled.script.rpn.lang.Math_Addition;
import com.rf.fled.script.rpn.lang.Math_Division;
import com.rf.fled.script.rpn.lang.Math_Modules;
import com.rf.fled.script.rpn.lang.Math_Multiply;
import com.rf.fled.script.rpn.lang.Math_Subtract;
import com.rf.fled.script.rpn.lang.Operator_Assignment;
import com.rf.fled.script.rpn.lang.Operator_Equals;
import com.rf.fled.script.rpn.lang.Operator_GreaterThan;
import com.rf.fled.script.rpn.lang.Operator_GreaterThanEqual;
import com.rf.fled.script.rpn.lang.Operator_LessThan;
import com.rf.fled.script.rpn.lang.Operator_LessThanEqual;
import com.rf.fled.script.rpn.lang.Operator_PlusEquals;
import java.util.List;

/**
 *
 * @author REx
 */
public class MathFunctionSet extends FunctionSet
{
    @Override
    protected void getFunctionList(List<FunctionPair> buffer) 
    {
        buffer.add(new FunctionPair("*", new Math_Multiply()));
        buffer.add(new FunctionPair("/", new Math_Division()));
        buffer.add(new FunctionPair("+", new Math_Addition()));
        buffer.add(new FunctionPair("-", new Math_Subtract()));
        buffer.add(new FunctionPair("%", new Math_Modules()));
        
        buffer.add(new FunctionPair("sin", new Function_Sin()));
        buffer.add(new FunctionPair("cos", new Function_Cos()));
        buffer.add(new FunctionPair("tan", new Function_Tan()));
        buffer.add(new FunctionPair("pow", new Function_Power()));
        buffer.add(new FunctionPair("echo", new Function_Echo()));
        buffer.add(new FunctionPair("echoln", new Function_EchoLn()));
        
        buffer.add(new FunctionPair("==", new Operator_Equals()));
        buffer.add(new FunctionPair("<", new Operator_LessThan()));
        buffer.add(new FunctionPair(">", new Operator_GreaterThan()));
        buffer.add(new FunctionPair("<=", new Operator_LessThanEqual()));
        buffer.add(new FunctionPair(">=", new Operator_GreaterThanEqual()));
        
        buffer.add(new FunctionPair("=", new Operator_Assignment()));
        buffer.add(new FunctionPair("+=", new Operator_PlusEquals()));
        buffer.add(new FunctionPair("return", new Function_Return()));
        
        buffer.add(new FunctionPair("if", new Construct_If()));
        buffer.add(new FunctionPair("else", new Construct_Else()));
        buffer.add(new FunctionPair("for", new Construct_For()));
    }
}
