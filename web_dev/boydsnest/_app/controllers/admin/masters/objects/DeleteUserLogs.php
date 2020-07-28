<?php

/**
 * Description of DeleteUserLogs
 *
 * @author REx
 */
class DeleteUserLogs
{
    public function __construct()
    {
        $log_ids = IsSetPost(DBLogger::LOGID);
        if ($log_ids === false)
        {
            throw new UserActionException("nothing to delete");
        }


        foreach($log_ids as $id)
        {
            if (!is_numeric($id))
            {
                throw new SecurityException(
                        "all message ids must be numeric");
            }
        }


        try
        {
            DBLogger::delete($log_ids);
        }
        catch(DBLoggerException $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>