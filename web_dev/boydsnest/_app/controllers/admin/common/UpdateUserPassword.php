<?php

/**
 * Description of UpdateUserPassword
 *
 * @author REx
 */
class UpdateUserPassword
{
    public function __construct()
    {
        $user_id = IsSetPost(USERS_USERID);
        if (!$user_id)
        {
            throw new UserActionException("a user must be selected to update");
        }
        if (!is_numeric($user_id))
        {
            throw new UserActionException("a user id must be numeric");
        }

        $password = IsSetPost(USERS_PASSWORD);
        if (!$password)
        {
            throw new UserActionException("a password must be set");
        }
        if (!is_string($password))
        {
            throw new UserActionException("a password must a string");
        }
        if (strlen($password) > 20)
        {
            throw new UserActionException("a password cannot be longer than 20 characters");
        }
        if (strlen($password) < 5)
        {
            throw new UserActionException("a password cannot be shorter than 5 characters");
        }
        
        $new_data[USERS_SALT] = GetNewSalt();
        $new_data[USERS_PASSWORD] = GetSecondOrderHash(
                $password,
                $new_data[USERS_SALT]);

        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $user_factory->update($new_data, $user_id);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>