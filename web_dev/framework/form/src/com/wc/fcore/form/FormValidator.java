/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.form;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author REx
 */
public interface FormValidator {

    public Map<String, String> validate(HttpServletRequest request);

}
