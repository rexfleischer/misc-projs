<?php

require_once FCORE_FILE_DBLOGGER;

/**
 * Description of AttemptLogin
 *
 * @author REx
 */
class AttemptLogin
{
    private $username;
    
    private $password;

    private $user_id;

    private $user_factory;

    public function __construct()
    {
        $this->collect();
        $this->validate();
        $this->do_action();
    }

    public function get_username_attempt()
    {
        return $this->username;
    }

    public function collect()
    {
        $this->password = IsSetPost(USERS_PASSWORD);
        $this->username = IsSetPost(USERS_USERNAME);
        if ($this->password === false ||
            $this->password === "" ||
            $this->username == false  ||
            $this->username == "")
        {
            throw new UserActionException(
                    "Both A Username And A Password Must Be Set");
        }
    }

    public function validate()
    {
        $this->user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        $user = $this->user_factory->select_first(
                array(
                    array(
                        DBFactory::ID_KEY   => USERS_USERNAME,
                        DBFactory::ID_SIGN  => "=",
                        DBFactory::ID_VAL   => $this->username
                    )
                ),
                array(
                    DBFactory::SELECT_GET_ONLY => array(
                        USERS_USERID,
                        USERS_USERNAME,
                        USERS_PASSWORD,
                        USERS_SALT,
                        USERS_ISACTIVE
                    )
                )
            );
        if ($user == null)
        {
            if (isset($_SESSION[SESSION_LOGIN_ATTEMPTS]))
            {
                $_SESSION[SESSION_LOGIN_ATTEMPTS]++;
            }
            else
            {
                $_SESSION[SESSION_LOGIN_ATTEMPTS] = 1;
            }
            $this->invalid_login_attempt();
        }
        if (!$user[USERS_ISACTIVE])
        {
            DBLogger::log(BN_LOGTYPE_FAILEDLOGIN,
                    "inactive user ".$user[USERS_USERNAME]." attempted to log in");
        }
        if (!BoydsnestSession::GetInstance()->CheckLoginAttempt(
                $user[USERS_USERNAME],
                $this->password,
                $user[USERS_SALT],
                $user[USERS_PASSWORD]))
        {
            $this->invalid_login_attempt();
        }
        $this->user_id = $user[USERS_USERID];
    }

    private function invalid_login_attempt()
    {
        DBLogger::log(BN_LOGTYPE_FAILEDLOGIN, 
                serialize(array(
                    USERS_USERNAME => $this->username,
                    USERS_PASSWORD => $this->password)
                ));
        throw new ValidationException(
                "Username Or Password Is Incorrect");
    }

    public function do_action()
    {
        $user = $this->user_factory->select_first($this->user_id);
        $session = BoydsnestSession::GetInstance();
        $session->setupUser($user);
        DBLogger::log(BN_LOGTYPE_USERLOG(), "logged in");
    }
}

?>