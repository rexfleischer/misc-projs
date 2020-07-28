<?php


/**
 * Description of Session
 *
 * @author REx
 */
abstract class Session
{

    protected abstract function setupUser($usermodel);

    public function  __construct(
            $fingerPrint = false,
            $checkSessionHijack = false)
    {
        if ($checkSessionHijack)
        {
            if (isset($_SESSION['HTTP_USER_AGENT']))
            {
                if ($_SESSION['HTTP_USER_AGENT'] !=
                        md5($_SERVER['HTTP_USER_AGENT']))
                {
                    $this->ResetSession();
                }
            } 
            else
            {
                $_SESSION['HTTP_USER_AGENT'] =
                    md5($_SERVER['HTTP_USER_AGENT']);
            }
        }
        if ($fingerPrint)
        {
            if (isset($_SESSION[SESSION_FINGERPRINT]))
            {
                if ($_SESSION[SESSION_FINGERPRINT] != md5($key))
                {
                    $this->ResetSession();
                }
            } 
            else
            {
                $_SESSION[SESSION_FINGERPRINT] = md5($fingerPrint);
            }
        }
    }

    public function ResetSession()
    {
        $_SESSION = array();
    }

    public function set($key, $value)
    {
        if ($key == null)
        {
            unset($_SESSION[$key]);
            return;
        }
        $_SESSION[$key] = $value;
    }

    public function get($key)
    {
        if (array_key_exists($key, $_SESSION))
        {
            return $_SESSION[$key];
        }
        return null;
    }

    /**
     * checks the login password, returns false if failed. sets $_SESSION['validated']
     * if successful
     * @param <string> $passAttempt
     * @param <string> $salt
     * @param <string> $password
     * @return <boolean>
     */
    public function CheckLoginAttempt(
            $username, $passAttempt, $salt, $password)
    {
        if (isset($_SESSION[SESSION_TIMEOUT_UNTIL]))
        {
            if ($_SESSION[SESSION_TIMEOUT_UNTIL] > time())
            {
                return false;
            }
            unset($_SESSION[SESSION_TIMEOUT_UNTIL]);
        }
        if (isset($_SESSION[SESSION_LOGIN_ATTEMPTS]))
        {
            if ($_SESSION[SESSION_LOGIN_ATTEMPTS] >= 
                    FCore::$LOGIN_MAX_ATTEMPTS)
            {
                unset($_SESSION[SESSION_LOGIN_ATTEMPTS]);
                $_SESSION[SESSION_TIMEOUT_UNTIL] =
                    time() + FCore::$LOGIN_TIME_FROZE;
                return false;
            }
            $_SESSION[SESSION_LOGIN_ATTEMPTS]++;
        } 
        else
        {
            $_SESSION[SESSION_LOGIN_ATTEMPTS] = 1;
        }
        if ($password != GetSecondOrderHash($passAttempt, $salt))
        {
            return false;
        }
        if (isset($_SESSION[SESSION_LOGIN_ATTEMPTS]))
        {
            unset($_SESSION[SESSION_LOGIN_ATTEMPTS]);
        }
        $_SESSION[SESSION_VALIDATED] = 1;
        return true;
    }

    /**
     * checks if validated is set from CheckLoginAttempt()
     * @return <boolean>
     */
    public function IsLoggedIn()
    {
        return isset($_SESSION[SESSION_VALIDATED]);
    }

    /**
     * unsets $_SESSION['validated'] to log out
     */
    public function InvalidateLogin()
    {
        unset($_SESSION[SESSION_VALIDATED]);
    }

    /**
     * Sets a fingerpint for CheckFingerPrint() to check against
     * @param <type> $key
     */
    public function SetFingerPrint($key)
    {
        $_SESSION[SESSION_FINGERPRINT] = md5($key);
    }

}

?>
