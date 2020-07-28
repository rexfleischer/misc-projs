

        <?php IsSetEcho($message); ?>

        <table>
            <tr>
                <td class="width_100">
                    Page Id
                </td>
                <td class="width_150">
                    Page Title
                </td>
                <td class="width_150">
                    Page Order
                </td>
                <td class="width_150">
                    Page Parent
                </td>
                <td class="width_100">
                    View Content
                </td>
                <td class="width_100">
                    Response List
                </td>
            </tr>
            <?php foreach($page_list as $page) { ?>
            
            <tr>
                <td>
                    <?php echo $page[DBForum::POST_ID]; ?>
                    
                </td>
                <td>
                    <?php for($i=0; $i<$page[DBForum::POSTINDENT]; $i++){ echo "&nbsp;&nbsp;"; } if (array_key_exists(PAGETITLE, $page)) echo $page[PAGETITLE]; ?>
                    
                </td>
                <td>
                    <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_LIST."content")); ?>" >
                        <input type="hidden" name="<?php echo DBForum::POST_ID; ?>" value="<?php echo $page[DBForum::POST_ID]; ?>" />
                        <input type="text" size="4" name="<?php echo DBForum::POSTORDER; ?>" value="<?php echo $page[DBForum::POSTORDER]; ?>" />
                        <input type="submit" value="Update" />
                    </form>
                </td>
                <td>
                    <form method="post" action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_LIST."content")); ?>" >
                        <input type="hidden" name="<?php echo DBForum::POST_ID; ?>" value="<?php echo $page[DBForum::POST_ID]; ?>" />
                        <input type="text" size="4" name="<?php echo DBForum::POSTPARENT; ?>" value="<?php echo $page[DBForum::POSTPARENT]; ?>" />
                        <input type="submit" value="Update" />
                    </form>
                </td>
                <td>
                    <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN, 
                        array(ACTION => ACTION_VIEW."content", DBForum::POST_ID => $page[DBForum::POST_ID])); ?>">View Content</a>
                </td>
                <td>
                    <?php if ($page[PAGETYPE] != BN_PAGETHREADTYPE_NONE) { ?>
                    <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN,
                        array(ACTION => ACTION_VIEW."response", DBForum::POST_ID => $page[DBForum::POST_ID])); ?>">View Content</a>
                    <?php } else { ?>
                    View Content
                    <?php } ?>
                    
                </td>
            </tr>
            <?php } ?>
            
        </table>

