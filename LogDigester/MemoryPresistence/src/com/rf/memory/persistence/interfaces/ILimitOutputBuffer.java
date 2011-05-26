/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.interfaces;

import java.io.OutputStream;

/**
 *
 * @author REx
 */
public abstract class ILimitOutputBuffer extends OutputStream
{
    public abstract String toNormalizedString();

    public abstract boolean isOverflow();

    public abstract void restart();
}
