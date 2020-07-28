<?php

class page_underConstruction extends _baselayout {

    protected function thisPageLayout(){
        ?>
<div id="text">
    Oops: This Page Is Currently Under Construction, We Apologize For Any Inconvenience<br />
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

$thispage = new page_underConstruction();
$thispage->EchoBaseLayout();

exit(0);

?>
