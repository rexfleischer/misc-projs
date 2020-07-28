<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_HTML;
require_once FCORE_FILE_DBFACTORY;
require_once FCORE_FILE_CONTROLLER;
require_once FCORE_FILE_DATACOLLECTION;
require_once FCORE_FILE_DBMESSAGE;
require_once FCORE_FILE_DBLOGGER;
require_once FCORE_FILE_DBFORUM;
require_once FCORE_FILE_ACTION;

class admin extends Controller
{
    public function __construct()
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/admin";
    }

    public function get_route_map()
    {
        return array(

            self::REDIRECTS => array(
                array(
                    self::REDIRECT_IF   => !BoydsnestSession::GetInstance()->IsLoggedIn(),
                    self::REDIRECT_TO   => 'ERROR_MESSAGE',
                    self::REDIRECT_DATA => "you must be logged in to view any of this content",
                ),
                array(
                    self::REDIRECT_IF   => !BoydsnestSession::GetInstance()->get(USERS_CANMESSAGE),
                    self::REDIRECT_TO   => "ERROR_MESSAGE",
                    self::REDIRECT_DATA => "you are not allowed to write messages",
                    self::REDIRECT_FOR  => array(
                        ACTION.ACTION_WRITE."message",
                        ACTION_WRITE."message"
                    )
                ),
                array(
                    self::REDIRECT_IF   => !BoydsnestSession::GetInstance()->get(USERS_ISMASTER),
                    self::REDIRECT_TO   => "ERROR_MESSAGE",
                    self::REDIRECT_DATA => "you are not allowed to view this data",
                    self::REDIRECT_FOR  => array(
                        ACTION.ACTION_UPDATE."users",
                        ACTION.ACTION_UPDATE."userpass",
                        ACTION.ACTION_DELETE."users",
                        ACTION.ACTION_PASS."usermaster",
                        ACTION_UPDATE."users",
                        ACTION.ACTION_LOG.ACTION_DELETE."users",
                        ACTION_LOG."users",
                        ACTION_LIST."users",
                        ACTION.ACTION_CREATE."users",
                        ACTION_CREATE."users",
                    )
                ),
                array(
                    self::REDIRECT_IF   => !BoydsnestSession::GetInstance()->get(USERS_ISFAMILY),
                    self::REDIRECT_TO   => "ERROR_MESSAGE",
                    self::REDIRECT_DATA => "you are not allowed to view this data",
                    self::REDIRECT_FOR  => array(
                        ACTION.ACTION_DELETE."content",
                        ACTION.ACTION_WRITE."content",
                        ACTION.ACTION_UPDATE."content",
                        ACTION_VIEW."content",
                        ACTION.ACTION_LIST."content",
                        ACTION_LIST."content",
                        ACTION.ACTION_CREATE."content",
                        ACTION_CREATE."content",
                        ACTION.ACTION_UPDATE."response",
                        ACTION.ACTION_DELETE."response",
                        ACTION.ACTION_VIEW."response",
                        ACTION_VIEW."response",
                    )
                )
            ),

            'ERROR_MESSAGE' => 'error_message',

            ACTION_DEFAULT  => 'alerts',
            
            'alerts'        => 'view_alerts',

            //=================================================================
            // everything about USERS
            
            ACTION.ACTION_UPDATE."users"    => 'view_user',
            ACTION.ACTION_UPDATE."userpass" => 'view_user',
            ACTION.ACTION_DELETE."users"    => 'view_user',
            ACTION.ACTION_PASS."usermaster" => 'view_user',
            ACTION_UPDATE."users"           => 'view_user',

            ACTION.ACTION_LOG.ACTION_DELETE."users" => 'view_user_logs',
            ACTION_LOG."users"              => 'view_user_logs',

            ACTION_LIST."users"             => 'list_users',

            ACTION.ACTION_CREATE."users"    => 'create_user',
            ACTION_CREATE."users"           => 'create_user',

            //=================================================================



            //=================================================================
            // everything about CONTENT

            ACTION.ACTION_DELETE."content"  => 'content_view',
            ACTION.ACTION_WRITE."content"   => 'content_view',
            ACTION.ACTION_UPDATE."contentrights" => 'content_view',
            ACTION_VIEW."content"           => 'content_view',

            ACTION.ACTION_LIST."content"    => 'content_list',
            ACTION_LIST."content"           => 'content_list',

            ACTION.ACTION_CREATE."content"  => 'content_create',
            ACTION_CREATE."content"         => 'content_create',

            ACTION.ACTION_UPDATE."response" => 'content_responses_list',
            ACTION.ACTION_DELETE."response" => 'content_responses_list',
            ACTION.ACTION_VIEW."response"   => 'content_responses_list',
            ACTION_VIEW."response"          => 'content_responses_list',

            //=================================================================


            
            //=================================================================
            // everything about PERSONAL
            
            ACTION.ACTION_VIEW."profilepass"        => 'user_profile',
            ACTION.ACTION_VIEW."profile"            => 'user_profile',
            ACTION_VIEW."profile"                   => 'user_profile',

            ACTION.ACTION_WRITE."message"           => 'write_message',
            ACTION_WRITE."message"                  => 'write_message',

            ACTION.ACTION_DELETE."readmessage"      => 'read_message',
            ACTION_READ."message"                   => 'read_message',

            ACTION.ACTION_DELETE."sentmessages"     => 'list_messages',
            ACTION_LIST."sentmessages"              => 'list_messages',
            ACTION.ACTION_DELETE."receivedmessages" => 'list_messages',
            ACTION_LIST."receivedmessages"          => 'list_messages',

            //=================================================================
        );
    }

    //=====================================================
    // misc

    // <editor-fold defaultstate="collapsed" desc="ready_master">
    public function ready_master()
    {
        $page = FCore::LoadMaster();
        $session =& BoydsnestSession::GetInstance();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/admin.css")."\n");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/sidemenu/sidemenu_1.php")."\n");
        $page->apply_string("javascript", "");

        $params = array();
        $params["sidemenu_0"]   = FCore::LoadViewPHP(
                "sidemenu/layout_1",
                array(
                    'title'     => 'Personal',
                    'content'   => $this->load_local_php_view(
                            "personal/views/sidemenu_messages"),
                ));
        $params["sidemenu_1"]   = FCore::LoadViewPHP(
                "sidemenu/layout_1",
                array(
                    'title'     => "Content Management",
                    'content'   => $this->load_local_php_view(
                            "content/views/sidemenu_content")
                ));
        if ($session->get(USERS_ISMASTER))
        {
            $params["sidemenu_2"]   = FCore::LoadViewPHP(
                    "sidemenu/layout_1",
                    array(
                        'title'     => 'Master\'s Menu',
                        'content'   => $this->load_local_php_view(
                            "masters/views/sidemenu_masters"),
                    ));
        }
        $page->apply_param(
                    "main_content",
                    FCore::LoadViewPHP("content/mainAndSideMenus",$params)
                );

        $page->commit_applies();
        return $page;
    }

    public function ready_master_common($view, $params)
    {
        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view($view, $params)
            );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="error_message">
    public function error_message($request, $message)
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("javascript", "");
        $page->apply_string("main_content",
                FCore::LoadViewPHP(
                        "content/general_message",
                        array('message' => $message)));

        $page->commit_applies();
        return $page->get_page();
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="view_alerts">
    public function view_alerts()
    {
        $master = $this->ready_master();
        $master->consume_string(
                "content",
                FCore::LoadViewPHP("content/general_message",
                        array('message' => 'this is still under construction')));

        return $master->get_page();
    }
    // </editor-fold>

    // misc
    //=====================================================


    //=====================================================
    // content

    // <editor-fold defaultstate="collapsed" desc="content_list">
    public function content_list($request, $message = "")
    {
        if ($request == ACTION.ACTION_LIST."content")
        {
            try
            {
                $this->load_local_object("content/objects/UpdateUserPageList");
                $message = "successfully updated list";
            }
            catch(UserActionException $e)
            {
                $prev = $e->getPrevious();
                if ($prev != null)
                {
                    DBLogger::log(
                            BN_LOGTYPE_ERROR,
                            $prev->getMessage());
                    FCore::GetLogger()->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage());
                }
                $message = $e->getMessage();
            }
        }

        $page_list = null;
        try
        {
            $page_list = FCore::LoadObject(
                    "pages/GetUserPageHierarchy",
                    array(
                        "extended" => true
                    ));
            $page_list = $page_list->get_data();
        }
        catch(UserActionException $e)
        {
            $prev = $e->getPrevious();
            if ($prev != null)
            {
                DBLogger::log(
                        BN_LOGTYPE_ERROR,
                        $prev->getMessage());
                FCore::GetLogger()->log(
                        Logger::LEVEL_ERROR,
                        $e->getMessage());
            }
            $message = $e->getMessage();
        }

        return $this->ready_master_common(
                "content/views/content_list", 
                array(
                    'message'   => $message,
                    'page_list' => $page_list,
                ));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="content_view">
    public function content_view($request, $message = "", $new_page = null)
    {
        if ($request == ACTION.ACTION_DELETE."content" ||
            $request == ACTION.ACTION_WRITE."content" ||
            $request == ACTION.ACTION_UPDATE."contentrights")
        {
            try
            {
                switch($request)
                {
                    case ACTION.ACTION_DELETE."content":
                        $this->load_local_object("content/objects/DeleteUserPage");
                        return $this->content_list($request, "successfully deleted page");
                        
                    case ACTION.ACTION_WRITE."content":
                        $this->load_local_object("content/objects/WriteUserPage");
                        $message = "successfully wrote to page";
                        break;
                        
                    case ACTION.ACTION_UPDATE."contentrights":
                        $this->load_local_object("content/objects/UpdateUserPageRights");
                        $message = "successfully updated page";
                        break;
                }
            }
            catch(UserActionException $e)
            {
                $prev = $e->getPrevious();
                if ($prev != null)
                {
                    DBLogger::log(
                            BN_LOGTYPE_ERROR,
                            $prev->getMessage());
                    FCore::GetLogger()->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage());
                }
                $message = $e->getMessage();
            }
        }

        $page_id = IsSetPost(DBForum::POST_ID);
        if (!$page_id)
        {
            $page_id = IsSetGet(DBForum::POST_ID);
        }
        if (!$page_id)
        {
            $page_id = $new_page;
        }

        $page_info = null;
        if ($page_id)
        {
            try
            {
                $page_info = FCore::LoadObject(
                        "pages/GetUserPage",
                        array(
                            'user_id'   => BoydsnestSession::GetInstance()->get(USERS_USERID),
                            'page_id'   => $page_id,
                        ));
            }
            catch(UserActionException $e)
            {
                $prev = $e->getPrevious();
                if ($prev != null)
                {
                    DBLogger::log(
                            BN_LOGTYPE_ERROR,
                            $prev->getMessage());
                    FCore::GetLogger()->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage());
                }
                $message = $e->getMessage();
            }
        }

        $user_list = null;
        if (is_a($page_info, "DataCollection") && $page_info->get(PAGEPRIVATE))
        {
            try
            {
                $user_list = $this->load_local_object(
                        "content/objects/GetUserRightsList");
            }
            catch(UserActionException $e)
            {
                $prev = $e->getPrevious();
                if ($prev != null)
                {
                    DBLogger::log(
                            BN_LOGTYPE_ERROR,
                            $prev->getMessage());
                    FCore::GetLogger()->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage());
                }
                $message = $e->getMessage();
            }
        }

        return $this->ready_master_common(
                "content/views/content_view",
                array(
                    'message'   => $message,
                    'page_info' => is_a($page_info, "DataCollection") ?
                                    $page_info->get_data() : array(),
                    'user_list' => is_a($user_list, "DataCollection") ?
                                    $user_list->get_data() : array()
                ));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="content_create">
    public function content_create($request, $message = "")
    {
        $object = null;
        if ($request == ACTION.ACTION_CREATE."content")
        {
            try
            {
                $object = $this->load_local_object(
                        "content/objects/CreateUserPage");
                $object->do_action();
                return $this->content_view(
                        $request,
                        "successfully created page",
                        $object->get(DBForum::POST_ID));
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
                $prev = $e->getPrevious();
                if ($prev != null)
                {
                    FCore::GetLogger()->log(
                            Logger::LEVEL_ERROR,
                            $prev->getMessage());
                }
            }
        }

        $hierarchy = false;
        try
        {
            $hierarchy = FCore::LoadObject(
                    "pages/GetUserPageHierarchy",
                    array("extended" => true));
            $hierarchy = $hierarchy->get_data();
            if ($hierarchy == null)
            {
                $hierarchy = false;
            }
        }
        catch(UserActionException $e)
        {
            $prev = $e->getPrevious();
            if ($prev != null)
            {
                DBLogger::log(
                        BN_LOGTYPE_ERROR,
                        $prev->getMessage());
                FCore::GetLogger()->log(
                        Logger::LEVEL_ERROR,
                        $e->getMessage());
            }
            $message = $e->getMessage();
        }
        
        return $this->ready_master_common(
                "content/views/content_create", array(
                    'hierarchy' => $hierarchy,
                    'message'   => $message,
                    'user_id'   => BoydsnestSession::GetInstance()->get(USERS_USERID),
                    'data'      => is_a($object, "DataCollection") ? $object->get_data() : array()
                ));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="content_responses_list">
    public function content_responses_list($request, $message = "")
    {
        if ($request == ACTION.ACTION_VIEW."response" ||
            $request == ACTION.ACTION_UPDATE."response" ||
            $request == ACTION.ACTION_DELETE."response")
        {
            
        }

        return $this->ready_master_common(
                "content/views/content_responses_list", array());
    }
    // </editor-fold>

    // content
    //=====================================================

    
    //=====================================================
    // masters

    // <editor-fold defaultstate="collapsed" desc="create_user">
    public function create_user($request)
    {
        $message = "";
        $user_data = false;

        if (ACTION.ACTION_CREATE."users" == $request)
        {
            $user_data = $this->load_local_object("masters/objects/CreateUser");
            try
            {
                $user_data->do_create();
                return $this->view_user(
                        $request,
                        "successfully created user",
                        $user_data->get(USERS_USERID));
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $data_defaults = array();
        try
        {
            $defaults_object = $this->load_local_object(
                    "masters/objects/GetUserDefaults");
            $data_defaults = $defaults_object->get_data();
        }
        catch(Exception $e)
        {
            DBLogger::log(BN_LOGTYPE_ERROR, $e->getMessage());
        }

        if ($user_data)
        {
            $user_data = $user_data->get_data();
            foreach($user_data as $key => $value)
            {
                $data_defaults[$key] = $value;
            }
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "masters/views/create_user", array(
                            'message'       => $message,
                            'user_data'     => $data_defaults,
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="list_users">
    public function list_users($request, $message = "")
    {
        $user_list;
        try
        {
            $user_list = $this->load_local_object("masters/objects/GetUserList");
        }
        catch(UserActionException $e)
        {
            if ($message == "")
            {
                $message = $e->getMessage();
            }
            else
            {
                $message .= "<br />\n".$e->getMessage();
            }
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "masters/views/list_users", array(
                            'message'   => $message,
                            'user_list' => $user_list->get_data(),
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="view_user">
    public function view_user($request, $message = "", $new_user_id = false)
    {
        if (ACTION.ACTION_UPDATE."users" == $request ||
            ACTION.ACTION_UPDATE."userpass" == $request ||
            ACTION.ACTION_DELETE."users" == $request ||
            ACTION.ACTION_PASS."usermaster" == $request)
        {
            try
            {
                switch($request)
                {
                    case ACTION.ACTION_UPDATE."users":
                        $this->load_local_object("masters/objects/UpdateUser");
                        $message = "successfully updated user";
                        break;
                    case ACTION.ACTION_UPDATE."userpass":
                        $this->load_local_object("common/UpdateUserPassword");
                        $message = "successfully updated user";
                        break;
                    case ACTION.ACTION_PASS."usermaster":
                        $this->load_local_object("masters/objects/PassMasterToUser");
                        $message = "successfully passed user.. you will".
                                " not be able to perform any other actions";
                        break;
                    case ACTION.ACTION_DELETE."users":
                        $this->load_local_object("masters/objects/DeleteUser");
                        return $this->list_users($request, "successfully deleted user");
                }
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $user_data;
        try
        {
            $user_data = $this->load_local_object(
                    "masters/objects/GetUserInfo", $new_user_id);
            $user_data = $user_data->get_data();
        }
        catch(UserActionException $e)
        {
            if ($message == "")
            {
                $message = $e->getMessage();
            }
            else
            {
                $message .= "<br />\n".$e->getMessage();
            }
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "masters/views/update_user", array(
                            'message'   => $message,
                            'user_data' => $user_data,
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="view_user_logs">
    public function view_user_logs($request)
    {
        $message = "";
        if (ACTION.ACTION_LOG.ACTION_DELETE."users" == $request)
        {
            try
            {
                $this->load_local_object("masters/objects/DeleteUserLogs");
                $message = "successfully deleted messages";
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $user_logs;
        try
        {
            $user_logs = $this->load_local_object("masters/objects/GetUserLog");
        }
        catch(UserActionException $e)
        {
            if ($message == "")
            {
                $message = $e->getMessage();
            }
            else
            {
                $message .= "<br />\n".$e->getMessage();
            }
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "masters/views/user_logs", array(
                            'message'   => $message,
                            'user_logs' => $user_logs->get_data(),
                            'start'     => $user_logs->get_start(),
                            'amount'    => $user_logs->get_amount(),
                            'username'  => $user_logs->get_username(),
                            'user_id'   => $user_logs->get_user_id(),
                        ))
                );
        return $master->get_page();
        
    }
    // </editor-fold>

    // masters
    //=====================================================


    //=====================================================
    // personal

    // <editor-fold defaultstate="collapsed" desc="user_profile">
    public function user_profile($request, $message = "")
    {
        if ($request == ACTION.ACTION_VIEW."profile" ||
            $request == ACTION.ACTION_VIEW."profilepass")
        {
            try
            {
                switch($request)
                {
                    case ACTION.ACTION_VIEW."profile":
                        $this->load_local_object("personal/objects/UpdateUserProfile");
                        $message = "successfully updated your profile";
                        break;
                    case ACTION.ACTION_VIEW."profilepass":
                        $this->load_local_object("common/UpdateUserPassword");
                        $message = "successfully updated your profile";
                        break;
                    default:
                        $message = "Invalid request";
                        break;
                }
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $user = false;
        try
        {
            $user = $this->load_local_object("personal/objects/GetUserProfile");
        }
        catch(UserActionException $e)
        {
            if ($message == "")
            {
                $message = $e->getMessage();
            }
            else
            {
                $message .= "<br />\n".$e->getMessage();
            }
        }

        return $this->ready_master_common(
                "personal/views/myprofile", array(
                    'message'   => $message,
                    'user'      => $user ? $user->get_data() : null
                ));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="write_message">
    public function write_message($request)
    {
        $message = "";
        if ($request == ACTION.ACTION_WRITE."message")
        {
            try
            {
                $this->load_local_object('personal/objects/WriteMessage');
                $message = "successfully sent message";
                return $this->list_messages($request, $message);
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $user_to            = IsSetPost(USERS_USERID, null);
        $message_content    = IsSetPost(DBMessage::MESSAGE, "");
        $message_title      = IsSetPost(DBMessage::TITLE, "");
        if ($user_to == null)
        {
            $user_to = IsSetGet(USERS_USERID, null);
        }

        $user_list = false;
        try
        {
            $user_list = $this->load_local_object(
                    "personal/objects/GetUserList", $user_to);
            $user_list = $user_list->get_data();
        }
        catch(UserActionException $e)
        {
            $message .= $e->getMessage();
        }


        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "personal/views/writemessage", array(
                            'message_content'   => $message_content,
                            'message_title'     => $message_title,
                            'message'           => $message,
                            'user_list'         => $user_list,
                            'user_id'           => $user_to
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="read_message">
    public function read_message($request, $message = "")
    {
        if (ACTION.ACTION_DELETE."readmessage" == $request)
        {
            try
            {
                $this->load_local_object("personal/objects/DeleteMessages");
                $message = "successfully deleted messages";
                return $this->list_messages($message);
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $title      = "";
        $content    = "";
        $user_id    = null;
        $message_id = 0;
        try
        {
            $message_data = $this->load_local_object(
                    "personal/objects/ReadMessage");
            $title      = $message_data->get(DBMessage::TITLE);
            $content    = $message_data->get(DBMessage::MESSAGE);
            $user_id    = $message_data->get(USERS_USERID);
            $message_id = $message_data->get(DBMessage::MESSAGE_ID);
        }
        catch(UserActionException $e)
        {
            $title      = "Error";
            $content    = $e->getMessage();
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "personal/views/readmessage", array(
                            'title'     => $title,
                            'content'   => $content,
                            'user_id'   => $user_id,
                            'message'   => $message,
                            'message_id'=> $message_id,
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="list_messages">
    public function list_messages($request, $message = false)
    {
        if ($request == ACTION.ACTION_DELETE."sentmessages" ||
                $request == ACTION.ACTION_DELETE."receivedmessages")
        {
            try
            {
                $this->load_local_object("personal/objects/DeleteMessages");
                $message = "successfully deleted messages";
            }
            catch(UserActionException $e)
            {
                $message = $e->getMessage();
            }
        }

        $title = "";

        // 'from' and 'to' are magic strings that just tell the
        // loaded object to get the messages that are either from
        // the user or to the user
        $message_list = null;
        $origin = "to";
        if ($request == ACTION.ACTION_DELETE."sentmessages" ||
                $request == ACTION_LIST."sentmessages")
        {
            $origin = 'from';
            $message_list = $this->load_local_object(
                    "personal/objects/GetMessageList", $origin);
            $message_list = $message_list->get_data();
        }
        else
        {
            $origin = 'to';
            $message_list = $this->load_local_object(
                    "personal/objects/GetMessageList", $origin);
            $message_list = $message_list->get_data();
        }

        $master = $this->ready_master();
        $master->consume_string(
                "content",
                $this->load_local_php_view(
                        "personal/views/listmessages$origin", array(
                            'message_list'  => $message_list,
                            'message'       => $message,
                        ))
                );
        return $master->get_page();
    }
    // </editor-fold>

    // personal
    //=====================================================
}

try
{
    $this_page = new admin();
    echo $this_page->init_request(
            IsSetGet(ACTION, ACTION_DEFAULT));
}
catch(Exception $e)
{
    FCore::ShowError(500, $e->getMessage());
}

?>