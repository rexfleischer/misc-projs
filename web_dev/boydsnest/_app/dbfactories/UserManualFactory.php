<?php

/**
 * Description of UserManualFactory
 *
 * @author REx
 */
class UserManualFactory extends DBFactory
{
    public function __construct($conn = null)
    {
        parent::__construct(USERMANUAL, USERMANUAL_PAGEID, $conn);
    }
}

?>
