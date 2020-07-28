<?php


// <editor-fold defaultstate="collapsed" desc="directories">
define("FCORE_BASE_PATH", str_replace("\\", "/", dirname(__FILE__)));

define("FCORE_DIR_CORE",        FCORE_BASE_PATH . "/Core");

define("FCORE_DIR_DATABASE",    FCORE_BASE_PATH . "/Database");

define("FCORE_DIR_HELPER",      FCORE_BASE_PATH . "/Helper");

define("FCORE_DIR_HTML",        FCORE_BASE_PATH . "/Html");

define("FCORE_DIR_INTERFACES",  FCORE_BASE_PATH . "/Interfaces");
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="file paths">
define("FCORE_FILE_ERROR",          FCORE_DIR_CORE . "/Error.php");

define("FCORE_FILE_LOGGER",         FCORE_DIR_CORE . "/Logger.php");

define("FCORE_FILE_DBCOUNTER",      FCORE_DIR_DATABASE . "/helpers/DBCounter.php");

define("FCORE_FILE_DBDATATYPE",     FCORE_DIR_DATABASE . "/helpers/DBDataType.php");

define("FCORE_FILE_DBLOGGER",       FCORE_DIR_DATABASE . "/helpers/DBLogger.php");

define("FCORE_FILE_DBMESSAGE",      FCORE_DIR_DATABASE . "/helpers/DBMessage.php");

define("FCORE_FILE_DBFORUM",        FCORE_DIR_DATABASE . "/helpers/DBForum.php");

define("FCORE_FILE_DBCONNECT",      FCORE_DIR_DATABASE . "/DBConnect.php");

define("FCORE_FILE_DBFACTORY",      FCORE_DIR_DATABASE . "/DBFactory.php");

define("FCORE_FILE_ARRAY",          FCORE_DIR_HELPER . "/Array.php");

define("FCORE_FILE_CACHE",          FCORE_DIR_HELPER . "/Cache.php");

define("FCORE_FILE_CONTROLLER",     FCORE_DIR_HELPER . "/Controller.php");

define("FCORE_FILE_DATACOLLECTION", FCORE_DIR_HELPER . "/DataCollection.php");

define("FCORE_FILE_DATARULES",      FCORE_DIR_HELPER . "/DataRules.php");

define("FCORE_FILE_PAGEMASTER",     FCORE_DIR_HELPER . "/PageMaster.php");

define("FCORE_FILE_SESSION",        FCORE_DIR_HELPER . "/Session.php");

define("FCORE_FILE_BBCONSUMER",     FCORE_DIR_HTML . "/BBConsumer.php");

define("FCORE_FILE_HTML",           FCORE_DIR_HTML . "/Html.php");

define("FCORE_FILE_ERROR_PAGE",     FCORE_DIR_HTML . "/error.php");

define("FCORE_FILE_ACTION",         FCORE_DIR_INTERFACES . "/Action.php");

define("FCORE_FILE_RESPONSE",       FCORE_DIR_INTERFACES . "/Response.php");

define("FCORE_FILE_VALIDATABLE",    FCORE_DIR_INTERFACES . "/Validatable.php");
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="extensions">
define("FCORE_EXT_CACHE", "cache");
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="error codes">
$error_codes = array(
    '400' => array(
        'header' => 'Bad Request',
        'content' => 'Your browser sent a request that this server could not understand.'),
    '401' => array(
        'header' => 'Authorization Required',
        'content' => 'This server could not verify that you are authorized to ' .
        'access the document requested. Either you supplied the ' .
        'wrong credentials (e.g., bad password), or your browser ' .
        'doesn\'t understand how to supply the credentials required.'),
    '402' => array(
        'header' => 'Payment Required',
        'content' => 'INTERROR'),
    '403' => array(
        'header' => 'Forbidden',
        'content' => 'You don\'t have permission to access THEREQUESTURI on this ' .
        'server.'),
    '404' => array(
        'header' => 'Not Found',
        'content' => 'We couldn\'t find <acronym title="THEREQUESTURI">that uri' .
        '</acronym> on our server, though it\'s most certainly not ' .
        'your fault.'),
    '405' => array(
        'header' => 'Method Not Allowed',
        'content' => 'The requested method THEREQMETH is not allowed for the URL ' .
        'THEREQUESTURI.'),
    '406' => array(
        'header' => 'Not Acceptable',
        'content' => 'An appropriate representation of the requested resource ' .
        'THEREQUESTURI could not be found on this server.'),
    '407' => array(
        'header' => 'Proxy Authentication Required',
        'content' => 'This server could not verify that you are authorized to ' .
        'access the document requested. Either you supplied the wrong ' .
        'credentials (e.g., bad password), or your browser doesn\'t ' .
        'understand how to supply the credentials required.'),
    '408' => array(
        'header' => 'Request Time-out',
        'content' => 'Server timeout waiting for the HTTP request from the client.'),
    '409' => array(
        'header' => 'Conflict',
        'INTERROR'),
    '410' => array(
        'header' => 'Gone',
        'content' => 'The requested resourceTHEREQUESTURIis no longer available on ' .
        'this server and there is no forwarding address. Please remove ' .
        'all references to this resource.'),
    '411' => array(
        'header' => 'Length Required',
        'content' => 'A request of the requested method GET requires a valid ' .
        'Content-length.'),
    '412' => array(
        'header' => 'Precondition Failed',
        'content' => 'The precondition on the request for the URL THEREQUESTURI ' .
        'evaluated to false.'),
    '413' => array(
        'header' => 'Request Entity Too Large',
        'content' => 'The requested resource THEREQUESTURI does not allow request ' .
        'data with GET requests, or the amount of data provided in the ' .
        'request exceeds the capacity limit.'),
    '414' => array(
        'header' => 'Request-URI Too Large',
        'content' => 'The requested URL\'s length exceeds the capacity limit for ' .
        'this server.'),
    '415' => array(
        'header' => 'Unsupported Media Type',
        'content' => 'The supplied request data is not in a format acceptable for ' .
        'processing by this resource.'),
    '416' => array(
        'header' => 'Requested Range Not Satisfiable',
        'content' => ''),
    '417' => array(
        'header' => 'Expectation Failed',
        'content' => 'The expectation given in the Expect request-header field could ' .
        'not be met by this server. The client sent <code>Expect:</code>'),
    '422' => array(
        'header' => 'Unprocessable Entity',
        'content' => 'The server understands the media type of the request entity, but ' .
        'was unable to process the contained instructions.'),
    '423' => array(
        'header' => 'Locked',
        'content' => 'The requested resource is currently locked. The lock must be released ' .
        'or proper identification given before the method can be applied.'),
    '424' => array(
        'header' => 'Failed Dependency',
        'content' => 'The method could not be performed on the resource because the requested ' .
        'action depended on another action and that other action failed.'),
    '425' => array(
        'header' => 'No code',
        'content' => 'INTERROR'),
    '426' => array(
        'header' => 'Upgrade Required',
        'content' => 'The requested resource can only be retrieved using SSL. The server is ' .
        'willing to upgrade the current connection to SSL, but your client ' .
        'doesn\'t support it. Either upgrade your client, or try requesting ' .
        'the page using https://'),
    '500' => array(
        'header' => 'Internal Server Error',
        'content' => 'INTERROR'),
    '501' => array(
        'header' => 'Method Not Implemented',
        'content' => 'GET to THEREQUESTURI not supported.'),
    '502' => array(
        'header' => 'Bad Gateway',
        'content' => 'The proxy server received an invalid response from an upstream server.'),
    '503' => array(
        'header' => 'Service Temporarily Unavailable',
        'content' => 'The server is temporarily unable to service your request due to ' .
        'maintenance downtime or capacity problems. Please try again later.'),
    '504' => array(
        'header' => 'Gateway Time-out',
        'content' => 'The proxy server did not receive a timely response from the ' .
        'upstream server.'),
    '505' => array(
        'header' => 'HTTP Version Not Supported',
        'content' => 'INTERROR'),
    '506' => array(
        'header' => 'Variant Also Negotiates',
        'content' => 'A variant for the requested resource <code>THEREQUESTURI</code> ' .
        'is itself a negotiable resource. This indicates a configuration error.'),
    '507' => array(
        'header' => 'Insufficient Storage',
        'content' => 'The method could not be performed on the resource because the ' .
        'server is unable to store the representation needed to successfully ' .
        'complete the request. There is insufficient free space left in your ' .
        'storage allocation.'),
    '510' => array(
        'header' => 'Not Extended',
        'content' => 'A mandatory extension policy in the request is not accepted by the ' .
        'server for this resource.')
);
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="required includes">
require_once FCORE_FILE_ERROR;
require_once FCORE_FILE_LOGGER;
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="FCore">
class FCore
{
    static $DIR_ROOT_DBFACTORIES        = null;
    static $DIR_ROOT_MASTERS            = null;
    static $DIR_ROOT_COMMONOBJECTS      = null;
    static $DIR_ROOT_COMMONVIEWS        = null;

    static $LOG_DB_TABLE        = null;
    static $LOG_DB_MESSAGE_COL  = null;
    static $LOG_TO_DB           = null;
    static $LOG_TO_MAIL         = null;
    static $LOG_TO_HTML         = null;
    static $LOG_TO_FILE         = null;
    static $LOG_TO_FILE_LEVEL   = null;
    static $LOG_TO_DB_LEVEL     = null;
    static $LOG_TO_MAIL_LEVEL   = null;
    static $LOG_TO_HTML_LEVEL   = null;
    static $LOG_GLOBAL_LEVEL    = null;

    static $CACHE       = null;
    static $CACHE_DIR   = null;

    static $EMAIL_WEBMASTER         = null;
    static $EMAIL_WEBMASTER_FROM    = null;
    static $EMAIL_WEBMASTER_ERROR   = null;
    static $EMAIL_WEBMASTER_NOTICE  = null;

    static $SESSION                     = null;
    static $SESSION_CHECK_FINGERPRINT   = null;
    static $SESSION_CHECK_HIJACK        = null;
    
    static $STATISTIC_TABLE     = null;
    static $LOGIN_MAX_ATTEMPTS  = null;
    static $LOGIN_TIME_FROZE    = null;

    private static $DB_DEFAULT_USER = null;
    private static $DB_DEFAULT_PASS = null;
    private static $DB_DEFAULT_DB   = null;
    private static $DB_DEFAULT_HOST = null;
    private static $GLOBAL_LOGGER   = null;

    /**
     *
     * @param array $inits
     */
    public static function init(array $inits)
    {
        foreach ($inits as $key => $value)
        {
            self::$$key = $value;
        }
        if (self::$LOG_GLOBAL_LEVEL != null)
        {
            self::$GLOBAL_LOGGER = new Logger(self::$LOG_GLOBAL_LEVEL);
        }
        else
        {
            self::$GLOBAL_LOGGER = new Logger(1);
        }
        set_error_handler("FCoreErrorHandler");
        set_exception_handler("FCoreExceptionHandler");
        register_shutdown_function("FCoreExitHandler");
    }

    /**
     *
     * @global DBConnect $__conn
     * @return DBConnect
     */
    public static function GetDefaultConnection()
    {
        static $__conn;
        require_once FCORE_FILE_DBCONNECT;
        if ($__conn == null)
        {
            $__conn = new DBConnect(self::$GLOBAL_LOGGER);
        }
        if (!$__conn->isConnected())
        {
            $__conn->connect(self::$DB_DEFAULT_HOST,
                    self::$DB_DEFAULT_USER,
                    self::$DB_DEFAULT_PASS,
                    self::$DB_DEFAULT_DB);
        }
        return $__conn;
    }

    /**
     *
     * @return Logger
     */
    public static function GetLogger()
    {
        return self::$GLOBAL_LOGGER;
    }

    /**
     *
     * @global <type> $error_codes
     * @param <type> $num
     */
    public static function ShowError($num, $info)
    {
        if (ob_get_length() > 0)
        {
            ob_end_clean();
        }
        global $error_codes;
        extract($error_codes[$num]);
        include(FCORE_FILE_ERROR_PAGE);
        echo $info;
        exit();
    }
    /**
     * loads a db factory
     * @param <type> $factory
     * @param <type> $conn
     * @return DBFactory
     */
    public static function LoadDBFactory($factory, $conn = null, $object_cache = true)
    {
        $logger = self::GetLogger();
        if ($logger != null)
        {
            $logger->log(Logger::LEVEL_DEBUG,
                    "$factory factory attempting to load");
        }

        if ($object_cache)
        {
            if (!isset(self::$db_factories[$factory]))
            {
                self::$db_factories[$factory] = self::LoadClass(
                    self::$DIR_ROOT_DBFACTORIES . "/$factory.php", $factory, $conn);
            }
            return self::$db_factories[$factory];
        }
        else
        {
            return self::LoadClass(
                self::$DIR_ROOT_DBFACTORIES . "/$factory.php", $factory, $conn);
        }
    }
    private static $db_factories = array();

    /**
     *
     * @param <type> $master
     * @return PageMaster
     */
    public static function LoadMaster($master = 'default')
    {
        $logger = self::GetLogger();
        if ($logger != null)
        {
            $logger->log(Logger::LEVEL_DEBUG,
                "$master master attempting to load");
        }
        $file = self::$DIR_ROOT_MASTERS . "/$master.php";
        if (!is_file($file))
        {
            throw new Exception("$master.php not found");
        }
        require_once FCORE_FILE_PAGEMASTER;
        return new PageMaster($file);
    }

    /**
     *
     * @param <type> $view
     * @return <type>
     */
    public static function LoadView($view)
    {
        $logger = self::GetLogger();
        if ($logger != null)
        {
            $logger->log(Logger::LEVEL_DEBUG,
                    "$view view attempting to load");
        }
        $file = self::$DIR_ROOT_COMMONVIEWS . "/$view";
        if (!is_file($file))
        {
            throw new ControllerException(
                    "cannot find $file while trying to load a view");
        }
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        return $contents;
    }

    /**
     *
     * @param <type> $view
     * @param <type> $params
     * @param <type> $returnContents
     * @return <type>
     */
    public static function LoadViewPHP($view, $params = array())
    {
        $logger = self::GetLogger();
        if ($logger != null)
        {
            $logger->log(Logger::LEVEL_DEBUG,
                    "$view view attempting to load");
        }
        if (!is_array($params) && !is_null($params))
        {
            throw new Exception("\$params is not null or array");
        }
        $file = self::$DIR_ROOT_COMMONVIEWS . "/$view.php";
        if (!is_file($file))
        {
            throw new Exception("$view.php not found");
        }
        extract($params);
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        return $contents;
    }

    /**
     *
     * @param <type> $object
     * @param <type> $data
     * @param <type> $auto_check
     * @return class
     */
    public static function LoadObject($object, $data = null)
    {
        $logger = self::GetLogger();
        if ($logger != null)
        {
            $logger->log(Logger::LEVEL_DEBUG,
                    "$object object attempting to load");
        }
        $file = self::$DIR_ROOT_COMMONOBJECTS . "/$object.php";
        if (!is_file($file))
        {
            throw new Exception(
                    "cannot find $file while trying to load a model");
        }
        include_once($file);
        $info = pathinfo($file);
        $class = $info['filename'];
        $instance;
        if ($data != null)
        {
            $instance = new $class($data);
        }
        else
        {
            $instance = new $class();
        }
        return $instance;
    }

    /**
     * the helper that does the actual loading of classes
     * @param <type> $file
     * @param <type> $data
     * @return class
     */
    public static function LoadClass($file, $class, $data)
    {
        if (!is_file($file))
        {
            throw new Exception("$file not found");
        }
        include_once($file);
        if ($data == null)
        {
            return new $class();
        }
        return new $class($data);
    }

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="fcore definitions">
define("RESULT", 'result');
define("RESULT_SUCCESS", 1);
define("RESULT_FAILED", 2);
define("RESULT_PASS", 3);
define("RESULT_FAIL", 4);
define("ACTION", 'action');
define("ACTION_ATTEMPT",    'attempt');
define("ACTION_UPDATE",     'update');
define("ACTION_CREATE",     'create');
define("ACTION_PASS",       'pass');
define("ACTION_RIGHTS",     'rights');
define("ACTION_DELETE",     'delete');
define("ACTION_VIEW",       'view');
define("ACTION_READ",       'read');
define("ACTION_WRITE",      'write');
define("ACTION_LIST",       'list');
define("ACTION_LOG",        'log');
define("ACTION_UNKNOWN",    'unknown');
define("ACTION_DEFAULT",    'defualt');

define("SESSION_VALIDATED", 'valdiated');
define("SESSION_FINGERPRINT", 'fingerprint');
define("SESSION_LOGIN_ATTEMPTS", 'login_attempts');
define("SESSION_TIMEOUT_UNTIL", 'timeout_until');
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="helper funcitons">
/**
 * this is the basic print string function... it includes an '\n' at the end
 * @param <type> $str the string to print
 */
function echoln($str = "")
{
    echo $str . "\n";
}

/**
 * this is just like echoln(), but it also does an html break
 * @param <type> $str the string to print
 */
function htmlln($str = "")
{
    echo $str . "<br />\n";
}

/**
 * 
 * @param <type> $base
 * @param <type> $params 
 */
function buildurl($base, $params)
{
    $result = "$base?";
    if (is_string($params))
    {
        $result .= "?$params";
    } 
    else if (is_array($params))
    {
        $first = true;
        foreach($params as $key => $param)
        {
            if ($first)
            {
                $first = false;
            }
            else 
            {
                $result .= "&";
            }
            $result .= $key;
            if (!is_null($param))
            {
                $result .= "=$param";
            }
        }
    }
    return $result;
}

/**
 *
 * @param <type> $where 
 */
function Redirect($where)
{
    header("Location: $where");
}

/**
 *
 * @param <type> $var
 * @param <type> $default
 * @return <type> 
 */
function IsSetGet($var, $default = false)
{
    if (isset($_GET[$var]))
    {
        return $_GET[$var];
    }
    return $default;
}

/**
 *
 * @param <type> $var
 * @param <type> $default
 * @return <type> 
 */
function IsSetPost($var, $default = false)
{
    if (isset($_POST[$var]))
    {
        return $_POST[$var];
    }
    return $default;
}

/**
 *
 * @param <type> $var
 * @param <type> $default
 * @return <type>
 */
function IsSetGetPost($var, $default = false)
{
    if (isset($_GET[$var]))
    {
        return $_GET[$var];
    }
    if (isset($_POST[$var]))
    {
        return $_POST[$var];
    }
    return $default;
}

/**
 * 
 * @param <type> $var
 * @param <type> $default
 * @return <type>
 */
function IsSetRequest($var, $default = false)
{
    if (isset($_REQUEST[$var]))
    {
        return $_REQUEST[$var];
    }
    return $default;
}

/**
 * 
 * @param <type> $var
 * @param <type> $default
 * @return <type>
 */
function IsSetSession($var, $default = false)
{
    if (isset($_SESSION[$var]))
    {
        return $_SESSION[$var];
    }
    return $default;
}

/**
 * 
 * @param <type> $var
 * @param <type> $default
 */
function IsSetEcho(&$var, $default = '')
{
    if (isset($var))
    {
        echo $var;
    }
    echo $default;
}

/**
 * 
 * @param array $rules
 * @return <type>
 */
function GrabDataFromGlobal(DataRules &$rules)
{
    $result = array();
    foreach($rules->get_rule_keys() as $key)
    {
        $rule = $rules->get_rule($key);
        $attempt;
        if (!isset($rule[DataRules::METHOD]))
        {
            continue;
        }
        switch($rule[DataRules::METHOD])
        {
            case DataRules::METHOD_GET:
                $attempt = IsSetGet($key, false);
                break;
            case DataRules::METHOD_POST:
                $attempt = IsSetPost($key, false);
                break;
            case DataRules::METHOD_SESSION:
                $attempt = IsSetSession($key, false);
                break;
        }
        if ($attempt !== false)
        {
            $result[$key] = $attempt;
        }
    }
    return $result;
}

/**
 *
 * @param <type> $key
 * @param <type> $salt
 * @return <type> 
 */
function GetSecondOrderHash($key, $salt)
{
    return sha1($salt.sha1($key));
}

/**
 *
 * @return <type> 
 */
function GetNewSalt()
{
    return substr(md5(uniqid(rand(), true)), 0, 3);
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="file functions">
/**
 *
 * @param <type> $file
 * @return <type>
 */
function File_ReadDump($file)
{
    $fh = null;
    if (!$fh = @fopen($path, 'rb'))
    {
        return false;
    }
    flock($fp, LOCK_SH);
    $data = '';
    if (filesize($file) > 0)
    {
        $data = & fread($fp, filesize($file));
    }
    flock($fp, LOCK_UN);
    fclose($fp);
    return $data;
}

/**
 *
 * @param <string> $file the absolute path to the file
 * @param <type> $data the data to be dumped in the file
 * @return <boolean> true on success, false otherwise
 */
function File_WriteDump($file, $data, $append = true)
{
    if (!$fh = @fopen($path, $append ? "ab" : 'mb'))
    {
        return false;
    }
    flock($fh, LOCK_EX);
    fwrite($fh, $data);
    flock($fh, LOCK_UN);
    fclose($fh);
    return true;
}

/**
 * the main algorithm here is from codeigniter
 * @param <type> $dir
 * @return <type>
 */
function File_ClearDir($path, $del_dir = FALSE, $level = 0)
{
    // Trim the trailing slash
    $path = rtrim($path, DIRECTORY_SEPARATOR);

    if (!$current_dir = @opendir($path))
    {
        return FALSE;
    }

    while (FALSE !== ($filename = @readdir($current_dir)))
    {
        if ($filename != "." and $filename != "..")
        {
            if (is_dir($path . DIRECTORY_SEPARATOR . $filename))
            {
                // Ignore empty folders
                if (substr($filename, 0, 1) != '.')
                {
                    File_ClearDir($path . DIRECTORY_SEPARATOR .
                            $filename, $del_dir, $level + 1);
                }
            } 
            else
            {
                unlink($path . DIRECTORY_SEPARATOR . $filename);
            }
        }
    }
    @closedir($current_dir);

    if ($del_dir == TRUE AND $level > 0)
    {
        return @rmdir($path);
    }

    return TRUE;
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="cookies">
/**
 *
 * @param <type> $key
 * @return <type>
 */
function Cookie_Get($key)
{
    return $_COOKIE[$key];
}

/**
 *
 * @param <type> $key
 * @param <type> $value
 * @param <type> $expire
 * @param <type> $path
 * @param <type> $domain
 * @param <type> $secure
 * @param <type> $httponly
 */
function Cookie_Set($key, $value, $expire = 0, $path = '', $domain = '', $secure = false, $httponly = true)
{
    setcookie($name, $value, $expire, $path, $domain, $secure, $httponly);
}

/**
 *
 * @param <type> $key
 */
function Cookie_Delete($key)
{
    setcookie($key, false, time() - 3600);
    unset($_COOKIE[$key]);
}
// </editor-fold>


?>