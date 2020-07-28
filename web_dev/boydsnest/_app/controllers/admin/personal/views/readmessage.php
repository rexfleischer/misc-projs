<?php

require_once FCORE_FILE_BBCONSUMER;

$viewer = array();
$viewer['title']    = isset($title) ? $title : "No Title";
$viewer['content']  = isset($content) ? BBConsumer::consume($content) : "No Content";

if (isset($user_id) &&
        BoydsnestSession::GetInstance()->get(USERS_CANMESSAGE) &&
        BoydsnestSession::GetInstance()->get(USERS_USERID) != $user_id)
{
?>

<a style="float: left" href="<?php echo buildurl(BN_URL_PAGE_ADMIN,
        array(ACTION => ACTION_WRITE."message", USERS_USERID => $user_id)); ?>">Respond</a>
<?php
}
if (isset($message_id))
{
?>

<form style="float: left" action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
        array(ACTION => ACTION.ACTION_DELETE."readmessage")); ?>" method="post">
    <input type="submit" value="Delete" />
    <input type="hidden" name="<?php echo DBMessage::MESSAGE_ID; ?>[]" value="<?php echo $message_id; ?>" />
</form>
<?php
}
?>

<br />

<?php
echo FCore::LoadViewPHP("content/BasicTextViewer", $viewer);
?>