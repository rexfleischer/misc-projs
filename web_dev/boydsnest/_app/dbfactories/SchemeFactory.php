<?php

/**
 * Description of SchemeFactory
 *
 * @author REx
 */
class SchemeFactory extends DBFactory
{
    public function __construct($conn = null)
    {
        parent::__construct(SCHEME, SCHEME_SCHEMEID, $conn);
    }
}

?>
