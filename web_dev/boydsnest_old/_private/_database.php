<?php

require_once DIR_CORE_FCORE;


// <editor-fold defaultstate="collapsed" desc="Database Definitions">

define("USERS", 'users');
define("USERS_USERID",          'userID');
define("USERS_USERNAME",        'username');
define("USERS_PASSWORD",        'password');
define("USERS_EMAIL",           'email');
define("USERS_SALT",            'salt');
define("USERS_SECRETQUESTION",  'secretQuestion');
define("USERS_SECRETANSWER",    'secretAnswer');
define("USERS_MASTERNOTES",     'masterNotes');
define("USERS_CANDOWNLOAD",     'canDownload');
define("USERS_CANUPLOAD",       'canUpload');
define("USERS_CANMESSAGE",      'canMessage');
define("USERS_CANCDSELF",       'canCDSelf');
define("USERS_CANCDOTHER",      'canCDOther');
define("USERS_ISFAMILY",        'isFamily');
define("USERS_ISLOGGED",        'isLogged');
define("USERS_ISMASTER",        'isMaster');
define("USERS_ISACTIVE",        'isActive');
define("USERS_ISPERMISSIONED",  'isPermissioned');
define("USERS_CREATEDWHEN",     'createdWhen');
define("USERS_EXPIRESWHEN",     'expiresWhen');
define("USERS_LASTUPDATE",      'lastUpdate');
define("USERS_DEFAULTRIGHT",    'defaultRight');
define("USERS_DEFAULTRIGHT_NONE",       0);
define("USERS_DEFAULTRIGHT_SEE",        1);
define("USERS_DEFAULTRIGHT_COMMENT",    2);
define("USERS_DEFAULTRIGHT_WRITE",      3);

define("PAGES",     'pages');
define("PAGES_PAGEID",        'pageID');
define("PAGES_USERID",        'userID');
define("PAGES_CHILDOF",       'childOf');
define("PAGES_TITLE",         'title');
define("PAGES_RANK",          'rank');
define("PAGES_CONTENT",       'content');
define("PAGES_VIEWS",         'views');
define("PAGES_ISPRIVATE",     'isPrivate');
define("PAGES_HASFOLLOWERS",  'hasFollowers');
define("PAGES_FORUMTYPE",     'forumType');
define("PAGES_FORUMTYPE_INACTIVE",    -1);
define("PAGES_FORUMTYPE_NONE",        0);
define("PAGES_FORUMTYPE_LINEAR",      1);
define("PAGES_FORUMTYPE_FORUM",       2);

define("PAGERIGHTS",    'pageRights');
define("PAGERIGHTS_PAGEID",     'pageID');
define("PAGERIGHTS_USERID",     'userID');
define("PAGERIGHTS_USERRIGHT",  'userRight');
define("PAGERIGHTS_USERRIGHT_SEE",  1);
define("PAGERIGHTS_USERRIGHT_COMMENT", 2);
define("PAGERIGHTS_USERRIGHT_WRITE", 3);

define("PAGEFOLLOWERS", 'pageFollowers');
define("PAGEFOLLOWERS_PAGEID",  'pageID');
define("PAGEFOLLOWERS_USERID",  'userID');

define("PAGEFORUMS",'pageForums');
define("PAGEFORUMS_COMMENTID",      'commentID');
define("PAGEFORUMS_PAGEID",         'pageID');
define("PAGEFORUMS_CHILDOF",        'childOf');
define("PAGEFORUMS_USERSAID",       'userSaid');
define("PAGEFORUMS_COMMENT",        'comment');
define("PAGEFORUMS_TIMESAID",       'timeSaid');

define("MESSAGES", 'messages');
define("MESSAGES_MESSAGEID",        'messageID');
define("MESSAGES_USERFROM",         'userFrom');
define("MESSAGES_USERTO",           'userTo');
define("MESSAGES_TIMESENT",         'timeSent');
define("MESSAGES_TITLE",            'title');
define("MESSAGES_MESSAGE",          'message');
define("MESSAGES_SEEN",             'seen');
define("MESSAGES_USERFROMDELETED",  'userFromDeleted');
define("MESSAGES_USERTODELETED",    'userToDeleted');
define("MESSAGES_FUNCTION",         'function');
define("MESSAGES_EXTRA",            'extra');

define("LOGINLOG",'loginLog');
define("LOGINLOG_LOGID",          'logID');
define("LOGINLOG_TIMELOGGED",     'timeLogged');
define("LOGINLOG_USERATTEMPT",    'userAttempt');
define("LOGINLOG_PASSATTEMPT",    'passAttempt');

define("ERRORLOG",  'errorLog');
define("ERRORLOG_LOGID",            'logID');
define("ERRORLOG_TIMELOGGED",       'timeLogged');
define("ERRORLOG_MESSAGE",          'message');

define("USERLOG",   'userLog');
define("USERLOG_LOGID",             'logID');
define("USERLOG_TIMELOGGED",        'timeLogged');
define("USERLOG_USERID",            'userID');
define("USERLOG_MESSAGE",           'message');

define("DATATYPEBASE",  'dataTypeBase');
define("DATATYPEBASE_DATATYPEID",   'dtID');
define("DATATYPEBASE_DATATYPE",     'dataType');
define("DATATYPEBASE_TITLE",        'title');
define("DATATYPEBASE_TIMEMADE",     'timeMade');
define("DATATYPEBASE_LASTUPDATE",   'lastUpdate');
define("DATATYPEBASE_HEADER",       'header');

define("DTASSOCIATE",   'dtAssociate');
define("DTASSOCIATE_DTID",          'dtID');
define("DTASSOCIATE_OWNERTABLE",    'ownerTable');
define("DTASSOCIATE_OWNERID",       'ownerID');

define("DTTEXT",    'dtText');
define("DTTEXT_DTID",               'dtID');
define("DTTEXT_CONTENT",            'content');
define("DTMTEXT",   'dtMText');
define("DTMTEXT_DTID",              'dtID');
define("DTMTEXT_CONTENT",           'content');
define("DTBLOB",    'dtBlob');
define("DTBLOB_DTID",               'dtID');
define("DTBLOB_CONTENT",            'content');
define("DTMBLOB",   'dtMBlob');
define("DTMBLOB_DTID",              'dtID');
define("DTMBLOB_CONTENT",           'content');

define("DT_DTID",                   'dtID');
define("DT_CONTENT",                'content');

define("USERMANUAL",    'userManual');
define("USERMANUAL_PAGEID",         'pageID');
define("USERMANUAL_CONTENT",        'content');
define("USERMANUAL_TITLE",          'title');
define("USERMANUAL_RANK",           'rank');

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class _DB">
class _DB {
    const PUBLIC_ENTRY      = 'invalid1_bnentry';
    const PUBLIC_PASS       = 's0mePoo22';
    const PUBLIC_SERVER     = 'localhost';
    const PUBLIC_DATABASE   = 'invalid1_boydsnest';

    public static final function _Connect(){
        $db = new DBConnect();
        $db->connect(self::PUBLIC_SERVER, self::PUBLIC_ENTRY, self::PUBLIC_PASS, self::PUBLIC_DATABASE);
        return $db;
    }

    public static final function _PassIsMaster(DBConnect &$db, $currMaster, $newMaster){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($currMaster)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($newMaster)){ throw new InvalidParamException(2, _T::NUMBER); }
        if ($currMaster == $newMaster){ return; }
        if (!$db->DoesRecordExist(USERS, USERS_USERID."=$newMaster")){ throw new InvalidParamException('$newMaster'); }
        if (!$db->DoesRecordExist(USERS, USERS_USERID."=$currMaster")){ throw new InvalidParamException('$currMaster'); }
        $db->Update(USERS, USERS_ISMASTER."=0");
        $db->Update(USERS, USERS_ISMASTER."=1", USERS_USERID."=$newMaster");
    }

    //===============================================
    //	database Fixes
    //===============================================
    public static final function _FixUserLoggers(DBConnect &$db){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_USER">
class DB_USER {

    /**
     *
     * @param DBConnect $db
     * @param <string> $username
     * @param <string> $password
     * @param <string> $email
     * @param <string> $secretQuestion
     * @param <string> $secretAnswer
     * @param <string> $masterNotes
     * @param <boolean> $canDownload
     * @param <boolean> $canUpload
     * @param <boolean> $canMessage
     * @param <boolean> $canCreateSelf
     * @param <boolean> $canCreateOther
     * @param <boolean> $canDeleteSelf
     * @param <boolean> $canDeleteOther
     * @param <boolean> $isFamily
     * @param <boolean> $isActive
     * @param <boolean> $isLogged
     * @param <boolean> $isPermissioned
     * @param <int> $defaultRight
     * @return <int> ID with new user
     */
    public static final function _CreateUser(
            DBConnect &$db,
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
            $defaultRight){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($username)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_string($password)){ throw new InvalidParamException(2, _T::STRING); }
        if (!is_string($secretQuestion)){ throw new InvalidParamException(3, _T::STRING); }
        if (!is_string($email)){ throw new InvalidParamException(4, _T::STRING); }
        if (!is_string($secretAnswer)){ throw new InvalidParamException(5, _T::STRING); }
        if (!is_string($masterNotes)){ throw new InvalidParamException(6, _T::STRING); }
        if (!is_bool($canDownload)){ throw new InvalidParamException(7, _T::BOOL); }
        if (!is_bool($canUpload)){ throw new InvalidParamException(8,_T::BOOL ); }
        if (!is_bool($canMessage)){ throw new InvalidParamException(9, _T::BOOL); }
        if (!is_bool($canCDSelf)){ throw new InvalidParamException(10, _T::BOOL); }
        if (!is_bool($canCDOther)){ throw new InvalidParamException(11, _T::BOOL); }
        if (!is_bool($isFamily)){ throw new InvalidParamException(12, _T::BOOL); }
        if (!is_bool($isActive)){ throw new InvalidParamException(13, _T::BOOL); }
        if (!is_bool($isLogged)){ throw new InvalidParamException(14, _T::BOOL); }
        if (!is_bool($isPermissioned)){ throw new InvalidParamException(15, _T::BOOL); }
        if (!is_numeric($defaultRight)){ throw new InvalidParamException(16, _T::INTEGER); }
        if (!_FCore::ValidateUsername($username, 5, 40)){
            throw new InvalidValueException('$username');
        }
        if (!_FCore::ValidateNumeric($defaultRight, 0, 3)){
            throw new InvalidValueException('$defaultRight');
        }
        if (!_FCore::ValidateEmailAddress($email) && $email!=""){
            throw new InvalidValueException('$email');
        }
        if ($db->DoesRecordExist(USERS, USERS_USERNAME."='$username'")){
            throw new InvalidValueException('$username');
        }
        if ($password==null || $password==""){
            throw new InvalidValueException('$password');
        }
        $salt = _FCore::GetNewSalt();
        $realPass = _FCore::GetSecondOrderHash($password, $salt);
        return $db->InsertThenReturnID(USERS,
            USERS_USERNAME."='$username',".
            USERS_PASSWORD."='$realPass',".
            USERS_SALT."='$salt',".
            USERS_EMAIL."='$email',".
            USERS_SECRETQUESTION."='$secretQuestion',".
            USERS_SECRETANSWER."='$secretAnswer',".
            USERS_MASTERNOTES."='$masterNotes',".
            USERS_CANDOWNLOAD."=".($canDownload ? "1" : "0").",".
            USERS_CANUPLOAD."=".($canUpload ? "1" : "0").",".
            USERS_CANMESSAGE."=".($canUpload ? "1" : "0").",".
            USERS_CANCDSELF."=".($canCreateSelf ? "1" : "0").",".
            USERS_CANCDOTHER."=".($canCreateOther ? "1" : "0").",".
            USERS_ISFAMILY."=".($isFamily ? "1" : "0").",".
            USERS_ISMASTER."=".($isMaster ? "1" : "0").",".
            USERS_ISACTIVE."=".($isActive ? "1" : "0").",".
            USERS_ISLOGGED."=".($isLogged ? "1" : "0").",".
            USERS_ISPERMISSIONED."=".($isPermissioned ? "1" : "0").",".
            USERS_DEFAULTRIGHT."=$defaultRight".",".
            USERS_CREATEDWHEN."=NOW()");
    }
    /**
     *
     * @param DBConnect $db
     * @param <type> $userID
     * @param <type> $archive
     * @return <type> 
     */
    public static final function _DeleteUser(DBConnect &$db, $userID, $archive = false){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_bool($archive)){ throw new InvalidParamException(2, _T::BOOL); }
        if ($userID==$db->SelectOneOfOneField(USERS, USERS_USERID, USERS_ISMASTER."=1")){
            throw new InvalidValueException('$userID', "Cannot Delete Master");
        }
        if ($userID==$db->SelectOneOfOneField(USERS, USERS_USERID, USERS_USERNAME."='Guest'")){
            throw new InvalidValueException('$userID', "Cannot Delete Default User");
        }
        if ($archive){
            
        }
        return $db->Delete(USERS, USERS_USERID."=$userID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <type> $userID
     * @return DB_USER
     */
    public static final function _GetUser(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(USERS, USERS_USERID."=$userID");
        if (!$result){ throw new FCoreException("User Doesn't Exist"); }
        $returning = new DB_USER();
        $returning->dbRef = $db;
        $returning->userID = $result[USERS_USERID];
        $returning->username = $result[USERS_USERNAME];
        $returning->email = $result[USERS_EMAIL];
        $returning->secretQuestion = $result[USERS_SECRETQUESTION];
        $returning->secretAnswer = $result[USERS_SECRETANSWER];
        $returning->masterNotes = $result[USERS_MASTERNOTES];
        $returning->canDownload = $result[USERS_CANDOWNLOAD] == "1";
        $returning->canUpload = $result[USERS_CANUPLOAD] == "1";
        $returning->canMessage = $result[USERS_CANMESSAGE] == "1";
        $returning->canCDSelf = $result[USERS_CANCDSELF] == "1";
        $returning->canCDOther = $result[USERS_CANCDOTHER] == "1";
        $returning->isFamily = $result[USERS_ISFAMILY] == "1";
        $returning->isLogged = $result[USERS_ISLOGGED] == "1";
        $returning->isMaster = $result[USERS_ISMASTER] == "1";
        $returning->isActive = $result[USERS_ISACTIVE] == "1";
        $returning->isPermissioned = $result[USERS_ISPERMISSIONED] == "1";
        $returning->defaultRight = $result[USERS_DEFAULTRIGHT];
        $returning->expiresWhen = $result[USERS_EXPIRESWHEN];
        $returning->lastUpdate = $result[USERS_LASTUPDATE];
        $returning->createdWhen = $result[USERS_CREATEDWHEN];
        return $returning;
    }
    public static final function _GetUserList(DBConnect &$db){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        return $db->makeQuery("SELECT userID, username, masterNotes FROM users");
    }
    public static final function _GetFamilyOnlyUserList(DBConnect &$db){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        $users = $db->SelectAllOfOneField(USERS_USERID, USERS, USERS_ISFAMILY."=1");
        $result = array();
        $count = sizeof($users);
        for($i=0; $i<$count; $i++){
            $result[$i] = self::_GetUser($db, $users[$i]);
        }
        return $result;
    }
    public static final function _GetAllPageRightsForUser(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->makeQuery("SELECT pageID,userRight FROM pageRights WHERE userID=$userID");
        $count = sizeof($result);
        $returning = array();
        for($i=0; $i<$count; $i++){
            $returning[ $result[$i][PAGERIGHTS_PAGEID] ] = $result[$i][PAGERIGHTS_USERRIGHT];
        }
        return $returning;
    }

    private function  __construct() {
        ;
    }

    /**
     * @var <DBConnect>
     */
    private $dbRef;

    private $userID;
    private $username;
    private $email;
    private $secretQuestion;
    private $secretAnswer;
    private $masterNotes;
    private $canDownload;
    private $canUpload;
    private $canMessage;
    private $canCDSelf;
    private $canCDOther;
    private $isFamily;
    private $isLogged;
    private $isMaster;
    private $isActive;
    private $isPermissioned;
    private $defaultRight;
    private $expiresWhen;
    private $lastUpdate;
    private $createdWhen;

    public function getUserID(){ return $this->userID; }
    public function getUsername(){ return $this->username; }
    public function getEmail(){ return $this->email; }
    public function getSecretQuestion(){ return $this->secretQuestion; }
    public function getSecretAnswer(){ return $this->secretAnswer; }
    public function getMasterNotes(){ return $this->masterNotes; }
    public function getCanDownload(){ return $this->canDownload; }
    public function getCanUpload(){ return $this->canUpload; }
    public function getCanMessage(){ return $this->canMessage; }
    public function getCanCDSelf(){ return $this->canCDSelf; }
    public function getCanCDOther(){ return $this->canCDOther; }
    public function getIsMaster(){ return $this->isMaster; }
    public function getIsFamily(){ return $this->isFamily; }
    public function getIsActive(){ return $this->isActive; }
    public function getIsLogged(){ return $this->isLogged; }
    public function getIsPermissioned(){ return $this->isPermissioned; }
    public function getDefaultRight(){ return $this->defaultRight; }
    public function getExpiresWhen(){ return $this->expiresWhen; }
    public function getLastUpdate(){ return $this->lastUpdate; }
    public function getCreatedWhen(){ return $this->createdWhen; }

    public function  __toString() {
        return "userID=$this->userID,
                username=$this->username,
                secretQuestion=".$this->getSecretQuestion().",
                secretAnswer=".$this->getSecretAnswer().",
                masterNotes=".$this->getMasterNotes().",
                email=$this->email,
                canDownload=$this->canDownload,
                canUpload=$this->canUpload,
                canMessage=$this->canMessage,
                canCDSelf=$this->canCDSelf,
                canCDOther=$this->canCDOther,
                isMaster=$this->isMaster,
                isFamily=$this->isFamily,
                isActive=$this->isActive,
                isLogged=$this->isLogged,
                isPermissioned=$this->isPermissioned,
                defaultRight=$this->defaultRight,
                expiresWhen=$this->expiresWhen,
                lastUpdate=$this->lastUpdate,
                createdWhen=$this->createdWhen";
    }

    private function triggerLastUpdate(){
        try {
            $this->lastUpdate = $this->dbRef->SelectOneOfOneField(
                    USERS,
                    USERS_LASTUPDATE,
                    USERS_USERID."=$this->userID");
        } catch (Exception $e) { }
    }

    /**
     * sets the username of this objects user
     * @param <string> $value value the username is to be changed to
     * @return <boolean>
     */
    public function setUsername($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        if ( $this->dbRef->DoesRecordExist(USERS, USERS_USERNAME."='$value'")){
            throw new DBConnectErrorException("Duplicate Username");
        }
        $this->dbRef->Update(
                USERS,
                USERS_USERNAME."='$value'",
                USERS_USERID."=$this->userID");
        $this->username = $value;
        $this->triggerLastUpdate();
    }
    public function setEmail($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERS,
                USERS_EMAIL."='$value'",
                USERS_USERID."=$this->userID");
        $this->email = $value;
        $this->triggerLastUpdate();
    }
    /**
     * 
     * @param <string> $value
     * @return <mysql_result>
     */
    public function setPassword($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $salt = _FCore::GetNewSalt();
        $value = _FCore::GetSecondOrderHash($value, $salt);
        $this->dbRef->Update(
                USERS,
                USERS_PASSWORD."='$value',".USERS_SALT."='$salt'",
                USERS_USERID."=$this->userID");
        $this->triggerLastUpdate();
    }
    public function setSecretQuestion($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERS,
                USERS_SECRETQUESTION."='$value'",
                USERS_USERID."=$this->userID");
        $this->secretQuestion = $value;
        $this->triggerLastUpdate();
    }
    public function setSecretAnswer($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERS,
                USERS_SECRETANSWER."='$value'",
                USERS_USERID."=$this->userID");
        $this->secretAnswer = $value;
        $this->triggerLastUpdate();
    }
    public function setMasterNotes($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERS,
                USERS_MASTERNOTES."='$value'",
                USERS_USERID."=$this->userID");
        $this->masterNotes = $value;
        $this->triggerLastUpdate();
    }
    public function setCanDownload($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->canDownload == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_CANDOWNLOAD."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->canDownload = $value;
        $this->triggerLastUpdate();
    }
    public function setCanUpload($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->canUpload == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_CANUPLOAD."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->canUpload = $value;
        $this->triggerLastUpdate();
    }
    public function setCanMessage($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->canMessage == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_CANMESSAGE."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->canMessage = $value;
        $this->triggerLastUpdate();
    }
    public function setCanCDSelf($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->canCDSelf == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_CANCDSELF."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->canCDSelf = $value;
        $this->triggerLastUpdate();
    }
    public function setCanCDOther($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->canCDOther == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_CANCDOTHER."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->canCDOther = $value;
        $this->triggerLastUpdate();
    }
    public function setIsFamily($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->isFamily == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_ISFAMILY."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->isFamily = $value;
        $this->triggerLastUpdate();
    }
    public function setIsActive($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->isActive == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_ISACTIVE."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->isActive = $value;
        $this->triggerLastUpdate();
    }
    public function setIsLogged($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->isLogged == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_ISLOGGED."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->isLogged = $value;
        $this->triggerLastUpdate();
    }
    public function setIsPermissioned($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($this->isPermissioned == $value){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_ISPERMISSIONED."=".($value ? "1" : "0"),
                USERS_USERID."=$this->userID");
        $this->isPermissioned = $value;
        $this->triggerLastUpdate();
    }
    public function setDefaultRight($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($value)){ throw new InvalidParamException(0, _T::NUMBER); }
        if (!_FCore::ValidateNumeric($value, 0, 3)){ throw new InvalidValueException('$value'); }
        if ($value == $this->defaultRight){ return; }
        $this->dbRef->Update(
                USERS,
                USERS_DEFAULTRIGHT."=$value",
                USERS_USERID."=$this->userID");
        $this->defaultRight = $value;
        $this->triggerLastUpdate();
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_PAGE">
class DB_PAGE implements IThreadedForum {
    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     * @param <int> $childOf
     * @param <string> $title
     * @param <int> $rank
     * @param <string> $content
     * @param <int> $forumType
     * @return <int> new pageID
     */
    public static final function _CreatePage(
            DBConnect &$db,
            $userID,
            $childOf,
            $title,
            $rank,
            $content,
            $isPrivate,
            $forumType){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($childOf)){ throw new InvalidParamException(2, _T::NUMBER); }
        if (!is_string($title)){ throw new InvalidParamException(3, _T::STRING); }
        if (!is_numeric($rank)){ throw new InvalidParamException(4, _T::NUMBER); }
        if (!is_string($content)){ throw new InvalidParamException(5, _T::STRING); }
        if (!is_bool($isPrivate)){ throw new InvalidParamException(6, _T::BOOL); }
        if (!is_numeric($forumType)){ throw new InvalidParamException(7, _T::NUMBER); }
        if (!$db->DoesRecordExist(USERS, USERS_USERID."=$userID") ||
            $db->DoesRecordExist(USERS, USERS_USERNAME."='Guest' AND ".USERS_USERID."=$userID")){
            throw new InvalidValueException('$userID'); 
        }
        if (!_FCORE::ValidateNumeric($forumType, 0, 2)){ throw new InvalidValueException('$forumType'); }
        if (!$childOf!=0 && $db->DoesRecordExist(PAGES, PAGES_PAGEID."=$childOf AND ".PAGES_USERID."=$userID"))
        { throw new InvalidValueException('$childOf'); }
        $title = $db->escapeString($title);
        $content = $db->escapeString($content);
        return $db->InsertThenReturnID(PAGES,
                PAGES_USERID."=$userID,".
                PAGES_CHILDOF."=$childOf,".
                PAGES_TITLE."='$title',".
                PAGES_RANK."=$rank,".
                PAGES_CONTENT."='$content',".
                PAGES_FORUMTYPE."=$forumType,".
                PAGES_ISPRIVATE."=".($isPrivate ? "1" : "0"));
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $pageID
     * @return <mysql_result>
     */
    public static final function _DeletePage(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        DB_PAGECOMMENT::_DeleteAllCommentsOfPage($db, $pageID);
        $parentID = $db->SelectOneOfOneField(
                PAGES,
                PAGES_CHILDOF, 
                PAGES_PAGEID."=$pageID");
        $db->Update(PAGES, PAGES_CHILDOF."=$parentID", PAGES_CHILDOF."=$pageID");
        return $db->Delete(PAGES, PAGES_PAGEID."=$pageID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     */
    public static final function _DeleteAllUserPages(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $allPageIDs = $db->SelectAllOfOneField(PAGES_PAGEID, PAGES, PAGES_USERID."=$userID");
        $count = sizeof($allPageIDs);
        for($i=0; $i<$count; $i++){
            self::_DeletePage($db, $allPageIDs[$i]);
        }
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $pageID
     * @return DB_PAGE
     */
    public static final function _GetPage(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(PAGES, PAGES_PAGEID."=$pageID");
        return new DB_PAGE(
                $db,
                $result[PAGES_PAGEID],
                $result[PAGES_USERID],
                $result[PAGES_CHILDOF],
                $result[PAGES_TITLE],
                $result[PAGES_RANK],
                $result[PAGES_CONTENT],
                $result[PAGES_VIEWS],
                $result[PAGES_FORUMTYPE],
                $result[PAGES_ISPRIVATE]);
    }

    public static final function _GetPageListForUserID(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $pages = array();
        $rawPages = $db->makeQuery("SELECT * FROM pages WHERE userID=$userID");
        if ($rawPages){
            $count = sizeof($rawPages);
            for($i=0; $i<$count; $i++){
                $pages[$i] = new DB_PAGE($db,
                        $rawPages[$i][PAGES_PAGEID],
                        $rawPages[$i][PAGES_USERID],
                        $rawPages[$i][PAGES_CHILDOF],
                        $rawPages[$i][PAGES_TITLE],
                        $rawPages[$i][PAGES_RANK],
                        $rawPages[$i][PAGES_CONTENT],
                        $rawPages[$i][PAGES_VIEWS],
                        $rawPages[$i][PAGES_FORUMTYPE],
                        $rawPages[$i][PAGES_ISPRIVATE]);
            }
        }
        return $pages;
    }

    public static final function _GetUserRoots(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $pages = array();
        $rawPages = $db->makeQuery("SELECT * FROM pages WHERE userID=$userID AND childOf=0");
        if ($rawPages){
            $count = sizeof($rawPages);
            for($i=0; $i<$count; $i++){
                $pages[$i] = new DB_PAGE($db,
                        $rawPages[$i][PAGES_PAGEID],
                        $rawPages[$i][PAGES_USERID],
                        $rawPages[$i][PAGES_CHILDOF],
                        $rawPages[$i][PAGES_TITLE],
                        $rawPages[$i][PAGES_RANK],
                        $rawPages[$i][PAGES_CONTENT],
                        $rawPages[$i][PAGES_VIEWS],
                        $rawPages[$i][PAGES_FORUMTYPE],
                        $rawPages[$i][PAGES_ISPRIVATE]);
            }
        }
        return $pages;
    }

    public static final function _GetNextRankForPageID(DBConnect &$db, $pageID, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($userID)){ throw new InvalidParamException(2, _T::NUMBER); }
        $result = $db->makeQuery("SELECT MAX(rank)+1 FROM pages WHERE childOf=$pageID AND userID=$userID");
        return $result[0]['MAX(rank)+1'];
    }

    public static final function _PageHasPublicChildren(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        //$result = $db->DoesRecordExist(PAGES, $where);
        throw new FCoreException("Not Yet Implemented");
    }

    public static final function _GetPageRights(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->makeQuery("SELECT * FROM pageRights WHERE pageID=$pageID");
        $orderResults = array();
        $count = sizeof($result);
        for($i=0; $i<$count; $i++){
            $orderResults[ $result[$i][PAGERIGHTS_USERID] ] = $result[$i][PAGERIGHTS_USERRIGHT];
        }
        $allUserIDs = DB_USER::_GetUserList($db);
        $count = sizeof($allUserIDs);
        $returning = array();
        for($i=0; $i<$count; $i++){
            $returning[$i][USERS_USERID] = $allUserIDs[$i][USERS_USERID];
            $returning[$i][USERS_USERNAME] = $allUserIDs[$i][USERS_USERNAME];
            if (isset($orderResults[ $allUserIDs[$i][USERS_USERID] ])){
                $returning[$i][PAGERIGHTS_USERRIGHT] = $orderResults[ $allUserIDs[$i][USERS_USERID] ];
            } else {
                $returning[$i][PAGERIGHTS_USERRIGHT] = 0;
            }
        }
        return $returning;
    }

    public static final function _NotifyPageFollowers(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        
    }

    public static final function _GetPageChilren(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $pages = array();
        $rawPages = $db->makeQuery("SELECT * FROM pages WHERE childOf=$pageID");
        if ($rawPages){
            $count = sizeof($rawPages);
            for($i=0; $i<$count; $i++){
                $pages[$i] = new DB_PAGE($db,
                        $rawPages[$i][PAGES_PAGEID],
                        $rawPages[$i][PAGES_USERID],
                        $rawPages[$i][PAGES_CHILDOF],
                        $rawPages[$i][PAGES_TITLE],
                        $rawPages[$i][PAGES_RANK],
                        $rawPages[$i][PAGES_CONTENT],
                        $rawPages[$i][PAGES_VIEWS],
                        $rawPages[$i][PAGES_FORUMTYPE],
                        $rawPages[$i][PAGES_ISPRIVATE]);
            }
        }
        return $pages;
    }

    private function  __construct(
            DBConnect $dbRef,
            $pageID,
            $userID,
            $childOf,
            $title,
            $rank,
            $content,
            $views,
            $forumType,
            $isPrivate) {
        $this->dbRef = $dbRef;
        $this->pageID = $pageID;
        $this->userID = $userID;
        $this->childOf = $childOf;
        $this->title = $title;
        $this->rank = $rank;
        $this->content = $content;
        $this->views = $views;
        $this->forumType = $forumType;
        $this->isPrivate = $isPrivate;
    }

    public function  __toString() {
        return
            "\$pageID=$this->pageID,
            \$userID=$this->userID,
            \$childOf=$this->childOf,
            \$title=$this->title,
            \$rank=$this->rank,
            \$views=$this->views,
            \$forumType=$this->forumType,
            \$isPrivate=$this->isPrivate";
    }

//    public function getChildOf(){
//
//    }
    public function getID(){ return $this->getPageID(); }

    public function isGreaterThan(IThreadedForum $node){ return $this->getRank() > $node->getGreaterCompare(); }
    public function getGreaterCompare(){ return $this->getRank(); }

    public function getSuper(){ return $this; }


    /**
     *
     * @var <DBConnect>
     */
    private $dbRef;

    private $pageID;
    private $userID;
    private $childOf;
    private $title;
    private $rank;
    private $content;
    private $views;
    private $forumType;
    private $isPrivate;

    public function getPageID(){ return $this->pageID; }
    public function getUserID(){ return $this->userID; }
    public function getChildOf(){ return $this->childOf; }
    public function getTitle(){ return $this->title; }
    public function getRank(){ return $this->rank; }
    public function getContent(){ return $this->content; }
    public function getContentAsHTML(){ return htmlspecialchars_decode($this->content); }
    public function getViews(){ return $this->views; }
    public function getIsPrivate(){ return $this->isPrivate; }
    public function getRights($userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(0, _T::NUMBER); }
        return $this->dbRef->SelectOneOfOneField(
                PAGERIGHTS,
                PAGERIGHTS_USERRIGHT,
                PAGERIGHTS_USERID."=$userID AND ".
                PAGERIGHTS_PAGEID."=".$this->pageID);
    }
    public function getFollowers(){
        return $this->dbRef->SelectAllOfOneField(
                PAGEFOLLOWERS_USERID,
                PAGEFOLLOWERS,
                PAGEFOLLOWERS_PAGEID."=".$this->pageID);
    }
    public function getHasChildren(){
        return $this->dbRef->DoesRecordExist(PAGES, PAGES_CHILDOF."=$this->pageID");
    }
    public function getForumType(){ return $this->forumType; }

    public function setChildOf($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($value)){ throw new InvalidParamException(0, _T::NUMBER); }
        if ($value!=0 && !$this->dbRef->DoesRecordExist(
                PAGES,
                PAGES_PAGEID."=$value AND ".
                PAGES_USERID."=".$this->userID)){
            throw new InvalidValueException('$value');
        }
        $this->dbRef->Update(
                PAGES,
                PAGES_CHILDOF."=$value",
                PAGES_PAGEID."=".$this->pageID);
        $this->childOf = $value;
    }
    public function setTitle($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                PAGES,
                PAGES_TITLE."='$value'",
                PAGES_PAGEID."=".$this->pageID);
        $this->title = $value;
    }
    public function setIsPrivate($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_bool($value)){ throw new InvalidParamException(0, _T::BOOL); }
        $this->dbRef->Update(
                PAGES,
                PAGES_ISPRIVATE."=".($value ? "1" : "0"),
                PAGES_PAGEID."=".$this->pageID);
        $this->isPrivate = $value;
    }
    public function setRank($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($value)){ throw new InvalidParamException(0, _T::NUMBER); }
        $this->dbRef->Update(
                PAGES,
                PAGES_RANK."=$value",
                PAGES_PAGEID."=".$this->pageID);
        $this->rank = $value;
    }
    public function setContent($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(0, _T::STRING); }
        $value = html_entity_decode($value);
        $this->dbRef->Update(
                PAGES,
                PAGES_CONTENT."='$value'",
                PAGES_PAGEID."=".$this->pageID);
        $this->content = $value;
    }
    public function setViews_Increment(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        $this->dbRef->Update(
                PAGES,
                PAGES_VIEWS."=1+".$this->views,
                PAGES_PAGEID."=".$this->pageID);
        $this->views++;
    }
    public function setRights($userID, $rightLevel){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(0, _T::NUMBER); }
        if (!is_numeric($rightLevel)){ throw new InvalidParamException(0, _T::NUMBER); }
        if (!_FCORE::ValidateNumeric($rightLevel, 0, 3)){ throw new InvalidValueException('$rightLevel'); }
        if ($rightLevel == 0){
            return $this->dbRef->Delete(
                PAGERIGHTS,
                PAGERIGHTS_PAGEID."=$this->pageID AND ".PAGERIGHTS_USERID."=$userID");
        } else {
            if ($this->dbRef->DoesRecordExist(
                    PAGERIGHTS,
                    PAGERIGHTS_PAGEID."=$this->pageID AND ".PAGERIGHTS_USERID."=$userID")){
                return $this->dbRef->Update(
                        PAGERIGHTS,
                        PAGERIGHTS_USERRIGHT."=$rightLevel",
                        PAGERIGHTS_PAGEID."=$this->pageID AND ".PAGERIGHTS_USERID."=$userID");
            } else {
                return $this->dbRef->Insert(
                        PAGERIGHTS,
                        PAGERIGHTS_PAGEID."=$this->pageID, ".PAGERIGHTS_USERID."=$userID, ".PAGERIGHTS_USERRIGHT."=$rightLevel");
            }
        }
    }
    public function setFollowers($userID, $isFollowing){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(0, _T::NUMBER); }
        if (!is_bool($isFollowing)){ throw new InvalidParamException(0, _T::BOOL); }
        if ($isFollowing){
            return $this->dbRef->Insert(
                    PAGEFOLLOWERS,
                    PAGEFOLLOWERS_PAGEID."=".$this->pageID.", ".
                    PAGEFOLLOWERS_USERID."=$userID");
        } else {
            return $this->dbRef->Delete(
                    PAGEFOLLOWERS,
                    PAGEFOLLOWERS_PAGEID."=".$this->pageID.", ".
                    PAGEFOLLOWERS_USERID."=$userID");
        }
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_PAGECOMMENT">
class DB_PAGECOMMENT {
    const FORUM_ROOT = 0;

    /**
     *
     * @param DBConnect $db
     * @param <int> $pageID
     * @param <int> $childOf
     * @param <int> $userSaid
     * @param <string> $comment
     * @return <int>
     */
    public static final function _CreatePageComment(
            DBConnect &$db,
            $pageID,
            $childOf,
            $userSaid,
            $comment){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($childOf)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($userSaid)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_string($comment)){ throw new InvalidParamException(1, _T::STRING); }
        $comment = $db->escapeString($comment);
        return $db->InsertThenReturnID(PAGEFORUMS,
                PAGEFORUMS_CHILDOF."=$childOf,".
                PAGEFORUMS_PAGEID."=$pageID,".
                PAGEFORUMS_USERSAID."=$userSaid,".
                PAGEFORUMS_COMMENT."='$comment'");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $commentID
     * @return <mysqli_result>
     */
    public static final function _DeletePageComment(DBConnect &$db, $commentID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($commentID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $childOf = $db->SelectOneOfOneField(PAGEFORUMS_CHILDOF, PAGEFORUMS, PAGEFORUMS_COMMENTID."=$commentID");
        $db->Update(PAGEFORUMS, PAGEFORUMS_CHILDOF."=$childOf", PAGEFORUMS_CHILDOF."=$commentID");
        return $db->Delete(PAGEFORUMS, PAGEFORUMS_COMMENTID."=$commentID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     */
    public static final function _DeleteAllCommentsOfUser(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $allCommentIDs = $db->SelectAllOfOneField(PAGEFORUMS_COMMENTID, PAGEFORUMS, PAGEFORUMS_USERSAID."=$userID");
        $count = sizeof($allCommentIDs);
        for($i=0; $i<$count; $i++){
            self::_DeletePageComment($db, $allCommentIDs[$i]);
        }
    }
    public static final function _DeleteAllCommentsOfPage(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(PAGEFORUMS, PAGEFORUMS_PAGEID."=$pageID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $commentID
     * @return DB_PAGECOMMENT
     */
    public static final function _GetPageComment(DBConnect &$db, $commentID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($commentID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(PAGEFORUMS, PAGEFORUMS_COMMENTID."=$commentID");
        return new DB_PAGECOMMENT(
                $result[PAGEFORUMS_COMMENTID],
                $result[PAGEFORUMS_PAGEID],
                $result[PAGEFORUMS_CHILDOF],
                $result[PAGEFORUMS_USERSAID],
                $result[PAGEFORUMS_COMMENT],
                $result[PAGEFORUMS_TIMESAID]);
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $pageID
     * @return <array(commentID)>
     */
    public static final function _GetPageRootCommentIDs(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->SelectAllOfOneField(
                PAGEFORUMS_COMMENTID,
                PAGEFORUMS,
                PAGEFORUMS_PAGEID."=$pageID AND".
                PAGEFORUMS_CHILDOF."=".self::FORUM_ROOT);
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $commentID
     * @return <array(commentID)>
     */
    public static final function _GetCommentChildren(DBConnect &$db, $commentID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($commentID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->SelectAllOfOneField(
                PAGEFORUMS_COMMENTID,
                PAGEFORUMS,
                PAGEFORUMS_CHILDOF."=$commentID");
    }

    private function  __construct(
            $commentID,
            $pageID,
            $childOf,
            $userSaid,
            $comment,
            $timeSaid) {
        $this->commentID = $commentID;
        $this->pageID = $pageID;
        $this->childOf = $childOf;
        $this->userSaid = $userSaid;
        $this->comment = $comment;
        $this->timeSaid = $timeSaid;
    }

    private $commentID;
    private $pageID;
    private $childOf;
    private $userSaid;
    private $comment;
    private $timeSaid;

    public function getCommentID(){ return $this->commentID; }
    public function getPageID(){ return $this->pageID; }
    public function getChildOf(){ return $this->childOf; }
    public function getUserSaid(){ return $this->userSaid; }
    public function getComment(){ return $this->comment; }
    public function getTimeSaid(){ return $this->timeSaid; }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_MESSAGE">
class DB_MESSAGE {

    const NORMAL = 'normal';

    const CONTEXT_SENT = 1;
    const CONTEXT_RECIEVED = 2;
    const CONTEXT_RECIEVED_SEEN = 3;
    const CONTEXT_RECIEVED_NOTSEEN = 4;
    
    public static final function _CreateMessage(
            DBConnect &$db,
            $userTo,
            $userFrom,
            $title,
            $message,
            $func,
            $extra){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userTo)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($userFrom)){ throw new InvalidParamException(2, _T::NUMBER); }
        if (!is_string($title)){ throw new InvalidParamException(3, _T::STRING); }
        if (!is_string($message)){ throw new InvalidParamException(4, _T::STRING); }
        if (!is_string($func)){ throw new InvalidParamException(5, _T::STRING); }
        if (!is_string($extra)){ throw new InvalidParamException(6, _T::STRING); }
        $title = $db->escapeString($title);
        $message = $db->escapeString($message);
        $func = $db->escapeString($func);
        $extra = $db->escapeString($extra);
        return $db->InsertThenReturnID(MESSAGES, "userFrom=$userFrom,userTo=$userTo,title='$title',message='$message',function='$func',extra='$extra'");
    }
    public static final function _SetMesageAsSeen(DBConnect &$db, $messageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($messageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Update(MESSAGES, MESSAGES_SEEN."=1", MESSAGES_MESSAGEID."=$messageID");
    }
    public static final function _GetMessage(DBConnect &$db, $messageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($messageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(MESSAGES, MESSAGES_MESSAGEID."=$messageID");
        if (!$result){ throw new FCoreException("Message Doesn't Exist"); }
        return new DB_MESSAGE(
                $result[MESSAGES_MESSAGEID],
                $result[MESSAGES_USERFROM],
                $result[MESSAGES_USERTO],
                $result[MESSAGES_TIMESENT],
                $result[MESSAGES_TITLE],
                $result[MESSAGES_MESSAGE],
                $result[MESSAGES_SEEN],
                $result[MESSAGES_USERFROMDELETED],
                $result[MESSAGES_USERTODELETED],
                $result[MESSAGES_FUNCTION],
                $result[MESSAGES_EXTRA]);
    }
    public static final function _DeleteMessage(DBConnect &$db, $messageID, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($messageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($userID)){ throw new InvalidParamException(2, _T::NUMBER); }
        $result = $db->makeQuery("SELECT userTo,userFrom,userFromDeleted,userToDeleted FROM messages WHERE messageID=$messageID AND (userTo=$userID OR userFrom=$userID)");
        if (!$result) { throw new FCoreException("Message Doesn't Exist"); }
        if ($result[0][MESSAGES_USERTO]==$userID && $result[0][MESSAGES_USERFROM]==$userID){
            $db->Delete(MESSAGES, MESSAGES_MESSAGEID."=$messageID");
        } else if (($result[0][MESSAGES_USERTO]==$userID && $result[0][MESSAGES_USERFROMDELETED]==1) ||
            ($result[0][MESSAGES_USERFROM]==$userID && $result[0][MESSAGES_USERTODELETED]==1)){
            $db->Delete(MESSAGES, MESSAGES_MESSAGEID."=$messageID");
        } else if ($result[0][MESSAGES_USERFROMDELETED]==0 &&
                   $result[0][MESSAGES_USERTODELETED]==0){
            if ($result[0][MESSAGES_USERFROM]==$userID){
                $db->Update(MESSAGES, MESSAGES_USERFROMDELETED."=1", MESSAGES_MESSAGEID."=$messageID");
            } else if ($result[0][MESSAGES_USERTO]==$userID){
                $db->Update(MESSAGES, MESSAGES_USERTODELETED."=1", MESSAGES_MESSAGEID."=$messageID");
            } else { throw new FCoreException("Message Doesn't Exist"); }
        } else if ($result[0][MESSAGES_USERFROMDELETED]==1 &&
                   $result[0][MESSAGES_USERTODELETED]==1){
            $db->Delete(MESSAGES, MESSAGES_MESSAGEID."=$messageID");
        }
    }
    public static final function _GetMessageListForUser(DBConnect &$db, $userID, $context, $start, $amount){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($context)){ throw new InvalidParamException(2, _T::NUMBER); }
        if (!is_numeric($start)){ throw new InvalidParamException(3, _T::NUMBER); }
        if (!is_numeric($amount)){ throw new InvalidParamException(4, _T::NUMBER); }
        $result = null;
        switch($context){
            case self::CONTEXT_SENT:
                return $db->makeQuery(
                        "SELECT DISTINCT messages.messageID, messages.userTo, messages.timeSent, messages.title, messages.seen,
                            (SELECT username FROM users WHERE userID=messages.userTo) AS username
                            FROM messages, users WHERE ".
                        MESSAGES_USERFROMDELETED."=0 AND ".
                        MESSAGES_USERFROM."=$userID
                        LIMIT $start, $amount");
                break;
            case self::CONTEXT_RECIEVED:
                return $db->makeQuery(
                        "SELECT DISTINCT messages.messageID, messages.userFrom, messages.timeSent, messages.title, messages.seen,
                            (SELECT username FROM users WHERE userID=messages.userFrom) AS username
                            FROM messages, users WHERE ".
                        MESSAGES_USERTODELETED."=0 AND ".
                        MESSAGES_USERTO."=$userID 
                        LIMIT $start, $amount");
                break;
            case self::CONTEXT_RECIEVED_SEEN:
                return $db->makeQuery(
                        "SELECT DISTINCT messages.messageID, messages.userFrom, messages.timeSent, messages.title, messages.seen,
                            (SELECT username FROM users WHERE userID=messages.userFrom) AS username
                            FROM messages, users WHERE ".
                        MESSAGES_USERTODELETED."=0 AND ".
                        MESSAGES_SEEN."=1 AND ".
                        MESSAGES_USERTO."=$userID
                        LIMIT $start, $amount");
                break;
            case self::CONTEXT_RECIEVED_NOTSEEN:
                return $db->makeQuery(
                        "SELECT DISTINCT messages.messageID, messages.userFrom, messages.timeSent, messages.title, messages.seen,
                            (SELECT username FROM users WHERE userID=messages.userFrom) AS username
                            FROM messages, users WHERE ".
                        MESSAGES_USERTODELETED."=0 AND ".
                        MESSAGES_SEEN."=0 AND ".
                        MESSAGES_USERTO."=$userID
                        LIMIT $start, $amount");
                break;
            default:
                throw new InvalidValueException("\$context");
        }
    }

    private function  __construct(
            $messageID,
            $userFrom,
            $userTo,
            $timeSent,
            $title,
            $message,
            $seen,
            $userFromDeleted,
            $userToDeleted,
            $function,
            $extra){
        $this->messageID = $messageID;
        $this->userFrom = $userFrom;
        $this->userTo = $userTo;
        $this->timeSent = $timeSent;
        $this->title = $title;
        $this->message = $message;
        $this->seen = $seen;
        $this->userFromDeleted = $userFromDeleted;
        $this->userToDeleted = $userToDeleted;
        $this->function = $function;
        $this->extra = $extra;
    }

    private $messageID;
    private $userFrom;
    private $userTo;
    private $timeSent;
    private $title;
    private $message;
    private $seen;
    private $userFromDeleted;
    private $userToDeleted;
    private $function;
    private $extra;

    public function getMessageID(){ return $this->messageID; }
    public function getUserFrom(){ return $this->userFrom; }
    public function getUserTo(){ return $this->userTo; }
    public function getTimeSent(){ return $this->timeSent; }
    public function getTitle(){ return $this->title; }
    public function getMessage(){ return $this->message; }
    public function getSeen(){ return $this->seen; }
    public function getUserFromDeleted(){ return $this->userFromDeleted; }
    public function getUserToDeleted(){ return $this->userToDeleted; }
    public function getFunction(){ return $this->function; }
    public function getExtra(){ return $this->extra; }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_USERLOG">
class DB_USERLOG {

    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     * @param <string> $message
     * @return <int> ID of new log
     */
    public static final function _InsertUserLog(DBConnect &$db, $userID, $message){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_string($message)){ throw new InvalidParamException(2, _T::STRING); }
        $message = $db->escapeString($message);
        return $db->InsertThenReturnID(USERLOG,
                USERLOG_USERID."=$userID,".
                USERLOG_MESSAGE."='$message'");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <mysql_result>
     */
    public static final function _DeleteUserLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(USERLOG, USERLOG_LOGID."=$logID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     * @return <mysql_result>
     */
    public static final function _DeleteAllUserLogs(DBConnect &$db, $userID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(USERLOG, USERLOG_USERID."=$userID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <DB_USERLOG>
     */
    public static final function _GetUserLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(USERLOG, USERLOG_LOGID."=$logID");
        return new DB_USERLOG(
                $result[USERLOG_LOGID],
                $result[USERLOG_TIMELOGGED],
                $result[USERLOG_USERID],
                $result[USERLOG_MESSAGE]);
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $userID
     * @param <int> $start
     * @param <int> $amount
     * @return <array(DB_USERLOG)>
     */
    public static final function _GetUserLogList(DBConnect &$db, $userID, $start, $amount){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($start)){ throw new InvalidParamException(2, _T::NUMBER); }
        if (!is_numeric($amount)){ throw new InvalidParamException(3, _T::NUMBER); }
        $result = $db->SelectAllFromRange(USERLOG, $start, $amount, USERLOG_USERID."=$userID");
        $returning = array();
        $count = sizeof($result);
        for($i=0; $i<$count; $i++){
            $returning[$i] = new DB_USERLOG(
                $result[$i][USERLOG_LOGID],
                $result[$i][USERLOG_TIMELOGGED],
                $result[$i][USERLOG_USERID],
                $result[$i][USERLOG_MESSAGE]);
        }
        return $returning;
    }

    private function  __construct($logID, $timeLogged, $userID, $message){
        $this->logID = $logID;
        $this->timeLogged = $timeLogged;
        $this->userID = $userID;
        $this->message = $message;
    }

    private $logID;
    private $timeLogged;
    private $userID;
    private $message;

    public function getLogID(){ return $this->logID; }
    public function getTimeLogged(){ return $this->timeLogged; }
    public function getUserID(){ return $this->userID; }
    public function getMessage(){ return $this->message; }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_LOGINLOG">
class DB_LOGINLOG {

    /**
     *
     * @param DBConnect $db
     * @param <string> $user
     * @param <string> $pass
     * @return <int> id of new log
     */
    public static final function _InsertLoginLog(DBConnect &$db, $user, $pass){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($user)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_string($pass)){ throw new InvalidParamException(2, _T::STRING); }
        $user = $db->escapeString($user);
        $pass = $db->escapeString($pass);
        return $db->InsertThenReturnID(LOGINLOG, "userAttempt='$user', passAttempt='$pass'");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <mysql_result>
     */
    public static final function _DeleteLoginLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($logID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(LOGINLOG, "logID=$logID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <DB_LOGINLOG>
     */
    public static final function _GetLoginLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($logID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(LOGINLOG, LOGINLOG_LOGID."=$logID");
        return new DB_LOGINLOG(
                    $result[LOGINLOG_LOGID],
                    $result[LOGINLOG_TIMELOGGED],
                    $result[LOGINLOG_PASSATTEMPT],
                    $result[LOGINLOG_USERATTEMPT]);
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $start
     * @param <int> $amount
     * @return <array(DB_LOGINLOG)>
     */
    public static final function _GetLoginLogList(DBConnect &$db, $start, $amount){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($start)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($amount)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllFromRange(LOGINLOG, $start, $amount);
        $returning = array();
        $count = sizeof($result);
        for($i=0; $i<$count; $i++){
            $returning[$i] = new DB_LOGINLOG(
                    $result[$i][LOGINLOG_LOGID],
                    $result[$i][LOGINLOG_TIMELOGGED],
                    $result[$i][LOGINLOG_PASSATTEMPT],
                    $result[$i][LOGINLOG_USERATTEMPT]);
        }
        return $returning;
    }

    private function  __construct($logID, $timeLogged, $passAttempt, $userAttempt) {
        $this->logID = $logID;
        $this->timeLogged = $timeLogged;
        $this->passAttempt = $passAttempt;
        $this->userAttempt = $userAttempt;
    }

    private $logID;
    private $timeLogged;
    private $passAttempt;
    private $userAttempt;

    public function getLogID(){ return $this->logID; }
    public function getTimeLogged(){ return $this->timeLogged; }
    public function getPassAttempt(){ return $this->passAttempt; }
    public function getUserAttempt(){ return $this->userAttempt; }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_ERRORLOG">
class DB_ERRORLOG {

    /**
     *
     * @param DBConnect $db
     * @param <string> $message
     * @return <int> id of new log
     */
    public static final function _CreateErrorLog(DBConnect &$db, $message){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($message)){ throw new InvalidParamException(1, _T::STRING); }
        $message = $db->escapeString($message);
        return $db->InsertThenReturnID(ERRORLOG, ERRORLOG_MESSAGE."='$message'");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <mysql_result>
     */
    public static final function _DeleteErrorLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($logID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(ERRORLOG, ERRORLOG_LOGID."=$logID");
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $logID
     * @return <DB_ERRORLOG>
     */
    public static final function _GetErrorLog(DBConnect &$db, $logID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($logID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(ERRORLOG, ERRORLOG_LOGID."=$logID");
        return new DB_ERRORLOG(
                $result[ERRORLOG_LOGID],
                $result[ERRORLOG_TIMELOGGED],
                $result[ERRORLOG_MESSAGE]);
    }
    /**
     *
     * @param DBConnect $db
     * @param <int> $start
     * @param <int> $amount
     * @return <array(DB_ERRORLOG)>
     */
    public static final function _GetErrorLogList(DBConnect &$db, $start, $amount){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($start)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_numeric($amount)){ throw new InvalidParamException(2, _T::NUMBER); }
        $result = $db->SelectAllFromRange(ERRORLOG, $start, $amount);
        $count = sizeof($result);
        $returning = array();
        for($i=0; $i<$count; $i++){
            $returning[$i] = new DB_ERRORLOG(
                $result[$i][ERRORLOG_LOGID],
                $result[$i][ERRORLOG_TIMELOGGED],
                $result[$i][ERRORLOG_MESSAGE]);
        }
        return $returning;
    }

    private function  __construct($logID, $timeLogged, $message) {
        $this->logID = $logID;
        $this->timeLogged = $timeLogged;
        $this->message = $message;
    }

    private $logID;
    private $timeLogged;
    private $message;

    public function getLogID(){ return $this->logID; }
    public function getTimeLogged(){ return $this->timeLogged; }
    public function getMessage(){ return $this->message; }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_DATAOBJECT">
class DB_DATAOBJECT {

    const TEXT = 'dtText';
    const MTEXT = 'dtMText';
    const BLOB = 'dtBlob';
    const MBLOB = 'dbMBlob';

    /**
     *
     * @param DBConnect $db
     * @param <type> $title
     * @param <type> $header
     * @param <type> $dataType
     * @param <type> $content
     */
    public static final function _CreateDataObject(
            DBConnect &$db,
            $title,
            $header,
            $dataType,
            $content){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($title)){ throw new FCoreException("Invalid Param: Args[1] Not A String"); }
        if (!is_string($header)){ throw new FCoreException("Invalid Param: Args[2] Not A String"); }
        if (!is_string($dataType)){ throw new FCoreException("Invalid Param: Args[3] Not A String"); }
        if (!is_string($content)){ throw new FCoreException("Invalid Param: Args[4] Not A String"); }
        if (    $dataType != self::TEXT ||
                $dataType != self::MTEXT ||
                $dataType != self::BLOB ||
                $dataType != self::MBLOB){
            throw new FCoreException("Invalid Param Range: Unexpected DataType");
        }
        $newID = $db->InsertThenReturnID(DATATYPEBASE, "
            dataType='$dataType',
            title='$title',
            timeMade=NOW(),
            header='$header'");
        $db->Insert($dataType, 
            DT_DTID."=$newID,".
            DT_CONTENT."='$content'");
        return $newID;
    }
    public static final function _DeleteDataObject(DBConnect &$db, $dataObjectID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($dataObjectID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(DATATYPEBASE, DT_DTID."=$dataObjectID");
    }
    public static final function _GetDataObject(DBConnect &$db, $dataObjectID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($userID)){
            throw new FCoreException("Invalid Param");
        }
        $result = $db->SelectAllOfOneRecord(
                DATATYPEBASE,
                DATATYPEBASE_DATATYPEID."=$dataObjectID");
        if (!$results){
            throw new DBConnectErrorException("Data Object Doesn't Exist");
        }
        $content = $db->SelectOneOfOneField(
                $result[DATATYPEBASE_DATATYPE],
                DT_CONTENT, 
                DT_DTID."=$dataObjectID");
        if (!$content){
            throw new DBConnectErrorException("Data Object's Content Missing: DB May Require Cleaning");
        }
        $returning = new DB_DATAOBJECT();
        $returning->dbRef = $db;
        $returning->dataTypeID = $result[DATATYPEBASE_DATATYPEID];
        $returning->dataType = $result[DATATYPEBASE_DATATYPE];
        $returning->title = $result[DATATYPEBASE_TITLE];
        $returning->timeMade = $result[DATATYPEBASE_TIMEMADE];
        $returning->header = $result[DATATYPEBASE_HEADER];
        $returning->content = $content;
        return $returning;
    }
    /**
     *
     * @param DBConnect $db
     * @param <type> $dtID
     * @param <type> $ownerTable
     * @param <type> $ownerID
     */
    public static final function _MakeAssociation(DBConnect &$db, $dtID, $ownerTable, $ownerID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($dtID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_string($ownerTable)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_numeric($ownerID)){ throw new InvalidParamException(3, _T::NUMBER); }
        $ownerTable = $db->escapeString($ownerTable);
        return $db->InsertThenReturnID(DTASSOCIATE,
                DTASSOCIATE_DTID."=$dtID,".
                DTASSOCIATE_OWNERTABLE."='$ownerTable',".
                DTASSOCIATE_OWNERID."=$ownerID");
    }
    public static final function _GetAssociationsForOwner(DBConnect &$db, $ownerTable, $ownerID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($ownerTable)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_numeric($ownerID)){ throw new InvalidParamException(3, _T::NUMBER); }
        $ownerTable = $db->escapeString($ownerTable);
        return $db->SelectAllOfOneField(
                DTASSOCIATE_DTID,
                DTASSOCIATE,
                DTASSOCIATE_OWNERTABLE."='$ownerTable',".
                DTASSOCIATE_OWNERID."=$ownerID");
    }
    public static final function _GetAssociationsForData(DBConnect &$db, $dtID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($dtID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->makeQuery("SELECT ".
                DTASSOCIATE_OWNERID.",".DTASSOCIATE_OWNERTABLE." FROM ".
                DTASSOCIATE." WHERE ".DTASSOCIATE_DTID."=$dtID");
    }
    public static final function _DeleteAssociation(DBConnect &$db, $dtID, $ownerTable, $ownerID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($dtID)){ throw new InvalidParamException(1, _T::NUMBER); }
        if (!is_string($ownerTable)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_numeric($ownerID)){ throw new InvalidParamException(3, _T::NUMBER); }
        $ownerTable = $db->escapeString($ownerTable);
        return $db->Delete(DTASSOCIATE,
                DTASSOCIATE_DTID."=$dtID,".
                DTASSOCIATE_OWNERID."=$ownerID,".
                DTASSOCIATE_OWNERTABLE."='$ownerTable'");
    }

    private function  __construct() {
        ;
    }

    private $dbRef;

    private $dataTypeID;
    private $dataType;
    private $title;
    private $timeMade;
    private $lastUpdate;
    private $header;
    private $content;

    public function getDataTypeID(){ return $this->dataTypeID; }
    public function getDataType(){ return $this->dataType; }
    public function getTitle(){ return $this->title; }
    public function getTimeMade(){ return $this->timeMade; }
    public function getLastUpdate(){ return $this->lastUpdate; }
    public function getHeader(){ return $this->header; }
    public function getContent(){ return $this->content; }

    private function triggerLastUpdate($force = false){
        if ($force){
            $this->dbRef->Update(
                    DATATYPEBASE,
                    DATATYPEBASE_LASTUPDATE."=NOW()",
                    DATATYPEBASE_DATATYPEID."=$this->dataTypeID");
        } 
        try {
            $this->lastUpdate = $this->dbRef->SelectOneOfOneField(
                    DATATYPEBASE,
                    DATATYPEBASE_LASTUPDATE,
                    DATATYPEBASE_DATATYPEID."=$this->dataTypeID");
        } catch(Exception $e) { }
    }

    public function setTitle($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){
            throw new FCoreException("Invalid Param");
        }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                DATATYPEBASE,
                DATATYPEBASE_TITLE,
                DATATYPEBASE_DATATYPEID."=$this->dataTypeID");
        $this->title = $value;
        $this->triggerLastUpdate();
    }
    public function setHeader($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){
            throw new FCoreException("Invalid Param");
        }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                DATATYPEBASE,
                DATATYPEBASE_HEADER,
                DATATYPEBASE_DATATYPEID."=$this->dataTypeID");
        $this->header = $value;
        $this->triggerLastUpdate();
    }
    public function setContent($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){
            throw new FCoreException("Invalid Param");
        }
        if ($this->dataType == self::TEXT || $this->dataType == self::MTEXT){
            $value = $this->dbRef->escapeString($value);
        }
        $this->dbRef->Update(
                $this->dataType,
                DT_CONTENT,
                DT_DTID."=$this->dataTypeID");
        $this->triggerLastUpdate(true);
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DB_USERMANUAL">
class DB_USERMANUAL {
    public static final function _CreateUserManualPage(
            DBConnect &$db,
            $content,
            $title,
            $rank){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($content)){ throw new InvalidParamException(1, _T::STRING); }
        if (!is_string($title)){ throw new InvalidParamException(2, _T::STIRNG); }
        if (!is_numeric($rank)){ throw new InvalidParamException(3, _T::NUMBER); }
        $content = $db->escapeString($content);
        $title = $db->escapeString($title);
        return $db->InsertThenReturnID(
                USERMANUAL,
                USERMANUAL_TITLE."='$title',".
                USERMANUAL_CONTENT."='$content',".
                USERMANUAL_RANK."=$rank");
    }
    public static final function _DeleteUserManualPage(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        return $db->Delete(USERMANUAL, USERMANUAL_PAGEID."=$pageID");
    }
    public static final function _GetUserManualPage(DBConnect &$db, $pageID){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($pageID)){ throw new InvalidParamException(1, _T::NUMBER); }
        $result = $db->SelectAllOfOneRecord(USERMANUAL, USERMANUAL_PAGEID."=$pageID");
        return new DB_USERMANUAL(
                $db,
                $result[USERMANUAL_PAGEID],
                $result[USERMANUAL_CONTENT],
                $result[USERMANUAL_TITLE],
                $result[USERMANUAL_RANK]);
    }
    public static final function _GetTitleList(DBConnect &$db){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        return $db->makeQuery("SELECT pageID,title,rank FROM userManual ORDER BY rank");
    }

    private function __construct(
            DBConnect &$db,
            $pageID,
            $content,
            $title,
            $rank) {
        $this->dbRef = $db;
        $this->pageID = $pageID;
        $this->content = $content;
        $this->title = $title;
        $this->rank = $rank;
    }

    /**
     *
     * @var DBConnect
     */
    private $dbRef;

    private $pageID;
    private $content;
    private $title;
    private $rank;

    public function getPageID(){ return $this->pageID; }
    public function getContent(){ return $this->content; }
    public function getTitle(){ return $this->title; }
    public function getRank(){ return $this->rank; }

    public function setContent($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(1, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERMANUAL,
                USERMANUAL_CONTENT."='$value'",
                USERMANUAL_PAGEID."=".$this->pageID);
        $this->content = $value;
    }
    public function setTitle($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($value)){ throw new InvalidParamException(1, _T::STRING); }
        $value = $this->dbRef->escapeString($value);
        $this->dbRef->Update(
                USERMANUAL,
                USERMANUAL_TITLE."='$value'",
                USERMANUAL_PAGEID."=".$this->pageID);
        $this->content = $value;
    }
    public function setRank($value){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_numeric($value)){ throw new InvalidParamException(1, _T::NUMBER); }
        $this->dbRef->Update(
                USERMANUAL,
                USERMANUAL_RANK."=$value",
                USERMANUAL_PAGEID."=".$this->pageID);
        $this->content = $value;
    }
}
// </editor-fold>

?>
