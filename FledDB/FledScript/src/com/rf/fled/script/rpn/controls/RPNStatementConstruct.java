
package com.rf.fled.script.rpn.controls;

import com.rf.fled.script.tokenizer.TokenPair;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author REx
 */
public class RPNStatementConstruct implements RPNStatement
{
    private TokenPair construct;
    
    private RPNStatementQueue[] params;
    
    private int locationIfTrue;
    
    private int locationIfFalse;
    
    public RPNStatementConstruct(
            TokenPair construct, 
            List<RPNStatement> params,
            int locationIfTrue,
            int locationIfFalse)
    {
        this.construct      = construct;
        this.locationIfTrue = locationIfTrue;
        this.locationIfFalse= locationIfFalse;
        
        if (params != null)
        {
            this.params     = new RPNStatementQueue[params.size()];
            for(int i = 0; i < params.size(); i++)
            {
                this.params[i] = (RPNStatementQueue) params.get(i);
            }
        }
    }
    
    public RPNStatementQueue[] getParams()
    {
        return params;
    }
    
    public void setIfResolvesTrue(int location)
    {
        locationIfTrue = location;
    }
    
    public void setIfResolvesFalse(int location)
    {
        locationIfFalse = location;
    }
    
    public TokenPair getConstructToken()
    {
        return construct;
    }
    
    public int getLocationIfTrue()
    {
        return locationIfTrue;
    }
    
    public int getLocationIfFalse()
    {
        return locationIfFalse;
    }

    @Override
    public boolean isConstruct() 
    {
        return true;
    }

    @Override
    public boolean isQueue() 
    {
        return false;
    }

    @Override
    public RPNStatementConstruct getAsConstruct() 
    {
        return this;
    }

    @Override
    public RPNStatementQueue getAsQueue() 
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString()
    {
        return "[construct: " + construct + 
                ", ifTrue: " + locationIfTrue + 
                ", ifFalse: " + locationIfFalse + "]\n"
                + "[params: " + Arrays.toString(params) + "]";
    }
}
