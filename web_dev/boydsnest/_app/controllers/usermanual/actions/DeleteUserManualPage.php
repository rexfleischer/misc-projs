<?php

/**
 * Description of DeleteUserManualPage
 *
 * @author REx
 */
class DeleteUserManualPage
{
    private $page_id;

    private $manual_factory;

    public function __construct()
    {
        $this->collect();
        $this->do_action();
    }

    public function collect()
    {
        $this->manual_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMANUALMODEL);
        if (!$this->manual_factory)
        {
            throw new UserActionException(
                    "Cannot Connect To The Usermanual DB");
        }
        $this->page_id = IsSetPost(USERMANUAL_PAGEID);
        if (!$this->page_id)
        {
            throw new UserActionException(
                    "A Page Must Be Selected To Delete");
        }
    }

    public function do_action()
    {
        $this->manual_factory->delete($this->page_id);
    }
}

?>