/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler;

import com.rf.fled.environment.bytecode.ByteCode;

/**
 *
 * @author REx
 */
public interface ICompiler
{
    public ByteCode compile(String expression)
            throws  ByteScriptSyntaxException;
}
