<?php

define("APPDIR", str_replace("\\", "/", dirname(__FILE__)));

require_once 'config.php';

function echoln($str = "")
{
    echo $str . "\n";
}

function htmlln($str = "")
{
    echo $str . "<br />\n";
}

function buildurl($base, $params)
{
    $result = "$base?";
    if (is_string($params))
    {
        $result .= "?$params";
    } 
    else if (is_array($params))
    {
        $first = true;
        foreach($params as $key => $param)
        {
            if ($first)
            {
                $first = false;
            }
            else 
            {
                $result .= "&";
            }
            $result .= $key;
            if (!is_null($param))
            {
                $result .= "=$param";
            }
        }
    }
    return $result;
}

function Redirect($where)
{
    header("Location: $where");
}

function IsSetGet($var, $default = false)
{
    if (isset($_GET[$var]))
    {
        return $_GET[$var];
    }
    return $default;
}

function IsSetPost($var, $default = false)
{
    if (isset($_POST[$var]))
    {
        return $_POST[$var];
    }
    return $default;
}

function IsSetGetPost($var, $default = false)
{
    if (isset($_GET[$var]))
    {
        return $_GET[$var];
    }
    if (isset($_POST[$var]))
    {
        return $_POST[$var];
    }
    return $default;
}

function IsSetRequest($var, $default = false)
{
    if (isset($_REQUEST[$var]))
    {
        return $_REQUEST[$var];
    }
    return $default;
}

function IsSetSession($var, $default = false)
{
    if (isset($_SESSION[$var]))
    {
        return $_SESSION[$var];
    }
    return $default;
}

function IsSetEcho(&$var, $default = '')
{
    if (isset($var))
    {
        echo $var;
    }
    echo $default;
}

function get_files_from_dir($dir) 
{
    $files = array();
    $handle = opendir($dir);
    if ($handle) 
    {
        while (false !== ($file = readdir($handle))) 
        {
            if ($file != "." && $file != "..") 
            {
                if (is_file($dir.'/'.$file))
                {
                    $files[] = $file;
                }
            }
        }
        closedir($handle);
    }
    return $files;
} 

?>