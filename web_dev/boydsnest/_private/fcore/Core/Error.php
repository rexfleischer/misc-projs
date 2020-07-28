<?php

class UserActionException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

class DBConnectException extends Exception
{
    private $query;

    public function __construct($message, $query = null)
    {
        parent::__construct($message);
        $this->query = $query;
    }

    public function getQuery()
    {
        return $this->query;
    }
}

class DBFactoryException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

class DataRulesException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

class ControllerException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

class ValidationException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

class SecurityException extends Exception
{
    public function __construct($message, $previous = null)
    {
        parent::__construct($message, 0, $previous);
    }
}

function FCoreExitHandler()
{
    $logger = FCore::GetLogger();
    $logger->setIsLogging(false);
    if ($logger->size())
    {
        try
        {
            if (FCore::$LOG_TO_HTML)
            {
                echo $logger->getFormattedLogs_Html(
                        FCore::$LOG_TO_HTML_LEVEL);
            }
        } 
        catch(Exception $e){}

        try
        {
            if (FCore::$LOG_TO_MAIL)
            {
                mail(FCore::$EMAIL_WEBMASTER,
                    FCore::$EMAIL_WEBMASTER_NOTICE,
                    $logger->getFormattedLogs_String(
                            FCore::$LOG_TO_MAIL_LEVEL),
                    "From: ".FCore::$EMAIL_WEBMASTER_FROM);
            }
        } 
        catch(Exception $e){}

        try
        {
            if (FCore::$LOG_TO_DB)
            {
                $logger->pushToDatabase(
                        FCore::$LOG_TO_DB_LEVEL,
                        FCore::$LOG_DB_MESSAGE_COL,
                        FCore::$LOG_DB_TABLE);
            }
        } 
        catch(Exception $e){}

        try
        {
            if (FCore::$LOG_TO_FILE)
            {
                $logger->getFormattedLogs_String(
                        FCore::$LOG_TO_MAIL_LEVEL);
            }
        }
        catch(Exception $e){}
    }
}

function FCoreExceptionHandler(Exception $e)
{
    try
    {
        $logger =& FCore::GetLogger();
        if (FCore::$LOG_TO_HTML)
        {
            if ($logger)
            {
                echo $logger->getFormattedLogs_Html(
                        FCore::$LOG_TO_HTML_LEVEL);
            }
            echo "UNHANDLED EXCEPTION:<br />\n";
            echo "LINE: ".$e->getLine()."<br />\n";
            echo "FILE: ".$e->getFile()."<br />\n";
            echo "MESSAGE: ".$e->getMessage()."<br />\n";
            echo "STACK TRACE: ".$e->getTraceAsString();
            exit(1);
        } 
        else
        {
            $message = "";
            if ($logger)
            {
                $message .= $logger->getFormattedLogs_String(
                        FCore::$LOG_TO_MAIL_LEVEL);
            }
            $message .= "\n\n";
            $message .= "UNHANDLED EXCEPTION:\n";
            $message .= "LINE: ".$e->getLine()."\n";
            $message .= "FILE: ".$e->getFile()."\n";
            $message .= "MESSAGE: ".$e->getMessage()."\n";
            $message .= "STACK TRACE: ".$e->getTraceAsString();
            mail(FCore::$EMAIL_WEBMASTER,
                    FCore::$EMAIL_WEBMASTER_ERROR, $message,
                    "From: ".FCore::$EMAIL_WEBMASTER_FROM);
            //FCore::ShowError(500);
        }
    } 
    catch(Exception $e)
    {
        
    }
}

function FCoreErrorHandler($errno, $errstr, $errfile, $errline)
{
    try
    {
        if (!(error_reporting() & $errno))
        {
            return;
        }
        $logger =& FCore::GetLogger();
        switch ($errno)
        {
            case E_ERROR:
            case E_CORE_ERROR:
            case E_USER_ERROR:
            case E_COMPILE_ERROR:
                if (FCore::$LOG_TO_HTML)
                {
                    $message = "";
                    if ($logger)
                    {
                        $message .= $logger->getFormattedLogs_Html(
                                FCore::$LOG_TO_HTML_LEVEL);
                    } 
                    else
                    {
                        $message .= "Could Not Dump Logs\n";
                    }
                    echo "Fatal Error -- Dumping Log:<br />\n";
                    echo "A FATAL ERROR OCCURED:<br />\n";
                    echo "ERROR($errno): '$errstr'<br />\n";
                    echo "In File $errfile At Line $errline\n";
                    exit(1);
                } 
                else
                {
                    $message = "";
                    if ($logger)
                    {
                        $message .= $logger->getFormattedLogs_String(
                                FCore::$LOG_TO_MAIL_LEVEL);
                    }
                    else
                    {
                        $message .= "Could Not Dump Logs\n";
                    }
                    $message .= "Fatal Error -- Dumping Log:\n";
                    $message .= "A FATAL ERROR OCCURED:\n";
                    $message .= "ERROR($errno): '$errstr'\n";
                    $message .= "In File $errfile At Line $errline\n";
                    mail(FCore::$EMAIL_WEBMASTER,
                            FCore::$EMAIL_WEBMASTER_ERROR, $message,
                            "From: ".FCore::$EMAIL_WEBMASTER_FROM);
                    FCore::ShowError(500);
                }
                return true;
                break;
            case E_RECOVERABLE_ERROR:
            case E_PARSE:
                if ($logger)
                {
                    $logger->log(Logger::LEVEL_ERROR,
                            $errstr, $errfile, $errline);
                }
                return true;
                break;
            case E_WARNING:
            case E_CORE_WARNING:
            case E_DEPRECATED:
            case E_USER_DEPRECATED:
            case E_COMPILE_WARNING:
            case E_USER_DEPRECATED:
            case E_USER_WARNING:
                if ($logger)
                {
                    $logger->log(Logger::LEVEL_WARN,
                            $errstr, $errfile, $errline);
                }
                return true;
                break;
            case E_STRICT:
            case E_USER_NOTICE:
            case E_NOTICE:
                if ($logger)
                {
                    $logger->log(Logger::LEVEL_DEBUG,
                            $errstr, $errfile, $errline);
                }
                return true;
                break;
            default:
                return false;
        }
    }
    catch(Exception $e)
    {
        
    }
}

?>