<?php

class underConstruction {
    public static function constructionStyle(){
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
    public static function constructionLayout(){
        ?>
<div id="text">
    Oops: This Page Is Currently Under Construction, We Apologize For Any Inconvenience<br />
</div>
<div id="_constructionPicture"></div>
        <?php
    }
}

?>
