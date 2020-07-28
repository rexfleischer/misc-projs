/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.rpn2.RPN2Compiler;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.List;

/**
 *
 * @author REx
 */
public interface ICompilingDeclaration extends Function 
{
    public int compile (
            CompilingEnvironment environment, 
            ByteCodeWriter writer,
            List<TokenPair> statement,
            RPN2Compiler compiler,
            int position)
            throws FledSyntaxException;
}
