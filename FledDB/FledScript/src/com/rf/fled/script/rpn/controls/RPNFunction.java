/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn.controls;

import com.rf.fled.script.language.Function;
import com.rf.fled.script.rpn.RPNExecutionState;
import com.rf.fled.script.tokenizer.TokenPair;

/**
 *
 * @author REx
 */
public interface RPNFunction extends Function
{
    /**
     * 
     * @param params
     * @param variables not always used, but when the function is perform 
     * assignments, then it is used.
     * @return 
     */
    public TokenPair compute(
            TokenPair[] params, 
            RPNExecutionState state);
    
    public int numOfParams();
}
