<?php

class BBConsumer
{

    private static $REPLACES = array(
        "/\[b\]/"       => "<b>",
        "/\[\/b\]/"     => "</b>",
        "/\[u\]/"       => "<u>",
        "/\[\/u\]/"     => "</u>",
        "/\[i\]/"       => "<i>",
        "/\[\/i\]/"     => "</i>",
        "/\[br\]/"      => "<br />"
    );

    public static function consume($str)
    {
        return preg_replace(
                array_keys(self::$REPLACES),
                array_values(self::$REPLACES),
                $str);
    }

}

?>