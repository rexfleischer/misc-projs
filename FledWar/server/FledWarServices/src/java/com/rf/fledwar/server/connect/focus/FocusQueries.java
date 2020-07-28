/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

/**
 * this class basically works as a controller for 
 * a focus
 * @author REx
 */
public enum FocusQueries
{
    RANGE
    {
        @Override
        public FocusQuery getFocusQuery()
        {
            return new FocusQuery_Range();
        }
    },
    LIST
    {
        @Override
        public FocusQuery getFocusQuery()
        {
            return new FocusQuery_List();
        }
    };
    
    public abstract FocusQuery getFocusQuery();
}
