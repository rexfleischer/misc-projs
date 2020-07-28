<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_HTML;
require_once FCORE_FILE_CONTROLLER;

class logout extends Controller
{
    public function __construct()
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/logout";
    }

    public function get_route_map()
    {
        return array(
            ACTION_DEFAULT      => 'default_request',

            'default_request'   => 'default_request'
        );
    }

    public function ready_master()
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/terms.css"));
        $page->apply_string("javascript", "");

        return $page;
    }

    public function default_request()
    {
        $session = BoydsnestSession::GetInstance();
        $username = $session->get(USERS_USERNAME);
        $session->ResetSession();
        $master = $this->ready_master();
        $master->apply_param(
                "main_content",
                $this->load_local_php_view(
                    "content",
                    array("username" => $username)
                )
            );

        $master->commit_applies();
        return $master->get_page();
    }
}

try
{
    $this_page = new logout();
    echo $this_page->init_request('default');
}
catch(Exception $e)
{
    FCore::ShowError(500, $e->getMessage());
}

?>