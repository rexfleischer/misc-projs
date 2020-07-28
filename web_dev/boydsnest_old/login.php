<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
require_once DIR_DATABASE_DATABASE;
require_once DIR_PAGE_ERRORPAGES;

//==============================================================================
//  Possible State Decl
//==============================================================================
define("LOGINSTATE_UNKNOWN", 0);
define("LOGINSTATE_FAILEDATTEMPT", 1);
define("LOGINSTATE_ALREADYLOGGEDIN", 2);
define("LOGINSTATE_SUCCESSFULATTEMPT", 3);
define("LOGINSTATE_NOTLOGGEDIN", 4);

function LoginPage_Forum(){
    ?>
<form action="<?php echo URL_PAGE_LOGIN; ?>" method="post">
    <div id="text">Username: <input type="text" tabindex="1" name="<?php echo USERS_USERNAME; ?>" /></div>
    <div id="text">Password: <input type="password" tabindex="2" name="<?php echo USERS_PASSWORD; ?>" /></div>
    <br />
    <div id="text"><input type="submit" value="login" /></div>
</form>
    <?php
}

//==============================================================================
//  Failed Login Attempt Action
//==============================================================================
class loginstate_failedAttempt extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    Error: Username or Password is incorrect
</div>
        <?php
        LoginPage_Forum();
    }
    protected function thisPageStyle(){
    }
    protected function thisPagePreProcessing(){
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}
//==============================================================================
//  Already Logged In Action
//==============================================================================
class loginstate_alreadyLoggedIn extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    You are already logged in as <?php echo _SESSION::GetUsername(); ?>
</div>
        <?php
    }
    protected function thisPageStyle(){
    }
    protected function thisPagePreProcessing(){
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}
//==============================================================================
//  Successful Login Attempt Action
//==============================================================================
class loginstate_successfulAttempt extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    Welcome, <?php echo _SESSION::GetUsername(); ?>!!
</div>
        <?php
    }
    protected function thisPageStyle(){
    }
    protected function thisPagePreProcessing(){
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}
//==============================================================================
//  Failed Login Attempt Action
//==============================================================================
class loginstate_NotLoggedIn extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    You are not logged in:
</div>
        <?php
        LoginPage_Forum();
    }
    protected function thisPageStyle(){
    }
    protected function thisPagePreProcessing(){
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}

//==============================================================================
//  Login Page State Machine
//==============================================================================
class page_login extends _stateMachine {
    public function changeState($state){
        switch($state){
            case LOGINSTATE_FAILEDATTEMPT:
                $this->_CurrState = new loginstate_failedAttempt();
                break;
            case LOGINSTATE_ALREADYLOGGEDIN:
                $this->_CurrState = new loginstate_alreadyLoggedIn();
                break;
            case LOGINSTATE_SUCCESSFULATTEMPT:
                $this->_CurrState = new loginstate_successfulAttempt();
                break;
            case LOGINSTATE_NOTLOGGEDIN:
                $this->_CurrState = new loginstate_NotLoggedIn();
                break;
            case LOGINSTATE_UNKNOWN:
            default:
                $this->_CurrState = new error_UnknownAction();
                break;
        }
    }
}

//==============================================================================
//  State Decision
//==============================================================================
$thispage = new page_login();
$state = LOGINSTATE_UNKNOWN;
if (_S::IsLoggedIn()){
    $state = LOGINSTATE_ALREADYLOGGEDIN;
} else if (isset($_POST[USERS_USERNAME]) && isset($_POST[USERS_PASSWORD])){
    $db = _DB::_Connect();
    if (_SESSION::Login($db, $_POST[USERS_USERNAME], $_POST[USERS_PASSWORD])){
        $state = LOGINSTATE_SUCCESSFULATTEMPT;
    } else {
        DB_LOGINLOG::_InsertLoginLog($db, $_POST[USERS_USERNAME], $_POST[USERS_PASSWORD]);
        $state = LOGINSTATE_FAILEDATTEMPT;
    }
    $db->disconnect();
} else {
    $state = LOGINSTATE_NOTLOGGEDIN;
}
$thispage->changeState($state);
$thispage->trigger();

?>
