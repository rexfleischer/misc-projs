<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Html
 *
 * @author REx
 */
class Html
{
    public static final function CssInclude($url)
    {
        return "<LINK href='$url' rel='stylesheet' type='text/css' />";
    }

    public static final function JavaScriptInclude($url)
    {
        return "<script type='text/javascript' src='$url'></script>";
    }
    
    public static final function Comment($note)
    {
        return "<!-- $note -->";
    }
}

?>