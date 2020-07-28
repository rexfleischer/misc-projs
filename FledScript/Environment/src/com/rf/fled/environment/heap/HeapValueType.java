
package com.rf.fled.environment.heap;

/**
 *
 * @author REx
 */
public enum HeapValueType
{
    /**
     * void type, this is mostly for system stuff
     */
    VOID,
    
    /**
     * boolean type. true, false, or null
     */
    BOOLEAN,
    
    /**
     * integer type... can also be null
     */
    INTEGER,
    
    /**
     * double type... 
     */
    DOUBLE,
    
    /**
     * string type
     */
    STRING,
    
    /**
     * object type. the actual implementation will be a system object
     * that represents an object in the best way possible for performance.
     */
    OBJECT,
    
    /**
     * meaning it can be any of the above and can switch between any
     * of the types. the only type that this cannot be is an array
     */
    UNKNOWN, 
    
    /**
     * an array of any type. 
     */
    ARRAY,
}
