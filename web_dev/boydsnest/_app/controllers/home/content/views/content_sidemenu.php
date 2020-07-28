

<?php foreach($page_list as $page) { ?>

        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_HOME,
                    array(ACTION => ACTION_VIEW."content",
                          USERS_USERID => $page[USERS_USERID])); ?>">
                 <?php echo $page[USERS_USERNAME]; ?>
            </a>
        </div>
                 
            <?php
            if (isset($page[DBForum::POSTCHILDREN]))
            {
                foreach($page[DBForum::POSTCHILDREN] as $post)
                {

            ?>

        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_HOME,
                    array(ACTION => ACTION_VIEW."content",
                          USERS_USERID => $page[USERS_USERID],
                          DBForum::POST_ID => $post[DBForum::POST_ID])); ?>">
                 <?php for($i=0; $i<$post[DBForum::POSTINDENT]+1; $i++){echo "&nbsp;&nbsp;"; } echo $post[PAGETITLE]; ?>
            </a>
        </div>
        <?php
                }
            }
        }
?>

