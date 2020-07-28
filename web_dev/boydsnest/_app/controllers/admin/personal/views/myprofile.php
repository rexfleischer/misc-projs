
            <?php IsSetEcho($message); ?>
            <table>
                <tr>
                    <td class="width_150">
                        Username
                    </td>
                    <td class="width_150">
                        Account Created
                    </td>
                </tr>
                <tr>
                    <td>
                        <?php echo $user[USERS_USERNAME]; ?>
                    </td>
                    <td>
                        <?php echo $user[USERS_CREATEDWHEN]; ?>
                    </td>
                </tr>
            </table>
            <?php if ($user[USERS_EXPIRESWHEN] != '0000-00-00 00:00:00') { ?>
            <fieldset class="bn_fieldset">
                <legend>Your account expires on:</legend>
                <?php echo $user[USERS_EXPIRESWHEN]; ?>
            </fieldset>
            <?php } ?>
            <fieldset class="bn_fieldset">
                <legend>General Information</legend>
                <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_VIEW."profile")); ?>">
                    <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user[USERS_USERID]; ?>" />
                    <table>
                        <tr>
                            <td>
                                Email:
                            </td>
                            <td>
                                <input type="text" name="<?php echo USERS_EMAIL; ?>" value="<?php echo $user[USERS_EMAIL]; ?>" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Secret Question:
                            </td>
                            <td>
                                <input type="text" name="<?php echo USERS_SECRETQUESTION; ?>" value="<?php echo $user[USERS_SECRETQUESTION]; ?>" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Secret Answer:
                            </td>
                            <td>
                                <input type="text" name="<?php echo USERS_SECRETANSWER; ?>" value="<?php echo $user[USERS_SECRETANSWER]; ?>" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Scheme Using:
                            </td>
                            <td>
                                <input type="text" name="<?php echo USERS_SCHEMEUSING; ?>" value="<?php echo $user[USERS_SCHEMEUSING]; ?>" />
                            </td>
                            <td>
                                this is not implemented and will not effect anything if changed... yet!
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="submit" value="Update" />
                            </td>
                        </tr>
                    </table>
                </form>
            </fieldset>
            <fieldset class="bn_fieldset">
                <legend>Reset Your Password</legend>
                <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_VIEW."profilepass")); ?>">
                    <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user[USERS_USERID]; ?>" />
                    New Password: <input type="text" name="<?php echo USERS_PASSWORD; ?>" />
                    <br />
                    <input type="submit" value="Change Password" />
                </form>
            </fieldset>
