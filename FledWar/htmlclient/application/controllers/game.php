<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class game extends CI_Controller 
{
    public function index()
    {
        if (!isset($_SESSION) || 
            !array_key_exists("session_id", $_SESSION))
        {
            log_message("error",
                "unable to show game because user not logged in");
            header('Location: '.$this->config->item("base_url")."login/");
        }
        else
        {
            $this->load->view('game/index');
        }
    }
    
    public function js_config()
    {
        $this->load->view('game/js_config');
    }
    
    public function start()
    {
        if (!array_key_exists("username", $_SESSION) ||
            !array_key_exists("session_id", $_SESSION))
        {
            header('Location: '.$this->config->item("base_url")."login/");
            return;
        }
        
        $username = $_SESSION["username"];
        $session_id = $_SESSION["session_id"];
        $start_url = sprintf($this->config->item("svc_start_url"),
                $username,
                $session_id);
        
        $raw_result = file_get_contents($start_url);
        
        echo $raw_result;
    }
}