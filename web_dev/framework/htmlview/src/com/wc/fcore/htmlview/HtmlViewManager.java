/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.htmlview;

import com.wc.fcore.htmlview.exception.HtmlViewException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class HtmlViewManager {

    private ArrayList<HtmlViewable> views;

    public HtmlViewManager(){
        views = new ArrayList<HtmlViewable>();
    }

    public void registerView(HtmlViewable view){
        views.add(view);
    }

    public void printViews(PrintWriter pw) throws HtmlViewException {
        try {
            Iterator<HtmlViewable> it = views.iterator();
            while(it.hasNext()){
                it.next().toHtml(pw);
            }
        } catch(HtmlViewException e){
            throw new HtmlViewException(
                    "An Error Occurred While Printing The Views", e);
        }
    }

}
