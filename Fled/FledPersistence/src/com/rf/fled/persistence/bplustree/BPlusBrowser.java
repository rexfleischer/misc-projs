/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.localization.LanguageStatements;

/**
 * 
 * @author REx
 */
public class BPlusBrowser implements Browser<KeyValuePair<Long, Object>>
{
    /**
     * the current managing btree
     */
    private BPlusTree bplustree;
    
    /**
     * the current page the browser is on
     */
    private BPlusPage page;
    
    /**
     * the current index of the page that the browser is on
     */
    private int index;
    
    /**
     * 
     * @param page
     * @param pageManager
     * @param index 
     */
    public BPlusBrowser(
            BPlusPage page, 
            BPlusTree bplustree, 
            int index)
    {
        this.page = page;
        this.bplustree = bplustree;
        this.index = index;
    }
    
    @Override
    public boolean valid()
    {
        return page.indexInRange(index);
    }

    @Override
    public boolean curr(KeyValuePair<Long, Object> pair) 
            throws FledPersistenceException
    {
        // all we have to do is check to make sure that this index
        // is a valid one. 
        if (page.indexInRange(index))
        {
            if (pair != null)
            {
                pair.setKey(page.getKey(index));
                pair.setValue(page.getValue(index));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean next(KeyValuePair<Long, Object> pair) 
            throws FledPersistenceException
    {
        // add one to move the index
        index++;
        
        // check to see if this is still in range of the page
        if (page.indexInRange(index))
        {
            // if it is, then just return
            pair.setKey(page.getKey(index));
            pair.setValue(page.getValue(index));
            return true;
        }
        else
        {
            // check to see if the next bucket is 0. if it is 0, then we
            // know that we got to the end of the tree
            if (page.getNextId() != 0)
            {
                return false;
            }
            
            page = bplustree.loadPage(page.getNextId());
            
            if (page == null)
            {
                // something messed up bad
                throw new FledPersistenceException(
                        LanguageStatements.FILE_DOES_NOT_EXISTS.toString());
            }
            
            // set the index to the first index in the new page
            index = 0;
            if (!page.indexInRange(index))
            {
                // something messed up really bad... this means that
                // the next page was empty
                throw new FledPersistenceException(
                        LanguageStatements.UNEXPECTED_EXCEPTION.toString());
            }
            
            // if we get here, then we are finished
            pair.setKey(page.getKey(index));
            pair.setValue(page.getValue(index));
            return true;
        }
    }

    @Override
    public boolean prev(KeyValuePair<Long, Object> pair) 
            throws FledPersistenceException
    {
        index--;
        if (index < 0)
        {
            // if this is true, then we know that we hit the beginning of
            // the tree
            if (page.getPrevId() == BPlusTree.NULL_PAGE)
            {
                return false;
            }
            page = bplustree.loadPage(page.getPrevId());
            
            if (page == null)
            {
                // something messed up bad
                throw new FledPersistenceException(
                        LanguageStatements.FILE_DOES_NOT_EXISTS.toString());
            }
            
            // set the index to the first index in the new page
            index = page.compacityUsed() - 1;
            if (!page.indexInRange(index))
            {
                // something messed up really bad... this means that
                // the next page was empty
                throw new FledPersistenceException(
                        LanguageStatements.UNEXPECTED_EXCEPTION.toString());
            }
        }
        
        // if we get here... we're good
        pair.setKey(page.getKey(index));
        pair.setValue(page.getValue(index));
        return true;
    }
}
