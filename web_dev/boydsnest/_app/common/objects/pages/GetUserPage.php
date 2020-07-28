<?php

/**
 * Description of GetUserPage
 *
 * @author REx
 */
class GetUserPage extends DataCollection
{
    public function __construct($data)
    {
        parent::__construct();

        if (!isset($data["user_id"]) || !is_numeric($data["user_id"]))
        {
            throw new UserActionException("user_id must be numeric", null);
        }
        if (!isset($data["page_id"]) || !is_numeric($data["page_id"]))
        {
            throw new UserActionException("page_id must be numeric", null);
        }

        $forum = DBForum::GetForum(BN_DATATYPE_USERPAGES, $data["user_id"]);
        
        $this->data   = $forum->post_get($data["page_id"]);
        if ($this->data[DBForum::METADATA] != null)
        {
            $extra = unserialize($this->data[DBForum::METADATA]);

            $this->data[PAGETITLE] =
                    array_key_exists(PAGETITLE, $extra)
                    ? $extra[PAGETITLE] : "";
            $this->data[PAGETYPE] =
                    array_key_exists(PAGETYPE, $extra)
                    ? $extra[PAGETYPE] : BN_PAGETHREADTYPE_NONE;
            $this->data[PAGEPRIVATE] =
                    array_key_exists(PAGEPRIVATE, $extra)
                    ? $extra[PAGEPRIVATE] : false;
            if ($this->data[DBDataType::CONTENT] == null)
            {
                $this->data[DBDataType::CONTENT] = "";
            }
        }

        $this->data[PAGEFOLLOWERS] = array();
        $this->data[PAGERIGHTS] = array();
        $forum_meta = unserialize($forum->get_metadata());
        if (is_array($forum_meta) &&
            array_key_exists($data["page_id"], $forum_meta))
        {
            $this_meta = $forum_meta[$data["page_id"]];
            if (array_key_exists(PAGEFOLLOWERS, $this_meta))
            {
                $this->data[PAGEFOLLOWERS] = $this_meta[PAGEFOLLOWERS];
            }
            if (array_key_exists(PAGERIGHTS, $this_meta))
            {
                $this->data[PAGERIGHTS] = $this_meta[PAGERIGHTS];
            }
        }
    }
}

?>