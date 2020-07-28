<?php

require_once FCORE_FILE_DBMESSAGE;

/**
 * Description of ReadMessage
 *
 * @author REx
 */
class ReadMessage extends DataCollection
{
    public function __construct()
    {
        parent::__construct();

        $message_id = IsSetGet(DBMessage::MESSAGE_ID);
        if ($message_id === false)
        {
            throw new UserActionException(
                    "a message must be specified");
        }
        $user_id = IsSetGet(USERS_USERID);
        if ($user_id === false)
        {
            $user_id = BoydsnestSession::GetInstance()->get(USERS_USERID);
        }


        if ($user_id != BoydsnestSession::GetInstance()->get(USERS_USERID)
                && !BoydsnestSession::GetInstance()->get(USERS_ISMASTER))
        {
            throw new SecurityException(
                    "you must be the master to view other users messages");
        }


        $this->data = DBMessage::GetMessage($message_id);
        $this->data = $this->data[0];
        $this->data[DBMessage::MESSAGE] =
                DBMessage::GetMessageContent($message_id);
        $this->data[USERS_USERID] =
                $this->data[DBMessage::ORIGIN_ID];
    }
}

?>