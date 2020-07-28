<?php

class DBCounterException extends Exception
{
    public function __construct($message = "")
    {
        parent::__construct($message);
    }
}

/**
 * Description of DBCounter
 *
 * @author REx
 */
class DBCounter
{
    const FCORE_COUNTER = 'fcore_counter';

    const NAME  = 'name';
    
    const COUNT = 'count';

    public static function Increment($key)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                    "INSERT INTO ".self::FCORE_COUNTER." SET
                        ".self::NAME."='$key', ".self::COUNT."=1
                     ON DUPLICATE KEY UPDATE ".self::COUNT."=".self::COUNT."+1");
            $db->commit();
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBCounterException($e->getMessage());
        }
    }

    public static function Delete($key)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                    "DELETE FROM ".self::FCORE_COUNTER." WHERE ".self::NAME."='$key'");
            $db->commit();
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBCounterException($e->getMessage());
        }
    }

    public static function GetCount($key)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $result = $db->quick_query(
                    "SELECT ".self::COUNT." FROM ".self::FCORE_COUNTER." WHERE
                        ".self::NAME."='$key'", true);
            if ($result)
            {
                return $result[0][self::COUNT];
            }
            return false;
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBCounterException($e->getMessage());
        }
    }

    public static function SetCount($key, $count)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                    "INSERT INTO ".self::FCORE_COUNTER." SET
                        ".self::NAME."='$key', ".self::COUNT."=$count
                     ON DUPLICATE KEY UPDATE ".self::COUNT."=$count");
            $db->commit();
            return $count;
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBCounterException($e->getMessage());
        }
    }
}

?>