
<?php IsSetEcho($message); ?>

<h3><?php IsSetEcho($username); ?>'s Logs</h3>

<?php
if (is_array($user_logs))
{
?>

<form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_LOG.ACTION_DELETE."users", "start" => $start, "amount" => $amount, USERS_USERID => $user_id)); ?>" >

    <table>
        <tr >
            <td class="width_50">
                
            </td>
            <td class="width_100">
                Number
            </td>
            <td class="width_150">
                Logged At
            </td>
            <td>
                message
            </td>
        </tr>

        <?php $counter = $start + 1; foreach($user_logs as $user) { ?>
        <tr>
            <td>
                <input type="checkbox" name="<?php echo DBLogger::LOGID."[]"; ?>" value="<?php echo $user[DBLogger::LOGID]; ?>" />
            </td>
            <td>
                <?php echo $counter; $counter++; ?>
            </td>
            <td>
                <?php echo $user[DBLogger::TIME]; ?>
            </td>
            <td>
                <?php echo $user[DBLogger::MESSAGE]; ?>
            </td>
        </tr>
        <?php } ?>

        <tr>
            <td>

            </td>
            <td>
                <input type="submit" value="Delete" />
            </td>
            <td>

            </td>
            <td>

            </td>
        </tr>

    </table>

</form>

<?php
}
else
{
?>
        There is nothing to list
<?php
}
?>