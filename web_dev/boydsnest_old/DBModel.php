<?php

require_once 'Array.php';

/**
 * the getMapInfo is the most important function and the most
 * complicated function. this function can return null, making
 * DBModel do the default things. but, you can make DBModel do
 * most of the easy validation automatically by filling this array in
 * for each field. so, here is a basic description of all the
 * key values and what there meanings are, and what the defaults are
 * if they are not filled out:
 *
 * $returning => (
 *  'colName' => array(
 *          'required' => true/false
 *              // default: false
 *              // explination: this tells the system if this is a required field.
 *              // this is important because if the get() function is called, it can be
 *              // set to only get the fields that are required... else it can be used
 *              // for telling the system to grab information from different tables.
 *              // also, if you dont want to even worry about this, you can override
 *              // the function that actually builds the get query
 *          'validation' => array(
 *              'type'      => string, int, bool
 *              'maxlength' => int // checks the max length of a string
 *              'minlength' => int // checks the min length of a string
 *              'minvalue'  => int // checks for a min value
 *              'maxvalue'  => int // checks for a max value
 *              'pregmatch' => string // runs the string to see if it matchs
 *              'enum'      => array of strings // checks to see if the value
 *                                              // is one of the string in the
 *                                              // given array.
 *              'callfunc'  => string // a func to be called. the function must
 *                                    // return true if it passes the test. also
 *                                    // the ONLY param sent to the function will
 *                                    // be the piece of data in question.
 *          )
 *      )
 * )
 */

/**
 * Description of DBModel
 *
 * @author REx
 */
abstract class DBModel {

    const DATE_FORMAT_COMMON = "Y-m-d H:i:s";

    const AUTO_REQUIRED     = 'required';

    const AUTO_VALIDATION   = 'validation';

    const AUTO_VALIDATION_TYPE          = 'type';

    const AUTO_VALIDATION_TYPE_STRING   = 'string';

    const AUTO_VALIDATION_TYPE_INT      = 'int';

    const AUTO_VALIDATION_MAXLENGTH     = 'maxlength';

    const AUTO_VALIDATION_MINLENGTH     = 'minlength';

    const AUTO_VALIDATION_MAXVALUE      = 'maxvalue';
    
    const AUTO_VALIDATION_MINVALUE      = 'minvalue';

    const AUTO_VALIDATION_PREGMATCH     = 'pregmatch';

    const AUTO_VALIDATION_CALLFUNC      = 'callfunc';

    const AUTO_VALIDATION_ENUM          = 'enum';

    const FIND_KEY  = 'key';

    const FIND_SIGN = 'sign';

    const FIND_VAL  = 'val';

    const FIND_FUNC = 'func';

    const INFO_VALUE    = 'value';

    const INFO_QUOTE    = 'quote';

    const INFO_ESCAPE   = 'escape';

    /**
     * throw an Exception with the validation error, otherwise don't
     * return anything.
     */
    protected abstract function isValid(&$convolvedData);

    protected abstract function getTableName();

    protected abstract function getIdColumnName();

    protected abstract function getMapInfo();

    protected $conn;

    protected $identifier;

    protected $newData;

    protected $currData;

    protected $needsCommit;

    protected $needsGet;

    protected $isCollection;

    protected $mapInfo;

    /**
     *
     * @param DBConnect $conn
     * @param <type> $identifier this can be null, and array, or a number.
     * if it is null, then the DBModel assumes that it is a new object and
     * will insert attempt an insert on commit(), and get() will throw an exception.
     * if it is a number, then it will assume that it is an id. if it is an array,
     * there are two ways it can handle the array depending on the context:
     * first context:
     * array(
     *  1 => 123,
     *  2 => 234,
     *  3 => 345
     * )
     * and so on for all of the primary keys for all of the object you want
     * to update, get, or delete. basically, the values in the array are considered
     * to be the primary keys because they are all numbers.
     * second context:
     * array(
     *  1 => array(
     *      'key'   => 'colName1',
     *      'sign'  => '!=',
     *      'val'   => 'someval'
     *  ),
     *  2 => "&&",
     *  3 => array(
     *      'key'   => 'colName2',
     *      'sign'  => '!=',
     *      'val'   => 'someval'
     *  ),
     *  4 => "|| (",
     *  5 => array(
     *      'key'   => 'colName1',
     *      'sign'  => '=',
     *      'val'   => 'someval'
     *  ),
     *  6 => "&&",
     *  7 => array(
     *      'key'   => 'colName3',
     *      'sign'  => '<>',
     *      'val'   => 'NOW()',
     *      'func'  => 1
     *  ),
     *  8 => ")"
     * )
     * which would result in the logic:
     *      colName1!='someval' && colName2!='someval' || (colName1='someval' && colName3<>'someval')
     * with every value in all the arrays excaped from the given connection
     * @param <type> $newData this is an array of all the default new data.
     * this can be null as well if you want to set all of the values via
     * setValue.
     *
     * @note DBModel can work as a collection or as a single entity. if it is
     * working as a collection (like $identifier return more then 1 recored
     * from the database in a given where clause), then the commit, get, and
     * deletes will reflect that as well. if you call delete, it will delete
     * all of the records that are returned from the resulting where clause.
     * there are also special functions, one of which is NOW(). this basically
     * makes DBModel not escape and surround the result with quotes
     */
    public function  __construct(DBConnect &$conn, $identifier){
        if ($conn == null){
            throw new Exception('$conn must be a valid DBConnect object');
        }
        if (!is_numeric($identifier) && !is_array($identifier) && !is_null($identifier)){
            throw new InvalidArgumentException("\$identifier is not a valid param");
        }

        $this->conn = $conn;
        $this->mapInfo      = $this->getMapInfo();
        $this->currData     = array();
        $this->newData      = array();
        $this->isCollection = false;
        $this->needsCommit  = false;

        if ($identifier == null){
            $this->identifier   = null;
            $this->needsGet     = false;
        } else {
            $this->identifier   = $identifier;
            $this->needsGet     = true;
        }
    }

    /**
     * this resets everything in the object. it is a good idea to do this
     * though because then you dont have to make a new object.
     * @param <type> $identifier
     */
    public function setIdentifier($identifier){
        if (!is_numeric($identifier) && !is_array($identifier) && !is_null($identifier)){
            throw new InvalidArgumentException("\$identifier is not a valid param");
        }
        $this->currData     = array();
        $this->newData      = array();
        $this->isCollection = false;
        $this->needsCommit  = false;

        if ($identifier == null){
            $this->identifier   = $identifier;
            $this->needsGet     = true;
        } else {
            $this->identifier   = null;
            $this->needsGet     = false;
        }
    }

    /**
     * 
     * @return <type> 
     */
    public function getIdentifier(){
        return $this->identifier;
    }

    /**
     * returns whether or not this model represents of collection of
     * instances
     * @return <boolean>
     */
    public function isCollection(){
        return $this->isCollection;
    }

    /**
     * @throws Exception if the given instance is not a collection
     * @return <int> the size of the collection
     */
    public function size(){
        if ($this->isCollection){
            return sizeof($this->currData);
        } else {
            return sizeof($this->currData)!=0 ? 1 : 0;
        }
    }

    /**
     * if (that part of the array is an value of array)
     *      $val['sign'] the sign that is used for comparing
     *      $val['val']  the value to be checked
     *      $val['key']  the key
     *      total = escape($val['key']).escape($val['sign'])."'".escape($val['val'])."'"
     * else
     *      total = $val
     *
     * @param array $params
     */
    protected function consumeIdentifier(){
        $identifier = $this->identifier;
        if (is_null($identifier)){
            return "";
        }
        if (is_numeric($identifier)){
            return $this->getIdColumnName()."=".$identifier;
        }
        if (!is_array($identifier)){
            throw new Exception("invalid identifier type");
        }
        ksort($identifier);
        reset($identifier);
        $search = "";
        if (is_numeric(current($identifier))){
            // if the first value is a number, then we can deduce
            // that all of the values are ids of the records this
            // model is manipulating. so, we just get the the
            // id column name for the table and set each one to the
            // value, and or all of them together
            foreach($identifier as $val){
                if (!is_numeric($val)){
                    if ($search != ""){
                        $search .= " || ";
                    }
                    $search .= $this->getIdColumnName()."=".$val;
                }
            }
        } else {
            // go through each value and build the where clause of the query
            foreach($identifier as $val){
                // check for the type the value is
                if (is_array($val)){
                    // check to make sure that all of the required keys are set
                    if (!isset($val[DBModel::FIND_KEY])){
                        throw new Exception("invalid array identifier: key not set");
                    }
                    if (!isset($val[DBModel::FIND_SIGN])){
                        throw new Exception("invalid array identifier: sign not set");
                    }
                    if (!isset($val[DBModel::FIND_VAL])){
                        throw new Exception("invalid array identifier: val not set");
                    }
                    // check to see if 'func' is set... if it isn't, then
                    // we escape the string and wrap it around quotes,
                    // if it is set, then let it string go unaffected
                    $value = "";
                    if (isset($val[DBModel::FIND_FUNC])){
                        if ($val[DBModel::FIND_FUNC]){
                            $value = $val[DBModel::FIND_VAL];
                        } else {
                            $value = "'".$this->conn->escapeString($val[DBModel::FIND_VAL])."'";
                        }
                    } else {
                        $value = "'".$this->conn->escapeString($val[DBModel::FIND_VAL])."'";
                    }
                    // include this into the search
                    $search .=
                        // escape the key just incase
                        $this->conn->escapeString($val[DBModel::FIND_KEY]).
                            // escape the sign just incase
                            $this->conn->escapeString($val[DBModel::FIND_SIGN]).
                        $value;
                } else if (is_string($val)){
                    $search .= " $val ";
                } else {
                    throw new Exception("invalid array identifier");
                }
            }
        }
        return $search;
    }

    protected function consumeRequiredFields(){
        if (!is_array($this->mapInfo)){ return "*"; }
        if (sizeof($this->mapInfo) == 0){ return "*"; }
        $result = "";
        foreach($this->mapInfo as $col => $data){
            if (isset($data[DBModel::AUTO_REQUIRED])){
                if ($data[DBModel::AUTO_REQUIRED]){
                    if ($result != ""){
                        $result .= ",";
                    }
                    $result .= $col;
                }
            }
        }
        return $result;
    }

    /**
     * 
     * @param <type> $key
     * @param <type> $value
     * @param <type> $escape
     * @param <type> $quote 
     */
    public function setValue($key, $value, $escape = true, $quote = true){
        $this->newData[$key] = array(
            self::INFO_VALUE    => $value,
            self::INFO_QUOTE    => $quote,
            self::INFO_ESCAPE   => $escape
        );
        $this->needsCommit = true;
    }

    /**
     * 
     * @param <string> $key
     * @param <int> $index
     * @return <mixed>
     */
    public function getValue($key, $index = 0){
        if ($this->needsGet){
            throw new Exception("you must call get() before you can get any values");
        }
        if ($index){
            if (!$this->isCollection){
                throw new Exception("collection context call when object is not a collection");
            }
            if (array_key_exists($index, $this->currData)){
                if (array_key_exists($key, $this->currData[$index])){
                    return $this->currData[$index][$key];
                }
                throw new Exception("key $key not found in given collection object");
            }
            throw new Exception("index $index not found in collection array");
        } else {
            if ($this->isCollection){
                throw new Exception("non-collection context call when object is a collection");
            }
            if (array_key_exists($key, $this->currData)){
                return $this->currData[$key];
            }
            throw new Exception("$key is not a key for the given data model");
        }
    }

    /**
     * deletes all of data this object represents and then unsets itself
     */
    public function delete(){
        if (!$this->identifier){
            throw new Exception("cannot call delete if there is no assigned id");
        }
        try {
            $this->conn->quickQuery($this->createDeleteQuery());
            $this->conn->commit();
            unset($this);
        } catch(Exception $e){
            $this->conn->rollback();
            throw new Exception("An Error Occurred While Attempting To Delete: ".$e->getMessage());
        }
        
    }

    /**
     * 
     * @param <boolean> $getAll tells DBModel to get all of the fields. if you
     * need to get information from multiple tables, and you override key functions
     * to get the desired result.
     * @param <int> $limitStart the start of a limit clause
     * @param <int> $limitAmount the amount for the limit clause
     */
    public function get($getAll = false, $limitStart = false, $limitAmount = false){
        // if there is no identifier, then there can be no select
        if ($this->identifier == null){
            throw new Exception("cannot get an object without its primary key");
        }
        $gets = "";
        if ($getAll){
            $gets = "*";
        } else {
            $gets = $this->consumeRequiredFields();
        }
        $limit = "";
        if ($limitStart !== false && $limitAmount !== false){
            $limit = "LIMIT $limitStart, $limitAmount";
        }

        // do a quick query and tell DBConnect to hand back an array of the
        // row data
        $this->currData = $this->conn->quickQuery(
                $this->createGetQuery($gets, $limit),
                true);

        if ($this->currData == null){
            $this->isCollection = false;
            
        } else if (sizeof($this->currData) > 1){
            // if more than one record is found, then the return is considered
            // a collection.
            
            // flag the instance that it is a collection
            $this->isCollection = true;

            // go over every row of data and get the primary key of every
            // record. this helps the system
            $this->identifier = array();
            foreach($this->currData as $key => $node){
                $this->identifier[$key] = $node[$this->getIdColumnName()];
                unset($this->currData[$this->getIdColumnName()]);
            }
        } else {
            $this->isCollection = false;
            $this->currData = $this->currData[0];
            $this->identifier = $this->currData[$this->getIdColumnName()];
            unset($this->currData[$this->getIdColumnName()]);
        }
        $this->needsGet = false;
    }

    /**
     * commits the data to the presistance layer
     * @throws Exception on db error, or if this call causes an exception
     * from a lower system
     */
    public function commit(){
        if (sizeof($this->newData) == 0){
            $this->needsCommit = false;
            return;
        }
        try {
            $dataTo = null;
            // it has to check every convolved value of. so if it is
            // a collection, then there has to be an iteration, if not,
            // then just one can be checked
            if ($this->isCollection){
                $dataTo = array();
                foreach($this->currData as $key => $val){
                    // validate every record
                    $insert = array();
                    foreach($this->newData as $key => $value){
                        $insert[$key] = $value[self::INFO_VALUE];
                    }
                    $dataTo[$key] = FCoreArray::convolve($val, $insert);
                    $this->validate($dataTo[$key]);
                }
            } else {
                $insert = array();
                foreach($this->newData as $key => $value){
                    $insert[$key] = $value[self::INFO_VALUE];
                }
                $dataTo = FCoreArray::convolve($this->currData, $insert);
                $this->validate($dataTo);
            }

            // figure out if its an insert of update
            $command = ($this->identifier) ? "UPDATE" : "INSERT INTO";

            // create the set values
            $sets = "";
            foreach($this->newData as $key => $val){
                if ($sets != ""){
                    $sets .= ",";
                }
                $sets .= $this->conn->escapeString($key)."=".
                        ($val[self::INFO_QUOTE] ? "'" : "").
                            ($val[self::INFO_ESCAPE] ?
                                $this->conn->escapeString($val[self::INFO_VALUE]) :
                                $val[self::INFO_VALUE]).
                        ($val[self::INFO_QUOTE] ? "'" : "");
            }

            // do a quick query because everything is already escaped
            $this->conn->quickQuery($this->createCommitQuery($command, $sets));

            // if the identifier wasn't set, then it was an insert, so
            // set the identifier to the last insert
            if (!$this->identifier){
                $this->identifier = $this->conn->getLastInsert();
            }
            
            if ($this->conn->isError()){
                $this->conn->rollback();
                throw new Exception("DB Error: " . $this->conn->getError());
            } else {
                $this->conn->commit();
                // now that everything is done, set the data
                $this->currData = $dataTo;
                // set the defaults back
                $this->newData = array();
                $this->needsCommit = false;
            }
        } catch(Exception $e) {
            $this->conn->rollback();
            throw new Exception("An Error Occurred While In \$DBModel->commit() Call: " . $e->getMessage());
        }
    }

    protected function createGetQuery($gets, $limit){
        return "SELECT $gets FROM ".
                    $this->getTableName().
                " WHERE ".$this->consumeIdentifier().($limit != "" ? " $limit" : "");
    }

    protected function createDeleteQuery(){
        return "DELETE FROM ".
                    $this->getTableName().
                " WHERE ".$this->consumeIdentifier();
    }

    protected function createCommitQuery($command, $sets){
        return "$command "
                .$this->getTableName().
            " SET $sets".
            ($this->identifier ? " WHERE ".$this->consumeIdentifier() : "");
    }

    /**
     * this does not return true of false. instead, it will return
     * nothing on success, and will throw an exception on failure
     * @param <type> $convolvedData
     */
    public function validate(&$convolvedData = null){
        if ($convolvedData == null){
            $convolvedData = FCoreArray::convolve(
                    $this->currData, $this->newData);
        }
        // get all the map data and then iterate over all of the
        // columns of data to do auto validation
        foreach($this->mapInfo as $column => $config){
            if (!is_array($config)){
                throw new Exception("Validation Error: $column is configured incorrectly");
            }
            // first, check if the data is in the convolved data,
            // because if it is not then there is no reason to continue
            if (!isset($convolvedData[$column])){
                continue;
            }
            // now check if the validation array is
            if (isset($config[DBModel::AUTO_VALIDATION])){
                if (!is_array($config[DBModel::AUTO_VALIDATION])){
                    throw new Exception("Validation Error: $column validation is configured incorrectly");
                }
                // if datatype validation is set, do that
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_TYPE])){
                    switch($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_TYPE]){
                        case DBModel::AUTO_VALIDATION_TYPE_STRING:
                            if (!is_string($convolvedData[$column])){
                                throw new Exception("Validation Error: $column is not a string");
                            }
                            break;
                        case DBModel::AUTO_VALIDATION_TYPE_INT:
                            if (!is_numeric($convolvedData[$column])){
                                throw new Exception("Validation Error: $column is not an int");
                            }
                            break;
                        default:
                            throw new Exception("Validation Error: $column has an unknown datatype");
                            break;
                    }
                }
                // string length validation
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MAXLENGTH])){
                    if (strlen($convolvedData[$column]) >
                            $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MAXLENGTH]){
                        throw new Exception("Validation Error: $column is too long of a string");
                    }
                }
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MINLENGTH])){
                    if (strlen($convolvedData[$column]) <
                            $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MINLENGTH]){
                        throw new Exception("Validation Error: $column is too short of a string");
                    }
                }
                // number validation
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MAXVALUE])){
                    if ($convolvedData[$column] >
                            $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MAXVALUE]){
                        throw new Exception("Validation Error: $column is too small of a number");
                    }
                }
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MINVALUE])){
                    if ($convolvedData[$column] <
                            $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_MINVALUE]){
                        throw new Exception("Validation Error: $column is too large of a number");
                    }
                }
                // preg match check
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_PREGMATCH])){
                    if (preg_match($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_PREGMATCH],
                            $convolvedData[$column]) == 0){
                        throw new Exception("Validation Error: $column does not match the given regular expression");
                    }
                }
                // call a function
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_CALLFUNC])){
                    if (is_callable($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_CALLFUNC])){
                        $func = $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_CALLFUNC];
                        if (!$func($convolvedData[$column])){
                            throw new Exception("Validation Error: $column failed a user defined function");
                        }
                    } else {
                        throw new Exception(
                                "Validation Error: " .
                                $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_CALLFUNC] .
                                " is not callable");
                    }
                }
                // enum validation
                if (isset($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_ENUM])){
                    if (!is_array($config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_ENUM])){
                        throw new Exception("Validation Error: $column is configured incorrectly");
                    }
                    if (!array_search(
                            $convolvedData[$column],
                            $config[DBModel::AUTO_VALIDATION][DBModel::AUTO_VALIDATION_ENUM])){
                        throw new Exception("Validation Error: $column failed the enum test");
                    }
                }
            }
        }
        $this->isValid($convolvedData);
    }

}

?>
