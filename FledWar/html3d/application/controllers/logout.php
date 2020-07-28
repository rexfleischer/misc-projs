<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class logout extends CI_Controller 
{
    public function index()
    {
        $this->attempt_logout();
        
        header('Location: '.$this->config->item("base_url"));
    }
    
    public function invalidate_session()
    {
        session_destroy();
        
        header('Location: '.$this->config->item("base_url"));
    }
    
    private function attempt_logout()
    {
        $session_id = $_SESSION["session_id"];
        $username = $_SESSION["username"];
        
        $logout_path = sprintf($this->config->item("svc_logout_url"), 
                $username, 
                $session_id);
        
        unset($_SESSION["session_id"]);
        unset($_SESSION["username"]);
        session_destroy();
        
        $raw_result = file_get_contents($logout_path);
        $result = json_decode($raw_result);
        
        if ($result->result == "successful")
        {
            return true;
        }
        
        log_message("error", "unable to login [$raw_result]");
        return false;
    }
}
