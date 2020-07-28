<?php

/**
 * Description of DBConnect
 *
 * @author REx
 */
class DBConnect
{
    const VALUE         = 'value';

    const TYPE          = 'type';

    const TYPE_INTEGER  = 'i';

    const TYPE_DOUBLE   = 'd';

    const TYPE_STRING   = 's';

    const TYPE_BLOB     = 'b';

    /**
     * the global log instance
     * @var Logger
     */
    private $logger;

    /**
     *
     * @var string
     */
    private $db;

    /**
     * handle for the database connection
     * @var mysqli
     */
    private $conn;

    /**
     * @return mysqli current DB connection
     */
    public function getConnection()
    {
        return $this->conn;
    }

    /**
     * @return <boolean> true if a connection to DB exists
     */
    public function isConnected()
    {
        return $this->conn != null;
    }

    /**
     *
     * @param <type> $logger
     */
    public function  __construct()
    {
        $this->logger = FCore::GetLogger();
        if ($this->logger != null)
        {
            $this->logger->log(Logger::LEVEL_TRACE, __FUNCTION__." called");
        }
    }

    /**
     *
     * @param String $server address to the server
     * @param String $user the username used to connect to the DB
     * @param String $pass the password used to connect to the DB
     * @param String $db the DB to use in the server
     */
    public function connect($server, $user, $pass, $db)
    {
        if ($this->logger != null)
        {
            $this->logger->log(
                    Logger::LEVEL_TRACE,
                    __FUNCTION__." called");
        }
        $this->db = $db;
        $this->conn = new mysqli($server, $user, $pass, $db);
        if (mysqli_connect_error())
        {
            throw new DBConnectException(
                    "Error(".mysqli_connect_errno().") ".
                    mysqli_connect_error());
        }
        $this->conn->autocommit(false);
    }

    /**
     * closes the current connection to the DB
     */
    public function disconnect()
    {
        if ($this->logger != null)
        {
            $this->logger->log(Logger::LEVEL_TRACE, __FUNCTION__." called");
        }
        if ($this->conn != null)
        {
            $this->conn->close();
            $this->conn = null;
        }
    }

    /**
     * destroys the object always calls disconnect()
     */
    public function  __destruct() {
        $this->disconnect();
    }

    /**
     *
     * @param String $query the query to be made
     * @param boolean $makeArray false if you want the raw
     *  result returned, true if you want an array of the data
     * @return <mixed> sql result if makeArray==false, array
     *  of rows if makeArray==true
     */
    public function quick_query($query, $makeArray = false)
    {
        if ($this->logger != null)
        {
            $this->logger->log(
                    Logger::LEVEL_TRACE,
                    __FUNCTION__." called");
            $this->logger->log(
                    Logger::LEVEL_DEBUG,
                    "Query: $query");
        }
        if (!is_string($query) || !is_bool($makeArray))
        {
            throw new DBConnectException("Invalid Param");
        }
        $result = $this->conn->query($query);
        if ($this->conn->error)
        {
            throw new DBConnectException($this->get_error(), $query);
        }
        if (!$makeArray)
        {
            return $result;
        }
        if (!$result)
        {
            return null;
        }
        if ($result->num_rows == 0)
        {
            return null;
        }
        $returning = array();
        try
        {
            for($i=0; $i<$result->num_rows; $i++)
            {
                $returning[$i] = $result->fetch_assoc();
            }
        } 
        catch (Exception $e)
        {
            if ($this->logger != null)
            {
                $this->logger->log(Logger::LEVEL_ERROR,
                        "An Error Occurred While Returning The Sql Query");
            }
            return $result;
        }
        $result->free();
        return $returning;
    }

    /**
     *
     * @param <type> $query
     * @param <type> $params 
     */
    public function quick_prepare($query, $params)
    {
        if ($this->logger != null)
        {
            $this->logger->log(
                    Logger::LEVEL_TRACE,
                    __FUNCTION__." called");
            $this->logger->log(
                    Logger::LEVEL_DEBUG,
                    "Query: $query");
        }
        $prepare = $this->conn->prepare($query);
        foreach($params as $value)
        {
            
        }
    }

    /**
     *
     * @param <type> $str
     * @return mysqli_stmt
     */
    public function prepare($str)
    {
        $args = func_get_args();
        if ($this->logger != null)
        {
            $this->logger->log(Logger::LEVEL_TRACE, __FUNCTION__." called");
        }
        return $this->conn->prepare($str);
    }

    /**
     *
     * @return int
     */
    public function get_last_insert()
    {
        return $this->conn->insert_id;
    }

    /**
     *
     * @param string $string string to be escaped
     * @return <string> escaped string
     */
    public function escape_string($string)
    {
        return $this->conn->real_escape_string($string);
    }

    /**
     * returns true if there is a sql error
     * @return <boolean> true if sql error
     */
    public function is_error()
    {
        return $this->conn->error != null;
    }

    /**
     * gets the error that mysqli sent
     * @return <string> the sql error
     */
    public function get_error()
    {
        return "Error(".$this->conn->errno."): ".$this->conn->error;
    }

    /**
     * returns true if there is warnings
     * @return <boolean> true if sql warnings
     */
    public function is_warning()
    {
        return $this->conn->warning_count != 0;
    }

    /**
     * returns a string of the warnings from mysqli
     * @return <array> array of string listing the sql warnings
     */
    public function get_warning()
    {
        $result = $this->conn->get_warnings();
        $count = sizeof($result);
        $returning = array();
        for ($i=0; $i<$count; $i++)
        {
            $returning[$i] = "Warning $i: ".$result[$i]['errno'].
                ": ".$result[$i]['message']."\n";
        }
        return $returning;
    }

    public function commit()
    {
        if ($this->logger != null)
        {
            $this->logger->log(Logger::LEVEL_TRACE, __FUNCTION__." called");
        }
        return $this->conn->commit();
    }

    public function rollback()
    {
        if ($this->logger != null)
        {
            $this->logger->log(Logger::LEVEL_TRACE, __FUNCTION__." called");
        }
        return $this->conn->rollback();
    }
}

?>
