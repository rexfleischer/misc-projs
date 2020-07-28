<?php

/**
 * Description of GetUserManualPageContent
 *
 * @author REx
 */
class GetUserManualPageContent extends DataCollection
{
    public function __construct()
    {
        parent::__construct();
        $this->collect();
    }

    public function collect()
    {
        $manual_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMANUALMODEL);
        if (!$manual_factory)
        {
            throw new UserActionException("Cannot Load Usermanual DB");
        }
        $page_id = IsSetGet(USERMANUAL_PAGEID);
        if (!$page_id)
        {
            $page_id = IsSetPost(USERMANUAL_PAGEID);
        }
        if (!$page_id)
        {
            $this->data = $manual_factory->select_first(array(
                    array(
                        DBFactory::ID_ESCAPE=> false,
                        DBFactory::ID_KEY   => USERMANUAL_RANK,
                        DBFactory::ID_SIGN  => "=",
                        DBFactory::ID_VAL   => "(SELECT min(".USERMANUAL_RANK.") FROM ".USERMANUAL.")"
                    )
                ));
            if (!$this->data)
            {
                throw new UserActionException("No Usermanual Pages Found");
            }
        }
        else
        {
            $this->data = $manual_factory->select_first($page_id);
            if (!$this->data)
            {
                throw new UserActionException(
                        "Cannot Find Usermanual Page With Requested ID");
            }
        }
    }
}

?>