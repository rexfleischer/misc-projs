<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class about extends CI_Controller 
{
    public function index()
    {
        $this->load->view('about/index');
    }
}