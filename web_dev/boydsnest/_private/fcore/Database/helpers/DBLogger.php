<?php

class DBLoggerException extends Exception
{
    public function __construct($message = "")
    {
        parent::__construct($message);
    }
}

/**
 * Description of DBLogger
 *
 * @author REx
 */
class DBLogger
{
    const FCORE_LOG = 'fcore_log';

    const LOGID     = 'logID';

    const ORIGIN    = 'origin';

    const TIME      = 'time';
    
    const MESSAGE   = 'message';

    /**
     *
     * @param <type> $origin
     * @param <type> $message
     * @return <type> 
     */
    public static function log($origin, $message)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $db->quick_query(
                    "INSERT INTO ".self::FCORE_LOG." SET
                        ".self::ORIGIN."='$origin',
                        ".self::MESSAGE."='$message'");
            $log_id = $db->get_last_insert();
            $db->commit();
            return $log_id;
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBLoggerException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin
     * @param <type> $start
     * @param <type> $amount
     * @return <type> 
     */
    public static function GetLimitedBy($origin, $start = 0, $amount = 50)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            return $db->quick_query(
                    "SELECT * FROM ".self::FCORE_LOG." WHERE
                        ".self::ORIGIN."='$origin'
                        ORDER BY ".self::TIME."
                        LIMIT $start, $amount",
                    true);
        }
        catch(Exception $e)
        {
            throw new DBLoggerException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin
     * @return <type> 
     */
    public static function GetCountOfOrigin($origin)
    {
        try
        {
            $db =& FCore::GetDefaultConnection();
            $result = $db->quick_query(
                    "SELECT count(*) FROM ".self::FCORE_LOG." WHERE
                        ".self::ORIGIN."='$origin'",
                    true);
            if ($result)
            {
                return $result[0]['count(*)'];
            }
            return 0;
        }
        catch(Exception $e)
        {
            throw new DBLoggerException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $logIDs 
     */
    public static function delete($logIDs)
    {
        try
        {
            $deletes = "";
            if (is_array($logIDs))
            {
                foreach($logIDs as $id)
                {
                    if ($deletes != "")
                    {
                        $deletes .= " || ";
                    }
                    $deletes .= self::LOGID."=$id";
                }
            }
            else if (is_numeric($logIDs))
            {
                $deletes = self::LOGID."=$logIDs";
            }
            else
            {
                throw new InvalidArgumentException('$logIDs');
            }
            $db =& FCore::GetDefaultConnection();
            $db->quick_query("DELETE FROM ".self::FCORE_LOG." WHERE $deletes");
            $db->commit();
        }
        catch(Exception $e)
        {
            $db->rollback();
            throw new DBLoggerException($e->getMessage());
        }
    }
}

?>