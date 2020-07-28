<?php

/**
 * base defines for files and urls
 */
define("BN_DIR_BASEPATH",   str_replace("\\", "/", dirname(__FILE__)));

define("BN_URL_BASEHOST",   "http://localhost/boydsnest");


/**
 * directories
 */
// <editor-fold defaultstate="collapsed" desc="directories">
define("BN_DIR_CONTROLLERS",    BN_DIR_BASEPATH . "/_app/controllers");
// </editor-fold>


/**
 * db factory definitions
 */
// <editor-fold defaultstate="collapsed" desc="DBFactory definitions">
define("BN_DBFACTORY_PAGEFOLLOWERMODEL",  "PageFollowerFactory");

define("BN_DBFACTORY_PAGERIGHTSMODEL",    "PageRightsFactory");

define("BN_DBFACTORY_PAGEMODEL",          "PagesFactory");

define("BN_DBFACTORY_SCHEMEMODEL",        "SchemeFactory");

define("BN_DBFACTORY_USERMANUALMODEL",    "UserManualFactory");

define("BN_DBFACTORY_USERMODEL",          "UsersFactory");
// </editor-fold>


/**
 * the definitions for the database
 */
// <editor-fold defaultstate="collapsed" desc="database definitions">
define("USERS", 'users');
define("USERS_USERID", 'userID');
define("USERS_USERNAME", 'username');
define("USERS_PASSWORD", 'password');
define("USERS_EMAIL", 'email');
define("USERS_SALT", 'salt');
define("USERS_SECRETQUESTION", 'secretQuestion');
define("USERS_SECRETANSWER", 'secretAnswer');
define("USERS_MASTERNOTES", 'masterNotes');
define("USERS_SCHEMEUSING", 'schemeUsing');
define("USERS_CANDOWNLOAD", 'canDownload');
define("USERS_CANUPLOAD", 'canUpload');
define("USERS_CANMESSAGE", 'canMessage');
define("USERS_CANCDSELF", 'canCDSelf');
define("USERS_CANCDOTHER", 'canCDOther');
define("USERS_CANSCHEME", 'canScheme');
define("USERS_ISFAMILY", 'isFamily');
define("USERS_ISLOGGED", 'isLogged');
define("USERS_ISMASTER", 'isMaster');
define("USERS_ISACTIVE", 'isActive');
define("USERS_ISPERMISSIONED", 'isPermissioned');
define("USERS_CREATEDWHEN", 'createdWhen');
define("USERS_EXPIRESWHEN", 'expiresWhen');
define("USERS_LASTUPDATE", 'lastUpdate');
define("USERS_DEFAULTRIGHT", 'defaultRight');
define("USERS_DEFAULTRIGHT_NONE", 0);
define("USERS_DEFAULTRIGHT_SEE", 1);
define("USERS_DEFAULTRIGHT_COMMENT", 2);
define("USERS_DEFAULTRIGHT_WRITE", 3);

define("PAGERIGHTS",    'pageRights');

define("PAGEFOLLOWERS", 'pageFollowers');

define("PAGETITLE",     'title');
define("PAGEPRIVATE",   'private');
define("PAGETYPE",      'type');

define("USERMANUAL", 'userManual');
define("USERMANUAL_PAGEID", 'pageID');
define("USERMANUAL_CONTENT", 'content');
define("USERMANUAL_TITLE", 'title');
define("USERMANUAL_RANK", 'rank');

define("SCHEME", 'scheme');
define("SCHEME_SCHEMEID", 'schemeID');
define("SCHEME_SCHEMENAME", 'schemeName');
define("SCHEME_BACKGROUND_COLOR", "background_color");
define("SCHEME_BACKGROUND_COLOR_ACCENT", "background_color_accent");
define("SCHEME_BACKGROUND_COLOR_ACCENT_2",  "background_color_accent_2");
define("SCHEME_MAIN_MENU_LINK_HOVER_COLOR", "main_menu_link_hover_color");
define("SCHEME_MAIN_MENU_LINK_COLOR", "main_menu_link_color");
define("SCHEME_MAIN_MENU_INDEX_PIC", "main_menu_index_pic");
define("SCHEME_TEXT_COLOR", "text_color");
define("SCHEME_TEXT_COLOR_ACCENT", "text_color_accent");
// </editor-fold>


/**
 * the definitions for the urls
 */
// <editor-fold defaultstate="collapsed" desc="url definitions">
define("BN_URL_CSS",                BN_URL_BASEHOST . "/public/css");

define("BN_URL_JQUERY",             BN_URL_BASEHOST . "/public/javascript/jQuery.js");

define("BN_URL_JAVASCRIPT",         BN_URL_BASEHOST . "/public/javascript");

define("BN_URL_ASSETS",             BN_URL_BASEHOST . "/public/assets");

define("BN_URL_AJAX",               BN_URL_BASEHOST . "/public/ajax");

define("BN_URL_PAGE_ADMIN",         BN_URL_BASEHOST . "/public/pages/admin.php");

define("BN_URL_PAGE_CONTACTUS",     BN_URL_BASEHOST . "/public/pages/contactus.php");

define("BN_URL_PAGE_HOME",          BN_URL_BASEHOST . "/public/pages/home.php");

define("BN_URL_PAGE_INDEX",         BN_URL_BASEHOST . "/index.php");

define("BN_URL_PAGE_LOGIN",         BN_URL_BASEHOST . "/public/pages/login.php");

define("BN_URL_PAGE_LOGOUT",        BN_URL_BASEHOST . "/public/pages/logout.php");

define("BN_URL_PAGE_TERMS",         BN_URL_BASEHOST . "/public/pages/terms.php");

define("BN_URL_PAGE_USERMANUAL",    BN_URL_BASEHOST . "/public/pages/usermanual.php");
// </editor-fold>


/**
 * log types
 */
// <editor-fold defaultstate="collapsed" desc="log types">
define("BN_LOGTYPE_FAILEDLOGIN",    "failedlogin");

define("BN_LOGTYPE_ERROR",          "error");

function BN_LOGTYPE_USERLOG($user_id = false)
{
    if (!$user_id)
    {
        $user_id = BoydsnestSession::GetInstance()->get(USERS_USERID);
    }
    return USERS.$user_id;
}
// </editor-fold>


/**
 * data type origins
 */
// <editor-fold defaultstate="collapsed" desc="datatype origins">
define("BN_DATATYPE_USERPAGES",     "userpages");

define("BN_DATATYPE_PAGERESPONSES", "pageresponses");
// </editor-fold>


/**
 * pages thread types
 */
// <editor-fold defaultstate="collapsed" desc="thread types">
define("BN_PAGETHREADTYPE_NONE",    "none");

define("BN_PAGETHREADTYPE_SINGLE",  "single");

define("BN_PAGETHREADTYPE_MULTI",   "multi");
// </editor-fold>


/**
 * we do the require after base dir because we need that for fcore
 * configuration and such
 */
if (!defined("BN_START_NO_FCORE"))
{
    require_once '_private/Config.php';
}


/**
 * Description of BoydsnestSession
 *
 * @author REx
 */
// <editor-fold defaultstate="collapsed" desc="boydsnest session impementation">
define("BN_SESSION_PAGE_STATE",     "session_page_state");

if (!defined("BN_START_NO_SESSION"))
{
    require_once FCORE_FILE_SESSION;
    session_start();
    class BoydsnestSession extends Session
    {

        protected static $instance;

        public function __construct()
        {
            parent::__construct(
                    FCore::$SESSION_CHECK_FINGERPRINT,
                    FCore::$SESSION_CHECK_HIJACK);
        }

        public function setupUser($user)
        {
            $_SESSION[USERS_USERID]         = $user[USERS_USERID];
            $_SESSION[USERS_USERNAME]       = $user[USERS_USERNAME];
            $_SESSION[USERS_SCHEMEUSING]    = $user[USERS_SCHEMEUSING];
            $_SESSION[USERS_CANDOWNLOAD]    = $user[USERS_CANDOWNLOAD];
            $_SESSION[USERS_CANUPLOAD]      = $user[USERS_CANUPLOAD];
            $_SESSION[USERS_CANMESSAGE]     = $user[USERS_CANMESSAGE];
            $_SESSION[USERS_CANCDSELF]      = $user[USERS_CANCDSELF];
            $_SESSION[USERS_CANCDOTHER]     = $user[USERS_CANCDOTHER];
            $_SESSION[USERS_CANSCHEME]      = $user[USERS_CANSCHEME];
            $_SESSION[USERS_ISFAMILY]       = $user[USERS_ISFAMILY];
            $_SESSION[USERS_ISLOGGED]       = $user[USERS_ISLOGGED];
            $_SESSION[USERS_ISMASTER]       = $user[USERS_ISMASTER];
            $_SESSION[USERS_ISACTIVE]       = $user[USERS_ISACTIVE];
            $_SESSION[USERS_ISPERMISSIONED] = $user[USERS_ISPERMISSIONED];
            $_SESSION[USERS_CREATEDWHEN]    = $user[USERS_CREATEDWHEN];
            $_SESSION[USERS_EXPIRESWHEN]    = $user[USERS_EXPIRESWHEN];
            $_SESSION[USERS_LASTUPDATE]     = $user[USERS_LASTUPDATE];
            $_SESSION[USERS_DEFAULTRIGHT]   = $user[USERS_DEFAULTRIGHT];
        }

        public static function init()
        {
            self::$instance = new BoydsnestSession();
            FCore::$SESSION = self::$instance;
        }

        /**
         *
         * @return BoydsnestSession
         */
        public static function GetInstance()
        {
            return self::$instance;
        }

    } BoydsnestSession::init();
}
// </editor-fold>


?>