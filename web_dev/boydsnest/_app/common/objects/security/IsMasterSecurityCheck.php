<?php

/**
 * Description of IsMasterSecurityCheck
 *
 * @author REx
 */
class IsMasterSecurityCheck
{
    public function __construct()
    {
        $session = BoydsnestSession::GetInstance();
        if (!$session->IsLoggedIn())
        {
            throw new SecurityException(
                    "You Must Be The Site Master To Preform This Action");
        }
        else if(!$session->get(USERS_ISMASTER))
        {
            throw new SecurityException(
                    "You Must Be The Site Master To Preform This Action");
        }
    }
}

?>