<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;

class page_index extends _baselayout {

    protected function thisPageLayout(){
        ?>
<div id="text">
    Welcome To Boydsnest!!<br />
</div>
<div id="_constructionPicture"></div>
        <?php
    }
    protected function thisPageStyle(){
        ?>
<style type="text/css">
<!--
#_constructionPicture
{
 background-image:url('<?php echo URL_ASSETS; ?>/Lighthouse.jpg');
 width:956px;
 height:550px;
 margin:2px auto 2px auto;
}
-->
</style>
        <?php
    }
    protected function thisPagePreProcessing(){
    }
}

$thispage = new page_index();
$thispage->EchoBaseLayout();

?>
