<?php

/**
 * Description of DBFactory
 *
 * @author REx
 */
class DBFactory
{
    /**
     * this is the 'safe' way to build an identifier in case
     * there is a need to take user input and build an identifier
     * with it. an example of the useage is:
     *      NOTE: if escape is not set, then it defaults to true
     * return array(
     *  array(self::ID_KEY => 'col1',
     *        self::ID_SIGN => '=', 
     *        self::ID_VAL => 'val1'),
     *  "&&",
     *  array(self::ID_KEY => 'col2',
     *        self::ID_SIGN => '!=',
     *        self::ID_VAL => 'val2',
     *        self::ID_ESCAPE => false)
     * );
     * which would result in:
     *  WHERE col1='escaped(val1)' && col2!=val2
     * NOTE: escaped(val1) of course is not the actual handed in value, it is
     *       just showing that it escapes the value then puts it in
     *
     * also, the identifier can be handed in an array of numbers which would
     * result in:
     * WHERE pri_key=123 || pri_key=234 || pri_key=345 || ....
     * or a string which would result in:
     * WHERE str
     * or just a single number that would result in:
     * WHERE pri_key=123
     */
    const ID_KEY    = 'id_key';
    
    const ID_SIGN   = 'id_sign';

    const ID_VAL    = 'id_val';

    const ID_ESCAPE = 'id_escape';

    /**
     * the next group is the flags that are
     * put into the keys of the array for select()
     */
    const SELECT_LIMIT_START    = 'select_limit';
    
    const SELECT_LIMIT_AMOUNT   = 'select_amount';

    const SELECT_GET_ONLY       = 'select_get_only';

    const SELECT_ORDER_BY       = 'select_order_by';

    /**
     * for the next three, this is what comes from the
     * get_config_map() function. an array with these
     * attributes, plus the table definitions:
     *
     * return array(
     *      self::DB_TABLE_NAME => 'some_name',
     *      self::DB_PRI_KEY_NAME => 'some_column',
     *      self::DB_SELECT_REQUIRE => array(
     *          'col1', 'col2', 'col3'
     *      )
     * );
     */
    // the name of the table
    const DB_TABLE_NAME     = 'table_name';

    // the primary key of the table
    const DB_PRI_KEY_NAME   = 'pri_key_name';

    const DB_DATA_RULES     = 'data_rules';

    /**
     * these are for the data array that is handed in on insert() and
     * update()... they are placed in an array where the key is the
     * column name. but if no array exists, then the value automatically
     * gets quoted and escaped. for instance:
     * return array(
     *      'col1' => array(
     *          self::INSERT_VALUE => 'val1',
     *          self::INSERT_ESCAPE_VAL => true,
     *          self::INSERT_QUOTE  => false
     *      ),
     *      'col2' => 'val2'
     * );
     */
    const INSERT_VALUE      = 'insert_value';

    const INSERT_ESCAPE_VAL = 'insert_escape_val';

    const INSERT_QUOTE      = 'insert_quote';

    /**
     *
     * @var array
     */
    protected $config_map;

    /**
     *
     * @var DBConnect
     */
    protected $conn;

    /**
     *
     * @param <type> $table_name
     * @param <type> $pri_key_name
     * @param <type> $conn
     * @param <type> $data_rules 
     */
    public function  __construct($table_name, $pri_key_name, $conn = null, $data_rules = null)
    {
        $this->config_map = array();
        $this->config_map[self::DB_PRI_KEY_NAME]    = $pri_key_name;
        $this->config_map[self::DB_TABLE_NAME]      = $table_name;
        $this->config_map[self::DB_DATA_RULES]      = $data_rules;
        if ($conn == null)
        {
            $this->conn =& FCore::GetDefaultConnection();
        }
        else
        {
            $this->conn =& $conn;
        }
    }

    /**
     *
     * @param <type> $prefered_method
     * @param <type> $for_update if this is true, then the rules object will
     * not make any of the values required and therefore will not throw
     * an error when auto validated
     * @return DataRules
     */
    public function get_db_data_rules($prefered_method = false, $for_update = true)
    {
        if ($this->config_map[self::DB_DATA_RULES] == null)
        {
            return null;
        }
        $rules = $this->config_map[self::DB_DATA_RULES];
        if ($prefered_method !== false)
        {
            foreach($this->config_map[self::DB_DATA_RULES] as $key => $rule)
            {
                $rules[$key][DataRules::METHOD] = $prefered_method;
            }
        }
        if ($for_update)
        {
            foreach($this->config_map[self::DB_DATA_RULES] as $key => $rule)
            {
                $rules[$key][DataRules::REQUIRED] = false;
            }
            return new DataRules($rules);
        }
        else
        {
            return new DataRules($rules);
        }
    }

    /**
     *
     * @param array $data 
     */
    public function insert(array $data)
    {
        try
        {
            $this->conn->quick_query(
                    "INSERT ".$this->config_map[self::DB_TABLE_NAME].
                        " SET ".$this->consume_data($data)
                    );
            $insert_id = $this->conn->get_last_insert();
            $this->conn->commit();
            return $insert_id;
        }
        catch(DBConnectException $e)
        {
            $this->conn->rollback();
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            throw new DBFactoryException(
                    "An Error Occurred While Attempting To Insert: ", $e);
        }
    }

    /**
     *
     * @param array $data
     * @param int $identifier
     */
    public function update(array $data, $identifier)
    {
        try
        {
            $this->conn->quick_query(
                    "UPDATE ".$this->config_map[self::DB_TABLE_NAME].
                        " SET ".$this->consume_data($data)
                        .$this->consume_identifier($identifier)
                );
            $this->conn->commit();
        }
        catch(DBConnectException $e)
        {
            $this->conn->rollback();
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            throw new DBFactoryException(
                    "An Error Occurred While Attempting To Update: ", $e);
        }
    }

    /**
     *
     * @param mixed $identifier
     * @param array $flags
     * @return array
     */
    public function select($identifier, array $flags = array())
    {
        try
        {
            $limit = "";
            $order = "";
            $get = "*";
            $where_clause = $this->consume_identifier($identifier);
            if (isset($flags[self::SELECT_ORDER_BY]))
            {
                $order = " ORDER BY ".$flags[self::SELECT_ORDER_BY];
            }
            if (isset($flags[self::SELECT_LIMIT_START]))
            {
                $limit = " LIMIT ".$flags[self::SELECT_LIMIT_START];
                if (isset($flags[self::SELECT_LIMIT_AMOUNT]))
                {
                    $limit .= ",".$flags[self::SELECT_LIMIT_AMOUNT];
                }
            }
            if (isset($flags[self::SELECT_GET_ONLY]))
            {
                if (is_array($flags[self::SELECT_GET_ONLY]))
                {
                    $get = implode(",", $flags[self::SELECT_GET_ONLY]);
                }
                else
                {
                    $get = $flags[self::SELECT_GET_ONLY];
                }
            }

            return $this->conn->quick_query(
                    "SELECT $get FROM ".$this->config_map[self::DB_TABLE_NAME].
                        " $where_clause $order $limit", true
                    );
        }
        catch(DBConnectException $e)
        {
            $this->conn->rollback();
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            throw new DBFactoryException(
                    "An Error Occurred While Attempting To Select: ", $e);
        }
    }

    /**
     *
     * @param mixed $identifier
     * @param array $flags
     * @return array
     */
    public function select_first($identifier, array $flags = array())
    {
        $flags[self::SELECT_LIMIT_START] = 1;
        $result = $this->select($identifier, $flags);
        if (is_array($result))
        {
            if (sizeof($result) > 0)
            {
                $result = $result[0];
            }
        }
        return $result;
    }

    /**
     *
     * @param mixed $identifier
     */
    public function delete($identifier)
    {
        try
        {
            $this->conn->quick_query(
                    "DELETE FROM ".$this->config_map[self::DB_TABLE_NAME].
                        $this->consume_identifier($identifier)
                    );
            $this->conn->commit();
        }
        catch(DBConnectException $e)
        {
            $this->conn->rollback();
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            throw new DBFactoryException(
                    "An Error Occurred While Attempting To Delete: ", $e);
        }
    }

    /**
     * 
     */
    public function delete_all()
    {
        try
        {
            $this->conn->quick_query(
                    "DELETE FROM ".$this->config_map[self::DB_TABLE_NAME]
                    );
            $this->conn->commit();
        }
        catch(DBConnectException $e)
        {
            $this->conn->rollback();
            FCore::GetLogger()->log(Logger::LEVEL_ERROR, $e->getMessage());
            throw new DBFactoryException(
                    "An Error Occurred While Attempting To Delete: ", $e);
        }
    }

    /**
     *
     * @param <type> $data
     * @return <type> 
     */
    private function consume_data(&$data)
    {
        $result = "";
        foreach($data as $key => $value)
        {
            $insert = $value;
            if (is_array($value))
            {
                if (!isset($value[self::INSERT_VALUE]))
                {
                    continue;
                }
                $insert = $value[self::INSERT_VALUE];
                if (isset($value[self::INSERT_ESCAPE_VAL]))
                {
                    if ($value[self::INSERT_ESCAPE_VAL])
                    {
                        $insert = $this->conn->escape_string($insert);
                    }
                }
                else
                {
                    $insert = $this->conn->escape_string($insert);
                }
                if (isset($value[self::INSERT_QUOTE]))
                {
                    if ($value[self::INSERT_QUOTE])
                    {
                        $insert = "'$insert'";
                    }
                }
                else
                {
                     $insert = "'$insert'";
                }
            }
            else
            {
                $insert = $this->conn->escape_string($insert);
                $insert = "'$insert'";
            }
            if ($result != "")
            {
                $result .= ",";
            }
            $result .= "$key=$insert";
        }
        return $result;
    }

    /**
     *
     * @param <type> $identifier
     * @return <type> 
     */
    private function consume_identifier(&$identifier)
    {
        if (is_null($identifier) || $identifier == "")
        {
            return "";
        }
        if (is_numeric($identifier))
        {
            return " WHERE ".$this->config_map[self::DB_PRI_KEY_NAME]."=".$identifier;
        }
        if (is_string($identifier))
        {
            return " WHERE $identifier";
        }
        if (!is_array($identifier))
        {
            throw new Exception("invalid identifier type");
        }
        ksort($identifier);
        reset($identifier);
        $search = "";
        if (is_numeric(current($identifier)))
        {
            // if the first value is a number, then we can deduce
            // that all of the values are ids of the records this
            // model is manipulating. so, we just get the the
            // id column name for the table and set each one to the
            // value, and or all of them together
            foreach($identifier as $val)
            {
                if (is_numeric($val))
                {
                    if ($search != "")
                    {
                        $search .= " || ";
                    }
                    $search .= $this->config_map[self::DB_PRI_KEY_NAME]."=".$val;
                }
            }
        } 
        else
        {
            // go through each value and build the where clause of the query
            foreach($identifier as $val)
            {
                // check for the type the value is
                if (is_array($val))
                {
                    // check to make sure that all of the required keys are set
                    if (!isset($val[self::ID_KEY]))
                    {
                        throw new Exception(
                                "invalid array identifier: key not set");
                    }
                    if (!isset($val[self::ID_SIGN]))
                    {
                        throw new Exception(
                                "invalid array identifier: sign not set");
                    }
                    if (!isset($val[self::ID_VAL]))
                    {
                        throw new Exception(
                                "invalid array identifier: val not set");
                    }
                    // check to see if 'func' is set... if it isn't, then
                    // we escape the string and wrap it around quotes,
                    // if it is set, then let it string go unaffected
                    $value = "";
                    if (isset($val[self::ID_ESCAPE]))
                    {
                        if ($val[self::ID_ESCAPE])
                        {
                            $value = "'".$this->conn->escape_string(
                                    $val[self::ID_VAL])."'";
                        }
                        else
                        {
                            $value = $val[self::ID_VAL];
                        }
                    }
                    else
                    {
                        $value = "'".$this->conn->escape_string($val[self::ID_VAL])."'";
                    }
                    // include this into the search
                    $search .= $val[self::ID_KEY]." ".$val[self::ID_SIGN]." ".$value;
                }
                else if (is_string($val))
                {
                    $search .= " $val ";
                }
                else
                {
                    throw new Exception("invalid array identifier");
                }
            }
        }
        return " WHERE $search";
    }

}

?>
