<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class medievalwar extends CI_Controller 
{
    public function index()
    {
        $arguments = array(
            "main" => $this->config->item("base_url") . "game/medievalwar.js"
        );
        
        
        $this->load->view('play/game', $arguments);
    }
}
