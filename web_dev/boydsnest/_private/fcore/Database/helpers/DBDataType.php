<?php

class DBDataTypeException extends Exception
{
    public function __construct($message = "")
    {
        parent::__construct($message);
    }
}

/**
 * Description of DBDataType
 *
 * @author REx
 */
class DBDataType
{
    const TABLE_BASE        = 'dataTypeBase';

    const DATATYPE_BLOB     = 'dtBlob';

    const DATATYPE_MBLOB    = 'dtMBlob';

    const DATATYPE_TEXT     = 'dtText';

    const DATATYPE_MTEXT    = 'dtMText';

    public static $valid_data_types = array(
        self::DATATYPE_BLOB, self::DATATYPE_MBLOB,
        self::DATATYPE_TEXT, self::DATATYPE_MTEXT,
    );


    const DATATYPE_ID   = 'dtID';

    const ORIGIN_ID     = 'origin_id';

    const ORIGIN_TYPE   = 'origin_type';

    const DATATYPE      = 'dataType';

    const TIMEMADE      = 'timeMade';

    const LASTUPDATED   = 'lastUpdate';

    const METADATA      = 'metaData';

    const CONTENT       = 'content';

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @param <type> $datatype
     * @param <type> $content
     * @param <type> $metadata 
     */
    public static function CreateData(
            $origin_type,
            $origin_id,
            $datatype,
            $content = null,
            $metadata = null)
    {
        // check values
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        if (!is_null($datatype) && !in_array($datatype, self::$valid_data_types))
        {
            throw new DBDataTypeException('$datatype cannot be null or invalid type');
        }
        $db = null;
        try
        {
            // get default connection
            $db =& FCore::GetDefaultConnection();

            // do query
            $db->quick_query(
                    "INSERT INTO ".self::TABLE_BASE." SET 
                        ".self::ORIGIN_TYPE."='$origin_type',
                        ".self::ORIGIN_ID."=$origin_id,
                        ".self::DATATYPE."='$datatype',
                        ".($metadata != null ? self::METADATA."='$metadata'," : "")."
                        ".self::TIMEMADE."=NOW()");

            // get the last insert for returning and inserting content
            $insert_id = $db->get_last_insert();

            if (!is_string($content))
            {
                $content = "";
            }
            $content = $db->escape_string($content);
            // insert content into the datatype's table
            $db->quick_query(
                    "INSERT INTO $datatype SET
                        ".self::DATATYPE_ID."=$insert_id,
                        ".self::CONTENT."='$content'");

            // dont forget to commit
            $db->commit();
            return $insert_id;
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @param <type> $metadata 
     */
    public static function UpdateMetaData($origin_type, $origin_id, $metadata)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if ($metadata == null || $metadata == "")
        {
            throw new DBDataTypeException('$metadata cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $metadata = $db->escape_string($metadata);
            $db->quick_query(
                    "UPDATE ".self::TABLE_BASE." SET ".self::METADATA."='$metadata' WHERE
                        ".self::ORIGIN_ID."=$origin_id &&
                        ".self::ORIGIN_TYPE."='$origin_type'");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $content
     * @param <type> $datatype_id
     * @param <type> $datatype
     * @return <type> 
     */
    public static function UpdateContentFromID($content, $datatype_id, $datatype = null)
    {
        if (!is_null($datatype) && !in_array($datatype, self::$valid_data_types))
        {
            throw new DBDataTypeException('$datatype cannot be null or invalid type');
        }
        if (!is_null($datatype_id) && !is_numeric($datatype_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        if ($content == null)
        {
            throw new DBDataTypeException('$content cannot be null or empty');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            if ($datatype == null)
            {
                $datatype = $db->quick_query(
                        "SELECT ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                            ".self::DATATYPE_ID."=$datatype_id", true);
                if ($datatype == null)
                {
                    return null;
                }
                $datatype = $datatype[0][self::DATATYPE];
            }
            if (!is_string($content))
            {
                $content = "";
            }
            $content = $db->escape_string($content);
            $db->quick_query(
                    "UPDATE $datatype SET ".self::CONTENT."='$content' WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $content
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @return <type> 
     */
    public static function UpdateContentFromOrigin($content, $origin_type, $origin_id)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        if (!is_string($content))
        {
            throw new DBDataTypeException('$content cannot be null or empty');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $datatype_id = $db->quick_query(
                    "SELECT ".self::DATATYPE_ID.", ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_ID."=$origin_id &&
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
            if ($datatype_id == null)
            {
                return null;
            }
            $datatype = $datatype_id[0][self::DATATYPE];
            $datatype_id = $datatype_id[0][self::DATATYPE_ID];
            $content = $db->escape_string($content);
            $db->quick_query(
                    "UPDATE $datatype SET ".self::CONTENT."='$content' WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     */
    public static function GetPrimaryData($origin_type, $origin_id)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $data = $db->quick_query(
                    "SELECT * FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_ID."=$origin_id &&
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
            if ($data == null)
            {
                return null;
            }
            return $data[0];
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    public static function GetAllPrimaryDataFromOriginType($origin_type)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            return $db->quick_query(
                    "SELECT * FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @return <type>
     */
    public static function GetDataContentFromOrigin($origin_type, $origin_id)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $datatype_id = $db->quick_query(
                    "SELECT ".self::DATATYPE_ID.", ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_ID."=$origin_id &&
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
            if ($datatype_id == null)
            {
                return null;
            }
            $datatype = $datatype_id[0][self::DATATYPE];
            $datatype_id = $datatype_id[0][self::DATATYPE_ID];
            $content = $db->quick_query(
                    "SELECT ".self::CONTENT." FROM $datatype WHERE
                        ".self::DATATYPE_ID."=$datatype_id", true);
            if ($content == null)
            {
                return null;
            }
            return $content[0][self::CONTENT];
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $datatype_id
     * @param <type> $datatype
     * @return <type>
     */
    public static function GetDataContentFromID($datatype_id, $datatype = null)
    {
        if (!is_null($datatype) && !in_array($datatype, self::$valid_data_types))
        {
            throw new DBDataTypeException('$datatype cannot be null or invalid type');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            if ($datatype == null)
            {
                $datatype = $db->quick_query(
                        "SELECT ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                            ".self::DATATYPE_ID."=$datatype_id", true);
                if ($datatype == null)
                {
                    return null;
                }
                $datatype = $datatype[0][self::DATATYPE];
            }
            $content = $db->quick_query(
                    "SELECT ".self::CONTENT." FROM $datatype WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            if ($content == null)
            {
                return null;
            }
            $content[0][self::CONTENT];
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id 
     */
    public static function DeleteDataFromOrigin($origin_type, $origin_id)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException('$origin_id cannot be null or non numeric');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $datatype_id = $db->quick_query(
                    "SELECT ".self::DATATYPE_ID.", ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_ID."=$origin_id &&
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
            if ($datatype_id == null)
            {
                return null;
            }
            $datatype = $datatype_id[0][self::DATATYPE];
            $datatype_id = $datatype_id[0][self::DATATYPE_ID];
            $db->quick_query(
                    "DELETE FROM ".self::TABLE_BASE." WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->quick_query(
                    "DELETE FROM $datatype WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $datatype_id 
     */
    public static function DeleteDataFromID($datatype_id, $datatype = null)
    {
        if ($datatype_id == null || !is_numeric($datatype_id))
        {
            throw new DBDataTypeException('$datatype_id cannot be null or non numeric');
        }
        if (!is_null($datatype) && !in_array($datatype, self::$valid_data_types))
        {
            throw new DBDataTypeException('$datatype cannot be null or invalid type');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            if ($datatype == null)
            {
                $datatype = $db->quick_query(
                        "SELECT ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                            ".self::DATATYPE_ID."=$datatype_id", true);
                if ($datatype == null)
                {
                    return null;
                }
                $datatype = $datatype[0][self::DATATYPE];
            }
            $db->quick_query(
                    "DELETE FROM ".self::TABLE_BASE." WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->quick_query(
                    "DELETE FROM $datatype WHERE
                        ".self::DATATYPE_ID."=$datatype_id");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }

    public static function DeleteAllFromOriginType($origin_type)
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException('$origin_type cannot be null or empty');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $datatype_ids = $db->quick_query(
                    "SELECT ".self::DATATYPE_ID.", ".self::DATATYPE." FROM ".self::TABLE_BASE." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type'", true);
            if ($datatype_ids == null)
            {
                return null;
            }
            foreach($datatype_ids as $data_set)
            {
                $db->quick_query(
                        "DELETE FROM ".self::TABLE_BASE." WHERE
                            ".self::DATATYPE_ID."=".$data_set[self::DATATYPE_ID]);
                $db->quick_query(
                        "DELETE FROM ".$data_set[self::DATATYPE]." WHERE
                            ".self::DATATYPE_ID."=".$data_set[self::DATATYPE_ID]);
            }
            $db->commit();
            return $datatype_ids;
        }
        catch(Exception $e)
        {
            if ($db != null)
            {
                $db->rollback();
            }
            throw new DBDataTypeException($e->getMessage());
        }
    }
}

?>