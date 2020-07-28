<?php

require_once FCORE_FILE_DATARULES;

/**
 * Description of UsersFactory
 *
 * @author REx
 */
class UsersFactory extends DBFactory
{
    public function __construct($conn = null)
    {
        parent::__construct(USERS, USERS_USERID, $conn, array(
            USERS_USERID        => array(
                DataRules::TYPE         => DataRules::TYPE_NUM,
                DataRules::MINVALUE     => 1,
                DataRules::REQUIRED     => false
            ),
            USERS_USERNAME      => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 40,
                DataRules::MINLENGTH    => 5,
                DataRules::REQUIRED     => true,
            ),
            USERS_PASSWORD      => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 40,
                DataRules::MINLENGTH    => 40,
                DataRules::REQUIRED     => true,
            ),
            USERS_SALT          => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 3,
                DataRules::MINLENGTH    => 3,
                DataRules::REQUIRED     => true,
            ),
            USERS_EMAIL         => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 60,
                DataRules::MINLENGTH    => 5,
                DataRules::DEFAULT_     => "",
            ),
            USERS_SECRETQUESTION=> array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 255,
                DataRules::MINLENGTH    => 5,
                DataRules::DEFAULT_     => "",
            ),
            USERS_SECRETANSWER  => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 255,
                DataRules::MINLENGTH    => 0,
                DataRules::DEFAULT_     => "",
            ),
            USERS_MASTERNOTES   => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 255,
                DataRules::MINLENGTH    => 5,
                DataRules::DEFAULT_     => "",
            ),
            USERS_SCHEMEUSING   => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 40,
                DataRules::MINLENGTH    => 5,
                DataRules::DEFAULT_     => "default",
            ),
            USERS_CANDOWNLOAD   => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CANUPLOAD     => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CANMESSAGE    => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CANCDSELF     => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CANCDOTHER    => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CANSCHEME     => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_ISFAMILY      => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_ISLOGGED      => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_ISMASTER      => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_ISACTIVE      => array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_ISPERMISSIONED=> array(
                DataRules::TYPE     => DataRules::TYPE_INTBOOL,
                DataRules::DEFAULT_ => 0,
            ),
            USERS_CREATEDWHEN   => array(
                
            ),
            USERS_EXPIRESWHEN   => array(
                DataRules::TYPE         => DataRules::TYPE_STRING,
                DataRules::MAXLENGTH    => 20,
                DataRules::MINLENGTH    => 5,
                DataRules::DEFAULT_     => "",
            ),
            USERS_LASTUPDATE    => array(
                
            ),
            USERS_DEFAULTRIGHT  => array(
                DataRules::TYPE         => DataRules::TYPE_NUM,
                DataRules::MINVALUE     => USERS_DEFAULTRIGHT_NONE,
                DataRules::MAXVALUE     => USERS_DEFAULTRIGHT_WRITE,
                DataRules::DEFAULT_     => USERS_DEFAULTRIGHT_NONE
            )
        ));
    }
}

?>
