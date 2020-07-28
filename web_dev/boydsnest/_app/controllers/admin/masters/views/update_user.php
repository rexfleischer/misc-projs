
        <?php IsSetEcho($message); ?>
        <h3><?php echo $user_data[USERS_USERNAME]; ?>'s Details</h3>
        <table>
            <tr>
                <td class="width_150">
                    Created When
                </td>
                <td class="width_150">
                    Last Updated
                </td>
            </tr>
            <tr>
                <td>
                    <?php echo $user_data[USERS_CREATEDWHEN]; ?>
                </td>
                <td>
                    <?php echo $user_data[USERS_LASTUPDATE]; ?>
                </td>
            </tr>
        </table>

        <fieldset class="bn_fieldset">
            <legend>General Information</legend>
            <form action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                    array(ACTION => ACTION.ACTION_UPDATE."users", USERS_USERID => $user_data[USERS_USERID])); ?>" method="post">
                <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user_data[USERS_USERID]; ?>" />
                <table>
                    <tr>
                        <td>
                            email:
                        </td>
                        <td>
                            <input type="text" name="<?php echo USERS_EMAIL; ?>" value="<?php IsSetEcho($user_data[USERS_EMAIL], ""); ?>" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Default Right:
                        </td>
                        <td>
                            <select name="<?php echo USERS_DEFAULTRIGHT; ?>">
                                <option value="<?php echo USERS_DEFAULTRIGHT_NONE; ?>" <?php if($user_data[USERS_DEFAULTRIGHT] == USERS_DEFAULTRIGHT_NONE){ echo "selected=\"selected\""; } ?> >none</option>
                                <option value="<?php echo USERS_DEFAULTRIGHT_SEE; ?>" <?php if($user_data[USERS_DEFAULTRIGHT] == USERS_DEFAULTRIGHT_SEE){ echo "selected=\"selected\""; } ?> >see</option>
                                <option value="<?php echo USERS_DEFAULTRIGHT_COMMENT; ?>" <?php if($user_data[USERS_DEFAULTRIGHT] == USERS_DEFAULTRIGHT_COMMENT){ echo "selected=\"selected\""; } ?> >comment</option>
                                <option value="<?php echo USERS_DEFAULTRIGHT_WRITE; ?>" <?php if($user_data[USERS_DEFAULTRIGHT] == USERS_DEFAULTRIGHT_WRITE){ echo "selected=\"selected\""; } ?> >write</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Download:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANDOWNLOAD; ?>" value="1" <?php if($user_data[USERS_CANDOWNLOAD]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Upload:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANUPLOAD; ?>" value="1" <?php if($user_data[USERS_CANUPLOAD]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Message:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANMESSAGE; ?>" value="1" <?php if($user_data[USERS_CANMESSAGE]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Create/Delete Self:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANCDSELF; ?>" value="1" <?php if($user_data[USERS_CANCDSELF]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Create/Delete Other:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANCDOTHER; ?>" value="1" <?php if($user_data[USERS_CANCDOTHER]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Can Create Schemes:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_CANSCHEME; ?>" value="1" <?php if($user_data[USERS_CANSCHEME]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Is Family:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_ISFAMILY; ?>" value="1" <?php if($user_data[USERS_ISFAMILY]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Is Logged:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_ISLOGGED; ?>" value="1" <?php if($user_data[USERS_ISLOGGED]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Is Active:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_ISACTIVE; ?>" value="1" <?php if($user_data[USERS_ISACTIVE]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Is Permissioned:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo USERS_ISPERMISSIONED; ?>" value="1" <?php if($user_data[USERS_ISPERMISSIONED]){ echo "checked=\"checked\""; } ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Notes:
                        </td>
                        <td>
                            <input type="text" name="<?php echo USERS_MASTERNOTES; ?>" value="<?php IsSetEcho($user_data[USERS_MASTERNOTES], ""); ?>" />
                        </td>
                    </tr>
                </table>
                <input type="submit" value="Update" />
            </form>
        </fieldset>

        <fieldset class="bn_fieldset">
            <legend>Reset Password</legend>
            <form action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                    array(ACTION => ACTION.ACTION_UPDATE."userpass", USERS_USERID => $user_data[USERS_USERID])); ?>" method="post">
                <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user_data[USERS_USERID]; ?>" />
                <input type="text" name="<?php echo USERS_PASSWORD; ?>" />
                <br />
                <input type="submit" value="Reset Password" />
            </form>
        </fieldset>

        <fieldset class="bn_fieldset">
            <legend>Pass Master To User</legend>
            <form action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                    array(ACTION => ACTION.ACTION_PASS."usermaster", USERS_USERID => $user_data[USERS_USERID])); ?>" method="post">
                <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user_data[USERS_USERID]; ?>" />
                <input type="submit" value="Pass Master" />

                <br />
                This is not reversible unless the user that gets master
                <br />
                passes it back to you.
                <br />

            </form>
        </fieldset>

        <fieldset class="bn_fieldset">
            <legend>Delete User</legend>
            <form action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                    array(ACTION => ACTION.ACTION_DELETE."users", USERS_USERID => $user_data[USERS_USERID])); ?>" method="post">
                <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user_data[USERS_USERID]; ?>" />
                <input type="submit" value="Delete User" />

                <br />
                This is not reversible and will delete all of the pages,
                <br />
                messages, and all other data associated to this user.
                <br />

            </form>
        </fieldset>