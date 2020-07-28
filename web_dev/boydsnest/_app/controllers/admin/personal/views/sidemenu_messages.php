        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_VIEW."profile")); ?>">
                 My Profile
            </a>
        </div>
<?php if (BoydsnestSession::GetInstance()->get(USERS_CANMESSAGE)) { ?>
        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_WRITE."message")); ?>">
                 Compose Message
            </a>
        </div>
<?php } ?>
        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_LIST."receivedmessages")); ?>">
                 Received Messages
            </a>
        </div>
        <div class="clear">
            <a href="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION_LIST."sentmessages")); ?>">
                 Sent Messages
            </a>
        </div>