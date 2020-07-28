/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn.controls;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.rpn.RPNCompiler;
import com.rf.fled.script.rpn.RPNExecutionState;
import com.rf.fled.script.rpn.RPNExecutor;
import com.rf.fled.script.rpn.controls.RPNStatement;
import com.rf.fled.script.rpn.controls.RPNStatementQueue;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.List;

/**
 *
 * @author REx
 */
public interface RPNConstruct extends Function
{
    public boolean resolvesTrue(
            RPNStatementQueue[] constructParams, 
            RPNExecutionState state,
            RPNExecutor executor,
            boolean firstIteration)
            throws FledExecutionException;
    
    public int compile(
            TokenPair thisToken,
            List<TokenPair> infix,
            int cursor,
            List<RPNStatement> buffer,
            RPNCompiler compiler)
            throws FledSyntaxException, UndefinedFunctionException;
    
    public String[] chainsWith();
    
    public int paramsInParathesis();
    
    public boolean iterative();
    
    public boolean canInitiateChains();
}
