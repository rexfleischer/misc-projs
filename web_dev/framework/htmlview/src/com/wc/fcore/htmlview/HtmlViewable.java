/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.htmlview;

import com.wc.fcore.htmlview.exception.HtmlViewException;
import java.io.PrintWriter;

/**
 *
 * @author REx
 */
public interface HtmlViewable {

    public void toHtml(PrintWriter pw) throws HtmlViewException;

}
