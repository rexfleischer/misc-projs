<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_HTML;
require_once FCORE_FILE_CONTROLLER;
require_once FCORE_FILE_DATACOLLECTION;
require_once FCORE_FILE_DBFACTORY;
require_once FCORE_FILE_DBFORUM;

class home extends Controller
{
    public function __construct()
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/home";
    }

    public function get_route_map()
    {
        return array(
            self::REDIRECTS => array(
                array(
                    self::REDIRECT_IF   => !BoydsnestSession::GetInstance()->IsLoggedIn(),
                    self::REDIRECT_TO   => 'ERROR_MESSAGE',
                    self::REDIRECT_DATA => 'you must be logged in to view this page',
                )
            ),

            'ERROR_MESSAGE' => 'error_message',

            ACTION_DEFAULT      => ACTION_VIEW."content",

            //=================================================================
            // everything about CONTENT

            ACTION_VIEW."content"   => 'view_content',

            //=================================================================
        );
    }

    //=====================================================
    // misc

    // <editor-fold defaultstate="collapsed" desc="ready_master">
    public function ready_master($content_state)
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/home.css"));
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/sidemenu/sidemenu_1.php")."\n");
        $page->apply_string("javascript", "");

        $page_list = null;
        try
        {
            $page_list = $this->load_local_object(
                    "content/objects/GetSideMenuContentState", $content_state);
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
        
        $params = array();
        $params["sidemenu_0"]   = FCore::LoadViewPHP(
                "sidemenu/layout_1",
                array(
                    'title'     => '',
                    'content'   => $this->load_local_php_view(
                            "content/views/content_sidemenu",
                            array(
                                "page_list" => is_a($page_list, "DataCollection") ?
                                        $page_list->get_data() : array()
                            )),
                ));

        $page->apply_param(
                    "main_content",
                    FCore::LoadViewPHP("content/mainAndSideMenus",$params)
                );

        $page->commit_applies();

        return $page;
    }
    
    public function ready_master_common($view, $params = array(), $content_state = null)
    {
        $master = $this->ready_master($content_state);
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

    // misc
    //=====================================================


    //=====================================================
    // content

    // <editor-fold defaultstate="collapsed" desc="view_content">
    public function view_content($request, $message = "")
    {
        $page_state = false;
        try
        {
            $page_state = $this->load_local_object("content/objects/GetPageState");
            $page_state = $page_state->get_state();
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
            $message .= $e->getMessage();
            $page_state = false;
        }

        if ($page_state)
        {
            $page_data = false;
            try
            {
                $page_data = FCore::LoadObject("pages/GetUserPage",
                        array(
                            "user_id"   => $page_state[USERS_USERID],
                            "page_id"   => $page_state[DBForum::POST_ID],
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
                $message .= $e->getMessage();
                $page_data = false;
            }
            $page_data = is_a($page_data, "DataCollection") ?
                    $page_data->get_data() : array();

            $response_list = false;
            if (array_key_exists(PAGETYPE, $page_data) &&
                $page_data[PAGETYPE] != BN_PAGETHREADTYPE_NONE)
            {
                try
                {
                    $response_list = FCore::LoadObject(
                            "responses/GetUserPageResponseList",
                            $page_state[DBForum::POST_ID]);
                    $response_list = $response_list->get_data();
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
                    $message .= $e->getMessage();
                }
            }

            return $this->ready_master_common(
                "content/views/content_view",
                array(
                    'message'   => $message,
                    "user_id"   => $page_state[USERS_USERID],
                    "page_id"   => $page_state[DBForum::POST_ID],
                    'content'   => array_key_exists(DBDataType::CONTENT, $page_data) ?
                                        $page_data[DBDataType::CONTENT] : null,
                    'title'     => array_key_exists(PAGETITLE, $page_data) ?
                                        $page_data[PAGETITLE] : null,
                    'response_list' => is_array($response_list) ?
                                        $response_list : array(),
                ),
                $page_state);
        }
        else
        {
            return $this->ready_master_common(
                "content/views/content_welcome",
                array(
                    'message'   => $message
                ),
                $page_state);
        }
    }
    // </editor-fold>

    // content
    //=====================================================


}

try
{
    $this_page = new home();
    echo $this_page->init_request('default');
}
catch(Exception $e)
{
    FCore::ShowError(500, $e->getMessage());
}

?>