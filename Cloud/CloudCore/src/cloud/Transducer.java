/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud;

import cloud.exceptions.TransducerException;

/**
 *
 * @author REx
 */
public interface Transducer<_Iy, _Oy>
{
    public _Oy transduce(_Iy input) throws TransducerException;
}
