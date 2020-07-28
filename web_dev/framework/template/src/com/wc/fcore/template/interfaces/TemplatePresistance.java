/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.template.interfaces;

import com.wc.fcore.template.interfaces.TemplateData;
import com.wc.fcore.template.exception.TemplateException;

/**
 *
 * @author REx
 */
public interface TemplatePresistance {

    public void save(TemplateData data) throws TemplateException;

    public void delete(String dir) throws TemplateException;

    public TemplateData get(String dir) throws TemplateException;

}
