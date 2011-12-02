package com.rf.fled.presistance.bplustree.old;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.presistance.bplustree;
//
//import com.rf.fled.exceptions.FledPresistanceException;
//import com.rf.fled.language.LanguageStatements;
//import com.rf.fled.interfaces.Browser;
//import com.rf.fled.util.Pair;
//
///**
// *
// * @author REx
// */
//public class BPlusBrowser implements Browser<Pair<Long, Object>>
//{
//    /**
//     * the current managing btree
//     */
//    private BPlusTree bplustree;
//    
//    /**
//     * the current page the browser is on
//     */
//    private BPlusPage page;
//    
//    /**
//     * the current index of the page that the browser is on
//     */
//    private int index;
//    
//    /**
//     * the manager for getting pages
//     */
//    private FlatFileManager pageManager;
//    
//    /**
//     * 
//     * @param page
//     * @param pageManager
//     * @param index 
//     */
//    public BPlusBrowser(
//            BPlusPage page, 
//            FlatFileManager pageManager, 
//            int index)
//    {
//        this.page = page;
//        this.pageManager = pageManager;
//        this.index = index;
//    }
//    
//    @Override
//    public boolean valid()
//    {
//        return page.indexInRange(index);
//    }
//
//    @Override
//    public boolean curr(Pair<Long, Object> pair) 
//            throws FledPresistanceException
//    {
//        // all we have to do is check to make sure that this index
//        // is a valid one. 
//        if (page.indexInRange(index))
//        {
//            if (pair != null)
//            {
//                pair.setLeft(page.getKey(index));
//                pair.setRight(page.getValue(index));
//            }
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean next(Pair<Long, Object> pair) 
//            throws FledPresistanceException
//    {
//        // add one to move the index
//        index++;
//        
//        // check to see if this is still in range of the page
//        if (page.indexInRange(index))
//        {
//            // if it is, then just return
//            pair.setLeft(page.getKey(index));
//            pair.setRight(page.getValue(index));
//            return true;
//        }
//        else
//        {
//            // check to see if the next bucket is 0. if it is 0, then we
//            // know that we got to the end of the tree
//            if (page.getNextBucketId() != 0)
//            {
//                return false;
//            }
//            
//            // get the next page
//            page = (BPlusPage) pageManager.getPage(
//                    bplustree.buildPageId(page.getNextBucketId()), 
//                    bplustree.getPageSerializer());
//            if (page == null)
//            {
//                // something messed up bad
//                throw new FledPresistanceException(
//                        LanguageStatements.FILE_DOES_NOT_EXISTS);
//            }
//            
//            // set the index to the first index in the new page
//            index = 0;
//            if (!page.indexInRange(index))
//            {
//                // something messed up really bad... this means that
//                // the next page was empty
//                throw new FledPresistanceException(
//                        LanguageStatements.UNEXPECTED_EXCEPTION);
//            }
//            
//            // if we get here, then we are finished
//            pair.setLeft(page.getKey(index));
//            pair.setRight(page.getValue(index));
//            return true;
//        }
//    }
//
//    @Override
//    public boolean prev(Pair<Long, Object> pair) 
//            throws FledPresistanceException
//    {
//        index--;
//        if (index < 0)
//        {
//            // if this is true, then we know that we hit the beginning of
//            // the tree
//            if (page.getPreviousBucketId() == 0)
//            {
//                return false;
//            }
//            page = (BPlusPage) pageManager.getPage(
//                    bplustree.buildPageId(page.getPreviousBucketId()), 
//                    bplustree.getPageSerializer());
//            
//            // get the next page
//            page = (BPlusPage) pageManager.getPage(
//                    bplustree.buildPageId(page.getNextBucketId()), 
//                    bplustree.getPageSerializer());
//            if (page == null)
//            {
//                // something messed up bad
//                throw new FledPresistanceException(
//                        LanguageStatements.FILE_DOES_NOT_EXISTS);
//            }
//            
//            // set the index to the first index in the new page
//            index = page.compacityUsed() - 1;
//            if (!page.indexInRange(index))
//            {
//                // something messed up really bad... this means that
//                // the next page was empty
//                throw new FledPresistanceException(
//                        LanguageStatements.UNEXPECTED_EXCEPTION);
//            }
//        }
//        
//        // if we get here... we're good
//        pair.setLeft(page.getKey(index));
//        pair.setRight(page.getValue(index));
//        return true;
//    }
//}
