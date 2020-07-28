///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.bytescript.executor;
//
//import com.rf.fled.script.FledExecutionException;
//import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
//import com.rf.fled.script.rpn2.util.ByteCodeOperation;
//import com.rf.fled.script.rpn2.util.ByteCodeReader;
//import com.rf.fled.script.rpn2.util.Value;
//
///**
// *
// * @author REx
// */
//public class ByteCodeExecutor_Switch implements IByteCodeExecutor
//{
//    private ExecutionEnvironment enviornment;
//
//    public ByteCodeExecutor_Switch ()
//    {
//        
//    }
//
//    @Override
//    public void init (ExecutionEnvironment environment) throws FledExecutionException
//    {
//        this.enviornment = environment;
//    }
//
//    @Override
//    public Value execute (ByteCodeReader function) 
//            throws FledExecutionException
//    {
//        Value result = null;
//        boolean keepGoing = true;
//        for(int i = 0, n = function.size(); keepGoing && i < n; i++)
//        {
//            switch(ByteCodeOperation.getFromCode(function.getCode(i)))
//            {
//                /**
//                 * operators: +, -, %, *, /
//                 */
//                case ADD_I:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(ValueType.INTEGER, 
//                            left.getValueAsInteger() + right.getValueAsInteger()));
//                }
//                break;
//                case SUB_I:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.INTEGER, 
//                            left.getValueAsInteger() - right.getValueAsInteger()));
//                }
//                break;
//                case MUL_I:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.INTEGER, 
//                            left.getValueAsInteger() * right.getValueAsInteger()));
//                }
//                break;
//                case DIV_I:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.INTEGER, 
//                            left.getValueAsInteger() / right.getValueAsInteger()));
//                }
//                break;
//                case MOD_I:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.INTEGER, 
//                            left.getValueAsInteger() % right.getValueAsInteger()));
//                }
//                break;
//                case ADD_D:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                            left.getValueAsDouble() + right.getValueAsDouble()));
//                }
//                break;
//                case SUB_D:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                            left.getValueAsDouble() - right.getValueAsDouble()));
//                }
//                break;
//                case MUL_D:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                            left.getValueAsDouble() * right.getValueAsDouble()));
//                }
//                break;
//                case DIV_D:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                            left.getValueAsDouble() / right.getValueAsDouble()));
//                }
//                break;
//                case ADD_S:
//                {
//                    Value right = enviornment.pop(); 
//                    Value left = enviornment.pop();
//                    enviornment.push(new Value(Value.ValueType.STRING, 
//                            left.getValueAsString() + right.getValueAsString()));
//                }
//                break;
//                
//    
//                /**
//                 * loads a hardcoded value
//                 */
//                case PUSH_H:
//                {
//                    Value value = new Value(null, null);
//                    i += function.readValue(i + 1, value);
//                    enviornment.push(value);
//                }
//                break;
//                   
//                    
//                /**
//                 * conversions
//                 */
//                case I_TO_D:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                                ((Integer)enviornment.pop().value).doubleValue()));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.DOUBLE, 
//                                ((Integer)enviornment.pop().value).doubleValue()));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case I_TO_S:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Integer)enviornment.pop().value).toString()));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Integer)enviornment.pop().value).toString()));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case D_TO_I:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.INTEGER, 
//                                ((Double)enviornment.pop().value).intValue()));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.INTEGER, 
//                                ((Double)enviornment.pop().value).intValue()));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case D_TO_S:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Double)enviornment.pop().value).toString()));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Double)enviornment.pop().value).toString()));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case B_TO_S:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Boolean)enviornment.pop().value).toString()));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.STRING, 
//                                ((Boolean)enviornment.pop().value).toString()));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case O_TO_S:
//                {
//                    throw new UnsupportedOperationException(
//                            "canot convert object to string yet");
//                }
////                break;
//                case N_TO_S:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.STRING, "null"));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.STRING, "null"));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case R_TO_S:
//                {
//                    throw new UnsupportedOperationException(
//                            "canot convert referece to string yet");
//                }
////                break;
//                case S_TO_B:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Boolean.parseBoolean(((String)enviornment.pop().value))));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Boolean.parseBoolean(((String)enviornment.pop().value))));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case S_TO_I:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Integer.parseInt(((String)enviornment.pop().value))));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Integer.parseInt(((String)enviornment.pop().value))));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                case S_TO_D:
//                {
//                    int depth = function.getCode(++i);
//                    if (depth == 0)
//                    {
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Double.parseDouble(((String)enviornment.pop().value))));
//                    }
//                    else
//                    {
//                        Value first = enviornment.pop();
//                        enviornment.push(new Value(Value.ValueType.BOOLEAN, 
//                                Double.parseDouble(((String)enviornment.pop().value))));
//                        enviornment.push(first);
//                    }
//                }
//                break;
//                    
//                    
//                    
//                case ST_PUS:
//                {
////                    enviornment.
//                }
//                break;
//                case ST_POP:
//                {
//                    
//                }
//                break;
//                case RETURN_S:
//                {
//                    keepGoing = false;
//                    result = enviornment.pop();
//                }
//                break;
//                case RETURN_V:
//                {
//                    keepGoing = false;
//                }
//                break;
//                default:
//                    throw new FledExecutionException("unknown operation: " + function.getCode(i));
//            }
//        }
//        return result;
//    }
//    
//}
