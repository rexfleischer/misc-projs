

        <?php IsSetEcho($message); ?>
        <fieldset class="bn_fieldset">
            <legend>Create Page</legend>
            <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                array(ACTION => ACTION.ACTION_CREATE."content")); ?>" >
                <input type="hidden" name="<?php echo USERS_USERID; ?>" value="<?php echo $user_id; ?>" />
                <table>
                    <tr>
                        <td>
                            Page Position:
                        </td>
                        <td>
                            <?php if ($hierarchy) { ?>
                            
                            <table>
                                <tr>
                                    <td class="width_100">
                                        title
                                    </td>
                                    <td class="width_100">
                                        after
                                    </td>
                                    <td class="width_100">
                                        child
                                    </td>
                                </tr>
                                <?php foreach($hierarchy as $page) { ?>
                                
                                <tr>
                                    <td>
                                        <?php for($i=0; $i<$page[DBForum::POSTINDENT]; $i++){ echo "&nbsp;"; } if (array_key_exists(PAGETITLE, $page)) echo $page[PAGETITLE]; ?>
                                        
                                    </td>
                                    <td>
                                        <input type="radio" name="position" value="<?php echo $page[DBForum::POSTPARENT]; ?>:<?php echo $page[DBForum::POST_ID]; ?>" />
                                    </td>
                                    <td>
                                        <input type="radio" name="position" value="<?php echo $page[DBForum::POST_ID]; ?>" />
                                    </td>
                                </tr>
                                <?php } ?>
                                
                            </table>
                            <?php } else { ?>
                            
                            root <input type="hidden" name="position" value="0" />
                            <?php } ?>
                            
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Page Title:
                        </td>
                        <td>
                            <input type="text" name="<?php echo PAGETITLE; ?>" value="<?php IsSetEcho($data[PAGETITLE]); ?>" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Conversation Type:
                        </td>
                        <td>
                            <select name="<?php echo PAGETYPE; ?>" >
                                <?php $selected = isset($data[PAGETYPE]) ? $data[PAGETYPE] : BN_PAGETHREADTYPE_NONE; ?>
                                <option value="<?php echo BN_PAGETHREADTYPE_NONE; ?>" <?php if ($selected == BN_PAGETHREADTYPE_NONE) echo "selected=\"selected\""; ?> >None</option>
                                <option value="<?php echo BN_PAGETHREADTYPE_SINGLE; ?>" <?php if ($selected == BN_PAGETHREADTYPE_SINGLE) echo "selected=\"selected\""; ?> >Single</option>
                                <option value="<?php echo BN_PAGETHREADTYPE_MULTI; ?>" <?php if ($selected == BN_PAGETHREADTYPE_MULTI) echo "selected=\"selected\""; ?> >Threaded</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Private Page:
                        </td>
                        <td>
                            <input type="checkbox" name="<?php echo PAGEPRIVATE; ?>" value="1" <?php if (isset($data[PAGEPRIVATE]) && $data[PAGEPRIVATE]){echo "checked=\"checked\"";} ?> />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Create Page" />
                        </td>
                        <td>
                        </td>
                    </tr>
                </table>
            </form>
        </fieldset>

