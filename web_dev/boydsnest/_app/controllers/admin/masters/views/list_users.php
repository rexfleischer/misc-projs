
<?php IsSetEcho($message); ?>

<?php
if (is_array($user_list))
{
?>

<h3>User List</h3>

<table>
    <tr >
        <td class="width_150">
            Username
        </td>
        <td class="width_150">
            Created When
        </td>
        <td class="width_150">
            Last Updated
        </td>
        <td class="width_100">
            Details
        </td>
        <td class="width_100">
            Logs
        </td>
    </tr>

    <?php foreach($user_list as $user) { ?>
    <tr>
        <td>
            <?php echo $user[USERS_USERNAME]; ?>
        </td>
        <td>
            <?php echo $user[USERS_CREATEDWHEN]; ?>
        </td>
        <td>
            <?php echo $user[USERS_LASTUPDATE]; ?>
        </td>
        <td>
            <a class="a_link" href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_UPDATE."users", USERS_USERID => $user[USERS_USERID])); ?>">Details</a>
        </td>
        <td>
            <?php if ($user[USERS_ISLOGGED]) { ?>
            <a class="a_link" href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_LOG."users", USERS_USERID => $user[USERS_USERID])); ?>">Logs</a>
            <?php } else { ?>
            Logs
            <?php } ?>
        </td>
    </tr>
    <?php } ?>

</table>

<?php 
}
else
{
    var_dump($user_list);
?>
        There are nothing to list
<?php
}
?>