/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum;

import com.wc.fcore.forum.exception.ForumException;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface Forum {

    public void registerForumable(Forumable data) throws ForumException;

    public ArrayList<ForumInsert> orderForum() throws ForumException;

}
