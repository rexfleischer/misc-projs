<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_CONTROLLER;
require_once FCORE_FILE_DBFACTORY;
require_once FCORE_FILE_DBLOGGER;
require_once FCORE_FILE_HTML;

class errors extends Controller
{
    public function __construct()
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/errors";
    }

    public function get_route_map()
    {
        return array(
            ACTION_DEFAULT      => 'unknown_error',

            'unknown_error'     => 'unknown_error',

            'must_be_logged_in' => 'must_be_logged_in',
        );
    }

    public function ready_master()
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("javascript", "");

        return $page;
    }

    public function unknown_error()
    {
        
    }

    public function must_be_logged_in()
    {
        
    }
}

try
{
    $page = new errors();
    echo $page->init_request(IsSetGet(ACTION, 'default'));
}
catch(Exception $e)
{
    FCore::ShowError(500, $e);
}

?>