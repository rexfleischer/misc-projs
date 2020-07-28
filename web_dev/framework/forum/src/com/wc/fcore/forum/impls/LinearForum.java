/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum.impls;

import com.wc.fcore.forum.Forum;
import com.wc.fcore.forum.ForumInsert;
import com.wc.fcore.forum.Forumable;
import com.wc.fcore.forum.exception.ForumException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author REx
 */
public class LinearForum implements Forum {

    private ArrayList<Forumable> stack;

    public LinearForum(){
        stack = new ArrayList<Forumable>();
    }

    public void registerForumable(Forumable data) throws ForumException {
        stack.add(data);
    }

    public ArrayList<ForumInsert> orderForum() throws ForumException {
        ArrayList<ForumInsert> result = new ArrayList<ForumInsert>();

        for(Forumable part : stack){
            result.add(new LinearForumInsert(part));
        }

        Collections.sort(result);

        return result;
    }

    public class LinearForumInsert implements ForumInsert {

        private Forumable able;

        public LinearForumInsert(Forumable able){
            this.able = able;
        }

        public int getDepth() {
            return 0;
        }

        public ForumInsert getParent() {
            return null;
        }

        public Forumable getFormable() {
            return able;
        }

        public int compareTo(Object o) {
            return able.compareTo(o);
        }

    }

}
