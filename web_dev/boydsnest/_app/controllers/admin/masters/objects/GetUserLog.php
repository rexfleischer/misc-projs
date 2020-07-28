<?php

/**
 * Description of GetUserLog
 *
 * @author REx
 */
class GetUserLog extends DataCollection
{
    private $start;

    private $amount;

    private $username;

    private $user_id;

    public function get_start()
    {
        return $this->start;
    }

    public function get_amount()
    {
        return $this->amount;
    }

    public function get_username()
    {
        return $this->username;
    }

    public function get_user_id()
    {
        return $this->user_id;
    }

    public function __construct()
    {
        parent::__construct();

        // getting the user id
        $this->user_id = IsSetGetPost(USERS_USERID);
        if (!$this->user_id)
        {
            throw new UserActionException("no user selected");
        }
        if (!is_numeric($this->user_id))
        {
            throw new UserActionException("user id must be numeric");
        }

        $this->start  = IsSetGetPost('start', 0);
        $this->amount = IsSetGetPost('amount', 20);

        if (!is_numeric($this->start))
        {
            $this->start = 0;
        }
        if (!is_numeric($this->amount))
        {
            $this->amount = 20;
        }

        // get the username
        try
        {
            $user_factory = FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $this->username = $user_factory->select_first(
                    $this->user_id,
                    array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERNAME
                        )
                    ));
            if ($this->username == null)
            {
                $this->username = "unknown";
            }
            else
            {
                $this->username = $this->username[USERS_USERNAME];
            }
        }
        catch(Exception $e)
        {
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            DBLogger::log(BN_LOGTYPE_ERROR, $e->getMessage());
            $this->username = "unknown";
        }

        // getting the user logs
        try
        {
            $this->data = DBLogger::GetLimitedBy(
                    USERS.$this->user_id, $this->start, $this->amount);
        }
        catch(DBLoggerException $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>