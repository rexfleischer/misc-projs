        <h4><?php IsSetEcho($message); ?></h4>
        <div class="form_setup">
            <table>
                <form method="post" action="<?php echo buildurl(BN_URL_PAGE_USERMANUAL, array(ACTION => ACTION.ACTION_CREATE)); ?>" >
                    <tr>
                        <td>
                            Title:
                        </td>
                        <td>
                            <input tabindex="1" class="form_input_text" type="text" name="<?php echo USERMANUAL_TITLE; ?>" value="<?php IsSetEcho($title); ?>" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Rank:
                        </td>
                        <td>
                            <input tabindex="2" class="form_input_text" type="text" name="<?php echo USERMANUAL_RANK; ?>" value="<?php IsSetEcho($rank); ?>" />
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input tabindex="3" class="form_input_submit" type="submit" value="Create" />
                        </td>
                    </tr>
                </form>
            </table>
        </div>