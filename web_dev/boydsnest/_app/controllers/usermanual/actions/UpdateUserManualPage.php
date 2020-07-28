<?php

/**
 * Description of UpdateUserManualPage
 *
 * @author REx
 */
class UpdateUserManualPage
{
    private $page_id;
    
    private $page_title;
    
    private $page_rank;
    
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
        $this->page_title = IsSetPost(USERMANUAL_TITLE);
        $this->page_rank = IsSetPost(USERMANUAL_RANK);
        if ($this->page_id === false || 
                $this->page_title === false ||
                $this->page_rank === false ||
                $this->page_id === '' ||
                $this->page_title === '' ||
                $this->page_rank === '')
        {
            throw new UserActionException(
                    "Must Specify Page Rank, Title And ID");
        }
    }
    
    public function validate()
    {
        if (!is_string($this->page_title))
        {
            throw new ValidationException(
                    "The Title Must Be A String");
        }
        if (strlen($this->page_title) < 5 || 40 < strlen($this->page_title))
        {
            throw new ValidationException(
                    "The Title's Length Must Be No Less Then ".
                    "5 And No Greater Then 40 Characters");
        }
        if (!is_numeric($this->page_rank))
        {
            throw new ValidationException(
                    "The Rank Of Usermanual Must Be Numeric");
        }
    }

    public function do_action()
    {
        $this->manual_factory->update(
                array(USERMANUAL_RANK => $this->page_rank,
                    USERMANUAL_TITLE => $this->page_title),
                $this->page_id);
    }
}

?>