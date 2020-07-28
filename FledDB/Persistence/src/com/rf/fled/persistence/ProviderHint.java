/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 *
 * @author REx
 */
public enum ProviderHint 
{
    /**
     * IPersistence flags
     */
    // this tells the IPersistence implementation if keys are allowed
    // to collide, and tells them to put them in buckets if they do. it
    // is up to the IPersistence implementation to provide a bucket type.
    // this is because different IPersistence types require different 
    // types of bucket strategies.
    RECORDS_ARE_BUCKETS,
    // tells certain IPersistence implementations how many records are
    // on a single file (depending on the IFileManager implementation as well)
    RECORDS_PER_PAGE, 
    // tells the IPersistence implemenation how to serialize values
    // into a byte array
    VALUE_SERIALIZER, 
    // for special cases when the page serialize of a specific IPersistence
    // needs to change. this should only be used by internal systems.
    PAGE_SERIALIZER, 
}
