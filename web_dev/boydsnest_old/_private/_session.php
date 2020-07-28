<?php

require_once DIR_CORE_FCORE;
require_once DIR_DATABASE_DATABASE;

class _SESSION {
    const GUEST = 'Guest';
    const TIME_BEGIN = 'begintime';
    const TIME_OUT = 1800;
    const LOGINATTEMPTS = 'loginattempts';
    const LOGINATTEMPTS_MAX = 10;
    const LOGINATTEMPTS_CURR = 'loginattempts_curr';

    public static final function GetUsername(){
        return $_SESSION[USERS_USERNAME];
    }
    public static final function GetUserID(){
        return $_SESSION[USERS_USERID];
    }
    public static final function GetIsMaster(){
        return $_SESSION[USERS_ISMASTER];
    }
    public static final function GetIsFamily(){
        return $_SESSION[USERS_ISFAMILY];
    }
    public static final function GetCanMessage(){
        return $_SESSION[USERS_CANMESSAGE];
    }
    public static final function GetCanCDOther(){
        return $_SESSION[USERS_CANCDOTHER];
    }
    public static final function GetCanCDSelf(){
        return $_SESSION[USERS_CANCDSELF];
    }
    public static final function GetDefaultRight(){
        return $_SESSION[USERS_DEFAULTRIGHT];
    }
    public static final function GetCanViewPage(DBConnect &$db, $pageID){
        if (self::GetDefaultRight() > 0 || self::GetIsMaster()){ return true; }
        $page = DB_PAGE::_GetPage($db, $pageID);
        if (!$page->getIsPrivate() || $page->getUserID()==self::GetUserID()){
            return true;
        }
        return $page->getRights(self::GetUserID()) >= "1";
    }
    public static final function GetCanCommentOnPage(DBConnect &$db, $pageID){
        if (self::GetDefaultRight() > 1 || self::GetIsMaster()){ return true; }
        $page = DB_PAGE::_GetPage($db, $pageID);
        if (!$page->getIsPrivate() || $page->getUserID()==self::GetUserID()){
            return true;
        }
        return $page->getRights(self::GetUserID()) >= "2";
    }
    public static final function GetCanWritePage(DBConnect &$db, $pageID){
        if (self::GetDefaultRight() > 2 || self::GetIsMaster()){ return true; }
        $page = DB_PAGE::_GetPage($db, $pageID);
        if (!$page->getIsPrivate() || $page->getUserID()==self::GetUserID()){
            return true;
        }
        return $page->getRights(self::GetUserID()) >= "3";
    }
    public static final function SetupUser(DBConnect &$db, $userID){
        $userObj = DB_USER::_GetUser($db, $userID);
        $_SESSION[USERS_USERNAME] = $userObj->getUsername();
        $_SESSION[USERS_USERID] = $userObj->getUserID();
        $_SESSION[USERS_ISMASTER] = $userObj->getIsMaster();
        $_SESSION[USERS_ISFAMILY] = $userObj->getIsFamily();
        $_SESSION[USERS_CANMESSAGE] = $userObj->getCanMessage();
        $_SESSION[USERS_CANCDOTHER] = $userObj->getCanCDOther();
        $_SESSION[USERS_CANCDSELF] = $userObj->getCanCDSelf();
        $_SESSION[USERS_DEFAULTRIGHT] = $userObj->getDefaultRight();
    }

    public static final function Initiate(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (_D::DEBUG){
            logger_Initiate(1);
        } else {
            logger_Initiate(10);
        }
        self::CheckLogin();
    }
    public static final function SetDefault(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        _S::_RESET();
        _S::_SET(USERS_USERNAME, self::GUEST);
        _S::_SET(self::TIME_BEGIN, time());
    }
    public static final function LoginAttemptCounter(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if(isset($_SESSION[self::LOGINATTEMPTS_CURR])){
            $_SESSION[self::LOGINATTEMPTS_CURR]++;
            if($_SESSION[self::LOGINATTEMPTS_CURR] >= self::LOGINATTEMPTS_MAX){
                logger_Debug(__FILE__, __LINE__, "Max Login Attempts Reached");
                return false;
            }
            logger_Debug(__FILE__, __LINE__, "counter=".$_SESSION[self::LOGINATTEMPTS_CURR]);
            return true;
        } else {
            logger_Debug(__FILE__, __LINE__, "Login Attempt Counter Started");
            $_SESSION[self::LOGINATTEMPTS_CURR] = 1;
            return true;
        }
    }
    public static final function Login(DBConnect &$db, $nameattempt, $passAttempt){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!self::LoginAttemptCounter()){ return false; }
        if (!is_string($nameattempt)){ throw new InvalidParamException(1, _T::STRING); }
        $nameattempt = $db->escapeString($nameattempt);
        $pass_salt = $db->makeQuery("SELECT userID,salt,password FROM users WHERE username='$nameattempt'");
        if (!$pass_salt){ return false; }
        if (!_S::CheckLoginAttempt($passAttempt, $pass_salt[0][USERS_SALT], $pass_salt[0][USERS_PASSWORD])){ return false; }
        _S::_UNSET(self::LOGINATTEMPTS_CURR);
        self::SetupUser($db, $pass_salt[0][USERS_USERID]);
        return true;
    }
    public static final function Logout(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        $olduser = self::GetUsername();
        _S::InvalidateLogin();
        self::SetDefault();
        return $olduser;
    }
    public static final function CheckLogin(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if ($_SESSION[self::TIME_BEGIN] + self::TIME_OUT <= time()){
            logger_Debug(__FILE__, __LINE__, "Timed Out: Logging Out");
            self::SetDefault();
            return;
        }
        logger_Debug(__FILE__, __LINE__, "Successful Login Check: ".self::GetUsername());
        _S::_SET(self::TIME_BEGIN, time());
    }
}

?>
