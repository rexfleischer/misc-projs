

        <?php IsSetEcho($message); ?>

        <fieldset class="bn_fieldset">
            <legend>Page Content</legend>
            <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                array(ACTION => ACTION.ACTION_WRITE."content", DBForum::POST_ID => $page_info[DBForum::POST_ID])); ?>" >
                <input type="hidden" name="<?php echo DBForum::POST_ID; ?>" value="<?php echo $page_info[DBForum::POST_ID]; ?>" />
                <?php echo FCore::LoadViewPHP("form/BasicTextEditor",
                        array('title'   => "Content",
                              'name'    => DBDataType::CONTENT,
                              'content' => $page_info[DBDataType::CONTENT])); ?>
                <table>
                    <tr>
                        <td class="width_150">
                            Page Title:
                        </td>
                        <td class="width_150">
                            <input type="text" name="<?php echo PAGETITLE; ?>" value="<?php echo $page_info[PAGETITLE]; ?>" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Private:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo PAGEPRIVATE; ?>" value="1" <?php if ($page_info[PAGEPRIVATE]) echo "checked=\"checked\""; ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Created At:
                        </td>
                        <td>
                            <?php echo $page_info[DBForum::TIMEMADE]; ?>
                            
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Number Of Followers:
                        </td>
                        <td>
                            <?php echo sizeof($page_info[PAGEFOLLOWERS]); ?>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Update" />
                        </td>
                        <td>
                        </td>
                    </tr>
                </table>
            </form>
        </fieldset>

        <?php if ($page_info[PAGEPRIVATE]) { ?>

        <fieldset class="bn_fieldset">
            <legend>User Rights</legend>
            <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, 
                array(ACTION => ACTION.ACTION_UPDATE."contentrights", DBForum::POST_ID => $page_info[DBForum::POST_ID])); ?>">
                <input type="hidden" name="<?php echo DBForum::POST_ID; ?>" value="<?php echo $page_info[DBForum::POST_ID]; ?>" />
                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td colspan="2">User Rights Level</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="width_150">
                            Username
                        </td>
                        <td class="width_100">
                            None
                        </td>
                        <td class="width_100">
                            View
                        </td>
                        <td class="width_100">
                            Comment
                        </td>
                        <td class="width_100">
                            Write
                        </td>
                    </tr>
                <?php
                $all_users = "";
                $page_rights = array_key_exists(PAGERIGHTS, $page_info) ?
                        $page_info[PAGERIGHTS] : array();
                foreach($user_list as $user)
                {
                    $selected = array_key_exists($user[USERS_USERID], $page_rights)
                            ? $page_rights[$user[USERS_USERID]] : $user[USERS_DEFAULTRIGHT];
                    if ($all_users != "")
                    {
                        $all_users .= ":";
                    }
                    $all_users .= $user[USERS_USERID];
                    ?>
                
                    <tr>
                        <td>
                            <?php echo $user[USERS_USERNAME]; ?>

                        </td>
                        <td><?php if($user[USERS_DEFAULTRIGHT] > USERS_DEFAULTRIGHT_NONE){ ?>

                            N / A
                            <?php } else { ?>

                            <input type="radio" name="<?php echo $user[USERS_USERID]; ?>" value="<?php echo USERS_DEFAULTRIGHT_NONE; ?>" <?php if($selected == USERS_DEFAULTRIGHT_NONE) echo "checked=\"checked\""; ?> />
                            <?php } ?>
                        </td>
                        <td><?php if($user[USERS_DEFAULTRIGHT] > USERS_DEFAULTRIGHT_SEE){ ?>

                            N / A
                            <?php } else { ?>

                            <input type="radio" name="<?php echo $user[USERS_USERID]; ?>" value="<?php echo USERS_DEFAULTRIGHT_SEE; ?>" <?php if($selected == USERS_DEFAULTRIGHT_SEE) echo "checked=\"checked\""; ?> />
                            <?php } ?>
                        </td>
                        <td><?php if($user[USERS_DEFAULTRIGHT] > USERS_DEFAULTRIGHT_COMMENT){ ?>

                            N / A
                            <?php } else { ?>

                            <input type="radio" name="<?php echo $user[USERS_USERID]; ?>" value="<?php echo USERS_DEFAULTRIGHT_COMMENT; ?>" <?php if($selected == USERS_DEFAULTRIGHT_COMMENT) echo "checked=\"checked\""; ?> />
                            <?php } ?>
                        </td>
                        <td>
                            <input type="radio" name="<?php echo $user[USERS_USERID]; ?>" value="<?php echo USERS_DEFAULTRIGHT_WRITE; ?>" <?php if($selected == USERS_DEFAULTRIGHT_WRITE) echo "checked=\"checked\""; ?> />
                        </td>
                    </tr>
                <?php } ?>
                
                    <tr>
                        <td><input type="submit" value="Update Rights" /></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
                <input type="hidden" name="user_ids" value="<?php echo $all_users; ?>" />
            </form>
        </fieldset>
        <?php } ?>

        <fieldset class="bn_fieldset">
            <legend>Delete Page And Children</legend>
            <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                array(ACTION => ACTION.ACTION_DELETE."content", DBForum::POST_ID => $page_info[DBForum::POST_ID])); ?>" >
                <input type="hidden" name="<?php echo DBForum::POST_ID; ?>" value="<?php echo $page_info[DBForum::POST_ID]; ?>" />
                This will delete the page and all of its children.
                This action cannot be reversed.
                <br />
                <input type="submit" value="Delete" />
            </form>
        </fieldset>

