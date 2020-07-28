<?php
$editor = array();
$editor['submit'] = "Update";
$editor['name'] = USERMANUAL_CONTENT;
if (isset($page_content)){ $editor['content'] = $page_content; }
if (isset($page_title)){ $editor['title'] = $page_title; }
?>
        <?php IsSetEcho($message); ?>
        <form action="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(ACTION => ACTION.ACTION_WRITE)); ?>" method="post">
            <input type="hidden" name="<?php echo USERMANUAL_PAGEID; ?>" value="<?php echo $page_id; ?>" />
            <?php echo FCore::LoadViewPHP("form/BasicTextEditor", $editor); ?>
        </form>