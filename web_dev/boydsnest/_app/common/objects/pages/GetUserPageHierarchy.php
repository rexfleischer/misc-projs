<?php

/**
 * Description of GetUserPageHierarchy
 *
 * @author REx
 */
class GetUserPageHierarchy extends DataCollection
{
    public function __construct($data = null)
    {
        parent::__construct();

        $user_id    = BoydsnestSession::GetInstance()->get(USERS_USERID);
        $page_root  = 0;
        $extended   = false;

        if (is_array($data))
        {
            if (array_key_exists(USERS_USERID, $data))
            {
                $user_id = $data[USERS_USERID];
            }
            if (array_key_exists("page_id", $data))
            {
                $page_root = $data["page_id"];
            }
            if (array_key_exists("extended", $data))
            {
                $extended = $data["extended"];
            }
        }
        else if (is_numeric($data))
        {
            $user_id = $data;
        }

        $forum = DBForum::GetForum(BN_DATATYPE_USERPAGES, $user_id);
        if ($forum == null)
        {
            $this->data = null;
            return;
        }

        $data;
        if ($extended)
        {
            $data = $forum->build_hierarchy($page_root);
        }
        else
        {
            $data = $forum->post_get_children($page_root);
        }

        if ($data == null)
        {
            $this->data = null;
            return;
        }

        foreach($data as $page)
        {
            $meta = unserialize($page[DBForum::METADATA]);
            if (is_array($meta))
            {
                if (array_key_exists(PAGETITLE, $meta))
                {
                    $page[PAGETITLE] = $meta[PAGETITLE];
                }
                if (array_key_exists(PAGEPRIVATE, $meta))
                {
                    $page[PAGEPRIVATE] = $meta[PAGEPRIVATE];
                }
                if (array_key_exists(PAGETYPE, $meta))
                {
                    $page[PAGETYPE] = $meta[PAGETYPE];
                }
            }
            $this->data[] = $page;
        }
    }
}

?>