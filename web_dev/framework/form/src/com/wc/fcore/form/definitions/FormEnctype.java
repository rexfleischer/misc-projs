/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.form.definitions;

/**
 *
 * @author REx
 */
public enum FormEnctype {

    TEXT_PLAIN(){
        @Override public String toString(){
            return "text/plain";
        }
    },
    MULTIPART_FORMDATA(){
        @Override public String toString(){
            return "multipart/form-data";
        }
    },
    APPLICATION_XWWWFORMURLENCODED(){
        @Override public String toString(){
            return "application/x-www-form-urlencoded";
        }
    };

}
