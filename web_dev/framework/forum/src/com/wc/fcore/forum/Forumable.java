/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum;

/**
 *
 * @author REx
 */
public interface Forumable extends Comparable {

    public String toForumHtml();

    public boolean isChildOf(Forumable insert);

}
