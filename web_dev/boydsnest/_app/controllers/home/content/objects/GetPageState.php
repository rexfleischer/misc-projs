<?php

/**
 * Description of GetPageState
 *
 * @author REx
 */
class GetPageState
{
    private $state;
    
    public function get_state()
    {
        return $this->state;
    }

    public function __construct()
    {
        $page_state = array();

        $page_state[USERS_USERID] = IsSetGet(USERS_USERID);
        $page_state[DBForum::POST_ID] = IsSetGet(DBForum::POST_ID);

        if (!$page_state[USERS_USERID] &&
            !$page_state[DBForum::POST_ID])
        {
            $page_state = false;
        }
        else if ($page_state[USERS_USERID] &&
                !$page_state[DBForum::POST_ID])
        {
            $page_state[DBForum::POST_ID] = DBForum::GetFirstRootIdOfForum(
                    BN_DATATYPE_USERPAGES,
                    $page_state[USERS_USERID]);
            if ($page_state[DBForum::POST_ID] == null)
            {
                throw new UserActionException(
                        "no pages were found with this user", null);
            }
        }
        else if (!$page_state[USERS_USERID] ||
                 !$page_state[DBForum::POST_ID])
        {
            $page_state = false;
        }

        if (is_array($page_state))
        {
            if (!is_numeric($page_state[USERS_USERID]))
            {
                throw new UserActionException(
                        "user id must be numeric", null);
            }
            if (!is_numeric($page_state[DBForum::POST_ID]))
            {
                throw new UserActionException(
                        "page id must be numeric", null);
            }
        }

        $this->state = $page_state;
    }
}

?>