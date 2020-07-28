<?php

class DBMessageException extends Exception
{
    public function __construct($message = "")
    {
        parent::__construct($message);
    }
}

/**
 * Description of DBMessage
 *
 * @author REx
 */
class DBMessage
{
    const FCORE_MESSAGES        = 'fcore_messages';

    const FCORE_MESSAGE_TOS     = 'fcore_message_tos';

    const FCORE_MESSAGE_OWNERS  = 'fcore_message_owners';

    
    const MESSAGE_ID    = 'message_id';

    const TIME_SENT     = 'time_sent';

    const TITLE         = 'title';

    const MESSAGE       = 'message';

    const FUNCTION_     = 'function';

    const ORIGIN_TYPE   = 'origin_type';

    const ORIGIN_ID     = 'origin_id';

    const SEEN          = 'seen';


    /**
     *
     * @param <type> $message
     * @param <type> $title
     * @param <type> $origin_from_set
     * @param <type> $origin_to_sets
     * @param <type> $function
     * @return <type> 
     */
    public static function CreateMessage(
            $message, $title,
            $origin_from_set,
            $origin_to_sets,
            $function = '')
    {
        if (!is_string($message))
        {
            throw new DBMessageException('$message must be a string');
        }
        if (!is_string($title))
        {
            throw new DBMessageException('$title must be a string');
        }
        if (!is_array($origin_from_set))
        {
            throw new DBMessageException('$origin_from_set must be an array');
        }
        if (!is_array($origin_to_sets))
        {
            throw new DBMessageException('$origins_to_ids cannot be null');
        }
        if (!is_string($function))
        {
            throw new DBMessageException('$function must be a string');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $message = $db->escape_string($message);
            $title = $db->escape_string($title);
            $db->quick_query(
                "INSERT INTO ".self::FCORE_MESSAGES." SET
                    ".self::MESSAGE."='$message',
                    ".self::TITLE."='$title',
                    ".self::ORIGIN_TYPE."='".$origin_from_set[self::ORIGIN_TYPE]."',
                    ".self::ORIGIN_ID."=".$origin_from_set[self::ORIGIN_ID].",
                    ".self::FUNCTION_."='$function'");
            $message_id = $db->get_last_insert();
            $db->quick_query(
                "INSERT INTO ".self::FCORE_MESSAGE_OWNERS." SET
                    ".self::MESSAGE_ID."=$message_id,
                    ".self::ORIGIN_TYPE."='".$origin_from_set[self::ORIGIN_TYPE]."',
                    ".self::ORIGIN_ID."=".$origin_from_set[self::ORIGIN_ID]);
            foreach($origin_to_sets as $to_set)
            {
                if (!is_array($to_set) && !is_numeric($to_set))
                {
                    throw new Exception("all id sets must be an array");
                }
                $origin_insert = "";
                if (is_array($to_set))
                {
                    $origin_insert =
                        self::ORIGIN_TYPE."='".$to_set[self::ORIGIN_TYPE]."',
                      ".self::ORIGIN_ID."=".$to_set[self::ORIGIN_ID];
                }
                else
                {
                    $origin_insert =
                        self::ORIGIN_TYPE."='".$origin_from_set[self::ORIGIN_TYPE]."',
                      ".self::ORIGIN_ID."=$to_set";
                }

                $db->quick_query(
                    "INSERT INTO ".self::FCORE_MESSAGE_TOS." SET
                        ".self::MESSAGE_ID."=$message_id, $origin_insert");
                $db->quick_query(
                    "INSERT INTO ".self::FCORE_MESSAGE_OWNERS." SET
                        ".self::MESSAGE_ID."=$message_id, $origin_insert");
            }
            $db->commit();
            return $message_id;
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_id
     * @param <type> $origin_owner_sets 
     */
    public static function DeleteMessages($message_id, $origin_owner_sets)
    {
        if (!is_numeric($message_id))
        {
            throw new DBMessageException('$message_id cannot be null');
        }
        if (!is_array($origin_owner_sets))
        {
            throw new DBMessageException('$origin_owner_sets cannot be null');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $delete_where = "";
            foreach($origin_owner_sets as $set)
            {
                if ($delete_where != "")
                {
                    $delete_where .= " || ";
                }
                $delete_where .= 
                        "(".self::ORIGIN_ID."=".$set[self::ORIGIN_ID]." AND
                          ".self::ORIGIN_TYPE."='".$set[self::ORIGIN_TYPE]."')";
            }
            $db->quick_query(
                    "DELETE FROM ".self::FCORE_MESSAGE_OWNERS." WHERE
                        ".self::MESSAGE_ID."=$message_id AND ($delete_where)");
            $count = $db->quick_query(
                    "SELECT count(*) FROM ".self::FCORE_MESSAGE_OWNERS." WHERE
                        ".self::MESSAGE_ID."=$message_id", true);
            if ($count == null || $count[0]["count(*)"] == 0)
            {
                $db->quick_query(
                    "DELETE FROM ".self::FCORE_MESSAGES." WHERE
                        ".self::MESSAGE_ID."=$message_id");
            }
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_ids
     * @param <type> $origin_type
     * @param <type> $origin_id 
     */
    public static function DeleteMessagesForOrigin($message_ids, $origin_type, $origin_id)
    {
        if (!is_array($message_ids))
        {
            throw new DBMessageException('$message_ids must be an array');
        }
        if (!is_string($origin_type))
        {
            throw new DBMessageException('$origin_type must be a string');
        }
        if (!is_numeric($origin_id))
        {
            throw new DBMessageException('$origin_id must be numeric');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();

            $delete_where = "";
            foreach($message_ids as $message_id)
            {
                if ($delete_where != "")
                {
                    $delete_where .= " || ";
                }
                $delete_where .= self::MESSAGE_ID."=$message_id";
            }
            $db->quick_query(
                "DELETE FROM ".self::FCORE_MESSAGE_OWNERS." WHERE
                    (".self::ORIGIN_ID."=$origin_id AND
                     ".self::ORIGIN_TYPE."='$origin_type') AND
                    ($delete_where)");
            
            $delete_ids = $db->quick_query(
                    "SELECT ".self::MESSAGE_ID.", count(*) as total
                        FROM ".self::FCORE_MESSAGE_OWNERS."
                        WHERE ($delete_where) 
                            GROUP BY ".self::MESSAGE_ID."
                            HAVING total > 0", true);

            if (is_array($delete_ids))
            {
                $delete_where = "";
                foreach($message_ids as $message_id)
                {
                    $delete = true;
                    if (is_array($delete_ids))
                    {
                        foreach($delete_ids as $delete_id)
                        {
                            if ($delete_id[self::MESSAGE_ID] == $message_id)
                            {
                                $delete = false;
                                break;
                            }
                        }
                    }
                    if ($delete)
                    {
                        if ($delete_where != "")
                        {
                            $delete_where .= " || ";
                        }
                        $delete_where .= self::MESSAGE_ID."=".$message_id;
                    }
                }
            }
            if (is_string($delete_where) && $delete_where != "")
            {
                $db->quick_query("DELETE FROM ".self::FCORE_MESSAGES." WHERE $delete_where");
            }
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_id 
     */
    public static function ForceDeleteMessage($message_id)
    {
        if (!is_numeric($message_id))
        {
            throw new DBMessageException('$message_id cannot be null');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                "DELETE FROM ".self::FCORE_MESSAGES." WHERE
                    ".self::MESSAGE_ID."=$message_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_id
     */
    public static function ForceDeleteMessageOfOriginType($origin_type)
    {
        if (!is_string($origin_type))
        {
            throw new DBMessageException('$message_id cannot be null');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                "DELETE FROM ".self::FCORE_MESSAGES." WHERE
                    ".self::ORIGIN_TYPE."='$origin_type'");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_id
     * @return <type> 
     */
    public static function GetMessage($message_id)
    {
        if ($message_id == null)
        {
            return null;
        }
        if (!is_numeric($message_id) && !is_array($message_id))
        {
            throw new DBMessageException(
                    '$message_id must be a single id or an array of ids');
        }
        if (is_array($message_id))
        {
            foreach($message_id as $id)
            {
                if (!is_numeric($id))
                {
                    throw new DBMessageException(
                        'all elements in the message array must be numeric');
                }
            }
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $message_where = "";
            if (is_array($message_id))
            {
                foreach($message_id as $id)
                {
                    if ($message_where != "")
                    {
                        $message_where .= " || ";
                    }
                    $message_where .= self::MESSAGE_ID."=$id";
                }
            }
            else
            {
                $message_where = self::MESSAGE_ID."=$message_id";
            }

            // get owner ids and order them according to the message
            // they belong to
            $owners = $db->quick_query(
                    "SELECT * FROM ".self::FCORE_MESSAGE_TOS."
                        WHERE $message_where", true);
            if ($owners == null)
            {
                return null;
            }
            $message_owners = array();
            foreach($owners as $owner)
            {
                if (!isset($message_owners[$owner[self::MESSAGE_ID]]))
                {
                    $message_owners[$owner[self::MESSAGE_ID]] = array();
                }
                $insert = array();
                $insert[self::ORIGIN_ID] = $owner[self::ORIGIN_ID];
                $insert[self::ORIGIN_TYPE] = $owner[self::ORIGIN_TYPE];
                $insert[self::SEEN] = $owner[self::SEEN];
                $message_owners[$owner[self::MESSAGE_ID]][] = $insert;
            }

            $returning = array();

            $messages = $db->quick_query(
                    "SELECT ".self::MESSAGE_ID.",".self::TIME_SENT.",
                            ".self::TITLE.",".self::FUNCTION_.",
                            ".self::ORIGIN_ID.",".self::ORIGIN_TYPE."
                       FROM ".self::FCORE_MESSAGES."
                       WHERE $message_where", true);
            foreach($messages as $message)
            {
                $message[self::FCORE_MESSAGE_TOS] =
                    $message_owners[$message[self::MESSAGE_ID]];
                $returning[] = $message;
            }

            return $returning;
        }
        catch(Exception $e)
        {
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $message_id
     * @return <type> 
     */
    public static function GetMessageContent($message_id)
    {
        if ($message_id == null)
        {
            throw new DBMessageException('$message_id cannot be null');
        }
        $db = false;
        try
        {
            $returning = array();
            $db =& FCore::GetDefaultConnection();
            $result = $db->quick_query(
                    "SELECT ".self::MESSAGE." FROM ".self::FCORE_MESSAGES." WHERE
                        ".self::MESSAGE_ID."=$message_id", true);
            if (!$result)
            {
                return null;
            }
            return $result[0][self::MESSAGE];
        }
        catch(Exception $e)
        {
            throw new DBMessageException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_from_id
     * @param <type> $start
     * @param <type> $amount
     * @return <type> 
     */
    public static function GetMessageLimitedByForOriginsFrom(
            $origin_type, $origin_from_id, $start = 0, $amount = 20)
    {
        if (!is_string($origin_type))
        {
            throw new DBMessageException('$origin_type must be a string');
        }
        if (!is_numeric($origin_from_id))
        {
            throw new DBMessageException('$origin_from_id must be numeric');
        }
        if (!is_numeric($start))
        {
            throw new DBMessageException('$start must be numeric');
        }
        if (!is_numeric($amount))
        {
            throw new DBMessageException('$amount must be numeric');
        }
        $db = false;
        $asking_messages = array();
        try
        {
            $db =& FCore::GetDefaultConnection();
            $message_ids = $db->quick_query(
                    "SELECT DISTINCT ".self::FCORE_MESSAGES.".".self::MESSAGE_ID."
                        FROM
                            ".self::FCORE_MESSAGES.",
                            ".self::FCORE_MESSAGE_OWNERS."
                        WHERE
                            ".self::FCORE_MESSAGES.".".self::ORIGIN_TYPE."='$origin_type' AND
                            ".self::FCORE_MESSAGES.".".self::ORIGIN_ID."=$origin_from_id AND
                            ".self::FCORE_MESSAGES.".".self::ORIGIN_ID."="
                                .self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_ID." AND
                            ".self::FCORE_MESSAGES.".".self::MESSAGE_ID."="
                                .self::FCORE_MESSAGE_OWNERS.".".self::MESSAGE_ID." AND
                            ".self::FCORE_MESSAGES.".".self::ORIGIN_TYPE."="
                                .self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_TYPE."
                        ORDER BY ".self::TIME_SENT."
                        LIMIT $start, $amount", true);
            if ($message_ids == null)
            {
                return null;
            }
            foreach($message_ids as $message)
            {
                $asking_messages[] = $message[self::MESSAGE_ID];
            }
        }
        catch(Exception $e)
        {
            throw new DBMessageException($e->getMessage());
        }
        return self::GetMessage($asking_messages);
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_to_id
     * @param <type> $start
     * @param <type> $amount
     * @return <type> 
     */
    public static function GetMessageLimitedByForOriginsTo(
            $origin_type, $origin_to_id, $start = 0, $amount = 20)
    {
        if (!is_string($origin_type))
        {
            throw new DBMessageException('$origin_type must be a string');
        }
        if (!is_numeric($origin_to_id))
        {
            throw new DBMessageException('$origin_to_id must be numeric');
        }
        if (!is_numeric($start))
        {
            throw new DBMessageException('$start must be numeric');
        }
        if (!is_numeric($amount))
        {
            throw new DBMessageException('$amount must be numeric');
        }
        $db = false;
        $asking_messages = array();
        try
        {
            $db =& FCore::GetDefaultConnection();
            $message_ids = $db->quick_query(
                    "SELECT DISTINCT ".self::FCORE_MESSAGE_TOS.".".self::MESSAGE_ID."
                        FROM
                                ".self::FCORE_MESSAGES.",
                                ".self::FCORE_MESSAGE_OWNERS.",
                                ".self::FCORE_MESSAGE_TOS."
                        WHERE
                                ".self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_TYPE."='$origin_type' AND
                                ".self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_ID."=$origin_to_id AND
                                ".self::FCORE_MESSAGE_TOS.".".self::ORIGIN_ID."="
                                    .self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_ID." AND
                                ".self::FCORE_MESSAGE_TOS.".".self::MESSAGE_ID."="
                                    .self::FCORE_MESSAGE_OWNERS.".".self::MESSAGE_ID." AND
                                ".self::FCORE_MESSAGE_TOS.".".self::ORIGIN_TYPE."="
                                    .self::FCORE_MESSAGE_OWNERS.".".self::ORIGIN_TYPE." AND
                                ".self::FCORE_MESSAGE_TOS.".".self::MESSAGE_ID."="
                                    .self::FCORE_MESSAGES.".".self::MESSAGE_ID."
                        ORDER BY ".self::TIME_SENT."
                        LIMIT $start, $amount", true);
            if ($message_ids == null)
            {
                return null;
            }
            foreach($message_ids as $message)
            {
                $asking_messages[] = $message[self::MESSAGE_ID];
            }
        }
        catch(Exception $e)
        {
            throw new DBMessageException($e->getMessage());
        }
        return self::GetMessage($asking_messages);
    }

    /**
     *
     * @param <type> $message_id
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @param <type> $value 
     */
    public static function SetSeen($message_id, $origin_type, $origin_id, $value)
    {
        if (!is_numeric($message_id))
        {
            throw new DBMessageException('$message_id must be numeric');
        }
        if (!is_string($origin_type))
        {
            throw new DBMessageException('$origin_type must be a string');
        }
        if (!is_numeric($origin_id))
        {
            throw new DBMessageException('$origin_to_id must be numeric');
        }
        if (!is_numeric($value))
        {
            throw new DBMessageException('$value must be numeric');
        }
        $db = false;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                    "UPDATE FROM ".self::FCORE_MESSAGE_TOS." 
                        SET
                            ".self::SEEN."=$value
                        WHERE
                            ".self::MESSAGE_ID."=$message_id AND
                            ".self::ORIGIN_TYPE."='$origin_type' AND
                            ".self::ORIGIN_ID."=$origin_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBMessageException($e->getMessage());
        }
    }
}
?>