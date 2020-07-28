<?php

/**
 * Description of GetResponseContent
 *
 * @author REx
 */
class GetResponseContent extends DataCollection
{
    public function __construct($data)
    {
        parent::__construct();

        if (!is_array($data))
        {
            throw new UserActionException("invalid data type", null);
        }
        if (!array_key_exists("page_id", $data))
        {
            throw new UserActionException("no page id specified", null);
        }
        if (!array_key_exists("post_id", $data))
        {
            throw new UserActionException("no post id specified", null);
        }

        $forum = DBForum::GetForum(BN_DATATYPE_PAGERESPONSES, $data["page_id"]);

        $this->data = $forum->post_get($data["post_id"]);
    }
}

?>