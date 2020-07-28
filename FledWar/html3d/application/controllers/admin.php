<?php

if (!defined('BASEPATH'))
  exit('No direct script access allowed');

class admin extends CI_Controller {

  public function __construct() {
    parent::__construct();

    if (!array_key_exists("username", $_SESSION) ||
            !array_key_exists("is_admin", $_SESSION) ||
            !$_SESSION["is_admin"]) {
      header("Location: " . $this->config->item("base_url") . "home/");
    }
  }

  public function index() {
    $this->status();
  }

  public function status() {
    $status_result = $this->query("svc_admin_status");

    $this->load->view("admin/status", 
            array("status" => $status_result["response"]));
  }

  public function loggedin() {
    $loggedin_results = $this->query("svc_admin_loggedin");

    $this->load->view("admin/loggedin", 
            array("loggedin" => $loggedin_results["response"]));
  }
  
  public function scopelist() {
    $scope_results = $this->query("svc_admin_scopelist");

    $this->load->view("admin/scope_list", 
            array("scopes" => $scope_results["response"]));
  }
  
  public function scopeview($id = false) {
    if (!$id) {
      header("Location: ".$this->config->item("base_url")."scope_list/");
    }
    
    $scope_result = $this->query("svc_admin_scopeview",
            array("scope_id" => $id));
    
    $this->load->view("admin/scope_view", 
            array("scope" => $scope_result["response"]));
  }

  public function management($result = false) {
    $this->load->view("admin/management", array("result" => $result));
  }

  public function management_refresh() {
    $result = new stdClass();
    $result->refresh = $this->query("svc_admin_refresh");
    $result->relogin = $this->relogin();
    
    $this->management($result);
  }

  public function management_reset() {
    $result = new stdClass();
    $result->refresh = $this->query("svc_admin_reset");
    $result->relogin = $this->relogin();
    
    $this->management($result);
  }

  public function config() {
    $config_result = $this->query("svc_admin_config");

    $this->load->view("admin/config", 
            array("config" => $config_result["response"]));
  }
  
  private function relogin()
  {
    $username = $_SESSION["username"];
    $password = $_SESSION["password"];
    session_destroy();
    
    session_start();
    require dirname(__FILE__)."/login.php";
    $login_controller = new login();
    return $login_controller->attempt_login($username, $password);
  }

  private function query($svc_admin, $extra_vars = false) {
    $admin_status_query = sprintf($this->config->item($svc_admin), 
            $_SESSION["username"],
            $_SESSION["session_id"]);
    if ($extra_vars) {
      $admin_status_query .= ("&".http_build_query($extra_vars));
    }

    $raw_result = file_get_contents($admin_status_query);
    return json_decode($raw_result, true);
  }

}