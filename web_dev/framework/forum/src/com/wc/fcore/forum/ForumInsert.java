/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum;

/**
 *
 * @author REx
 */
public interface ForumInsert extends Comparable {

    public int getDepth();

    public ForumInsert getParent();

    public Forumable getFormable();

}
