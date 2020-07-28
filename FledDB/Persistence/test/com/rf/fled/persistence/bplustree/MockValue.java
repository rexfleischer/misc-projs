/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class MockValue implements Serializable
{
    public long id;
    
    public String content;
    
    @Override
    public String toString()
    {
        return "[id="+id+",content="+content+"]";
    }
}
