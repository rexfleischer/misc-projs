/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 *
 * @author REx
 */
public class KeyValuePair<_Ky, _Vy>
{
    private _Ky key;
    
    private _Vy val;
    
    public KeyValuePair()
    {
        this.key   = null;
        this.val  = null;
    }
    
    public KeyValuePair(_Ky key, _Vy val)
    {
        this.key = key;
        this.val = val;
    }
    
    public void setKey(_Ky key)
    {
        this.key = key;
    }
    
    public void setValue(_Vy val)
    {
        this.val = val;
    }
    
    public _Ky getKey()
    {
        return key;
    }
    
    public _Vy getValue()
    {
        return val;
    }
}
