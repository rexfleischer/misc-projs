/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.view;

import com.rf.fledwar.model.util.SpaceConstents;

/**
 *
 * @author REx
 */
public enum WindowFocusType
{
    /**
     * the view constraints for looking at a single orbital... 
     * note: moons are considered orbitals as well.
     */
    ORBITAL(SpaceConstents.AU, SpaceConstents.AU  / 3),
    
    /**
     * the view constraints for looking at a single system without
     * zooming in for more details
     */
    SYSTEM(SpaceConstents.LIGHTYEAR * .1, SpaceConstents.AU * 2),
    
    /**
     * the view constraints for looking at multiple systems at once
     */
    MULTI_SYSTEM(SpaceConstents.LIGHTYEAR * 30, SpaceConstents.LIGHTYEAR * 10);
    
    /**
     * the max out represents the max width or height (whichever is greater)
     * for the view of the screen. 
     */
    public final double maxout;
    
    /**
     * the max in represents the max min width or height (whichever is greater)
     * that the view can be zoomed in to.
     */
    public final double maxin;

    private WindowFocusType(double maxout, double maxin)
    {
        this.maxout = maxout;
        this.maxin = maxin;
    }
}
