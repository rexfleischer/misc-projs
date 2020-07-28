/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.lang;

import com.rf.bytescript.environment.CompilingEnvironment;
import com.rf.bytescript.environment.ICompilingAssignment;
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
public class Assignment_Equal implements ICompilingAssignment
{

    @Override
    public void compile (
            CompilingEnvironment enviornment, 
            ByteCodeWriter writer, 
            List<Value> statement) 
            throws ByteScriptSyntaxException
    {
        Value name = statement.get(0);
        if (name.type != ValueType.VARIABLE)
        {
            throw new ByteScriptSyntaxException("illegal assignment operation");
        }
        
        Value operator = statement.get(1);
        if (operator.type != ValueType.ASSIGNMENT ||
                !operator.getValueAsString().equals("="))
        {
            throw new ByteScriptSyntaxException("illegal delcaration of a boolean");
        }
        
        Value assignee = statement.get(2);
        try
        {
            switch(assignee.type)
            {
                case NULL:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeNull();
                    enviornment.setVariable(name.getValueAsString(), new Value(ValueType.NULL, null));
                    break;
                case BOOLEAN:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeBoolean((Boolean) assignee.value);
                    enviornment.setVariable(name.getValueAsString(), new Value(ValueType.BOOLEAN, null));
                    break;
                case INTEGER:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeInteger((Integer) assignee.value);
                    enviornment.setVariable(name.getValueAsString(), new Value(ValueType.INTEGER, null));
                    break;
                case DOUBLE:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeDouble((Double) assignee.value);
                    enviornment.setVariable(name.getValueAsString(), new Value(ValueType.DOUBLE, null));
                    break;
                case STRING:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeString((String) assignee.value);
                    enviornment.setVariable(name.getValueAsString(), new Value(ValueType.STRING, null));
                    break;

    //            case FUNCTION:
    //                
    //                break;

                case VARIABLE:
                    writer.writePushVariable(assignee.getValueAsString());
                    enviornment.setVariable(name.getValueAsString(), enviornment.getVariable(assignee.getValueAsString()));
                    break;

                default:
                    throw new ByteScriptSyntaxException("cannot assign value");
            }
        }
        catch(Exception ex)
        {
            throw new ByteScriptSyntaxException("was unable to assign value", ex);
        }
        
        writer.writeSetVariable(name.getValueAsString());
    }

    @Override
    public int getPresedence ()
    {
        return 0;
    }

    @Override
    public ValueType getType ()
    {
        return ValueType.ASSIGNMENT;
    }
    
}
