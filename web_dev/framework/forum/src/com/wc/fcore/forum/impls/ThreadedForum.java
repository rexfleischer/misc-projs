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

/**
 *
 * @author REx
 */
public class ThreadedForum implements Forum {

    private ArrayList<Forumable> stack;

    public ThreadedForum(){
        stack = new ArrayList<Forumable>();
    }

    public void registerForumable(Forumable data) throws ForumException {
        stack.add(data);
    }

    public ArrayList<ForumInsert> orderForum() throws ForumException {
        ArrayList<ForumInsert> result = new ArrayList<ForumInsert>();

        for(Forumable part : stack){
            result.add(new ThreadedForumInsert(part));
        }
     
        

        return result;
    }

    public class ThreadedForumInsert implements ForumInsert {

        private int depth;
        private Forumable able;
        private ForumInsert parent;

        public ThreadedForumInsert(
                int depth,
                Forumable able,
                ForumInsert parent){
            this.depth = depth;
            this.able = able;
            this.parent = parent;
        }

        public int getDepth() {
            return depth;
        }

        public Forumable getFormable() {
            return able;
        }

        public ForumInsert getParent() {
            return parent;
        }

        public int compareTo(Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
