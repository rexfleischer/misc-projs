<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
require_once DIR_DATABASE_DATABASE;
require_once DIR_FORM_ADMIN_PAGECREATE;
require_once DIR_FORM_FORMTEXTFIELDWRITER;
require_once DIR_HELPER_MENUANDCONTENTHELPER1;
require_once DIR_HELPER_THREADEDFORUMBUILDER;
require_once DIR_HELPER_THREADEDFORUMVIEWER;
require_once DIR_PAGE_ERRORPAGES;


//==============================================================================
//  admin.php master template
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="abstract class page_adminabstract">
abstract class page_adminabstract extends _baselayout implements _mch1, _state {
    public abstract function absoluteContent();
    public abstract function absoluteStyle();
    public abstract function absolutePreProcessing();

    public function menuContent1(){
        ?>

<div id="text">Messages</div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::MESSAGES_LIST; ?>">Read</a></div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::MESSAGES_WRITE; ?>">Compose</a></div>
<?php if (_SESSION::GetCanCDOther() || _SESSION::GetCanCDSelf()) { ?>
<div id="text">Pages</div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_CREATE; ?>">Create</a></div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_MANAGE; ?>">Manage</a></div>
<?php
        }
    }
    public function menuContent2(){
        ?>

<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_CREATE; ?>">Create User</a></div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_VIEW; ?>">View Users</a></div>
<div><a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::LOGIN_LOGS; ?>">Failed Login Attempts</a></div>


<?php
    }
    public function pageContent(){
        $this->absoluteContent();
    }

    
    protected function thisPageLayout(){
        MenuAndContentHelper1_Layout($this, true, _SESSION::GetIsMaster(), "", "Master Options");
    }
    protected function thisPageStyle(){
        MenuAndContentHelper1_Style();
        $this->absoluteStyle();
    }
    protected function thisPagePreProcessing(){
        $this->absolutePreProcessing();
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php announcements
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_announcements">
class adminstate_announcements extends page_adminabstract {
    public function absoluteContent(){
        ?>
There Are No Announcements
<?php
    }
    public function absoluteStyle(){
        
    }
    public function absolutePreProcessing(){
        
    }
}
// </editor-fold>

//==============================================================================
//  admin.php messages list
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_messages_list">
class adminstate_messages_list extends page_adminabstract {
    public function absoluteContent(){
        echo "<h2>Messages</h2>";
        $count = sizeof($this->crossInfo[_ADMINPAGE::MESSAGES_LIST]);
        if ($count == 0){
            echo "No Messages";
            return;
        }
        for($i=0; $i<$count; $i++){
            $thismessage = $this->crossInfo[_ADMINPAGE::MESSAGES_LIST][$i];
            ?>

<div id="_listcontainer" class="<?php if($i%2==0){ echo 'on'; } else { echo 'off'; } ?>">
    <a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::MESSAGES_VIEW."&".MESSAGES_MESSAGEID."=".$thismessage[MESSAGES_MESSAGEID]; ?>">
        <div id="_listcontainer_width30px"><?php echo $i+$this->crossInfo[_ADMINPAGE::MESSAGES_START]; ?></div>
        <div id="_listcontainer_width150px"><?php echo $thismessage[USERS_USERNAME]; ?></div>
        <div id="_listcontainer_width150px"><?php echo $thismessage[MESSAGES_TITLE]; ?></div>
        <div id="_listcontainer_width100px"><?php echo $thismessage[MESSAGES_TIMESENT]; ?></div>
    </a>
    <?php
        echo _FORM::_FormBegin("deletemessage", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::MESSAGES_LIST);
        echo _FORM::Hidden(MESSAGES_MESSAGEID, $thismessage[MESSAGES_MESSAGEID]);
        echo _FORM::Hidden(ACTION_GO, 1);
        echo _FORM::Submit("Delete Message");
        echo _FORM::_FormEnd();
    ?>
</div>
<?php
        }
    }

    public function absoluteStyle(){
        
    }

    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        if (_FCORE::IsSetPOST(ACTION_GO)){
            $messageID = _FCORE::IsSetPostDefault(MESSAGES_MESSAGEID, false);
            if ($messageID){
                DB_MESSAGE::_DeleteMessage($db, $messageID, _SESSION::GetUserID());
            }
        }
        $this->crossInfo[_ADMINPAGE::MESSAGES_CONTEXT] = _FCORE::IsSetGetDefault(_ADMINPAGE::MESSAGES_CONTEXT, DB_MESSAGE::CONTEXT_RECIEVED);
        $this->crossInfo[_ADMINPAGE::MESSAGES_START] = _FCORE::IsSetGetDefault(_ADMINPAGE::MESSAGES_START, 0);
        $this->crossInfo[_ADMINPAGE::MESSAGES_AMOUNT] = _FCORE::IsSetGetDefault(_ADMINPAGE::MESSAGES_AMOUNT, 50);
        $this->crossInfo[_ADMINPAGE::MESSAGES_LIST] = DB_MESSAGE::_GetMessageListForUser(
                $db, _SESSION::GetUserID(),
                $this->crossInfo[_ADMINPAGE::MESSAGES_CONTEXT],
                $this->crossInfo[_ADMINPAGE::MESSAGES_START],
                $this->crossInfo[_ADMINPAGE::MESSAGES_AMOUNT]);
        $db->disconnect();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php messages view
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_messages_view">
class adminstate_messages_view extends page_adminabstract {
    public function absoluteContent(){
        if (!$this->crossInfo['message']){
            echo "An Error Has Occured In Getting The Message";
            return;
        }
        ?>

Title: <?php echo $this->crossInfo['message']->getTitle(); ?>
<br />
From: <?php echo $this->crossInfo['message']->getUserFrom(); ?>
<br />
Message:
<br />
<?php echo $this->crossInfo['message']->getMessage(); ?>

<?php
    }
    public function absoluteStyle(){
?>
<style type="text/css">
#_message_body
{
    
}
#_message_title
{
    
}
#_message_time
{
    
}
</style>
<?php
    }
    public function absolutePreProcessing(){
        $messageID = _FCORE::IsSetGET(MESSAGES_MESSAGEID);
        if ($messageID!=null){
            $db = _DB::_Connect();
            $this->crossInfo['message'] = DB_MESSAGE::_GetMessage($db, $messageID);
            $db->disconnect();
        }
    }
}
// </editor-fold>

//==============================================================================
//  admin.php messages write
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_messages_write">
class adminstate_messages_write extends page_adminabstract {
    public function absoluteContent(){
        echo "<h2>Send A Message</h2>";
        if (isset($this->crossInfo['results'])){
            echo $this->crossInfo['results'];
        }
        $select = _FORM::SelectBegin(USERS_USERID);
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        for($i=0; $i<$count; $i++){
            if($this->crossInfo[_ADMINPAGE::USERS_LIST][$i][USERS_USERID] != _SESSION::GetUserID() &&
               $this->crossInfo[_ADMINPAGE::USERS_LIST][$i][USERS_USERNAME] != _SESSION::GUEST){
                $select .= _FORM::Option(
                        $this->crossInfo[_ADMINPAGE::USERS_LIST][$i][USERS_USERNAME],
                        $this->crossInfo[_ADMINPAGE::USERS_LIST][$i][USERS_USERID],
                        $i==0);
            }
        }
        $select .= _FORM::SelectEnd();
        FormTextfieldWriter::EchoFormLayoutTemplate(
                URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::MESSAGES_WRITE,
                1,
                "",
                "User To: ".$select."<br />".
                "Subject: "._FORM::Text(MESSAGES_TITLE, "", 40, 40),
                MESSAGES_MESSAGE,
                70,
                20,
                "",
                "Send");
    }
    public function absoluteStyle(){
        FormTextfieldWriter::EchoCSS();
    }
    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        if (_FCORE::IsSetPOST(ACTION_GO)){
            $userTo = _FCORE::IsSetPOST(USERS_USERID);
            $title = _FCORE::IsSetPOST(MESSAGES_TITLE);
            $message = _FCORE::IsSetPOST(MESSAGES_MESSAGE);
            if ($userTo!=null && $title!=null && $message!=null){
                if (DB_MESSAGE::_CreateMessage($db, $userTo, _SESSION::GetUserID(), $title, $message, "", "")){
                    $this->crossInfo['results'] = "Message Successfully Sent";
                } else {
                    $this->crossInfo['results'] = "Message Failed To Send";
                }
            } else {
                $this->crossInfo['results'] = "Message Failed To Send";
            }
        }
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = DB_USER::_GetUserList($db);
        $db->disconnect();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php users create
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_users_create">
class adminstate_users_create extends page_adminabstract {
    public function absoluteContent(){
        echo "<h2>Create User</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        echo _FORM::_FormBegin('createuser', URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_CREATE);
        echo _FORM::Hidden(ACTION_GO, 1);
        echoln("<table>");
        echoln("    <tr>");
        echoln("        <td>Username:</td>");
        echoln("        <td>"._FORM::Text(USERS_USERNAME, "", 40, 40)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Password:</td>");
        echoln("        <td>"._FORM::Text(USERS_PASSWORD, "", 40, 40)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Email:</td>");
        echoln("        <td>"._FORM::Text(USERS_EMAIL, "", 40, 60)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Secret Question:</td>");
        echoln("        <td>"._FORM::Text(USERS_SECRETQUESTION, "", 40, 255)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Secret Answer:</td>");
        echoln("        <td>"._FORM::Text(USERS_SECRETANSWER, "", 40, 255)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Download From Site:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_CANDOWNLOAD, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Upload To Site:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_CANUPLOAD, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Send Private Messages:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_CANMESSAGE, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Create/Delete Content For Self:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_CANCDSELF, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Create/Delete Content For Others:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_CANCDOTHER, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Is Family:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_ISFAMILY, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Log User Activity:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_ISLOGGED, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Can Create Content Without Permission:</td>");
        echoln("        <td>"._FORM::CheckBox(USERS_ISPERMISSIONED, 1)."</td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Default Right:</td>");
        echoln("        <td>");
        echoln("            "._FORM::SelectBegin(USERS_DEFAULTRIGHT));
        echoln("            "._FORM::Option("None", USERS_DEFAULTRIGHT_NONE, true));
        echoln("            "._FORM::Option("Can View Everything", USERS_DEFAULTRIGHT_SEE));
        echoln("            "._FORM::Option("Can Comment On Everything", USERS_DEFAULTRIGHT_COMMENT));
        echoln("            "._FORM::Option("Can Change Any User Content", USERS_DEFAULTRIGHT_WRITE));
        echoln("            "._FORM::SelectEnd());
        echoln("        </td>");
        echoln("    </tr>");
        echoln("    <tr>");
        echoln("        <td>Your Notes (can only be viewed by you):</td>");
        echoln("        <td>"._FORM::Text(USERS_MASTERNOTES, "", 40, 255)."</td>");
        echoln("    </tr>");
        echoln("</table>");
        echo _FORM::Submit("Create user");
        echo _FORM::_FormEnd();
    }
    public function absoluteStyle(){
        
    }
    public function absolutePreProcessing(){
        if (_FCORE::IsSetPostDefault(ACTION_GO, false)){
            $db = _DB::_Connect();
            $username = _FCORE::IsSetPost(USERS_USERNAME);
            $password = _FCORE::IsSetPost(USERS_PASSWORD);
            $email = _FCORE::IsSetPost(USERS_EMAIL);
            $secretQuestion = _FCORE::IsSetPost(USERS_SECRETQUESTION);
            $secretAnswer = _FCORE::IsSetPost(USERS_SECRETANSWER);
            $masterNotes = _FCORE::IsSetPost(USERS_MASTERNOTES);
            $canDownload = _FCORE::IsSetPostDefault(USERS_CANDOWNLOAD, 0)==1;
            $canUpload = _FCORE::IsSetPostDefault(USERS_CANUPLOAD, 0)==1;
            $canMessage = _FCORE::IsSetPostDefault(USERS_CANMESSAGE, 0)==1;
            $canCDSelf = _FCORE::IsSetPostDefault(USERS_CANCDSELF, 0)==1;
            $canCDOther = _FCORE::IsSetPostDefault(USERS_CANCDOTHER, 0)==1;
            $isFamily = _FCORE::IsSetPostDefault(USERS_ISFAMILY, 0)==1;
            $isActive = true; //_FCORE::IsSetPostDefault(USERS_ISACTIVE, false);
            $isLogged = _FCORE::IsSetPostDefault(USERS_ISLOGGED, 0)==1;
            $isPermissioned = _FCORE::IsSetPostDefault(USERS_ISPERMISSIONED, 0)==1;
            $defaultRight = _FCORE::IsSetPost(USERS_DEFAULTRIGHT);
            try {
                DB_USER::_CreateUser(
                        $db,
                        $username,
                        $password,
                        $email,
                        $secretQuestion,
                        $secretAnswer,
                        $masterNotes,
                        $canDownload,
                        $canUpload,
                        $canMessage,
                        $canCDSelf,
                        $canCDOther,
                        $isFamily,
                        $isActive,
                        $isLogged,
                        $isPermissioned,
                        $defaultRight);
                $this->crossInfo[RESULTS] = "Successfully Made User";
            } catch (InvalidParamException $e){
                $this->crossInfo[RESULTS] = "An Internal Error Occured With This Message: ".$e->getMessage();
            } catch (InvalidValueException $e){
                $this->crossInfo[RESULTS] = $e->getMessage();
            } catch (Exception $e){
                $this->crossInfo[RESULTS] = $e->getMessage();
            }
        }
    }
}
// </editor-fold>

//==============================================================================
//  admin.php users view
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_users_view">
class adminstate_users_view extends page_adminabstract {
    public function absoluteContent(){
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        if ($count <= 2){
            echo "No Users To View";
            return;
        }
        echo "<h2>User List</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        for($i=0; $i<$count; $i++){
            $thisuser = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
            if ($thisuser[USERS_USERNAME]=="Guest" || $thisuser[USERS_USERID]==_SESSION::GetUserID()){
                continue;
            }
            ?>

<div id="_listcontainer" class="<?php if($i%2==0){ echo 'on'; } else { echo 'off'; } ?>">
    <a href="<?php echo URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_MANAGE."&".USERS_USERID."=".$thisuser[USERS_USERID]; ?>">
        <div id="_listcontainer_width150px"><?php echo $thisuser[USERS_USERNAME]; ?></div>
        <div id="_listcontainer_width400px"><?php if($thisuser[USERS_MASTERNOTES]){ echo $thisuser[USERS_MASTERNOTES]; } else { echo "No Notes On User"; } ?></div>
    </a>
    <div id="_listcontainer_width150px"><div id="_listcontainer_something">
        <?php
            echo _FORM::_FormBegin("deleteuser", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_LOGS."&".USERS_USERID."=".$thisuser[USERS_USERID]);
            echo _FORM::Submit("View User Logs");
            echo _FORM::_FormEnd();
        ?>
    </div></div>
    <div id="_listcontainer_tomato">
        <?php
            echo _FORM::_FormBegin("deleteuser", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_VIEW);
            echo _FORM::Hidden(ACTION_GO, ACTION_DELETE);
            echo _FORM::Hidden(USERS_USERID, $thisuser[USERS_USERID]);
            echo _FORM::Submit("Delete User");
            echo _FORM::_FormEnd();
        ?>
    </div>
</div>
<?php
        }
    }
    public function absoluteStyle(){

    }
    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        $action = _FCORE::IsSetPostDefault(ACTION_GO, false);
        if ($action){
            $userID = _FCORE::IsSetPostDefault(USERS_USERID, false);
            if ($userID){
                try {
                    DB_USER::_DeleteUser($db, $userID);
                    $this->crossInfo[RESULTS] = "Successfully Deleted User";
                } catch(Exception $e) {
                    $this->crossInfo[RESULTS] = "Failed To Delete User... System Error: ".$e->getMessage();
                }
            }
        }
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = DB_USER::_GetUserList($db);
        $db->disconnect();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php users logs
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_users_logs">
class adminstate_users_logs extends page_adminabstract {
    public function absoluteContent(){
        if (!$this->crossInfo[_ADMINPAGE::USERS_LOGS]){
            echo "No Logs To View";
            return;
        }
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LOGS]);
        if ($count==0){
            echo "No Logs To View";
            return;
        }
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        echo "<h2>".$this->crossInfo[USERS_USERNAME]."</h2>";
        for($i=0; $i<$count; $i++){
            $thislog = $this->crossInfo[_ADMINPAGE::USERS_LOGS][$i];
            $goTo = URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_LOGS."&".
                USERS_USERID."=".$thisuser[USERS_USERID]."&".
                _ADMINPAGE::USERS_LOGS_START."=".$this->crossInfo[_ADMINPAGE::USERS_LOGS_START]."&".
                _ADMINPAGE::USERS_LOGS_AMOUNT."=".$this->crossInfo[_ADMINPAGE::USERS_LOGS_AMOUNT];
            ?>

<div id="_listcontainer" class="<?php if($i%2==0){ echo 'on'; } else { echo 'off'; } ?>">
    <a href="<?php echo $goTo; ?>">
        <div id="_listcontainer_width30px"><?php echo $i+$this->crossInfo[_ADMINPAGE::USERS_LOGS_START]; ?></div>
        <div id="_listcontainer_width150px"><?php echo $thislog->getTimeLogged(); ?></div>
        <div id="_listcontainer_width400px"><?php echo $thislog->getMessage(); ?></div>
    </a>
    <div id="_listcontainer_tomato">
        <?php
            echo _FORM::_FormBegin("deleteuserlog", $goTo);
            echo _FORM::Hidden(ACTION_GO, ACTION_DELETE);
            echo _FORM::Hidden(USERLOG_LOGID, $thislog->getLogID());
            echo _FORM::Submit("Delete Log");
            echo _FORM::_FormEnd();
        ?>
    </div>
</div>
<?php
        }
    }
    public function absoluteStyle(){
        
    }
    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        $action = _FCORE::IsSetGetDefault(ACTION_GO, false);
        if ($action){
            $logID = _FCORE::IsSetPostDefault(USERLOG_LOGID, false);
            if ($logID){
                try {
                    DB_USERLOG::_DeleteUserLog($db, $logID);
                    $this->crossInfo[RESULTS] = "Successfully Deleted Log";
                } catch(Exception $e){
                    $this->crossInfo[RESULTS] = "Failed To Delete Log";
                }
            }
        }
        $userID = _FCORE::IsSetGetDefault(USERS_USERID, false);
        if (!$userID){
            $this->crossInfo[_ADMINPAGE::USERS_LOGS] = false;
            return;
        }
        $this->crossInfo[_ADMINPAGE::USERS_LOGS_START] = _FCORE::IsSetGetDefault(_ADMINPAGE::USERS_LOGS_START, 0);
        $this->crossInfo[_ADMINPAGE::USERS_LOGS_AMOUNT] = _FCORE::IsSetGetDefault(_ADMINPAGE::USERS_LOGS_AMOUNT, 50);
        $this->crossInfo[_ADMINPAGE::USERS_LOGS] = DB_USERLOG::_GetUserLogList(
                $db,
                $userID,
                $this->crossInfo[_ADMINPAGE::USERS_LOGS_START],
                $this->crossInfo[_ADMINPAGE::USERS_LOGS_AMOUNT]);
        $this->crossInfo[USERS_USERNAME] = $db->SelectOneOfOneField(USERS, USERS_USERNAME);
        $db->disconnect();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php users manage
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_users_manage">
class adminstate_users_manage extends page_adminabstract {
    public function absoluteContent(){
        if (!$this->crossInfo[_ADMINPAGE::USERS_LIST]){
            echo "No User Selected";
            return;
        }
        echo "<h2>Manage User</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        echo _FORM::_FormBegin('createuser', URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::USERS_MANAGE."&".USERS_USERID."=".$this->crossInfo[_ADMINPAGE::USERS_LIST]->getUserID());
        echo _FORM::Hidden(ACTION_GO, 1);
        echoln("    <table>");
        echoln("        <tr>");
        echoln("            <td>Username:</td>");
        echoln("            <td>".$this->crossInfo[_ADMINPAGE::USERS_LIST]->getUsername()."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Password:</td>");
        echoln("            <td>"._FORM::Text(USERS_PASSWORD, "", 40, 40)."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Email:</td>");
        echoln("            <td>"._FORM::Text(USERS_EMAIL, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getEmail(), 40, 60)."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Secret Question:</td>");
        echoln("            <td>"._FORM::Text(USERS_SECRETQUESTION, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getSecretQuestion(), 40, 255)."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Secret Answer:</td>");
        echoln("            <td>"._FORM::Text(USERS_SECRETANSWER, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getSecretAnswer(), 40, 255)."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Download From Site:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_CANDOWNLOAD, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getCanDownload())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Upload To Site:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_CANUPLOAD, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getCanUpload())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Send Private Messages:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_CANMESSAGE, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getCanMessage())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Create Content For Self:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_CANCDSELF, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getCanCDSelf())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Create Content For Others:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_CANCDOTHER, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getCanCDOther())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Is Family:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_ISFAMILY, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getIsFamily())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Log User Activity:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_ISLOGGED, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getIsLogged())."</td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Can Create Content Without Permission:</td>");
        echoln("            <td>"._FORM::CheckBox(USERS_ISPERMISSIONED, 1, $this->crossInfo[_ADMINPAGE::USERS_LIST]->getIsPermissioned())."</td>");
        echoln("        </tr>");
        $defaultright = $this->crossInfo[_ADMINPAGE::USERS_LIST]->getDefaultRight();
        echoln("        <tr>");
        echoln("            <td>Default Right:</td>");
        echoln("            <td>");
        echoln("                "._FORM::SelectBegin(USERS_DEFAULTRIGHT));
        echoln("                    "._FORM::Option("None", USERS_DEFAULTRIGHT_NONE, $defaultright==USERS_DEFAULTRIGHT_NONE));
        echoln("                    "._FORM::Option("Can View Everything", USERS_DEFAULTRIGHT_SEE, $defaultright==USERS_DEFAULTRIGHT_SEE));
        echoln("                    "._FORM::Option("Can Comment On Everything", USERS_DEFAULTRIGHT_COMMENT, $defaultright==USERS_DEFAULTRIGHT_COMMENT));
        echoln("                    "._FORM::Option("Can Change Any User Content", USERS_DEFAULTRIGHT_WRITE, $defaultright==USERS_DEFAULTRIGHT_WRITE));
        echoln("                "._FORM::SelectEnd());
        echoln("            </td>");
        echoln("        </tr>");
        echoln("        <tr>");
        echoln("            <td>Your Notes (can only be viewed by you):</td>");
        echoln("            <td>"._FORM::Text(USERS_MASTERNOTES, addslashes($this->crossInfo[_ADMINPAGE::USERS_LIST]->getMasterNotes()), 40, 255)."</td>");
        echoln("        </tr>");
        echoln("    </table>");
        echo _FORM::Submit("Update user");
        echo _FORM::_FormEnd();
    }
    public function absoluteStyle(){

    }
    public function absolutePreProcessing(){
        $userID = _FCORE::IsSetGetDefault(USERS_USERID, false);
        if ($userID){
            $db = _DB::_Connect();
            $this->crossInfo[_ADMINPAGE::USERS_LIST] = DB_USER::_GetUser($db, $userID);
            if (_FCORE::IsSetPostDefault(ACTION_GO, false)){
                try {
                    $pass = _FCORE::IsSetPostDefault(USERS_PASSWORD, false);
                    if ($pass){
                        if ($pass != ""){
                            $this->crossInfo[_ADMINPAGE::USERS_LIST]->setPassword(_FCORE::IsSetPost(USERS_PASSWORD));
                        }
                    }
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setEmail(_FCORE::IsSetPost(USERS_EMAIL));
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setSecretQuestion(_FCORE::IsSetPost(USERS_SECRETQUESTION));
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setSecretAnswer(_FCORE::IsSetPost(USERS_SECRETANSWER));
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setMasterNotes(_FCORE::IsSetPost(USERS_MASTERNOTES));
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setCanDownload(_FCORE::IsSetPostDefault(USERS_CANDOWNLOAD, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setCanUpload(_FCORE::IsSetPostDefault(USERS_CANUPLOAD, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setCanMessage(_FCORE::IsSetPostDefault(USERS_CANMESSAGE, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setCanCDSelf(_FCORE::IsSetPostDefault(USERS_CANCDSELF, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setCanCDOther(_FCORE::IsSetPostDefault(USERS_CANCDOTHER, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setIsFamily(_FCORE::IsSetPostDefault(USERS_ISFAMILY, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setIsActive(_FCORE::IsSetPostDefault(USERS_ISACTIVE, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setIsLogged(_FCORE::IsSetPostDefault(USERS_ISLOGGED, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setIsPermissioned(_FCORE::IsSetPostDefault(USERS_ISPERMISSIONED, 0)==1);
                    $this->crossInfo[_ADMINPAGE::USERS_LIST]->setDefaultRight(_FCORE::IsSetPost(USERS_DEFAULTRIGHT));
                    $this->crossInfo[RESULTS] = "Successfully Updated user";
                } catch(Exception $e){
                    $this->crossInfo[RESULTS] = "Failed To Upate User";
                }
            }
            $db->disconnect();
        } else {
            $this->crossInfo[_ADMINPAGE::USERS_LIST] = false;
        }
    }
}
// </editor-fold>

//==============================================================================
//  admin.php pages create
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_create">
abstract class adminstate_pages_create extends page_adminabstract {
    protected abstract function UserSelectOption_Get(DBConnect &$db);
    protected abstract function UserSelectOption_ECHO();
    protected abstract function CreatePageUserIDCheck($userID);

    public function absoluteContent(){
        echo "<h2>Create A New Page</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        echo _FORM::_FormBegin("createpage", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_CREATE);
        echo _FORM::Hidden(ACTION, ACTION_CREATE);
        $this->UserSelectOption_ECHO();
        echo "Child Of: ";
        echo "<span id='childofselecttarget'>";
        MakeCreateUserChildOfSelect(
                $this->crossInfo[_ADMINPAGE::PAGES_LIST],
                isset($this->crossInfo[PAGES_CHILDOF]) ? $this->crossInfo[PAGES_CHILDOF] : 0);
        echo "</span>";
        echo "<div>Page Title: "._FORM::Text(PAGES_TITLE, "", 40, 255)."</div>";
        echo "<div>Speech Type: ";
        echo _FORM::SelectBegin(PAGES_FORUMTYPE);
        echo _FORM::Option("No Comments", PAGES_FORUMTYPE_NONE, true);
        echo _FORM::Option("Comments", PAGES_FORUMTYPE_LINEAR, false);
        echo _FORM::Option("Conversation", PAGES_FORUMTYPE_FORUM, false);
        echo _FORM::SelectEnd();
        echo "</div>";
        echo "<div>Privacy: ";
        echo _FORM::SelectBegin(PAGES_ISPRIVATE);
        echo _FORM::Option("Public", 0, true);
        echo _FORM::Option("Private", 1, false);
        echo _FORM::SelectEnd();
        echo "</div>";
        echo _FORM::Submit("Create Page");
        echo _FORM::_FormEnd();
    }
    public function absoluteStyle(){
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/jQuery.js");
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/admin_pagescreate.js");
    }
    public function absolutePreProcessing(){
        $action = _FCORE::IsSetPostDefault(ACTION, false);
        $db = _DB::_Connect();
        if ($action !== false){
            $valid = true;
            $this->crossInfo[RESULTS] = "";
            //==================================================================
            $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPost(PAGES_USERID);
            $this->CreatePageUserIDCheck($this->crossInfo[PAGES_USERID]);
            //==================================================================
            $this->crossInfo[PAGES_FORUMTYPE] = _FCORE::IsSetPostDefault(PAGES_FORUMTYPE, 0);
            if (!_FCORE::ValidateNumeric($this->crossInfo[PAGES_FORUMTYPE], 0, 2)){
                $this->crossInfo[RESULTS] .= "<div>Invalid Forum Type</div>";
                $valid = false;
            }
            //==================================================================
            $this->crossInfo[PAGES_CHILDOF] = _FCORE::IsSetPostDefault(PAGES_CHILDOF, 0);
            if (!is_numeric($this->crossInfo[PAGES_CHILDOF])){
                $this->crossInfo[RESULTS] .= "<div>Invalid Parent Node</div>";
                $valid = false;
            } else {
                $this->crossInfo[PAGES_RANK] = DB_PAGE::_GetNextRankForPageID($db, 
                        $this->crossInfo[PAGES_CHILDOF],
                        $this->crossInfo[PAGES_USERID]);
                if (!$this->crossInfo[PAGES_RANK]){ $this->crossInfo[PAGES_RANK] = 0; }
                //==================================================================
                $this->crossInfo[PAGES_ISPRIVATE] = (_FCORE::IsSetPostDefault(PAGES_ISPRIVATE, 0) != 0);
                if (!$this->crossInfo[PAGES_ISPRIVATE]){
                    if ($db->DoesRecordExist(
                            PAGES,
                            PAGES_ISPRIVATE."=1 AND ".
                            PAGES_PAGEID."=".$this->crossInfo[PAGES_CHILDOF])){
                        $this->crossInfo[RESULTS] .= "<div>Cannot Have A Non-Private Page Child To A Private Page</div>";
                        $valid = false; 
                    }
                }
            }
            //==================================================================
            $this->crossInfo[PAGES_TITLE] = _FCORE::IsSetPOST(PAGES_TITLE);
            if ($this->crossInfo[PAGES_TITLE] == null){
                $this->crossInfo[RESULTS] .= "<div>Invalid Title</div>";
                $valid = false;
            }
            //==================================================================
            if ($valid){
                try {
                    DB_PAGE::_CreatePage(
                            $db,
                            $this->crossInfo[PAGES_USERID],
                            $this->crossInfo[PAGES_CHILDOF],
                            $this->crossInfo[PAGES_TITLE],
                            $this->crossInfo[PAGES_RANK],
                            "", // content = ""
                            $this->crossInfo[PAGES_ISPRIVATE],
                            $this->crossInfo[PAGES_FORUMTYPE]);
                    unset($this->crossInfo[PAGES_TITLE]);
                    unset($this->crossInfo[PAGES_RANK]);
                    unset($this->crossInfo[PAGES_FORUMTYPE]);
                    $this->crossInfo[RESULTS] .= "<div>Successfully Made Page</div>";
                } catch(Exception $e) {
                    $this->crossInfo[RESULTS] .= "<div>Message From System: ".$e->getMessage()."</div>";
                }
            }
        }
        $this->UserSelectOption_Get($db);
        $this->crossInfo[_ADMINPAGE::PAGES_LIST] = DB_PAGE::_GetPageListForUserID($db, $this->crossInfo[PAGES_USERID]);
                    // '$this->crossInfo[PAGES_USERID]' comes from '$this->UserSelectOption_Get' call
        $db->disconnect();
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_create_both">
class adminstate_pages_create_both extends adminstate_pages_create {
    protected function UserSelectOption_Get(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID,
                _FCORE::IsSetPostDefault(PAGES_USERID, _SESSION::GetUserID()));
        $userList = DB_USER::_GetUserList($db);
        $count = sizeof($userList);
        $found = 0;
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
        for($i=0; $i<$count; $i++){
            if ($userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                $found++;
            }
        }
    }
    protected function UserSelectOption_ECHO(){
        echo "User: ";
        echo _FORM::SelectBegin(PAGES_USERID);
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        for($i=0; $i<$count; $i++){
            $thisOpt = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
            echo _FORM::Option(
                    $thisOpt[USERS_USERNAME],
                    $thisOpt[PAGES_USERID],
                    $thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID]);
        }
        echo _FORM::SelectEnd();
    }
    protected function CreatePageUserIDCheck($userID){
        // always ok
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_create_self">
class adminstate_pages_create_self extends adminstate_pages_create {
    protected function UserSelectOption_Get(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _SESSION::GetUserID();
    }
    protected function UserSelectOption_ECHO(){
        echo _FORM::Hidden(PAGES_USERID, $this->crossInfo[PAGES_USERID]);
    }
    protected function CreatePageUserIDCheck($userID){
        if ($userID != _SESSION::GetUserID()){
            $this->crossInfo[RESULTS] .= "<div>You Cannot Create Content For Other People</div>";
            unset($this->crossInfo[PAGES_USERID]);
        }
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_create_other">
class adminstate_pages_create_other extends adminstate_pages_create {
    protected function UserSelectOption_Get(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID,
                _FCORE::IsSetPostDefault(PAGES_USERID, null));
        $userList = DB_USER::_GetUserList($db);
        $count = sizeof($userList);
        $found = 0;
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
        for($i=0; $i<$count; $i++){
            if ($userList[$i][PAGES_USERID]!=_SESSION::GetUserID() &&
                $userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                if ($this->crossInfo[PAGES_USERID]==null){
                    $this->crossInfo[PAGES_USERID] = $userList[$i][PAGES_USERID];
                }
                $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                $found++;
            }
        }
    }
    protected function UserSelectOption_ECHO(){
        echo "User: ";
        echo _FORM::SelectBegin(PAGES_USERID);
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        for($i=0; $i<$count; $i++){
            $thisOpt = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
            echo _FORM::Option(
                    $thisOpt[USERS_USERNAME],
                    $thisOpt[PAGES_USERID],
                    $thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID]);
        }
        echo _FORM::SelectEnd();
    }
    protected function CreatePageUserIDCheck($userID){
        if ($userID == _SESSION::GetUserID()){
            $this->crossInfo[RESULTS] .= "<div>You Cannot Create Content For Yourself</div>";
            unset($this->crossInfo[PAGES_USERID]);
        }
    }
}
// </editor-fold>

//==============================================================================
//  admin.php pages manage
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_manage">
abstract class adminstate_pages_manage extends page_adminabstract {

    public function absoluteContent(){
        echo "<h2>Page Management</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        //$this->UserSelectOption_ECHO();

        if (!isset($this->crossInfo[_ADMINPAGE::USERS_LIST])){
            echo _FORM::Hidden(PAGES_USERID, $this->crossInfo[PAGES_USERID]);
        } else {
            echo "User: ";
            echo _FORM::SelectBegin(PAGES_USERID, "_userselecttarget");
            echo _FORM::SelectEnd();
        }

        echo "<div id='pagehierarchytarget'>";
        ThreadedForumViewer::BuildViewForAdminPageManage(
                $this->crossInfo["arrayHierarchy"],
                $this->crossInfo[PAGES_USERID] == _SESSION::GetUserID() ?
                    _SESSION::GetCanCDSelf() :
                    _SESSION::GetCanCDOther());
        echo "</div>";
    }
    public function absoluteStyle(){
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/jQuery.js");
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/admin_pagesmanage.js");

        ?>

<script type="text/javascript">
    $(document).ready(function(){
<?php
if (isset($this->crossInfo[_ADMINPAGE::USERS_LIST])){
    echo "var parent = getUserSelectParent();\n";
    echo "var child;\n";
    $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
    for($i=0; $i<$count; $i++){
        $thisOpt = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
        echo "buildUserOption('".
        $thisOpt[USERS_USERNAME]."', ".
        $thisOpt[PAGES_USERID].", ".
        ($thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID] ? "true" : "false").", parent);\n";
        //echo "parent.append(child);\n";
    }
}

?>
    });
</script>

<?php
    }
    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        $this->crossInfo[RESULTS] = "";
        $action = _FCORE::IsSetPostDefault(ACTION_GO, false);
        if ($action){
            if (_SESSION::GetCanWritePage($db, $pageID)){
                $pageID = _FCORE::IsSetPostDefault(PAGES_PAGEID, false);
                if ($pageID){
                    switch($action){
                        case ACTION_DELETE:
                                try {
                                    DB_PAGE::_DeletePage($db, $pageID);
                                    $this->crossInfo[RESULTS] .= "<div>Successfully Deleted Page</div>";
                                } catch(Exception $e) {
                                    $this->crossInfo[RESULTS] .= "<div>Message From System: ".$e->getMessage()."</div>";
                                }
                            break;
                        case ACTION_PASS:
                            $childOf = _FCORE::IsSetPostDefault(PAGES_CHILDOF, false);
                            if ($childOf){
                                try {
                                    $page = DB_PAGE::_GetPage($db, $pageID);
                                    $newParent = DB_PAGE::_GetPage($db, $childOf);
                                    if ($newParent->getIsPrivate() && !$page->getIsPrivate()){
                                        $this->crossInfo[RESULTS] .= "<div>Cannot Have Public Folder Inside A Private Folder</div>";
                                    } else {
                                        $page->setChildOf($childOf);
                                        $this->crossInfo[RESULTS] .= "<div>Successfully Moved Page</div>";
                                    }
                                } catch(Exception $e) {
                                    $this->crossInfo[RESULTS] .= "<div>Message From System: ".$e->getMessage()."</div>";
                                }
                            } else {
                                $this->crossInfo[RESULTS] .= "<div>pageID=".var_dump($pageID,false).",childOf=".var_dump($childOf,false)."</div>";
                            }
                            break;
                        case ACTION_UPDATE:
                            $rank = _FCORE::IsSetPostDefault(PAGES_RANK, false);
                            if ($rank !== false){
                                try {
                                    $page = DB_PAGE::_GetPage($db, $pageID);
                                    $page->setRank($rank);
                                    $this->crossInfo[RESULTS] .= "<div>Successfully Updated Rank</div>";
                                } catch(Exception $e){
                                    $this->crossInfo[RESULTS] .= "<div>Message From System: ".$e->getMessage()."</div>";
                                }
                            } else {
                                $this->crossInfo[RESULTS] .= "<div>pageID=".var_export($pageID,true).",rank=".var_export($rank,true)."</div>";
                            }
                            break;
                        case ACTION_VIEW:
                                try {
                                    $page = DB_PAGE::_GetPage($db, $pageID);
                                    if ($page->getIsPrivate()){
                                        $parent = DB_PAGE::_GetPage($db, $page->getChildOf());
                                        if ($parent->getIsPrivate()){
                                            $this->crossInfo[RESULTS] .= "<div>A Public Folder Cannot Be A Child Of A Private Folder<div>";
                                        } else {
                                            $page->setIsPrivate(false);
                                        }
                                    } else {
                                        if (DB_PAGE::_PageHasPublicChildren($db, $page->getPageID())){

                                        }
                                    }
                                } catch(Exception $e){
                                    $this->crossInfo[RESULTS] .= "<div>Message From System: ".$e->getMessage()."</div>";
                                }
                            break;
                    }
                }
            }
        }
        //$this->UserSelectOption_GET($db);

        if (_SESSION::GetCanCDOther()){
            if (_SESSION::GetCanCDSelf()){
                $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID,
                        _FCORE::IsSetGetDefault(PAGES_USERID, _SESSION::GetUserID()));
                $userList = DB_USER::_GetUserList($db);
                $count = sizeof($userList);
                $found = 0;
                $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
                for($i=0; $i<$count; $i++){
                    if ($userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                        $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                        $found++;
                    }
                }
            } else {
                $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID,
                        _FCORE::IsSetPostDefault(PAGES_USERID, null));
                $userList = DB_USER::_GetUserList($db);
                $count = sizeof($userList);
                $found = 0;
                $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
                for($i=0; $i<$count; $i++){
                    if ($userList[$i][PAGES_USERID]!=_SESSION::GetUserID() &&
                        $userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                        if ($this->crossInfo[PAGES_USERID]==null){
                            $this->crossInfo[PAGES_USERID] = $userList[$i][PAGES_USERID];
                        }
                        $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                        $found++;
                    }
                }
            }
        } else {
            $this->crossInfo[PAGES_USERID] = _SESSION::GetUserID();
        }

        $bypase = ($this->crossInfo[PAGES_USERID] == _SESSION::GetUserID() || _SESSION::GetDefaultRight() > 0);
        $arr = $bypase ? DB_USER::_GetAllPageRightsForUser($db, $this->crossInfo[PAGES_USERID]) : array();
        $this->crossInfo["arrayHierarchy"] = ThreadedForumBuilder::MakeFolderHierarchyForUser(
                DB_PAGE::_GetPageListForUserID($db, $this->crossInfo[PAGES_USERID]),
                $arr,
                1,
                $bypase);

        $db->disconnect();
    }

    protected abstract function UserSelectOption_GET(DBConnect &$db);
    protected abstract function UserSelectOption_ECHO();
    protected abstract function AllowedToManipulatePage(DBConnect &$db, $pageID);
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_manage_both">
class adminstate_pages_manage_both extends adminstate_pages_manage {
    protected function UserSelectOption_GET(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID, false);
        if (!$this->crossInfo[PAGES_USERID]){
            $this->crossInfo[PAGES_USERID] = _FCORE::IsSetGetDefault(PAGES_USERID, _SESSION::GetUserID());
        }
        $userList = DB_USER::_GetUserList($db);
        $count = sizeof($userList);
        $found = 0;
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
        for($i=0; $i<$count; $i++){
            if ($userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                $found++;
            }
        }
    }
    protected function UserSelectOption_ECHO(){
        echo "User: ";
        echo _FORM::SelectBegin(PAGES_USERID, "_userselecttarget");
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        for($i=0; $i<$count; $i++){
            $thisOpt = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
            echo "<option value='".$thisOpt[PAGES_USERID]."' id='_userselectoption'".($thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID]?" selected='selected'":"").">".$thisOpt[USERS_USERNAME]."</option>";
//            echo _FORM::Option(
//                    $thisOpt[USERS_USERNAME],
//                    $thisOpt[PAGES_USERID],
//                    $thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID],
//                    "_userselectoption",
//                    "alert(this);");
        }
        echo _FORM::SelectEnd();
    }
    protected function AllowedToManipulatePage(DBConnect &$db, $pageID){
        return true;
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_manage_self">
class adminstate_pages_manage_self extends adminstate_pages_manage {
    protected function UserSelectOption_GET(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _SESSION::GetUserID();
    }
    protected function UserSelectOption_ECHO(){
        echo _FORM::Hidden(PAGES_USERID, $this->crossInfo[PAGES_USERID]);
    }
    protected function AllowedToManipulatePage(DBConnect &$db, $pageID){
        $userID = $db->SelectOneOfOneField(PAGES, PAGES_USERID, PAGES_PAGEID."=$pageID");
        if (!$userID){
            return false;
        }
        return ($userID == _SESSION::GetUserID());
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_manage_other">
class adminstate_pages_manage_other extends adminstate_pages_manage {
    protected function UserSelectOption_GET(DBConnect &$db){
        $this->crossInfo[PAGES_USERID] = _FCORE::IsSetPostDefault(PAGES_USERID,
                _FCORE::IsSetPostDefault(PAGES_USERID, null));
        $userList = DB_USER::_GetUserList($db);
        $count = sizeof($userList);
        $found = 0;
        $this->crossInfo[_ADMINPAGE::USERS_LIST] = array();
        for($i=0; $i<$count; $i++){
            if ($userList[$i][PAGES_USERID]!=_SESSION::GetUserID() &&
                $userList[$i][USERS_USERNAME]!=_SESSION::GUEST){
                if ($this->crossInfo[PAGES_USERID]==null){
                    $this->crossInfo[PAGES_USERID] = $userList[$i][PAGES_USERID];
                }
                $this->crossInfo[_ADMINPAGE::USERS_LIST][$found] = $userList[$i];
                $found++;
            }
        }
    }
    protected function UserSelectOption_ECHO(){
        echo "User: ";
        echo _FORM::SelectBegin(PAGES_USERID);
        $count = sizeof($this->crossInfo[_ADMINPAGE::USERS_LIST]);
        for($i=0; $i<$count; $i++){
            $thisOpt = $this->crossInfo[_ADMINPAGE::USERS_LIST][$i];
            echo _FORM::Option(
                    $thisOpt[USERS_USERNAME],
                    $thisOpt[PAGES_USERID],
                    $thisOpt[PAGES_USERID]==$this->crossInfo[PAGES_USERID]);
        }
        echo _FORM::SelectEnd();
    }
    protected function AllowedToManipulatePage(DBConnect &$db, $pageID){
        $userID = $db->SelectOneOfOneField(PAGES, PAGES_USERID, PAGES_PAGEID."=$pageID");
        if (!$userID){
            return false;
        }
        return ($userID != _SESSION::GetUserID());
    }
}
// </editor-fold>

//==============================================================================
//  admin.php pages write
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_pages_write">
class adminstate_pages_write extends page_adminabstract {
    public function absoluteContent() {
        ?>

<h2><?php echo $this->crossInfo[_ADMINPAGE::USERS_LIST]->getUsername(); ?>'s "<?php echo $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getTitle(); ?>" Page Content</h2>

    <?php if (isset($this->crossInfo[RESULTS])){ echo $this->crossInfo[RESULTS]; } ?>

    <?php
    echo FormTextfieldWriter::EchoFormLayoutTemplate(
        URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_WRITE."&".PAGES_PAGEID."=".$this->crossInfo[_ADMINPAGE::PAGES_INFO]->getPageID(),
        ACTION_UPDATE,
        _HTML::I_2._FORM::Hidden(PAGES_PAGEID, $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getPageID()),
        _HTML::I_3."Page Title: "._FORM::Text(PAGES_TITLE, $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getTitle(), 40, 255),
        PAGES_CONTENT,
        70,// cols
        20,// rows
        $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getContent(),
        "Update Page");
    ?>
<?php if (_SESSION::GetIsMaster() || $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getUserID() == _SESSION::GetUserID()){ ?>
<h2><?php echo $this->crossInfo[_ADMINPAGE::USERS_LIST]->getUsername(); ?>'s "<?php echo $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getTitle(); ?>" Page Rights</h2><?php
echo _HTML::I_1."<div id='_rightscontainer'>";
echo _HTML::I_2._FORM::_FormBegin("updatepage", URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::PAGES_WRITE);
echo _HTML::I_3._FORM::Hidden(ACTION_GO, ACTION_RIGHTS);
echo _HTML::I_3._FORM::Hidden(PAGES_PAGEID, $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getPageID());
$count = sizeof($this->crossInfo[_ADMINPAGE::RIGHTS_LIST]);
$rightslist = "";
$first = true;
for($i=0; $i<$count; $i++){
    if ($this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][USERS_USERNAME] != _SESSION::GUEST &&
        $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][USERS_USERID] != _SESSION::GetUserID()){

        if ($first){
            $first = false;
        } else {
            $rightslist .= "&";
        }
        $rightslist .= $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][USERS_USERID];

        echo _HTML::I_3."<div id='_rightindividual'>";
        echo _HTML::I_4."<div style='"._CSS::FloatLeftWidth(200)."'>";
        echo _HTML::I_5.$this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][USERS_USERNAME].": ";
        echo _HTML::I_4."</div>";
        echo _HTML::I_4."<div style='"._CSS::FloatLeftWidth(200)."'>";
        echo _HTML::I_5._FORM::SelectBegin($this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][USERS_USERID]);
        if ($this->crossInfo[_ADMINPAGE::PAGES_INFO]->getIsPrivate()){
            echo _HTML::I_6._FORM::Option("None", 0,
                    $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == 0);
            echo _HTML::I_6._FORM::Option("Can See", PAGERIGHTS_USERRIGHT_SEE,
                    $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == PAGERIGHTS_USERRIGHT_SEE);
        }
        echo _HTML::I_6._FORM::Option("Can Comment", PAGERIGHTS_USERRIGHT_COMMENT,
            $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getIsPrivate() ?
                $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == PAGERIGHTS_USERRIGHT_COMMENT
                :
                ($this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == 0 ||
                 $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == PAGERIGHTS_USERRIGHT_SEE ||
                 $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == PAGERIGHTS_USERRIGHT_COMMENT)
            );
        echo _HTML::I_6._FORM::Option("Can Write", PAGERIGHTS_USERRIGHT_WRITE,
                $this->crossInfo[_ADMINPAGE::RIGHTS_LIST][$i][PAGERIGHTS_USERRIGHT] == PAGERIGHTS_USERRIGHT_WRITE);
        echo _HTML::I_5._FORM::SelectEnd();
        echo _HTML::I_4."</div>";
        echo _HTML::I_3."</div>";
    }
}
echo _HTML::I_2._FORM::Hidden(_ADMINPAGE::RIGHTS_LIST, $rightslist);
echo _HTML::I_2._FORM::Submit("Update Rights");
echo _HTML::I_2._FORM::_FormEnd();
echo _HTML::I_1._HTML::DIV_E;

        }
    }
    public function absoluteStyle() {
        FormTextfieldWriter::JavaScriptInclude();
        FormTextfieldWriter::EchoCSS();
        ?>

<style type="text/css">
#_rightscontainer
{
    width: 600px;
    background-color: silver;
    border-style: solid;
    border-color: darkgreen;
    border-width: 4px;
    margin: 4px;
    padding: 2px;
    overflow: auto;
}
#_rightindividual
{
    width: 100%;
    height: 1.5em;
    padding-top: 3px;
}
</style>

<?php
    }
    public function absolutePreProcessing() {
        $pageID = _FCORE::IsSetPostDefault(
                        PAGES_PAGEID,
                        _FCORE::IsSetGetDefault(
                            PAGES_PAGEID,
                            false));
        if ($pageID){
            $db = _DB::_Connect();
            $this->crossInfo[_ADMINPAGE::PAGES_INFO] = DB_PAGE::_GetPage($db, $pageID);
            $this->crossInfo[_ADMINPAGE::USERS_LIST] = DB_USER::_GetUser($db, $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getUserID());
            $action = _FCORE::IsSetPostDefault(ACTION_GO, false);
            if ($action){
                switch($action){
                    case ACTION_RIGHTS:
                        $userID = $this->crossInfo[_ADMINPAGE::PAGES_INFO]->getUserID();
                        if (_SESSION::GetIsMaster() || $userID == _SESSION::GetUserID()){
                            $rightsList = explode("&",_FCORE::IsSetPost(_ADMINPAGE::RIGHTS_LIST));
                            $count = sizeof($rightsList);
                            for($i=0; $i<$count; $i++){
                                try {
                                    $this->crossInfo[_ADMINPAGE::PAGES_INFO]->setRights(
                                            $rightsList[$i],
                                            _FCORE::IsSetPOST($rightsList[$i]));
                                    $this->crossInfo[RESULTS] .= "<div>Right Updated</div>";
                                } catch(Exception $e){ $this->crossInfo[RESULTS] .= "<div>Exception".$e->getMessage().$e->getLine().$e->getFile()."</div>"; }
                            }
                        } else {
                            $this->crossInfo[RESULTS] .= "<div>Failed</div>";
                        }
                        break;
                    case ACTION_UPDATE:
                        $title = _FCORE::IsSetPostDefault(PAGES_TITLE, false);
                        $content = _FCORE::IsSetPostDefault(PAGES_CONTENT, false);
                        if ($title && $content){
                            if ((($this->crossInfo[_ADMINPAGE::PAGES_INFO]->getUserID() == _SESSION::GetUserID()) && _SESSION::GetCanCDSelf()) ||
                                (($this->crossInfo[_ADMINPAGE::PAGES_INFO]->getUserID() != _SESSION::GetUserID()) && _SESSION::GetCanCDOther())) {
                                try {
                                    $this->crossInfo[_ADMINPAGE::PAGES_INFO]->setTitle($title);
                                    $this->crossInfo[_ADMINPAGE::PAGES_INFO]->setContent($content);
                                    $this->crossInfo[RESULTS] .= "Successfully Updated Content";
                                } catch(Exception $e){
                                    $this->crossInfo[RESULTS] .= "An Internal Error Occurred: ".$e->getMessage();
                                }
                            } else {
                                _FCORE::Redirect(ERROR_HTTP_ACCESSDENIED);
                            }
                        } else {

                        }
                        break;
                }
            }
            $this->crossInfo[_ADMINPAGE::RIGHTS_LIST] = DB_PAGE::_GetPageRights($db, $pageID);
            $db->disconnect();
        } else {
            _FCORE::Redirect(ERROR_HTTP_UNKNOWNACTION);
        }
    }

}
// </editor-fold>

//==============================================================================
//  admin.php login logs
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class adminstate_login_logs">
class adminstate_login_logs extends page_adminabstract {
    public function absoluteContent(){
        if (!$this->crossInfo[_ADMINPAGE::LOGIN_LOGS]){
            echo "No Logs To View";
            return;
        }
        $count = sizeof($this->crossInfo[_ADMINPAGE::LOGIN_LOGS]);
        if ($count==0){
            echo "No Logs To View";
            return;
        }
        echo "<h2>Login Attempts</h2>";
        if (isset($this->crossInfo[RESULTS])){
            echo $this->crossInfo[RESULTS];
        }
        for($i=0; $i<$count; $i++){
            $thislog = $this->crossInfo[_ADMINPAGE::LOGIN_LOGS][$i];
            $goTo = URL_PAGE_ADMIN."?".ACTION."="._ADMINPAGE::LOGIN_LOGS."&".
                _ADMINPAGE::LOGIN_LOGS_START."=".$this->crossInfo[_ADMINPAGE::LOGIN_LOGS_START]."&".
                _ADMINPAGE::LOGIN_LOGS_AMOUNT."=".$this->crossInfo[_ADMINPAGE::LOGIN_LOGS_AMOUNT];
            ?>

<div id="_listcontainer" class="<?php if($i%2==0){ echo 'on'; } else { echo 'off'; } ?>">
    <div id="_listcontainer_width100px"><?php echo $i+$this->crossInfo[_ADMINPAGE::LOGIN_LOGS_START]; ?></div>
    <div id="_listcontainer_width150px"><?php echo $thislog->getTimeLogged(); ?></div>
    <div id="_listcontainer_width150px"><?php if($thislog->getUserAttempt()!=null){ echo $thislog->getUserAttempt(); }else{ echo "null"; } ?></div>
    <div id="_listcontainer_width150px"><?php if($thislog->getPassAttempt()!=null){ echo $thislog->getPassAttempt(); }else{ echo "null"; } ?></div>
    <div id="_listcontainer_tomato">
        <?php
            echo _FORM::_FormBegin("deleteloginlog", $goTo);
            echo _FORM::Hidden(ACTION_GO, ACTION_DELETE);
            echo _FORM::Hidden(LOGINLOG_LOGID, $thislog->getLogID());
            echo _FORM::Submit("Delete Log");
            echo _FORM::_FormEnd();
        ?>
    </div>
</div>
<?php
        }
    }
    public function absoluteStyle(){
        
    }
    public function absolutePreProcessing(){
        $db = _DB::_Connect();
        $action = _FCORE::IsSetPostDefault(ACTION_GO, false);
        if ($action){
            $logID = _FCORE::IsSetPostDefault(LOGINLOG_LOGID, false);
            if ($logID){
                try {
                    DB_LOGINLOG::_DeleteLoginLog($db, $logID);
                    $this->crossInfo[RESULTS] = "Successfully Deleted Log";
                }catch(Exception $e){
                    $this->crossInfo[RESULTS] = "Failed To Delete Log";
                }
            }
        }
        $this->crossInfo[_ADMINPAGE::LOGIN_LOGS_START] = _FCORE::IsSetGetDefault(_ADMINPAGE::LOGIN_LOGS_START, 0);
        $this->crossInfo[_ADMINPAGE::LOGIN_LOGS_AMOUNT] = _FCORE::IsSetGetDefault(_ADMINPAGE::LOGIN_LOGS_AMOUNT, 50);
        $this->crossInfo[_ADMINPAGE::LOGIN_LOGS] = DB_LOGINLOG::_GetLoginLogList($db,
                $this->crossInfo[_ADMINPAGE::LOGIN_LOGS_START],
                $this->crossInfo[_ADMINPAGE::LOGIN_LOGS_AMOUNT]);
        $db->disconnect();
    }
}
// </editor-fold>

//==============================================================================
//  admin.php control
//==============================================================================

// <editor-fold defaultstate="collapsed" desc="class page_admin">
class page_admin extends _stateMachine {
    public function changeState($state){
        switch($state){
            case _ADMINPAGE::ANNOUNCEMENTS:
                $this->_CurrState = new adminstate_announcements();
                break;
            case _ADMINPAGE::MESSAGES_LIST:
                $this->_CurrState = new adminstate_messages_list();
                break;
            case _ADMINPAGE::MESSAGES_VIEW:
                $this->_CurrState = new adminstate_messages_view();
                break;
            case _ADMINPAGE::MESSAGES_WRITE:
                $this->_CurrState = new adminstate_messages_write();
                break;
            case _ADMINPAGE::USERS_VIEW:
                $this->_CurrState = new adminstate_users_view();
                break;
            case _ADMINPAGE::USERS_CREATE:
                $this->_CurrState = new adminstate_users_create();
                break;
            case _ADMINPAGE::USERS_MANAGE:
                $this->_CurrState = new adminstate_users_manage();
                break;
            case _ADMINPAGE::USERS_LOGS:
                $this->_CurrState = new adminstate_users_logs();
                break;
            case _ADMINPAGE::LOGIN_LOGS:
                $this->_CurrState = new adminstate_login_logs();
                break;
            case _ADMINPAGE::PAGES_CREATE:
                if (_SESSION::GetCanCDOther() && _SESSION::GetCanCDSelf()){
                    $this->_CurrState = new adminstate_pages_create_both();
                } else if (_SESSION::GetCanCreateSelf()){
                    $this->_CurrState = new adminstate_pages_create_self();
                } else if (_SESSION::GetCanCreateOther()){
                    $this->_CurrState = new adminstate_pages_create_other();
                }
                break;
            case _ADMINPAGE::PAGES_WRITE:
                $this->_CurrState = new adminstate_pages_write();
                break;
            case _ADMINPAGE::PAGES_MANAGE:
                if (_SESSION::GetCanCDOther() && _SESSION::GetCanCDSelf()){
                    $this->_CurrState = new adminstate_pages_manage_both();
                } else if (_SESSION::GetCanCreateSelf()){
                    $this->_CurrState = new adminstate_pages_manage_self();
                } else if (_SESSION::GetCanCreateOther()){
                    $this->_CurrState = new adminstate_pages_manage_other();
                }
                break;
            case ERROR_MUSTBELOGGEDIN:
                $this->_CurrState = new error_MustBeLoggedIn();
                break;
            case 0:
            default:
                $this->_CurrState = new error_UnknownAction();
                break;
        }
    }
}
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="admin.php entry">
$thispage = new page_admin();
$state = _ADMINPAGE::ANNOUNCEMENTS;

if (_S::IsLoggedIn()){
    $action = _FCORE::IsSetGET(ACTION);
    if ($action != null && $action != 0){
        if ($action==_ADMINPAGE::PAGES_CREATE ||
            $action==_ADMINPAGE::PAGES_MANAGE){
            if (_SESSION::GetCanCDOther() || _SESSION::GetCanCDSelf()){
                $state = $action;
            }
        }
        if ($action==_ADMINPAGE::MESSAGES_LIST ||
            $action==_ADMINPAGE::MESSAGES_VIEW ||
            $action==_ADMINPAGE::MESSAGES_WRITE ||
            $action==_ADMINPAGE::PAGES_WRITE){
            $state = $action;
        }
        if (_SESSION::GetIsMaster()){
            if ($action==_ADMINPAGE::USERS_CREATE ||
                $action==_ADMINPAGE::USERS_MANAGE ||
                $action==_ADMINPAGE::USERS_VIEW ||
                $action==_ADMINPAGE::USERS_LOGS ||
                $action==_ADMINPAGE::LOGIN_LOGS){
                $state = $action;
            }
        }
    }
} else {
    $state = ERROR_MUSTBELOGGEDIN;
}

$thispage->changeState($state);
$thispage->trigger();
// </editor-fold>

?>
