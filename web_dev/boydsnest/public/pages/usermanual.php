<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_CONTROLLER;
require_once FCORE_FILE_DBFACTORY;
require_once FCORE_FILE_DBLOGGER;
require_once FCORE_FILE_DATACOLLECTION;
require_once FCORE_FILE_HTML;

/**
 * Description of usermanual
 *
 * @author REx
 */
class usermanual extends Controller
{
    public function __construct() 
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/usermanual";
    }

    public function get_route_map()
    {
        return array(
            ACTION_DEFAULT => ACTION_READ,

            ACTION.ACTION_UPDATE    => 'list_pages',

            ACTION.ACTION_DELETE    => 'list_pages',

            ACTION.ACTION_CREATE    => 'create_page',

            ACTION.ACTION_WRITE     => 'write_page',

            ACTION_CREATE   => 'create_page',

            ACTION_WRITE    => 'write_page',

            ACTION_LIST     => 'list_pages',

            ACTION_READ     => 'read_page',
        );
    }
    

    // <editor-fold defaultstate="collapsed" desc="ready_master">
    public function ready_master($request)
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/sidemenu/sidemenu_1.php")."\n");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/usermanual.css")."\n");
        $page->apply_string("javascript", "");

        $page_list = $this->load_local_object("actions/GetUserManualPageList");

        $params = array();
        $params["sidemenu_0"]   = FCore::LoadViewPHP(
                "sidemenu/layout_1",
                array(
                    'content'   => $this->load_local_php_view(
                            "views/sidemenu_pagelist", 
                            array('pagelist' => $page_list->get_data())),
                    'title'     => 'Manuals'
                ));
        if (BoydsnestSession::GetInstance()->get(USERS_ISMASTER))
        {
            $params["sidemenu_1"]   = FCore::LoadViewPHP(
                    "sidemenu/layout_1",
                    array(
                        'content'   => $this->load_local_php_view("views/mastermenu"),
                        'title'     => 'Master\'s Menu'
                    ));
        }
        $page->apply_param(
                    "main_content",
                    FCore::LoadViewPHP("content/mainAndSideMenus",$params)
                );

        $page->commit_applies();
        return $page;
    }
    // </editor-fold>
    

    // <editor-fold defaultstate="collapsed" desc="read_page">
    public function read_page()
    {
        $page_title = "";
        $page_content = "";
        try
        {
            $page_info = $this->load_local_object(
                    "actions/GetUserManualPageContent");
            $page_title = $page_info->get(USERMANUAL_TITLE);
            $page_content = $page_info->get(USERMANUAL_CONTENT);
        }
        catch(UserActionException $e)
        {
            if ($this->logger != null)
            {
                $this->logger->log(
                        Logger::LEVEL_WARN,
                        $e->getMessage(),
                        $e->getFile(), $e->getLine());
            }
            $page_title = "Page Not Found";
            $page_content = $e->getMessage();
        }
        catch(Exception $e)
        {
            if ($this->logger != null)
            {
                $this->logger->log(
                        Logger::LEVEL_ERROR,
                        $e->getMessage(),
                        $e->getFile(), $e->getLine());
            }
            $page_title = "Unexpected Error";
            $page_content = "An Unexpected Error Occurred,".
                    " Please Contact A Site Admin And Yell At Them";
        }

        $page = $this->ready_master($request);
        $page->consume_string(
                "content",
                $this->load_local_php_view(
                        "views/readmanual",
                        array(
                            'title'     => $page_title,
                            'content'   => $page_content
                        )));
        return $page->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="create_page">
    public function create_page($request)
    {
        FCore::LoadObject("security/IsMasterSecurityCheck");
        $page_title = IsSetPost(USERMANUAL_TITLE, '');
        $page_rank  = IsSetPost(USERMANUAL_RANK);
        $message    = 'Fill In The Information Below To Create A Usermanual Page';
        if ($request == ACTION.ACTION_CREATE)
        {
            try
            {
                $this->load_local_object("actions/CreateUserManualPage");
                return $this->init_request(ACTION_LIST);
            }
            catch(UserActionException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(ValidationException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(Exception $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = "An Unexpected Error Occurred: Please Contact The Admin";
            }
        }

        if (!$page_rank)
        {
            $page_rank_getter = $this->load_local_object(
                    "actions/GetNextUserManualRank");
            $page_rank = $page_rank_getter->get_rank();
        }

        $page = $this->ready_master($request);
        $page->consume_string(
                "content",
                $this->load_local_php_view(
                        "views/create",
                        array(
                            'title'     => $page_title,
                            'rank'      => $page_rank,
                            'message'   => $message,
                        )));
        return $page->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="write_page">
    public function write_page($request)
    {
        FCore::LoadObject("security/IsMasterSecurityCheck");
        $message    = '';
        if ($request == ACTION.ACTION_WRITE)
        {
            try
            {
                $this->load_local_object("actions/WriteUserManualPage");
                $message = 'Successfully Updated The Manual Page';
            }
            catch(UserActionException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(ValidationException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(Exception $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = "An Unexpected Error Occurred: Please Contact The Admin";
            }
        }

        $pageinfo;
        try
        {
            $pageinfo = $this->load_local_object(
                    "actions/GetUserManualPageContent");
            $pageinfo = $pageinfo->get_data();
        }
        catch(UserActionException $e)
        {
            if ($this->logger != null)
            {
                $this->logger->log(
                        Logger::LEVEL_WARN,
                        $e->getMessage(),
                        $e->getFile(), $e->getLine());
            }
            return $this->list_pages($request, $e->getMessage());
        }

        $page = $this->ready_master($request);
        $page->consume_string(
                "content",
                $this->load_local_php_view(
                        "views/writemanual",
                        array(
                            'page_id'       => $pageinfo[USERMANUAL_PAGEID],
                            'page_content'  => $pageinfo[USERMANUAL_CONTENT],
                            'page_title'    => $pageinfo[USERMANUAL_TITLE],
                            'message'       => $message,
                        )));
        return $page->get_page();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="list_pages">
    public function list_pages($request, $message = '')
    {
        FCore::LoadObject("security/IsMasterSecurityCheck");
        if ($request == ACTION.ACTION_UPDATE ||
                $request == ACTION.ACTION_DELETE)
        {
            try
            {
                if ($request == ACTION.ACTION_UPDATE)
                {
                    $this->load_local_object("actions/UpdateUserManualPage");
                    $message = "Successfully Updated A Usermanaul Page";
                }
                else
                {
                    $this->load_local_object("actions/DeleteUserManualPage");
                    $message = "Successfully Deleted A Usermanual Page";
                }
            }
            catch(UserActionException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(ValidationException $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_WARN,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = $e->getMessage();
            }
            catch(Exception $e)
            {
                if ($this->logger != null)
                {
                    $this->logger->log(
                            Logger::LEVEL_ERROR,
                            $e->getMessage(),
                            $e->getFile(), $e->getLine());
                }
                $message = "An Unexpected Error Occurred: Please Contact The Admin";
            }
        }

        $pagelist;
        try
        {
            $pagelist = $this->load_local_object("actions/GetUserManualPageList");
            $pagelist = $pagelist->get_data();
        }
        catch(UserActionException $e)
        {
            if ($this->logger != null)
            {
                $this->logger->log(
                        Logger::LEVEL_WARN,
                        $e->getMessage(),
                        $e->getFile(), $e->getLine());
            }
            $pagelist = $e->getMessage();
        }

        $page = $this->ready_master($request);
        $page->consume_string(
                "content",
                $this->load_local_php_view(
                        "views/infolist",
                        array(
                            'pagelist'  => $pagelist,
                            'message'   => $message,
                        )));
        return $page->get_page();
    }
    // </editor-fold>
}

try
{
    $this_page = new usermanual();
    echo $this_page->init_request(IsSetGet(ACTION, ACTION_DEFAULT));
}
catch(Exception $e)
{
    FCore::ShowError(500, $e->getMessage());
}

?>
