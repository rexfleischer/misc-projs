/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.filemanager;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class MockPage implements Serializable
{
    public Long id;
        
    public String content;

    public MockPage(Long id, String content)
    {
        this.id = id;
        this.content = content;
    }
}
