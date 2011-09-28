/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.presistance;

import cloud.Presistance;
import cloud.datatype.NitfDocument;
import cloud.exceptions.PresistanceException;

/**
 *
 * @author REx
 */
public class NitfLocalFilePresistance implements Presistance<NitfDocument>
{

    @Override
    public void delete(String id) throws PresistanceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NitfDocument read(String id) throws PresistanceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(String id, NitfDocument data) throws PresistanceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
