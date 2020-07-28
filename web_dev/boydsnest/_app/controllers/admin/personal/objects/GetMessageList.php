<?php

/**
 * Description of ListReceivedMessages
 *
 * @author REx
 */
class GetMessageList extends DataCollection
{
    private $start;

    private $amount;

    private $user_id;

    private $origin;

    private $user_factory;

    public function __construct($origin = 'to')
    {
        parent::__construct();
        $this->origin = $origin;
        $this->do_security_check();
        $this->collect();
        $this->do_action();
    }

    public function do_security_check()
    {
        $this->user_id = IsSetGet(USERS_USERID);
        if ($this->user_id === false)
        {
            $this->user_id = IsSetPost(USERS_USERID);
        }
        if ($this->user_id === false)
        {
            $this->user_id =
                    BoydsnestSession::GetInstance()->get(USERS_USERID);
        }
        if (!is_numeric($this->user_id))
        {
            throw new UserActionException(
                    'user_id must be numeric');
        }
        if ($this->user_id != BoydsnestSession::GetInstance()->get(USERS_USERID) &&
                !BoydsnestSession::GetInstance()->get(USERS_ISMASTER))
        {
            throw new SecurityException(
                    "must be the master to view other peoples messages");
        }
    }

    public function collect()
    {
        try
        {
            $this->user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }

        // initial try to get the required data to do the action
        $this->start = IsSetGet('start');
        $this->amount = IsSetGet('amount');

        // if any of them are false, then attempt post data
        if ($this->start === false || $this->amount === false)
        {
            $this->start = IsSetPost('start');
            $this->amount = IsSetPost('amount');
        }

        // if still any are false, do default values
        if ($this->start === false || $this->amount === false)
        {
            $this->start = 0;
            $this->amount = 40;
        }

        // now check to make sure that all of them are numeric
        if (!is_numeric($this->start))
        {
            throw new UserActionException('start must be numeric');
        }
        if (!is_numeric($this->amount))
        {
            throw new UserActionException('amount must be numeric');
        }
    }

    public function do_action()
    {
        try
        {
            $data = false;
            switch($this->origin)
            {
                case 'to':
                    $data = DBMessage::GetMessageLimitedByForOriginsTo(
                                USERS,
                                $this->user_id,
                                $this->start,
                                $this->amount);
                    break;
                case 'from':
                    $data = DBMessage::GetMessageLimitedByForOriginsFrom(
                                USERS,
                                $this->user_id,
                                $this->start,
                                $this->amount);
                    break;
                default:
                    throw new UserActionException(
                            "invalid message origin.. must be to or from");
            }
            if ($data == null || $data === false)
            {
                return;
            }
            $user_ids = array();
            foreach($data as $message)
            {
                if (array_search($message[DBMessage::ORIGIN_ID], $user_ids) === false)
                {
                    array_push($user_ids, $message[DBMessage::ORIGIN_ID]);
                }
            }
            $user_names = $this->user_factory->select(
                    $user_ids, array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_USERNAME
                        )
                    ));
            $user_includes = array();
            foreach($user_names as $name)
            {
                $user_includes[$name[USERS_USERID]] = $name[USERS_USERNAME];
            }
            $this->data = array();
            foreach($data as $message)
            {
                $insert = $message;
                $insert[USERS_USERID] = $message[DBMessage::ORIGIN_ID];
                if (isset($user_includes[$name[USERS_USERID]]))
                {
                    $insert[USERS_USERNAME] =
                            $user_includes[$name[USERS_USERID]];
                }
                else
                {
                    $insert[USERS_USERNAME] = "unknown";
                }
                $this->data[] = $insert;
            }
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>