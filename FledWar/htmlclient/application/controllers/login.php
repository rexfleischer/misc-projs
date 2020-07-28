<?php

if (!defined('BASEPATH'))
  exit('No direct script access allowed');

class login extends CI_Controller {

  public function index() {
    
    $this->load->view("login/login");
  }

  public function attempt() {
    
    if (empty($_POST['username'])) {
      header('Location: ' . $this->config->item("base_url") . "login/?error");
      log_message("error", "couldnt login because null username");
      return;
    }

    if (empty($_POST['password'])) {
      header('Location: ' . $this->config->item("base_url") . "login/?error");
      log_message("error", "couldnt login because null password");
      return;
    }

    $username = trim($_POST['username']);
    $password = trim($_POST['password']);

    if ($this->attempt_login($username, $password)) {
      if ($_SESSION["is_admin"]) {
        header('Location: ' . $this->config->item("base_url") . "admin/status");
      } else {
        header('Location: ' . $this->config->item("base_url") . "game/");
      }
    } else {
      header('Location: ' . $this->config->item("base_url") . "login/?error");
    }
  }

  public function attempt_login($username, $password) {
    
    $login_path = sprintf($this->config->item("svc_login_url"), $username, $password);

    $raw_result = file_get_contents($login_path);
    $result = json_decode($raw_result);

    if (!$result->ok || $result->response == null) {
      log_message("error", "unable to login [$raw_result]");
      return false;
    }

    $user = $result->response;

    $is_admin = false;
    foreach ($user->rights as $right) {
      if (strpos($right, "admin") !== false) {
        $is_admin = true;
        break;
      }
    }

    $_SESSION["user"] = $user;
    $_SESSION["is_admin"] = $is_admin;
    $_SESSION["session_id"] = $user->session_id;
    $_SESSION["username"] = $user->username;
    if ($is_admin) {
      $_SESSION["password"] = $password;
    }
    return $user;
  }

}
