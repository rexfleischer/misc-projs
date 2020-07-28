
package com.rf.fled.script.rpn.controls;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public interface RPNStatement extends Serializable
{
    public boolean isConstruct();
    
    public boolean isQueue();
    
    public RPNStatementConstruct getAsConstruct();
    
    public RPNStatementQueue getAsQueue();
}
