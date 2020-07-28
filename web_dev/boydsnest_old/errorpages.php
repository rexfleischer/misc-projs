<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;

class error_UnknownAction extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    An Unknown Error Has Occurred: Oops...
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

class error_MustBeLoggedIn extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    You Must Be Logged In To View This Page
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

class error_UnexpectedError extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    An Unexpected Error Has Occurred
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

class error_AccessDenied extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    Access Denied
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

class error_UnderConstruction extends _baselayout implements _state {
    protected function thisPageLayout(){
        ?>
<div id="text">
    Access Denied
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

$thiserror = _FCORE::IsSetGetDefault(ERROR_PAGE, false);
if ($thiserror){
    switch($thiserror){
        case ERROR_UNEXPECTEDERROR:
            $thiserror = new error_UnexpectedError();
            break;
        case ERROR_MUSTBELOGGEDIN:
            $thiserror = new error_MustBeLoggedIn();
            break;
        case ERROR_ACCESSDENIED:
            $thiserror = new error_AccessDenied();
            break;
        case ERROR_UNDERCONSTRUCTION:
            $thiserror = new error_UnderConstruction();
            break;
        case ERROR_UNKNOWNACTION:
        default:
            $thiserror = new error_UnknownAction();
            break;
    }
    $thiserror->trigger();
}

?>
