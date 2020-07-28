<?php

/**
 * Description of GetUserManualPageList
 *
 * @author REx
 */
class GetUserManualPageList extends DataCollection
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
        $this->data = $manual_factory->select(null,
                array(
                    DBFactory::SELECT_GET_ONLY => array(
                        USERMANUAL_PAGEID, USERMANUAL_TITLE, USERMANUAL_RANK
                    ),
                    DBFactory::SELECT_ORDER_BY => USERMANUAL_RANK
            ));
        if (!$this->data)
        {
            throw new UserActionException("No Data In The Usermanual DB");
        }
    }
}

?>