        <?php IsSetEcho($message); ?>
<?php if (is_array($pagelist)){ ?>
        <table>
            <caption></caption>
            <tr>
                <td class="width_150">Title</td>
                <td class="width_150">Rank</td>
                <td class="width_150">Update</td>
                <td class="width_150">Write</td>
                <td class="width_150">Delete</td>
            </tr>
            <?php foreach($pagelist as $value) { ?>
            <tr>
                <form action="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(ACTION => ACTION.ACTION_UPDATE)); ?>" method="post">
                    <input type="hidden" name="<?php echo USERMANUAL_PAGEID; ?>" value="<?php echo $value[USERMANUAL_PAGEID]; ?>" />
                    <td>
                        <input type="text" name="<?php echo USERMANUAL_TITLE; ?>" value="<?php echo $value[USERMANUAL_TITLE]; ?>" />
                    </td>
                    <td>
                        <input type="text" name="<?php echo USERMANUAL_RANK; ?>" value="<?php echo $value[USERMANUAL_RANK]; ?>" />
                    </td>
                    <td>
                        <input type="submit" value="Update" />
                    </td>
                    <td>
                        <a href="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(ACTION => ACTION_WRITE,USERMANUAL_PAGEID => $value[USERMANUAL_PAGEID]))?>">Write</a>
                    </td>
                </form>
                <form action="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(ACTION => ACTION.ACTION_DELETE)); ?>" method="post">
                    <input type="hidden" name="<?php echo USERMANUAL_PAGEID; ?>" value="<?php echo $value[USERMANUAL_PAGEID]; ?>" />
                    <td>
                        <input type="submit" value="Delete" />
                    </td>
                </form>
            </tr>
            <?php } ?>
        </table>
<?php } else { IsSetEcho($pagelist); }?>