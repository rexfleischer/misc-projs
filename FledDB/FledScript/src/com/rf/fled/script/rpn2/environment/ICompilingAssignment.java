/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.List;

/**
 *
 * @author REx
 */
public interface ICompilingAssignment extends Function 
{
    public void compile (
            CompilingEnvironment enviornment, 
            ByteCodeWriter writer, 
            List<TokenPair> statement)
            throws FledSyntaxException;
}
