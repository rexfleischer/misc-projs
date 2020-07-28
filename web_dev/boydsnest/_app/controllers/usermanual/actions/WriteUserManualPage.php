<?php

/**
 * Description of WriteUserManualPage
 *
 * @author REx
 */
class WriteUserManualPage
{
    private $page_id;
    
    private $page_content;
    
    private $manual_factory;

    public function __construct()
    {
        $this->collect();
        $this->validate();
        $this->do_action();
    }

    public function collect()
    {
        $this->manual_factory =&
                FCore::LoadDBFactory(BN_DBFACTORY_USERMANUALMODEL);
        if (!$this->manual_factory)
        {
            throw new UserActionException(
                    "Cannot Connect To Usermanual DB");
        }
        $this->page_id = IsSetPost(USERMANUAL_PAGEID);
        $this->page_content = IsSetPost(USERMANUAL_CONTENT);
        if ($this->page_id === false || 
            $this->page_id === '' ||
            $this->page_content === false)
        {
            throw new UserActionException(
                    "Must Specify Content And A Page ID");
        }
    }

    public function validate()
    {
        if (!is_numeric($this->page_id))
        {
            // i dont say anything here because it could be an attack
            throw new ValidationException("");
        }
        if (strlen($this->page_content) > 65000)
        {
            throw new ValidationException("Content Is Too Large");
        }
    }

    public function do_action()
    {
        $this->page_content = htmlentities($this->page_content, ENT_QUOTES);
        $this->manual_factory->update(
                array(USERMANUAL_CONTENT => $this->page_content),
                $this->page_id);
    }
}

?>