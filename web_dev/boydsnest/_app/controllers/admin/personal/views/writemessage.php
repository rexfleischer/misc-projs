<?php

$load_editor = false;
$editor = array();
$editor['title'] = "";
?>
        <?php IsSetEcho($message); ?>
        <form action="<?php echo buildurl(BN_URL_PAGE_ADMIN, array(ACTION => ACTION.ACTION_WRITE."message")); ?>" method="post">

            <?php 
            if (isset($user_id) && isset($username))
            {
                $load_editor = true;
                ?>
            
            <input type="hidden" name="<?php echo DBMessage::FCORE_MESSAGE_TOS; ?>[]" value="<?php echo $user_id; ?>" />
            <?php

                $editor['title'] .= "To: $username";
            }
            else if (isset($user_list))
            {
                $load_editor = true;
                $first = true;
                $editor['title'] .= "       To: ";
                $editor['title'] .= "       <select name=\"".DBMessage::FCORE_MESSAGE_TOS."[]\">\n";

                foreach($user_list as $user)
                {
                    $selected = false;
                    if (isset($user_id))
                    {
                        if ($user_id == $user[USERS_USERID])
                        {
                            $selected = true;
                        }
                    }
                    else
                    {
                        if ($first)
                        {
                            $selected = true;
                            $first = false;
                        }
                    }
                    $editor['title'] .= 
                            "           <option value=\"".$user[USERS_USERID]."\" ".
                            ($selected ? "selected='selected' " : "").">";
                    $editor['title'] .= $user[USERS_USERNAME];
                    $editor['title'] .= "</option>\n";
                }
                $editor['title'] .= "       </select>";
            }

            if ($load_editor)
            {
                $editor['submit'] = "Send Message";
                $editor['name'] = DBMessage::MESSAGE;
                if (isset($message_content))
                {
                    $editor['content'] = $message_content;
                }
                $editor['title'] .= " Subject: <input type=\"text\" name=\"".DBMessage::TITLE."\" value=\"";
                $editor['title'] .= isset($message_title) ? $message_title : "";
                $editor['title'] .= "\" />";
                echo FCore::LoadViewPHP("form/BasicTextEditor", $editor);
            }
            else
            {
                ?>
                An Error Occurred Or You May Have Gotten Here Incorrectly
            <?php
            }
            ?>

        </form>