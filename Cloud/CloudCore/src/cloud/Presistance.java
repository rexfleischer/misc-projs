/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud;

import cloud.exceptions.PresistanceException;

/**
 *
 * @author REx
 */
public interface Presistance<_Ty>
{
    public _Ty read(String id) throws PresistanceException;
    
    public void write(String id, _Ty data) throws PresistanceException;
    
    public void delete(String id)  throws PresistanceException;
}
