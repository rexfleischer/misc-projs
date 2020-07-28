<?php

/**
 * Description of UsernameAndIdList
 *
 * @author REx
 */
class UsernameAndIdList extends DataCollection
{
    public function __construct()
    {
        parent::__construct();
        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
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
                    ),
                    array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_USERNAME
                        ),
                        DBFactory::SELECT_ORDER_BY => USERS_USERNAME
                    ));
        }
        catch(Exception $e)
        {
            throw new DataCollection($e->getMessage());
        }
    }
}

?>