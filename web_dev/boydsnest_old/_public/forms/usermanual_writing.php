<?php

function MakeWritingForm($pageID, $content){
    ?>

<?php echo _FORM::_FormBegin("usermanual_writing", URL_PAGE_USERMANUAL."?".ACTION."=2&".USERMANUAL_PAGEID."=$pageID", "POST"); ?>
    <?php echo _FORM::Hidden(USERMANUAL_PAGEID, $pageID); ?>
    <?php echo _FORM::Hidden(ACTION_GO, 1); ?>
<br />
    <?php echo _FORM::TextArea(USERMANUAL_CONTENT, 100, 20, $content); ?>
<br />
<br />
    <?php echo _FORM::Submit("Save"); ?>
<?php echo _FORM::_FormEnd(); ?>
    <?php
}

?>
