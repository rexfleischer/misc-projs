/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.input;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface ImageCheckAlgorithm<_Iy>
{
    public abstract void initNewScreen();
    
    public abstract void finished();
    
    public abstract void setImages(Map<String, _Iy> images);
    
    public abstract void setConfig(String config, Object value);
    
    public abstract void setImageConfig(String imageKey, String config, Object value);
    
    public abstract Map<String, ArrayList<Rectangle>> getImages(String[] key);
}
