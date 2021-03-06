
package com.rf.bytescript.util;

import com.rf.bytescript.compiler.ICompiler;
import com.rf.bytescript.environment.CompilingEnvironment;
import com.rf.bytescript.environment.ICompilingAssignment;
import com.rf.bytescript.environment.ICompilingDeclaration;
import com.rf.bytescript.environment.ICompilingOperation;
import com.rf.bytescript.environment.IFunction;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.exception.UndefinedFunctionException;
import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.tokenizer.InfixToRPNConverter;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public class ByteCodeWriter
{
    private static final int BYTES_TO_NEXT = 516;
    
    private LinkedList<ByteArray> bytes;
    
    private HashMap<String, Integer> tags;
    
    private HashMap<String, ArrayList<Integer>> tags_temp;
 
    private int size;
    
    private InfixToRPNConverter converter;
    
    public ByteCodeWriter(InfixToRPNConverter converter)
    {
        bytes = new LinkedList<ByteArray>();
        bytes.add(new ByteArray(BYTES_TO_NEXT));
        tags = new HashMap<String, Integer>();
        tags_temp = new HashMap<String, ArrayList<Integer>>();
        this.converter = converter;
    }
    
    public void setTag_Permanent(String tagName)
    {
        tags.put(tagName, size + bytes.getLast().compacityUsed());
        writeOperation(ByteCodeOperation.NO_OP);
    }
    
    public void writeGotoTag(String tagName)
    {
        Integer position = tags.get(tagName);
        if (position == null)
        {
            throw new IllegalArgumentException("unknown tag: " + tagName);
        }
        writeGoto(position, false);
    }
    
    public void setTag_Temp(String tagName)
    {
        /**
         * we need to make room
         */
        if (tags_temp.containsKey(tagName))
        {
            tags_temp.get(tagName).add(size + bytes.getLast().compacityUsed());
        }
        else
        {
            ArrayList<Integer> insert = new ArrayList<Integer>();
            insert.add(size + bytes.getLast().compacityUsed());
            tags_temp.put(tagName, insert);
        }
        writeGoto(-1, false);
    }
    
    public void resolveTag(String tagName) 
            throws ByteScriptSyntaxException
    {
        ArrayList<Integer> resolving = tags_temp.get(tagName);
        if (resolving == null)
        {
            throw new IllegalArgumentException(tagName + " was not a found tag");
        }
        
        int gotoPosition = bytes.getLast().compacityUsed();
        bytes.getLast().writeByte(ByteCodeOperation.NO_OP.getCode(), gotoPosition);
        gotoPosition += size;
        Iterator<ByteArray> it = bytes.iterator();
        ByteArray curr = it.next();
        int counter = 0;
        for(Integer position : resolving)
        {
            while (position > (counter + curr.compacityUsed()))
            {
                if (it.hasNext())
                {
                    curr = it.next();
                    counter += curr.compacityUsed();
                }
                else
                {
                    throw new ByteScriptSyntaxException("cannot resolve all tags");
                }
            }
            curr.writeByte(ByteCodeOperation.GOTO.getCode(), position - counter);
            curr.writeInt(gotoPosition, position - counter + 1);
        }
    }
    
    public void writeOperation(ByteCodeOperation operation)
    {
        ByteArray array = bytes.getLast();
        array.writeByte(operation.getCode(), array.compacityUsed());
        nextCheck(array);
    }
    
    public void writeNull()
    {
        ByteArray array = bytes.getLast();
        array.writeByte(ByteCodeOperation.TYPE_N.getCode(), array.compacityUsed());
        nextCheck(array);
    }
    
    public void writeBoolean(boolean src)
    {
        ByteArray array = bytes.getLast();
        array.writeByte(ByteCodeOperation.TYPE_B.getCode(), array.compacityUsed());
        array.writeBoolean(src, array.compacityUsed());
        nextCheck(array);
    }
    
    public void writeInteger(int src)
    {
        ByteArray array = bytes.getLast();
        array.writeByte(ByteCodeOperation.TYPE_I.getCode(), array.compacityUsed());
        array.writeInt(src, array.compacityUsed());
        nextCheck(array);
    }
    
    public void writeDouble(double src)
    {
        ByteArray array = bytes.getLast();
        array.writeByte(ByteCodeOperation.TYPE_D.getCode(), array.compacityUsed());
        array.writeLong(Double.doubleToLongBits(src), array.compacityUsed());
        nextCheck(array);
    }
    
    public void writeString(String src)
    {
        ByteArray last = this.bytes.getLast();
        last.writeByte(ByteCodeOperation.TYPE_S.getCode(), last.compacityUsed());
        byte[] sbytes = src.getBytes(Charset.forName("UTF-8"));
        last.writeInt(sbytes.length, last.compacityUsed());
        last.write(sbytes, last.compacityUsed());
        nextCheck(last);
    }
    
    public void writeGoto(int dest, boolean relative)
    {
        ByteArray last = this.bytes.getLast();
        if (relative)
        {
            last.writeByte(ByteCodeOperation.GOTO_R.getCode(), last.compacityUsed());
        }
        else
        {
            last.writeByte(ByteCodeOperation.GOTO.getCode(), last.compacityUsed());
        }
        last.writeInt(dest, last.compacityUsed());
        nextCheck(last);
    }

    public void writeStatement(
            List<Value> infix, 
            CompilingEnvironment enviornment) 
            throws  ByteScriptSyntaxException, 
                    UndefinedFunctionException,
                    UndefinedVariableException
    {
        List<Value> convert = converter.convert(infix);
        /**
         * basically, with each statement, it tries to go through as if
         * the program is running so it can try to do type hinting.
         */
        ICompilingOperation lastFunction = null;
        ByteArray last = this.bytes.getLast();
        for(int i = 0; i < convert.size(); i++)
        {
            Value token = convert.get(i);
            
            switch(token.type)
            {
                case BOOLEAN:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeBoolean((Boolean) token.value);
                    enviornment.push(new Value(ValueType.BOOLEAN, null));
                }
                break;
                    
                case INTEGER:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeInteger((Integer) token.value);
                    enviornment.push(new Value(ValueType.INTEGER, null));
                }
                break;
                    
                case DOUBLE:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeDouble((Double) token.value);
                    enviornment.push(new Value(ValueType.DOUBLE, null));
                }
                break;
                    
                case STRING:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeString((String) token.value);
                    enviornment.push(new Value(ValueType.STRING, null));
                }
                break;
                    
                case VARIABLE:
                {
                    writePushVariable(token.getValueAsString());
                    enviornment.push(enviornment.getVariable(token.getValueAsString()));
                }
                break;
                    
                case FUNCTION:
                {
                    String name = token.getValueAsString();
                    if (name.contains("."))
                    {
                        throw new UnsupportedOperationException();
                        // object function call
                    }
                    else
                    {
                        IFunction function  = enviornment
                                .langFunctions
                                .getVariableSafe(name);
                        if (function != null)
                        {
                            ICompilingOperation cfunc = 
                                    (ICompilingOperation) function;
                            if (cfunc.getType() != ValueType.FUNCTION)
                            {
                                throw new UndefinedFunctionException(
                                        name + " is not a defined function");
                            }
                            if (cfunc.isAlwaysLast())
                            {
                                lastFunction = cfunc;
                            }
                            else
                            {
                                cfunc.compile(enviornment, this);
                            }
                        }
                        else
                        {
                            function = enviornment.getSystemFunctionSafe(name);
                            if (function != null)
                            {
                                writeOperation(ByteCodeOperation.CALL_S);
                                writeString(name);
                            }
                            else
                            {
                                writeOperation(ByteCodeOperation.CALL_C);
                                writeString(name);
                            }
                        }
                    }
                }
                break;
                    
                case OPERATOR:
                {
                    String name = token.getValueAsString();
                    IFunction function = enviornment
                                .langFunctions
                                .getVariableSafe(name);
                    if (!(function instanceof ICompilingOperation))
                    {
                        throw new ByteScriptSyntaxException("");
                    }
                    ICompilingOperation operation = (ICompilingOperation) function;
                    if (operation.isAlwaysLast())
                    {
                        lastFunction = operation;
                    }
                    else
                    {
                        operation.compile(enviornment, this);
                    }
                }
                break;
                    
                default:
                    throw new ByteScriptSyntaxException(
                            "unable to parse: invalid type for bytecode writing");
            }
        }
        if (lastFunction != null)
        {
            lastFunction.compile(enviornment, this);
        }
        nextCheck(last);
    }
    
    public byte[] finished()
    {
        int totalSize = size + bytes.getLast().compacityUsed();
        byte[] result = new byte[totalSize];
        int position = 0;
        for(ByteArray array : bytes)
        {
            System.arraycopy(array.array(), 0, result, position, array.compacityUsed());
            position += array.compacityUsed();
        }
        if (position != totalSize)
        {
            throw new IllegalArgumentException("something went wrong");
        }
        return result;
    }
    
    private void nextCheck(ByteArray last)
    {
        if (last.compacityUsed() > BYTES_TO_NEXT)
        {
            size += last.compacityUsed();
            bytes.add(new ByteArray(BYTES_TO_NEXT));
        }
    }

    public void writeConversion (ByteCodeOperation conversion, int depth)
    {
        if (depth == 1 || depth == 0)
        {
            ByteArray array = bytes.getLast();
            array.writeByte(conversion.getCode(), array.compacityUsed());
            array.writeByte((byte)depth, array.compacityUsed());
            nextCheck(array);
        }
        else 
        {
            throw new IllegalArgumentException("depth can only be 1 or 0");
        }
    }
    
    public void writePushVariable(String name)
    {
        writeOperation(ByteCodeOperation.PUSH_V);
        writeString(name);
    }
    
    public void writeSetVariable(String name)
    {
        writeOperation(ByteCodeOperation.ASSIGN);
        writeString(name);
    }

    public int writeDeclaration (
            List<Value> infix,
            int position, 
            ICompiler compiler,
            CompilingEnvironment enviornment) 
            throws  ByteScriptSyntaxException, 
                    UndefinedVariableException
    {
        IFunction function = enviornment
                .langFunctions
                .getVariable(infix.get(position).getValueAsString());
        ICompilingDeclaration declaration = (ICompilingDeclaration) function;
        return declaration.compile(enviornment, this, infix, compiler, position);
    }

    public void writeAssignment (
            List<Value> statement,
            Value assignment,
            CompilingEnvironment enviornment)
            throws  ByteScriptSyntaxException, 
                    UndefinedVariableException
    {
        IFunction function = enviornment
                .langFunctions
                .getVariable(assignment.getValueAsString());
        ICompilingAssignment declaration = (ICompilingAssignment) function;
        declaration.compile(enviornment, this, statement);
    }

    public void writePushStack (boolean isFuncCall)
    {
        writeOperation(ByteCodeOperation.ST_PUS);
        bytes.getLast().writeBoolean(isFuncCall, bytes.getLast().compacityUsed());
    }

    public void writePopStack (boolean isFuncCall)
    {
        writeOperation(ByteCodeOperation.ST_POP);
        bytes.getLast().writeBoolean(isFuncCall, bytes.getLast().compacityUsed());
    }
}
