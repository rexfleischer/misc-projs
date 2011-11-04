/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.language.LanguageStatements;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class BPlusPageManager implements Externalizable
{
    private final Object LOCK;
    
    private String workingDir;
    
    private Map<Long, WeakReference<BPlusPage>> openPages;
    
    public BPlusPageManager(String workingDir)
    {
        this.workingDir = workingDir;
        this.openPages  = new HashMap<Long, WeakReference<BPlusPage>>();
        this.LOCK       = new Object();
    }
    
    public void savePage(BPlusPage page)
    {
        
    }
    
    public BPlusPage getPage(long id)
            throws FledPresistanceException
    {
        BPlusPage result = null;
        synchronized(LOCK)
        {
            // first just try to get it from the cache
            result = openPages.get(id).get();
            
            // if the reference has been cleared already then we have to 
            // try to open it... if it exists
            if (result == null)
            {
                File file = new File(workingDir + "/" + id);
                if (!file.exists())
                {
                    // something messed up bad
                    throw new FledPresistanceException(
                            LanguageStatements.FILE_DOES_NOT_EXISTS);
                }
                result = openFile(file);
            }
        }
        return result;
    }
    
    public void deletePage(long id)
    {
        
    }
    
    private BPlusPage openFile(File page)
    {
        return null;
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws  IOException, 
                    ClassNotFoundException 
    {
        
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}