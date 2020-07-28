<?php

/**
 * Description of DeleteUserPage
 *
 * @author REx
 */
class DeleteUserPage
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

        try
        {
            $forum->post_delete($page_id, true);
            $meta = unserialize($forum->get_metadata());
            $new_meta = array();
            foreach($forum->post_get_id_list() as $page)
            {
                if (array_key_exists($page, $meta))
                {
                    $new_meta[$page] = $meta[$page];
                }
            }
            $forum->set_metadata(serialize($new_meta));
            DBForum::DeleteForum(
                    BN_DATATYPE_PAGERESPONSES,
                    $page_id);
        }
        catch(DBForumException $e)
        {
            throw new UserActionException(
                    "An error occurred while trying to delete the page: ".$e->getMessage(), $e);
        }
    }
}

?>