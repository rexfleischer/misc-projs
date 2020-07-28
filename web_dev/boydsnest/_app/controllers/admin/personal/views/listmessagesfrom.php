<?php
IsSetEcho($message);
if($message_list == null)
{ ?>

        <h3>Sent Messages</h3>
        <div class="text">
            there are no messages to list
        </div>
<?php }
else
{ ?>

<h3>Sent Messages</h3>

<form action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
        array(ACTION => ACTION.ACTION_DELETE."sentmessages")); ?>" method="post">
    <table>
        <?php foreach($message_list as $list_obj){ ?>
            <tr>
            <td>
                <input type="checkbox" name="<?php echo DBMessage::MESSAGE_ID; ?>[]" value="<?php echo $list_obj[DBMessage::MESSAGE_ID]; ?>" />
            </td>
            <td>
                <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                        array(ACTION => ACTION_READ."message", DBMessage::MESSAGE_ID => $list_obj[DBMessage::MESSAGE_ID])); ?>">
                    <i><?php echo $list_obj[DBMessage::TITLE]; ?></i>
                    At
                    <?php echo $list_obj[DBMessage::TIME_SENT]; ?>
                </a>
            </td>
        </tr>
        <?php } ?>
    </table>
    <input type="submit" value="Delete" />
</form>


<?php
}
?>