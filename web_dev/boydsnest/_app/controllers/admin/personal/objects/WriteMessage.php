<?php

require_once FCORE_FILE_DBMESSAGE;

/**
 * Description of WriteMessage
 *
 * @author REx
 */
class WriteMessage
{
    public function __construct()
    {
        if (!BoydsnestSession::GetInstance()->get(USERS_CANMESSAGE))
        {
            throw new SecurityException(
                    "you are not allowed to send messages");
        }


        
        $message    = IsSetPost(DBMessage::MESSAGE);
        $title      = IsSetPost(DBMessage::TITLE);
        $users_to   = IsSetPost(DBMessage::FCORE_MESSAGE_TOS);
        $user_from  = BoydsnestSession::GetInstance()->get(USERS_USERID);



        if ($message === false)
        {
            $message = '';
        }
        if ($title === false || $title == '')
        {
            $title = "none";
        }
        if (!is_array($users_to) || sizeof($users_to) == 0)
        {
            throw new UserActionException("no user to is specified");
        }
        if ($user_from === false)
        {
            throw new UserActionException("cannot find user from id");
        }


        try
        {
            DBMessage::CreateMessage(
                    $message,
                    $title,
                    array(
                        DBMessage::ORIGIN_ID => $user_from,
                        DBMessage::ORIGIN_TYPE => USERS
                    ),
                    $users_to);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>