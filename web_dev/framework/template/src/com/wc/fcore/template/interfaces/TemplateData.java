/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.template.interfaces;

import com.wc.fcore.template.exception.TemplateException;

/**
 *
 * @author REx
 */
public interface TemplateData {

    public void save() throws TemplateException;

    public void open() throws TemplateException;

    public String[] getTemplateKeys() throws TemplateException;

    public String setParam(String key, String info);

}
