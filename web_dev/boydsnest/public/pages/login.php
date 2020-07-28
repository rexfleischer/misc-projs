<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_CONTROLLER;
require_once FCORE_FILE_DBFACTORY;
require_once FCORE_FILE_DBLOGGER;
require_once FCORE_FILE_HTML;

class login extends Controller
{
    public function __construct()
    {
        parent::__construct();
    }

    public function get_local_dir()
    {
        return BN_DIR_CONTROLLERS . "/login";
    }

    public function get_route_map()
    {
        return array(
            ACTION_DEFAULT  => 'no_action',

            'no_action'     => 'default_request',

            ACTION_ATTEMPT  => 'attempt_login',
        );
    }

    protected function default_request()
    {
        $session = BoydsnestSession::GetInstance();
        $template = "";
        $message = "";
        if ($session->IsLoggedIn())
        {
            $template = "loginmessage";
            $message = "You Are Already Logged In";
        }
        else
        {
            $template = "userlogin";
            $message = "Enter The Information Below To Login";
        }
        $page = $this->ready_master();
        $page->apply_param(
                "main_content",
                $this->load_local_php_view(
                        $template,
                        array(
                            'message' => $message
                        ))
                );
        $page->commit_applies();
        return $page->get_page();
    }

    public function attempt_login()
    {
        $template = "userlogin";
        $message = "";
        try
        {
            // all it needs to do is attempt the action, it will
            // throw a DataCollection error if username and password
            // is not filled out, and it will throw a ValidationException
            // if the combination of the two is does not match
            $this->load_local_object("AttemptLogin");
            $template = "loginmessage";
            $session = BoydsnestSession::GetInstance();
            $message = "Welcome ".$session->get(USERS_USERNAME)."!!!";
        }
        catch(UserActionException $e)
        {
            $message = $e->getMessage();
        }
        $page = $this->ready_master();
        $page->apply_param(
                "main_content",
                $this->load_local_php_view(
                        $template,
                        array(
                            'message' => $message,
                            'username' => IsSetPost(USERS_USERNAME, "")
                        ))
                );
        $page->commit_applies();
        return $page->get_page();
    }

    public function ready_master()
    {
        $page = FCore::LoadMaster();

        $page->apply_string("title", "Boyds Nest");
        $page->apply_string("meta", "");
        $page->apply_string("javascript", "");
        $page->apply_string("style", Html::CssInclude(BN_URL_CSS . "/login.css"));

        return $page;
    }

}

try
{
    $page = new login();
    echo $page->init_request(IsSetGet(ACTION, 'default'));
}
catch(Exception $e)
{
    FCore::ShowError(500, $e);
}

?>