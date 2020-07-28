<?php

/**
 * Description of CreateUserPage
 *
 * @author REx
 */
class CreateUserPage extends DataCollection implements Action
{
    public function __construct()
    {
        parent::__construct();
    }

    public function do_action()
    {
        $this->data[USERS_USERID] = IsSetPost(USERS_USERID);
        if (!$this->data[USERS_USERID])
        {
            throw new UserActionException(
                    "a user must be selected");
        }

        $forum = DBForum::GetForum(
                BN_DATATYPE_USERPAGES,
                $this->data[USERS_USERID]);
        if ($forum == null)
        {
            try
            {
                DBForum::CreateForum(
                        BN_DATATYPE_USERPAGES,
                        $this->data[USERS_USERID],
                        DBDataType::DATATYPE_MTEXT);
                $forum = DBForum::GetForum(
                    BN_DATATYPE_USERPAGES,
                    $this->data[USERS_USERID]);
            }
            catch(DBForumException $e)
            {
                throw new UserActionException(
                    "an error occurred while trying to create the forum",
                    $e);
            }
        }

        $this->data[PAGETITLE]      = IsSetPost(PAGETITLE, false);
        $this->data[PAGETYPE]       = IsSetPost(PAGETYPE, false);
        $this->data[PAGEPRIVATE]    = IsSetPost(PAGEPRIVATE, 0);
        if (!$this->data[PAGETITLE] || $this->data[PAGETITLE] == '')
        {
            throw new UserActionException("title must be set", null);
        }
        if ($this->data[PAGETYPE] != BN_PAGETHREADTYPE_NONE &&
            $this->data[PAGETYPE] != BN_PAGETHREADTYPE_SINGLE &&
            $this->data[PAGETYPE] != BN_PAGETHREADTYPE_MULTI)
        {
            throw new UserActionException("invalid thread type", null);
        }
        if (!is_numeric($this->data[PAGEPRIVATE]))
        {
            var_dump($this->data[PAGEPRIVATE]);
            throw new UserActionException("invalid private page value", null);
        }
        $metadata   = serialize(array(
                PAGETITLE     => $this->data[PAGETITLE],
                PAGETYPE      => $this->data[PAGETYPE],
                PAGEPRIVATE   => $this->data[PAGEPRIVATE],
            ));
        $this->data[DBDataType::CONTENT] = IsSetPost(DBDataType::CONTENT, '');

        $parent = 0;
        $order = false;
        $this->data["position"] = IsSetPost("position");
        if ($this->data["position"])
        {
            $position = preg_split('/\:/', $this->data["position"]);
            if (is_array($position))
            {
                $parent = $position[0];
                if (sizeof($position) == 2)
                {
                    try
                    {
                        $children = $forum->post_get_children($parent);
                        if (is_array($children))
                        {
                            foreach($children as $child)
                            {
                                if ($order === false)
                                {
                                    if ($child[DBForum::POST_ID] == $position[1])
                                    {
                                        $order = $child[DBForum::POSTORDER] + 1;
                                    }
                                }
                                else
                                {
                                    $forum->post_update(
                                            $child[DBForum::POST_ID],
                                            false,
                                            false,
                                            $child[DBForum::POSTORDER] + 1,
                                            false);
                                }
                            }
                        }
                    }
                    catch(DBForumException $e)
                    {
                        throw new UserActionException(
                                "An error occurred while trying to create the page", $e);
                    }
                }
            }
        }

        try
        {
            $this->data[DBForum::POST_ID] = $forum->post_create(
                    BN_DATATYPE_USERPAGES,
                    $this->data[USERS_USERID],
                    $this->data[DBDataType::CONTENT],
                    $parent,
                    $order,
                    $metadata);
            DBForum::CreateForum(
                    BN_DATATYPE_PAGERESPONSES,
                    $this->data[DBForum::POST_ID],
                    DBDataType::DATATYPE_TEXT);
        }
        catch(DBForumException $e)
        {
            throw new UserActionException(
                    "An error occurred while trying to create the page", $e);
        }
    }
}

?>