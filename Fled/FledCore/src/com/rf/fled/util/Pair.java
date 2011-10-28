/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.util;

/**
 *
 * @author REx
 */
public class Pair<_Ly, _Ry>
{
    private _Ly left;
    
    private _Ry right;
    
    public Pair()
    {
        this.left   = null;
        this.right  = null;
    }
    
    public Pair(_Ly left, _Ry right)
    {
        this.left   = left;
        this.right  = right;
    }
    
    public void setLeft(_Ly left)
    {
        this.left = left;
    }
    
    public void setRight(_Ry right)
    {
        this.right = right;
    }
    
    public _Ly getLeft()
    {
        return left;
    }
    
    public _Ry getRight()
    {
        return right;
    }
}
