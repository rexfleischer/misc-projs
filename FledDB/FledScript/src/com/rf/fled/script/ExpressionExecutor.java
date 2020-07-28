
package com.rf.fled.script;

import com.rf.fled.script.tokenizer.TokenPair;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface ExpressionExecutor extends Serializable
{
    public TokenPair execute(
            List<Object> input, 
            Map<String, TokenPair> variables,
            boolean returnLast)
            throws FledExecutionException;
}
