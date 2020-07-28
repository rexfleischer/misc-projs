/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn.controls;

import com.rf.fled.script.tokenizer.TokenPair;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author REx
 */
public class RPNStatementQueue implements RPNStatement, Serializable, Iterable<TokenPair>
{
    private TokenPair[] rpn;
    
    public RPNStatementQueue(List<TokenPair> rpn)
    {
        this.rpn = new TokenPair[rpn.size()];
        rpn.toArray(this.rpn);
    }

    @Override
    public Iterator<TokenPair> iterator() 
    {
        return new RPNQueueIterator();
    }

    @Override
    public boolean isConstruct() 
    {
        return false;
    }

    @Override
    public boolean isQueue() 
    {
        return true;
    }

    @Override
    public RPNStatementConstruct getAsConstruct() 
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public RPNStatementQueue getAsQueue() 
    {
        return this;
    }
    
    @Override
    public String toString()
    {
        return "[statement queue: " + Arrays.toString(rpn) + "]";
    }
    
    public class RPNQueueIterator implements Iterator<TokenPair>
    {
        private int position;
        
        private RPNQueueIterator()
        {
            // cant touch this
            position = 0;
        }
        
        @Override
        public boolean hasNext() 
        {
            return RPNStatementQueue.this.rpn.length > position;
        }

        @Override
        public TokenPair next() 
        {
            return RPNStatementQueue.this.rpn[position++];
        }

        @Override
        public void remove() 
        {
            throw new UnsupportedOperationException();
        }
    }
}
