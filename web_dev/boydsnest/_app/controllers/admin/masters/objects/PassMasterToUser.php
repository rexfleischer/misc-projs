<?php

/**
 * Description of PassMasterToUser
 *
 * @author REx
 */
class PassMasterToUser
{
    public function __construct()
    {
        $new_master_id = IsSetPost(USERS_USERID);
        if (!$new_master_id)
        {
            throw new UserActionException("a user must be selected to update");
        }
        if (!is_numeric($new_master_id))
        {
            throw new UserActionException("a user id must be numeric");
        }

        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $user_factory->update(array(USERS_ISMASTER => "0"), "");
            $user_factory->update(array(USERS_ISMASTER => "1"), $new_master_id);
            BoydsnestSession::GetInstance()->set(USERS_ISMASTER, 0);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>