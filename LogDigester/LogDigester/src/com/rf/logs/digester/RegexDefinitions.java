/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester;

/**
 *
 * @author REx
 */
public class RegexDefinitions {

    public static final String MATCH_SPACE      = ".*";

    public static final String MATCH_IP         = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";

    public static final String MATCH_DATETIME   = "\\[([0-3]?[0-9]\\/[a-zA-Z]{3}\\/[0-9]*):([0-9]{2}:[0-9]{2}:[0-9]{2})";

    public static final String MATCH_REQUEST    = " (\\/[-_a-zA-Z\\/\\.]+) ";

}
