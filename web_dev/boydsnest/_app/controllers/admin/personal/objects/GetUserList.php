<?php

/**
 * Description of GetUserList
 *
 * @author REx
 */
class GetUserList extends DataCollection
{
    public function __construct($user_id = false)
    {
        parent::__construct();

        $user_factory = false;
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            if (!$user)
            {
                $this->data = $user_factory->select(
                        array(
                            array(
                                DBFactory::ID_KEY => USERS_USERNAME,
                                DBFactory::ID_SIGN => "!=",
                                DBFactory::ID_VAL => "guest"
                            ),
                            "AND",
                            array(
                                DBFactory::ID_KEY => USERS_USERNAME,
                                DBFactory::ID_SIGN => "!=",
                                DBFactory::ID_VAL => "system"
                            ),
                            "AND",
                            array(
                                DBFactory::ID_KEY => USERS_ISACTIVE,
                                DBFactory::ID_SIGN => "=",
                                DBFactory::ID_VAL => 1
                            ),
                            "AND",
                            array(
                                DBFactory::ID_KEY => USERS_USERID,
                                DBFactory::ID_SIGN => "!=",
                                DBFactory::ID_VAL =>
                                    BoydsnestSession::GetInstance()->get(USERS_USERID)
                            ),
                        ),
                        array(
                            DBFactory::SELECT_GET_ONLY => array(
                                USERS_USERID, USERS_USERNAME
                            ),
                            DBFactory::SELECT_ORDER_BY => USERS_USERNAME
                        ));
            }
            else
            {
                $this->data = $user_factory->select(
                        $user_id,
                        array(
                            DBFactory::SELECT_GET_ONLY => array(
                                USERS_USERID, USERS_USERNAME
                            ),
                            DBFactory::SELECT_ORDER_BY => USERS_USERNAME
                        ));
            }
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>