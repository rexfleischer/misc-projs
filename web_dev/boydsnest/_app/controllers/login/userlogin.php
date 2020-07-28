    <div class="login_form">
        <form action="<?php echo buildurl(BN_URL_PAGE_LOGIN, array(ACTION => ACTION_ATTEMPT)) ?>" method="post">
            <table class="login_form_table">
                <caption><?php IsSetEcho($message); ?></caption>
                <tr>
                    <td>
                        Login Name:
                    </td>
                    <td>
                        <input tabindex="1" class="login_form_input_text" type="text" name="<?php echo USERS_USERNAME; ?>" value="<?php IsSetEcho($username); ?>" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Login Pass:
                    </td>
                    <td>
                        <input tabindex="2" class="login_form_input_text" type="password" name="<?php echo USERS_PASSWORD; ?>" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input tabindex="3" class="login_form_input_submit" type="submit" value="Login" />
                    </td>
                </tr>
            </table>
        </form>
    </div>