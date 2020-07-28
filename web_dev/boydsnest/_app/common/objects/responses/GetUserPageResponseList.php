<?php

/**
 * Description of GetUserPageResponseList
 *
 * @author REx
 */
class GetUserPageResponseList extends DataCollection
{
    public function __construct($page_id)
    {
        parent::__construct();

        if (!is_numeric($page_id))
        {
            throw new UserActionException('page id must be numeric', null);
        }

        $forum = DBForum::GetForum(BN_DATATYPE_PAGERESPONSES, $page_id);
        if ($forum == null)
        {
            return;
        }

        $this->data = $forum->build_hierarchy();
    }
}

?>