<?php

/**
 * Description of CreateUserManualPage
 *
 * @author REx
 */
class CreateUserManualPage extends DataCollection
{
    private $manual_factory;

    public function __construct()
    {
        parent::__construct();
        $this->collect();
        $this->validate();
        $this->do_action();
    }

    public function collect()
    {
        $this->manual_factory =
                FCore::LoadDBFactory(BN_DBFACTORY_USERMANUALMODEL);
        if ($this->manual_factory == null)
        {
            throw new UserActionException(
                    "Cannot Load The Usermanual DB");
        }
        $this->data[USERMANUAL_TITLE]   = IsSetPost(USERMANUAL_TITLE);
        $this->data[USERMANUAL_RANK]    = IsSetPost(USERMANUAL_RANK);
        if ($this->data[USERMANUAL_TITLE] === false ||
            $this->data[USERMANUAL_RANK] === false ||
            $this->data[USERMANUAL_TITLE] === '' ||
            $this->data[USERMANUAL_RANK] === '')
        {
            throw new UserActionException(
                    "All Required Data Must Be Inserted");
        }
    }

    public function validate()
    {
        if (!is_string($this->data[USERMANUAL_TITLE]))
        {
            throw new ValidationException(
                    "The Title Must Be A String");
        }
        if (strlen($this->data[USERMANUAL_TITLE]) < 5 ||
                40 < strlen($this->data[USERMANUAL_TITLE]))
        {
            throw new ValidationException(
                    "The Title's Length Must Be No Less Then ".
                    "5 And No Greater Then 40 Characters");
        }
        if (!is_numeric($this->data[USERMANUAL_RANK]))
        {
            throw new ValidationException(
                    "The Rank Of Usermanual Must Be Numeric");
        }
    }

    public function do_action()
    {
        $this->manual_factory->insert($this->data);
    }
}

?>