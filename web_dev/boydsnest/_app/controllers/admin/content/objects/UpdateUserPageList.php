<?php

/**
 * Description of UpdateUserPageList
 *
 * @author REx
 */
class UpdateUserPageList
{
    public function __construct()
    {
        $page_id = IsSetPost(DBForum::POST_ID);
        if (!is_numeric($page_id))
        {
            throw new UserActionException(
                    "a page id must be specified", null);
        }

        $forum = DBForum::GetForum(
                BN_DATATYPE_USERPAGES,
                BoydsnestSession::GetInstance()->get(USERS_USERID));
        if ($forum == null)
        {
            throw new UserActionException(
                    "an error occurred while trying to get the forum", null);
        }

        $new_parent = IsSetPost(DBForum::POSTPARENT);
        $new_order  = IsSetPost(DBForum::POSTORDER);
        if (!is_bool($new_parent) && !is_numeric($new_parent))
        {
            throw new UserActionException(
                    "parent id is not in the correct format", null);
        }
        if (!is_bool($new_order) && !is_numeric($new_order))
        {
            throw new UserActionException(
                    "order id is not in the correct format", null);
        }

        $page = $forum->post_get($page_id, false);
        if ($new_order == $page[DBForum::POSTORDER] ||
            $new_parent == $page[DBForum::POSTPARENT])
        {
            return;
        }

        try
        {
            $forum->post_update(
                    $page_id,
                    false,
                    $new_parent,
                    $new_order,
                    false);
        }
        catch(DBForumException $e)
        {
            throw new UserActionException(
                    "An error occurred while trying to update the page: ".$e->getMessage(), $e);
        }
    }
}

?>