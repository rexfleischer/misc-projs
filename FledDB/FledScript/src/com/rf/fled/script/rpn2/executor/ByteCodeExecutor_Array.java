/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.executor;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledScriptInitException;
import com.rf.fled.script.UndefinedVariableException;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeReader;
import com.rf.fled.script.rpn2.util.Value;

/**
 *
 * @author REx
 */
public class ByteCodeExecutor_Array implements IByteCodeExecutor
{
    private static Operation[] ops;
    
    private static interface Operation
    {
        public void op(OperationState state)
                throws FledExecutionException;
    }
    
    private class OperationState
    {
        int i = 0;
        Value result = null;
        boolean keepGoing = true;
        ExecutionEnvironment enviornment;
        ByteCodeReader function;
        Value temp;
    }
    
    static
    {
        ops = new Operation[0x7f + 1];
        
        
        //<editor-fold defaultstate="collapsed" desc="no op">
        ops[ByteCodeOperation.NO_OP.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="basic math">
        ops[ByteCodeOperation.ADD_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.INTEGER,
                        left.getValueAsInteger() + right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.SUB_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.INTEGER,
                        left.getValueAsInteger() - right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.MUL_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.INTEGER,
                        left.getValueAsInteger() * right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.DIV_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.INTEGER,
                        left.getValueAsInteger() / right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.MOD_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.INTEGER,
                        left.getValueAsInteger() % right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.ADD_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                        left.getValueAsDouble() + right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.SUB_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                        left.getValueAsDouble() - right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.MUL_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                        left.getValueAsDouble() * right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.DIV_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                        left.getValueAsDouble() / right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.ADD_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.STRING,
                        left.getValueAsString() + right.getValueAsString()));
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="conditionals">
        ops[ByteCodeOperation.NEG_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                state.enviornment.push(new Value(
                        Value.ValueType.BOOLEAN, 
                        !((Boolean)state.enviornment.pop().value)));
            }
        };
        ops[ByteCodeOperation.EQ_N.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        (left.value == null) && (right.value == null)));
            }
        };
        ops[ByteCodeOperation.EQ_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsBoolean() == right.getValueAsBoolean()));
            }
        };
        ops[ByteCodeOperation.EQ_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() == right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.EQ_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0.00003 >= Math.abs(left.getValueAsDouble() - right.getValueAsDouble())));
            }
        };
        ops[ByteCodeOperation.EQ_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsString().equals(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.EQ_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.EQ_O.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.NEQ_N.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        (left.value != null) || (right.value != null)));
            }
        };
        ops[ByteCodeOperation.NEQ_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsBoolean() != right.getValueAsBoolean()));
            }
        };
        ops[ByteCodeOperation.NEQ_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() != right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.NEQ_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0.00003 <= Math.abs(left.getValueAsDouble() - right.getValueAsDouble())));
            }
        };
        ops[ByteCodeOperation.NEQ_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        !left.getValueAsString().equals(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.NEQ_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.NEQ_O.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.LT_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() < right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.LT_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsDouble() < right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.LT_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0 > left.getValueAsString().compareTo(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.LT_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.GT_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() > right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.GT_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsDouble() > right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.GT_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0 < left.getValueAsString().compareTo(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.GT_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.LTE_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() <= right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.LTE_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsDouble() <= right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.LTE_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0 >= left.getValueAsString().compareTo(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.LTE_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        ops[ByteCodeOperation.GTE_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsInteger() >= right.getValueAsInteger()));
            }
        };
        ops[ByteCodeOperation.GTE_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        left.getValueAsDouble() >= right.getValueAsDouble()));
            }
        };
        ops[ByteCodeOperation.GTE_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value right = state.enviornment.pop();
                Value left = state.enviornment.pop();
                state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                        0 <= left.getValueAsString().compareTo(right.getValueAsString())));
            }
        };
        ops[ByteCodeOperation.GTE_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException();
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="stack pushes">
        ops[ByteCodeOperation.PUSH_H.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value value = new Value(null, null);
                state.i += state.function.readValue(state.i + 1, value);
                state.enviornment.push(value);
            }
        };
        ops[ByteCodeOperation.PUSH_V.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value value = new Value(null, null);
                state.i += state.function.readValue(state.i + 1, value);
                try
                {
                    state.enviornment.push(
                            state.enviornment.getVariable(value.getValueAsString()));
                }
                catch (UndefinedVariableException ex)
                {
                    throw new FledExecutionException(null, ex);
                }
            }
        };
        ops[ByteCodeOperation.PUSH_R.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="declerations">
        ops[ByteCodeOperation.DECL_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.BOOLEAN)
                {
                    throw new FledExecutionException(
                            "type mismatch: boolean was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.BOOLEAN, var);
            }
        };
        ops[ByteCodeOperation.DECL_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.INTEGER)
                {
                    throw new FledExecutionException(
                            "type mismatch: integer was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.INTEGER, var);
            }
        };
        ops[ByteCodeOperation.DECL_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.DOUBLE)
                {
                    throw new FledExecutionException(
                            "type mismatch: double was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.DOUBLE, var);
            }
        };
        ops[ByteCodeOperation.DECL_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.STRING)
                {
                    throw new FledExecutionException(
                            "type mismatch: string was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.STRING, var);
            }
        };
        ops[ByteCodeOperation.DECL_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.UNKNOWN, var);
            }
        };
        ops[ByteCodeOperation.DECL_O.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.OBJECT)
                {
                    throw new FledExecutionException(
                            "type mismatch: boolean was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareVariable(
                        state.temp.getValueAsString(), Value.ValueType.OBJECT, var);
            }
        };
        ops[ByteCodeOperation.DECLR_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.BOOLEAN)
                {
                    throw new FledExecutionException(
                            "type mismatch: boolean was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.UNKNOWN, var);
            }
        };
        ops[ByteCodeOperation.DECLR_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.INTEGER)
                {
                    throw new FledExecutionException(
                            "type mismatch: boolean was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.INTEGER, var);
            }
        };
        ops[ByteCodeOperation.DECLR_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.DOUBLE)
                {
                    throw new FledExecutionException(
                            "type mismatch: double was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.DOUBLE, var);
            }
        };
        ops[ByteCodeOperation.DECLR_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.STRING)
                {
                    throw new FledExecutionException(
                            "type mismatch: string was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.STRING, var);
            }
        };
        ops[ByteCodeOperation.DECLR_U.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.UNKNOWN, var);
            }
        };
        ops[ByteCodeOperation.DECLR_O.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value var = state.enviornment.pop();
                if (var.type != Value.ValueType.OBJECT)
                {
                    throw new FledExecutionException(
                            "type mismatch: object was supposed to be defined,"
                            + " but value in stack was " + var);
                }
                state.i += state.function.readValue(state.i + 1, state.temp);
                state.enviornment.declareReference(
                        state.temp.getValueAsString(), Value.ValueType.OBJECT, var);
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="assigns">
        ops[ByteCodeOperation.ASSIGN.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value value = state.enviornment.pop();
                state.i += state.function.readValue(state.i + 1, state.temp);
                try
                {
                    state.enviornment.setVariable(state.temp.getValueAsString(), value);
                }
                catch (UndefinedVariableException ex)
                {
                    throw new FledExecutionException(
                            "error occurred whil assigning a boolean", ex);
                }
            }
        };
        //</editor-fold>
            
        
        //<editor-fold defaultstate="collapsed" desc="conversions">
        ops[ByteCodeOperation.I_TO_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                            ((Integer)state.enviornment.pop().value).doubleValue()));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.DOUBLE,
                            ((Integer)state.enviornment.pop().value).doubleValue()));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.I_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Integer)state.enviornment.pop().value).toString()));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Integer)state.enviornment.pop().value).toString()));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.D_TO_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.INTEGER,
                            ((Double)state.enviornment.pop().value).intValue()));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.INTEGER,
                            ((Double)state.enviornment.pop().value).intValue()));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.D_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Double)state.enviornment.pop().value).toString()));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Double)state.enviornment.pop().value).toString()));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.B_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Boolean)state.enviornment.pop().value).toString()));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.STRING,
                            ((Boolean)state.enviornment.pop().value).toString()));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.O_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException(
                        "canot convert object to string yet");
            }
        };
        ops[ByteCodeOperation.N_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.STRING, "null"));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.STRING, "null"));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.R_TO_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ops[ByteCodeOperation.S_TO_B.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Boolean.parseBoolean(((String)state.enviornment.pop().value))));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Boolean.parseBoolean(((String)state.enviornment.pop().value))));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.S_TO_I.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Integer.parseInt(((String)state.enviornment.pop().value))));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Integer.parseInt(((String)state.enviornment.pop().value))));
                    state.enviornment.push(first);
                }
            }
        };
        ops[ByteCodeOperation.S_TO_D.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                if (depth == 0)
                {
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Double.parseDouble(((String)state.enviornment.pop().value))));
                }
                else
                {
                    Value first = state.enviornment.pop();
                    state.enviornment.push(new Value(Value.ValueType.BOOLEAN,
                            Double.parseDouble(((String)state.enviornment.pop().value))));
                    state.enviornment.push(first);
                }
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="scope push and pop">
        ops[ByteCodeOperation.ST_PUS.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                try
                {
                    if (depth == 0)
                    {
                        state.enviornment.pushScope(false);
                    }
                    else
                    {
                        state.enviornment.pushScope(true);
                    }
                }
                catch (FledScriptInitException ex)
                {
                    throw new FledExecutionException("unable to push stack", ex);
                }
            }
        };
        ops[ByteCodeOperation.ST_POP.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int depth = state.function.getCode(++state.i);
                try
                {
                    if (depth == 1)
                    {
                        state.enviornment.popScope(true);
                    }
                    else
                    {
                        state.enviornment.popScope(false);
                    }
                }
                catch (FledExecutionException ex)
                {
                    throw new FledExecutionException("unable to pop stack", ex);
                }
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="function calls">
        ops[ByteCodeOperation.CALL_C.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ops[ByteCodeOperation.CALL_O.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ops[ByteCodeOperation.CALL_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="goto statement">
        ops[ByteCodeOperation.GOTO.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                int where = state.function.getAllCode().readInt(state.i + 1);
                state.i = where - 1;
            }
        };
        ops[ByteCodeOperation.GOTO_R.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ops[ByteCodeOperation.BRACH.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                Value statement = state.enviornment.pop();
                if (statement.getValueAsBoolean())
                {
                    /**
                     * just bypass the statement
                     */
                    state.i += 5;
                }
            }
        };
        //</editor-fold>
        
        
        //<editor-fold defaultstate="collapsed" desc="return statement">
        ops[ByteCodeOperation.RETURN_S.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                state.keepGoing = false;
                state.result = state.enviornment.pop();
            }
        };
        ops[ByteCodeOperation.RETURN_V.getCode()] = new Operation()
        {
            @Override
            public void op (OperationState state)
                    throws FledExecutionException
            {
                state.keepGoing = false;
            }
        };
        //</editor-fold>
    }
    
    private ExecutionEnvironment enviornment;

    public ByteCodeExecutor_Array ()
    {
        
    }

    @Override
    public void init (ExecutionEnvironment environment) throws FledExecutionException
    {
        this.enviornment = environment;
    }

    @Override
    public Value execute (ByteCodeReader function) throws FledExecutionException
    {
        OperationState state = null;
        try
        {
            state = new OperationState();
            state.enviornment = enviornment;
            state.function = function;
            state.temp = new Value(null, null);
            for(int n = function.size(); state.keepGoing && state.i < n; state.i++)
            {
                ops[function.getCode(state.i)].op(state);
            }
            return state.result;
        }
        catch(NullPointerException ex)
        {
            try
            {
                System.err.println(function.dumpCode());
            }
            catch(Exception _ex)
            {
                System.err.println("could not print code: " + _ex);
            }
            throw new FledExecutionException(
                    "unable to find operation for " + state.function.getCode(state.i) + 
                    "\nstate dump: " + state, 
                    ex);
        }
    }
    
}
