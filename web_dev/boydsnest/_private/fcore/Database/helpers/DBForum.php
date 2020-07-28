<?php

require_once 'DBDataType.php';

class DBForumException extends Exception
{
    public function __construct($message = "")
    {
        parent::__construct($message);
    }
}

/**
 * Description of DBForum
 *
 * @author REx
 */
class DBForum
{
    const DATATYPE_SELF_ORIGIN  = 'fcoreforum';

    const FCORE_FORUM       = 'fcore_forum';

    const FCORE_FORUM_POST  = 'fcore_forum_post';


    const FORUM_ID  = 'forumID';

    const POST_ID   = 'postID';


    const ORIGIN_TYPE   = 'origin_type';

    const ORIGIN_ID     = 'origin_id';


    const TIMEMADE      = 'timeMade';

    const POSTDATATYPE  = 'postDataType';

    const POSTORDER     = 'post_order';

    const POSTCHILDREN  = 'post_children';

    const POSTINDENT    = 'post_indent';

    const POSTPARENT    = 'post_parent';

    const LASTUSED      = 'lastUsed';

    const METADATA      = 'metadata';

    public static $valid_data_types = array(
        DBDataType::DATATYPE_TEXT, DBDataType::DATATYPE_MTEXT,
    );

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @param <type> $datatype
     * @param <type> $metadata
     * @return <type> 
     */
    public static function CreateForum(
            $origin_type,
            $origin_id,
            $datatype,
            $metadata = '')
    {
        if ($origin_type == null || $origin_type == "")
        {
            throw new DBDataTypeException(
                    '$origin_type cannot be null or empty');
        }
        if (!is_null($origin_id) && !is_numeric($origin_id))
        {
            throw new DBDataTypeException(
                    '$origin_id cannot be null or non numeric');
        }
        if (!is_null($datatype) && !in_array($datatype, self::$valid_data_types))
        {
            throw new DBDataTypeException(
                    '$datatype cannot be null or invalid type');
        }
        if (!is_string($metadata))
        {
            throw new DBDataTypeException(
                    '$metadata must be a string');
        }
        $db = null;
        try
        {
            $db =& FCore::GetDefaultConnection();
            $forum_data = $db->quick_query(
                    "SELECT ".self::FORUM_ID." FROM ".self::FCORE_FORUM." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type' AND
                        ".self::ORIGIN_ID."=$origin_id", true);
            if ($forum_data != null)
            {
                throw new DBDataTypeException(
                        "Already Have A Record With That Origin Type And Id");
            }
            $db->quick_query(
                    "INSERT INTO ".self::FCORE_FORUM." SET
                        ".self::ORIGIN_TYPE."='$origin_type',
                        ".self::ORIGIN_ID."=$origin_id,
                        ".self::POSTDATATYPE."='$datatype',
                        ".self::METADATA."='$metadata',
                        ".self::TIMEMADE."=NOW()");
            $db->commit();
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id 
     */
    public static function DeleteForum(
            $origin_type, $origin_id)
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
            $forum_id = $db->quick_query(
                    "SELECT ".self::FORUM_ID." FROM ".self::FCORE_FORUM." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type' AND
                        ".self::ORIGIN_ID."=$origin_id", true);
            if ($forum_id == null)
            {
                return;
            }
            $forum_id = $forum_id[0][self::FORUM_ID];
            $all_post_ids = $db->quick_query(
                    "SELECT ".self::POST_ID." FROM ".self::FCORE_FORUM_POST." WHERE
                        ".self::FORUM_ID."=$forum_id", true);
            foreach($all_post_ids as $post_id)
            {
                DBDataType::DeleteDataFromOrigin(
                        self::build_datatype_origin($origin_type),
                        $post_id[self::POST_ID]);
            }
            $db->quick_query(
                    "DELETE FROM ".self::FCORE_FORUM." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type' AND
                        ".self::ORIGIN_ID."=$origin_id");
            $db->commit();
            return $all_post_ids;
        }
        catch(Exception $e)
        {
            if ($db)
            {
                $db->rollback();
            }
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @return DBForum
     */
    public static function GetForum(
            $origin_type, $origin_id)
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
            $forum_data = $db->quick_query(
                    "SELECT * FROM ".self::FCORE_FORUM." WHERE
                        ".self::ORIGIN_TYPE."='$origin_type' AND
                        ".self::ORIGIN_ID."=$origin_id", true);
            if ($forum_data == null)
            {
                return null;
            }
            return new DBForum($forum_data[0]);
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $origin_type
     * @param <type> $origin_id
     * @return <type> 
     */
    public static function GetFirstRootIdOfForum(
            $origin_type, $origin_id)
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
            $post_id = $db->quick_query(
                    "SELECT ".self::POST_ID." FROM ".self::FCORE_FORUM_POST." WHERE
                        ".self::FORUM_ID."=(SELECT ".self::FORUM_ID." FROM ".self::FCORE_FORUM_POST." WHERE
                            ".self::ORIGIN_TYPE."='$origin_type' AND
                            ".self::ORIGIN_ID."=$origin_id LIMIT 1)
                        AND
                        ".self::POSTPARENT."=0
                        ORDER BY ".self::POSTORDER, true);
            if ($post_id == null)
            {
                return null;
            }
            return $post_id[0][self::POST_ID];
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    private static function validate_content($content, $datatype)
    {
        if (!is_string($content))
        {
            throw new DBForumException('$content must be a string');
        }
        switch($datatype)
        {
            case DBDataType::DATATYPE_TEXT:
                if (strlen($content) > 65000)
                {
                    throw new DBForumException(
                            '$content cannot be greater than 65000 characters');
                }
                break;
            case DBDataType::DATATYPE_MTEXT:
                if (strlen($content) > 16777000)
                {
                    throw new DBForumException(
                            '$content cannot be greater than 16777000 characters');
                }
                break;
            default:
                throw new DBForumException(
                        "$datatype is not a valid datatype");
                break;
        }
    }

    private $forum_id;

    private $post_datatype;

    private $time_made;

    private $last_used;

    private $origin_type;

    private $origin_id;

    private $metadata;

    /**
     *
     * @var DBConnect
     */
    private $db;

    public function get_forum_id()      { return $this->forum_id; }
    public function get_post_datatype() { return $this->post_datatype; }
    public function get_time_made()     { return $this->time_made; }
    public function get_last_used()     { return $this->last_used; }
    public function get_origin_type()   { return $this->origin_type; }
    public function get_origin_id()     { return $this->origin_id; }
    public function get_metadata()      { return $this->metadata; }

    /**
     *
     * @param <type> $metadata 
     */
    public function set_metadata($metadata)
    {
        self::validate_content($metadata, DBDataType::DATATYPE_TEXT);
        try
        {
            $metadata = $this->db->escape_string($metadata);
            $this->db->quick_query(
                    "UPDATE ".self::FCORE_FORUM." SET
                        ".self::METADATA."='$metadata' WHERE
                        ".self::FORUM_ID."=$this->forum_id");
            $this->db->commit();
        }
        catch(Exception $e)
        {
            $this->db->rollback();
            throw new DBForumException($e->getMessage());
        }
    }

    private function __construct($forum_data)
    {
        $this->forum_id         = $forum_data[self::FORUM_ID];
        $this->post_datatype    = $forum_data[self::POSTDATATYPE];
        $this->time_made        = $forum_data[self::TIMEMADE];
        $this->last_used        = $forum_data[self::LASTUSED];
        $this->origin_type      = $forum_data[self::ORIGIN_TYPE];
        $this->origin_id        = $forum_data[self::ORIGIN_ID];
        $this->metadata         = $forum_data[self::METADATA];
        $this->db               =& FCore::GetDefaultConnection();
    }

    /**
     *
     * @param <type> $post_origin_type
     * @param <type> $post_origin_id
     * @param <type> $content
     * @param <type> $order
     * @param <type> $parent the parent of the post... if this is set to zero,
     * then it assumes root
     */
    public function post_create(
            $post_origin_type,
            $post_origin_id,
            $content, $parent = 0, $order = false, $metadata = '')
    {
        self::validate_content($content, $this->post_datatype);
        if (!is_numeric($parent))
        {
            throw new DBForumException(
                    '$parent must be numeric');
        }
        if ($post_origin_type == null || $post_origin_type == "")
        {
            throw new DBForumException(
                    '$post_origin_type cannot be null or empty');
        }
        if (!is_null($post_origin_id) && !is_numeric($post_origin_id))
        {
            throw new DBForumException(
                    '$post_origin_id cannot be null or non numeric');
        }
        if (!is_bool($order) && !is_numeric($order))
        {
            throw new DBForumException(
                    '$order must be boolean or numeric');
        }
        if (!is_string($metadata))
        {
            throw new DBDataTypeException(
                    '$metadata must be a string');
        }
        try
        {
            if ($parent != 0)
            {
                if (null == $this->db->quick_query(
                        "SELECT ".self::POST_ID." FROM ".self::FCORE_FORUM_POST." WHERE
                            ".self::FORUM_ID."=$this->forum_id AND
                            ".self::POST_ID."=$parent", true))
                {
                    throw new Exception(
                            'parent id for the post does not exist in this thread');
                }
            }
            if (is_bool($order))
            {
                $order = $this->db->quick_query(
                        "SELECT max(".self::POSTORDER.") FROM ".self::FCORE_FORUM_POST." WHERE
                            ".self::POSTPARENT."=$parent AND
                            ".self::FORUM_ID."=$this->forum_id", true);
                if ($order == null)
                {
                    $order = 0;
                }
                else
                {
                    $order = $order[0]["max(".self::POSTORDER.")"];
                    $order++;
                }
            }
            $this->db->quick_query(
                    "INSERT INTO ".self::FCORE_FORUM_POST." SET
                        ".self::FORUM_ID."=$this->forum_id,
                        ".self::POSTPARENT."=$parent,
                        ".self::ORIGIN_ID."=$post_origin_id,
                        ".self::ORIGIN_TYPE."='$post_origin_type',
                        ".self::POSTORDER."=$order,
                        ".self::METADATA."='$metadata'");
            $post_id = $this->db->get_last_insert();
            DBDataType::CreateData(
                    self::build_datatype_origin($this->origin_type),
                    $post_id,
                    $this->post_datatype,
                    $content);
            $this->db->commit();
            return $post_id;
        }
        catch(Exception $e)
        {
            $this->db->rollback();
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $post_id
     * @param <type> $delete_children 
     */
    public function post_delete($post_id, $delete_children = false)
    {
        if (!is_numeric($post_id))
        {
            throw new DBForumException('$post_id must be numeric');
        }
        if (!is_bool($delete_children))
        {
            throw new DBForumException('$delete_children must be bool');
        }
        try
        {
            if ($delete_children)
            {
                $children_ids = $this->db->quick_query(
                        "SELECT ".self::POST_ID." FROM ".self::FCORE_FORUM_POST." WHERE 
                            ".self::POSTPARENT."=$post_id AND 
                            ".self::FORUM_ID."=$this->forum_id", true);
                if ($children_ids != null)
                {
                    foreach($children_ids as $child_id)
                    {
                        $this->post_delete($child_id[self::POST_ID], true);
                    }
                }
            }
            else
            {
                $new_parent = $this->db->quick_query(
                        "SELECT ".self::POSTPARENT." FROM ".self::FCORE_FORUM_POST." WHERE
                            ".self::POST_ID."=$post_id AND 
                            ".self::FORUM_ID."=$this->forum_id", true);
                if ($new_parent != null)
                {
                    $new_parent = $new_parent[0][self::POSTPARENT];
                    $this->db->quick_query(
                            "UPDATE ".self::FCORE_FORUM_POST." SET
                                ".self::POSTPARENT."=$new_parent WHERE
                                ".self::POSTPARENT."=$post_id AND 
                                ".self::FORUM_ID."=$this->forum_id");
                }
            }
            DBDataType::DeleteDataFromOrigin(
                    self::build_datatype_origin($this->origin_type),
                    $post_id);
            $this->db->quick_query(
                    "DELETE FROM ".self::FCORE_FORUM_POST." WHERE 
                        ".self::POST_ID."=$post_id AND 
                        ".self::FORUM_ID."=$this->forum_id");
            $this->db->commit();
        }
        catch(Exception $e)
        {
            $this->db->rollback();
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $post_id
     * @param <type> $new_content
     * @param <type> $new_parent
     * @param <type> $new_order
     * @return <type> 
     */
    public function post_update(
            $post_id,
            $new_content = false,
            $new_parent = false,
            $new_order  = false,
            $metadata   = false)
    {
        if (!is_numeric($post_id))
        {
            throw new DBForumException(
                    '$post_id must be numeric');
        }
        if (!is_bool($new_parent) && !is_numeric($new_parent))
        {
            throw new DBForumException(
                    '$new_parent must be numeric or boolean');
        }
        if (!is_bool($new_order) && !is_numeric($new_order))
        {
            throw new DBForumException(
                    '$new_order must be numeric or boolean');
        }
        if (!is_bool($new_content))
        {
            self::validate_content($new_content, $this->post_datatype);
        }
        if (!is_bool($metadata) && !is_string($metadata))
        {
            throw new DBDataTypeException(
                    '$metadata must be a string');
        }
        if (is_bool($new_content) && 
            is_bool($new_parent) &&
            is_bool($new_order) &&
            is_bool($metadata))
        {
            return;
        }
        try
        {
            if (!is_bool($new_parent) || !is_bool($new_order) || !is_bool($metadata))
            {
                if (is_bool($new_order) && !is_bool($new_parent))
                {
                    $new_order = $this->db->quick_query(
                            "SELECT max(".self::POSTORDER.") FROM ".self::FCORE_FORUM_POST." WHERE
                                ".self::POSTPARENT."=$new_parent AND
                                ".self::FORUM_ID."=$this->forum_id", true);
                    if ($new_order == null)
                    {
                        $new_order = 0;
                    }
                    else
                    {
                        $new_order = $new_order[0]["max(".self::POSTORDER.")"];
                        $new_order++;
                    }
                }
                $set = "";
                if (!is_bool($new_parent))
                {
                    if ($new_parent != 0)
                    {
                        $count = $this->db->quick_query(
                                "SELECT COUNT(*) FROM ".self::FCORE_FORUM_POST." WHERE
                                    ".self::FORUM_ID."=$this->forum_id &&
                                    ".self::POST_ID."=$new_parent",
                                true);
                        $count = $count[0]["COUNT(*)"];
                        if ($count == 0)
                        {
                            throw new Exception(
                                    "the parent id does not exist in this forum");
                        }
                    }
                    $set .= self::POSTPARENT."=$new_parent";
                    if (!is_bool($new_order) || !is_bool($metadata))
                    {
                        $set .= ",";
                    }
                }
                if (!is_bool($new_order))
                {
                    $set .= self::POSTORDER."=$new_order";
                    if (!is_bool($metadata))
                    {
                        $set .= ",";
                    }
                }
                if (!is_bool($metadata))
                {
                    $set .= self::METADATA."='$metadata'";
                }

                $this->db->quick_query(
                        "UPDATE ".self::FCORE_FORUM_POST." SET
                            $set
                            WHERE
                            ".self::POST_ID."=$post_id AND
                            ".self::FORUM_ID."=$this->forum_id");
                $this->db->commit();
            }
            if (is_string($new_content))
            {
                DBDataType::UpdateContentFromOrigin(
                        $new_content,
                        self::build_datatype_origin($this->origin_type),
                        $post_id);
            }
        }
        catch(Exception $e)
        {
            $this->db->rollback();
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $post_id
     * @return <type>
     */
    public function post_get_content($post_id)
    {
        if (!is_numeric($post_id))
        {
            throw new DBForumException(
                    '$post_id must be numeric');
        }
        return DBDataType::GetDataContentFromOrigin(
                self::build_datatype_origin($this->origin_type),
                $post_id);
    }

    /**
     *
     * @param int $post_id
     * @return array
     */
    public function post_get($post_id, $content = true)
    {
        if (!is_numeric($post_id))
        {
            throw new DBForumException(
                    '$post_id must be numeric');
        }
        if (!is_bool($content))
        {
            throw new DBForumException(
                    '$content must be boolean');
        }
        try
        {
            $result = $this->db->quick_query(
                    "SELECT * FROM ".self::FCORE_FORUM_POST." WHERE
                        ".self::FORUM_ID."=$this->forum_id &&
                        ".self::POST_ID."=$post_id", true);
            if ($result == null)
            {
                return null;
            }
            $result = $result[0];
            if ($content)
            {
                $result[DBDataType::CONTENT] = DBDataType::GetDataContentFromOrigin(
                        self::build_datatype_origin($this->origin_type),
                        $post_id);
            }
            return $result;
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @return array
     */
    public function post_get_id_list()
    {
        try
        {
            return $this->db->quick_query(
                    "SELECT ".self::POST_ID." FROM ".self::FCORE_FORUM_POST." WHERE
                        ".self::FORUM_ID."=$this->forum_id", true);
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param int $post_id
     * @return array
     */
    public function post_get_children($post_id = 0)
    {
        if (!is_numeric($post_id))
        {
            throw new DBForumException(
                    '$post_id must be numeric');
        }
        try
        {
            return $this->db->quick_query(
                "SELECT * FROM ".self::FCORE_FORUM_POST." WHERE
                    ".self::FORUM_ID."=$this->forum_id &&
                    ".self::POSTPARENT."=$post_id
                    ORDER BY ".self::POSTORDER, true);
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    /**
     *
     * @param <type> $from_post_id
     * @return array
     */
    public function build_hierarchy($root_post_id = 0)
    {
        if (!is_numeric($root_post_id))
        {
            throw new DBForumException(
                    '$from_post_id must be numeric');
        }
        try
        {
            $result = array();
            $all_posts = $this->db->quick_query(
                    "SELECT * FROM ".self::FCORE_FORUM_POST." WHERE
                        ".self::FORUM_ID."=$this->forum_id 
                        ORDER BY ".self::POSTORDER, true);
            if ($all_posts == null)
            {
                return null;
            }
            return $this->to_flatten_array(
                    $this->find_includes($root_post_id, $all_posts));
        }
        catch(Exception $e)
        {
            throw new DBForumException($e->getMessage());
        }
    }

    private function to_flatten_array(&$simi_sorted, $indent = 0)
    {
        $result = array();
        foreach($simi_sorted as $post)
        {
            $insert = $post;
            unset($insert[self::POSTCHILDREN]);
            $insert[self::POSTINDENT] = $indent;
            $result[] = $insert;
            $child_inserts = $this->to_flatten_array(
                    $post[self::POSTCHILDREN],
                    $indent + 1);
            foreach($child_inserts as $child)
            {
                $result[] = $child;
            }
        }
        return $result;
    }

    private function find_includes($parent_id, &$posts)
    {
        if ($posts == null)
        {
            return array();
        }
        $includes = array();
        foreach($posts as $post)
        {
            if ($parent_id == $post[self::POSTPARENT])
            {
                $post[self::POSTCHILDREN] = $this->find_includes(
                        $post[self::POST_ID], $posts);
                $includes[] = $post;
            }
        }
        return $includes;
    }

    protected static function build_datatype_origin($origin_type)
    {
        return $origin_type.self::DATATYPE_SELF_ORIGIN;
    }
}

?>