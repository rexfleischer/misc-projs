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
public class Declare_Boolean implements ICompilingDeclaration
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
        if (value.type != ValueType.BOOLEAN &&
                value.type != ValueType.VARIABLE)
        {
            throw new ByteScriptSyntaxException("illegal value of a boolean");
        }
        
        if (statement.get(position++).type != ValueType.SEPERATOR)
        {
            throw new ByteScriptSyntaxException("statements must end with a ';'");
        }
        
        if (value.type == ValueType.BOOLEAN)
        {
            writer.writeOperation(ByteCodeOperation.PUSH_H);
            writer.writeBoolean((Boolean) value.value);
        }
        else
        {
            writer.writePushVariable(value.getValueAsString());
        }
        if (isReference)
        {
            writer.writeOperation(ByteCodeOperation.DECLR_B);
        }
        else
        {
            writer.writeOperation(ByteCodeOperation.DECL_B);
        }
        writer.writeString(name.getValueAsString());
        try
        {
            environment.declareVariable(name.getValueAsString(), 
                                        new Value(ValueType.BOOLEAN, null));
        }
        catch (ByteScriptRuntimeException ex)
        {
            throw new ByteScriptSyntaxException(
                    "error occurred while setting a variable", ex);
        }
        
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
