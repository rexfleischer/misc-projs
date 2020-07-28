<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;

class page_Logout extends _baselayout {
    protected function  thisPageLayout(){
        ?>
<div id="text">
    FINE!!! LEAVE <?php echo strtoupper($this->crossInfo[0]); ?>!!!
    <br />
    Have A Good Day..
</div>
        <?php
    }
    protected function thisPageStyle(){
    }
    protected function thisPagePreProcessing(){
        $this->crossInfo[0] = _SESSION::Logout();
    }
}

$thispage = new page_Logout();
$thispage->EchoBaseLayout();

?>
