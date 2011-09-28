/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud;

import cloud.exceptions.DataTypeException;

/**
 *
 * @author REx
 */
public interface DataType<_Ty>
{
    public void init(_Ty data) throws DataTypeException;
    
    public void init(String id, _Ty data) throws DataTypeException;
    
    public _Ty getDump() throws DataTypeException;
    
    public String getId() throws DataTypeException;
    
    public Object get(String key) throws DataTypeException;
}
