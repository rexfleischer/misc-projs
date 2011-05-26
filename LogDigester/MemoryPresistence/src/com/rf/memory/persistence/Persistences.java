/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence;

import com.rf.memory.persistence.interfaces.IPersistence;
import com.rf.memory.persistence.persistence.SerializingPersistence;
import com.rf.memory.persistence.persistence.TxtFilePersistence;

/**
 *
 * @author REx
 */
public enum Persistences
{
    TXTFILE()
    {
        /**
         * @dir the directory where this presistence layer is going
         * to do its magic
         * @uniqueKey the thing that will tell this objects managed
         * information from another persistence layer
         */
        public IPersistence getPersistence(String dir, String uniqueKey)
        {
            return new TxtFilePersistence(dir, uniqueKey);
        }
    },
    SERIALIZINGFILE()
    {
        /**
         * @dir the directory where this presistence layer is going
         * to do its magic
         * @uniqueKey the thing that will tell this objects managed
         * information from another persistence layer
         */
        public IPersistence getPersistence(String dir, String uniqueKey)
        {
            return new SerializingPersistence(dir, uniqueKey);
        }
    };
}
