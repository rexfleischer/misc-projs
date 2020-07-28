<?php

/**
 * Description of DeleteUser
 *
 * @author REx
 */
class DeleteUser
{
    public function __construct()
    {
        // getting the user id
        $user_id = IsSetPost(USERS_USERID);
        if (!$user_id)
        {
            throw new UserActionException("no user selected");
        }
        if (!is_numeric($user_id))
        {
            throw new UserActionException("user id must be numeric");
        }

        // getting the user factory
        $user_factory;
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }

        // actually deleting
        try
        {
            $user_factory->delete($user_id);
            $page_ids = DBForum::DeleteForum(BN_DATATYPE_USERPAGES, $user_id);
            foreach($page_ids as $page_id)
            {
                DBForum::DeleteForum(BN_DATATYPE_PAGERESPONSES, $page_id);
            }
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>