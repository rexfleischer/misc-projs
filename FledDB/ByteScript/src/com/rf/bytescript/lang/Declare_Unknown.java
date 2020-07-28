/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.lang;

import com.rf.bytescript.compiler.ICompiler;
import com.rf.bytescript.environment.CompilingEnvironment;
import com.rf.bytescript.environment.ICompilingDeclaration;
import com.rf.bytescript.exception.ByteScriptRuntimeException;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeOperation;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
import java.util.List;

/**
 *
 * @author REx
 */
public class Declare_Unknown implements ICompilingDeclaration
{
    @Override
    public int compile (
            CompilingEnvironment environment, 
            ByteCodeWriter writer, 
            List<Value> statement, 
            ICompiler compiler,
            int position) 
            throws ByteScriptSyntaxException
    {
        if (statement.size() < position + 4)
        {
            throw new ByteScriptSyntaxException("unable to finish declaration");
        }
        int start = position;
        
        if (statement.get(position++).type != ValueType.DECLARATION)
        {
            throw new ByteScriptSyntaxException("illegal delcaration of a boolean");
        }
        
        boolean isReference = false;
        if (statement.get(position + 1).type == ValueType.OPERATOR &&
                statement.get(position + 1).getValueAsString().equals("*"))
        {
            isReference = true;
            position++;
        }
        
        Value name = statement.get(position++);
        if (name.type != ValueType.VARIABLE)
        {
            throw new ByteScriptSyntaxException("illegal delcaration of a boolean");
        }
        
        Value operator = statement.get(position++);
        if (operator.type != ValueType.ASSIGNMENT ||
                !operator.getValueAsString().equals("="))
        {
            throw new ByteScriptSyntaxException("illegal delcaration of a boolean");
        }
        
        Value value = statement.get(position++);
        if (value.type != ValueType.NULL &&
            value.type != ValueType.BOOLEAN &&
            value.type != ValueType.INTEGER &&
            value.type != ValueType.DOUBLE &&
            value.type != ValueType.STRING &&
            value.type != ValueType.VARIABLE)
        {
            throw new ByteScriptSyntaxException("illegal value of a boolean");
        }
        
        if (statement.get(position++).type != ValueType.SEPERATOR)
        {
            throw new ByteScriptSyntaxException("statements must end with a ';'");
        }
        
        try
        {
            switch(value.type)
            {
                case NULL:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeNull();
                    environment.push(new Value(ValueType.NULL, null));
                    break;
                case BOOLEAN:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeBoolean((Boolean) value.value);
                    environment.push(new Value(ValueType.BOOLEAN, null));
                    break;
                case INTEGER:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeInteger((Integer) value.value);
                    environment.push(new Value(ValueType.INTEGER, null));
                    break;
                case DOUBLE:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeDouble((Double) value.value);
                    environment.push(new Value(ValueType.DOUBLE, null));
                    break;
                case STRING:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeString((String) value.value);
                    environment.push(new Value(ValueType.STRING, null));
                    break;

    //            case FUNCTION:
    //                
    //                break;

                case VARIABLE:
                    writer.writePushVariable(value.getValueAsString());
                    environment.push(environment.getVariable(value.getValueAsString()));
                    break;

                default:
                    throw new ByteScriptSyntaxException("cannot assign value");
            }
        }
        catch(Exception ex)
        {
            throw new ByteScriptSyntaxException("was unable to assign value", ex);
        }
        
        try
        {
            if (isReference)
            {
                writer.writeOperation(ByteCodeOperation.DECLR_U);
                environment.declareReference(name.getValueAsString(), 
                                             environment.pop());
            }
            else
            {
                writer.writeOperation(ByteCodeOperation.DECL_U);
                environment.declareVariable(name.getValueAsString(), 
                                            environment.pop());
            }
        }
        catch(ByteScriptRuntimeException ex)
        {
            throw new ByteScriptSyntaxException("could not declare value", ex);
        }
        
        
        writer.writeString(name.getValueAsString());
        
        return position - start;
    }

    @Override
    public int getPresedence ()
    {
        return 0;
    }

    @Override
    public ValueType getType ()
    {
        return ValueType.DECLARATION;
    }
}
