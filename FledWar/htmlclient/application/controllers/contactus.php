<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class contactus extends CI_Controller 
{
    public function index()
    {
        $this->load->view('contactus/index');
    }
}