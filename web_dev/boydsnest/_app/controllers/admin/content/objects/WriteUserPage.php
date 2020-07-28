<?php

/**
 * Description of WriteUserPage
 *
 * @author REx
 */
class WriteUserPage
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

        $page_data = $forum->post_get($page_id);
        if ($page_data == null)
        {
            throw new UserActionException(
                    "an error occurred while trying to find the page", null);
        }

        $metadata   = unserialize($page_data[DBForum::METADATA]);
        $title = IsSetPost(PAGETITLE, '');
        if (strlen($title) < 5)
        {
            throw new UserActionException(
                    "title must be longer then 5 characters", null);
        }
        if (strlen($title) > 20)
        {
            throw new UserActionException(
                    "title must be no longer then 20 characters", null);
        }
        $metadata[PAGETITLE] = $title;

        $private = IsSetPost(PAGEPRIVATE, 0);
        if (!is_numeric($private))
        {
            throw new UserActionException(
                    "input error with private", null);
        }
        $metadata[PAGEPRIVATE] = $private ? true : false;

        $content    = IsSetPost(DBDataType::CONTENT, false);

        try
        {
            $forum->post_update(
                    $page_id,
                    $content,
                    false,
                    false,
                    serialize($metadata));
        }
        catch(DBForumException $e)
        {
            throw new UserActionException(
                    "An error occurred while trying to update the page: ".$e->getMessage(), $e);
        }
    }
}

?>