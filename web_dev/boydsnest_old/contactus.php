<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
//require_once DIR_HELPER_UNDERCONSTRUCTION_FORCE;

class page_contactus extends _baselayout {

    protected function thisPageLayout(){
        ?>

<div id="text">
    Being that this site is still under construction, the form for contacting <br />
    the Boyd's when you are not a user has not been made yet. Please contact the <br />
    Boyd's in some other manner.
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

$thispage = new page_contactus();
$thispage->EchoBaseLayout();

?>
