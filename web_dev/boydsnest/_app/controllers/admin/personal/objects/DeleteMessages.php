<?php

/**
 * Description of DeleteReceivedMessage
 *
 * @author REx
 */
class DeleteMessages
{
    private $message_ids;

    public function __construct()
    {
        $this->collect();
        $this->do_security_check();
        $this->do_action();
    }

    public function collect()
    {
        $this->message_ids = IsSetPost(DBMessage::MESSAGE_ID);
        if ($this->message_ids === false)
        {
            throw new UserActionException("nothing to delete");
        }
    }

    public function do_security_check()
    {
        foreach($this->message_ids as $id)
        {
            if (!is_numeric($id))
            {
                throw new SecurityException(
                        "all message ids must be numeric");
            }
        }
    }

    public function do_action()
    {
        DBMessage::DeleteMessagesForOrigin(
                $this->message_ids,
                USERS,
                BoydsnestSession::GetInstance()->get(USERS_USERID));
    }
}

?>