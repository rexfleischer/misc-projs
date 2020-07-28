<?php

/**
 * Description of GetUserInfo
 *
 * @author REx
 */
class GetUserInfo extends DataCollection
{
    public function __construct($user_id = false)
    {
        parent::__construct();

        // getting the user id
        if (!$user_id)
        {
            $user_id = IsSetGetPost(USERS_USERID);
            if (!$user_id)
            {
                throw new UserActionException("no user selected");
            }
            if (!is_numeric($user_id))
            {
                throw new UserActionException("user id must be numeric");
            }
        }

        // getting the user data
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $this->data = $user_factory->select_first($user_id);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>