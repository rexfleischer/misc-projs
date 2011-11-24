/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class MockValue implements Serializable
{
    public String something;
        
    public Long _else;

    public MockValue(Long _else, String something)
    {
        this._else = _else;
        this.something = something;
    }
}
