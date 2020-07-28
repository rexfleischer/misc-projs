<?php

/**
 * Description of UpdateUserPage
 *
 * @author REx
 */
class UpdateUserPageRights
{
    public function __construct()
    {
        // get page id
        $page_id = IsSetPost(DBForum::POST_ID);
        if (!$page_id || !is_numeric($page_id))
        {
            throw new UserActionException("no page selected", null);
        }

        // get the user ids and make sure that they are in the correct format
        $user_ids = IsSetPost("user_ids");
        if (!$user_ids)
        {
            throw new UserActionException("no users selected", null);
        }
        $user_ids = preg_split('/\:/', $user_ids);
        if (!is_array($user_ids))
        {
            throw new UserActionException("no users selected", null);
        }
        foreach($user_ids as $user_id)
        {
            if (!is_numeric($user_id))
            {
                throw new UserActionException(
                        "an error occurred with the format of user ids", null);
            }
        }

        // get the forum for the user
        $forum = DBForum::GetForum(
                BN_DATATYPE_USERPAGES,
                BoydsnestSession::GetInstance()->get(USERS_USERID));
        if ($forum == null)
        {
            throw new UserActionException(
                    "error occurred while trying to load the user pages", null);
        }
        $metadata = unserialize($forum->get_metadata());
        if (!array_key_exists($page_id, $metadata))
        {
            $metadata[$page_id] = array();
        }

        // get the list of rights for all the users
        $user_rights = array();
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $raw_rights = $user_factory->select(
                    $user_ids,
                    array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_DEFAULTRIGHT
                        )
                    ));
            foreach($raw_rights as $right)
            {
                $user_rights[$right[USERS_USERID]] = $right[USERS_DEFAULTRIGHT];
            }
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage(), $e);
        }

        foreach($user_ids as $user_id)
        {
            if (!array_key_exists($user_id, $user_rights))
            {
                throw new UserActionException(
                        "system error occurred with user ids 1", null);
            }
            $defaultright = $user_rights[$user_id];
            $askedright = IsSetPost($user_id);
            if ($askedright === false)
            {
                throw new UserActionException(
                        "system error occurred with user ids 2", null);
            }
            if ($askedright != USERS_DEFAULTRIGHT_NONE &&
                $askedright != USERS_DEFAULTRIGHT_SEE &&
                $askedright != USERS_DEFAULTRIGHT_COMMENT &&
                $askedright != USERS_DEFAULTRIGHT_WRITE)
            {
                throw new UserActionException(
                        "system error occurred with user ids 3", null);
            }
            if ($askedright < $defaultright)
            {
                throw new UserActionException(
                        "system error occurred with user ids 4", null);
            }
            if ($askedright == $defaultright)
            {
                if (array_key_exists($user_id, $metadata[$page_id][PAGERIGHTS]))
                {
                    unset($metadata[$page_id][PAGERIGHTS][$user_id]);
                }
            }
            else if($askedright > $defaultright)
            {
                $metadata[$page_id][PAGERIGHTS][$user_id] = $askedright;
            }
        }

        $forum->set_metadata(serialize($metadata));
    }
}

?>