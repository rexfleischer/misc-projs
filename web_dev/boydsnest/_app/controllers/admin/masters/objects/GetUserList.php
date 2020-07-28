<?php

/**
 * Description of GetUserList
 *
 * @author REx
 */
class GetUserList extends DataCollection
{
    public function __construct()
    {
        parent::__construct();

        $user_factory = false;
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $this->data = $user_factory->select(
                    array(
                        array(
                            DBFactory::ID_KEY   => USERS_USERNAME,
                            DBFactory::ID_SIGN  => "!=",
                            DBFactory::ID_VAL   => "system"
                        ),
                        "AND",
                        array(
                            DBFactory::ID_KEY   => USERS_USERNAME,
                            DBFactory::ID_SIGN  => "!=",
                            DBFactory::ID_VAL   => "Guest"
                        ),
                        "AND",
                        array(
                            DBFactory::ID_KEY   => USERS_USERID,
                            DBFactory::ID_SIGN  => "!=",
                            DBFactory::ID_VAL   => BoydsnestSession::GetInstance()->get(USERS_USERID)
                        ),
                    ),
                    array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_USERNAME, USERS_CREATEDWHEN,
                            USERS_ISACTIVE, USERS_LASTUPDATE, USERS_ISLOGGED
                        )
                    ));
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>