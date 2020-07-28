<?php

/**
 * Description of GetSideMenuContentState
 *
 * @author REx
 */
class GetSideMenuContentState extends DataCollection
{
    public function __construct($selected = null)
    {
        parent::__construct();

        $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        if (!$user_factory)
        {
            throw new UserActionException(
                    "could not connect to the user database", null);
        }

        try
        {
            $this->data = $user_factory->select(
                    array(
                        array(
                            DBFactory::ID_KEY   => USERS_USERNAME,
                            DBFactory::ID_SIGN  => "!=",
                            DBFactory::ID_VAL   => "system",
                        ),
                        "AND",
                        array(
                            DBFactory::ID_KEY   => USERS_USERNAME,
                            DBFactory::ID_SIGN  => "!=",
                            DBFactory::ID_VAL   => "guest",
                        ),
                        "AND",
                        array(
                            DBFactory::ID_KEY   => USERS_ISACTIVE,
                            DBFactory::ID_SIGN  => "=",
                            DBFactory::ID_VAL   => "1",
                        ),
                        "AND",
                        array(
                            DBFactory::ID_KEY   => USERS_ISFAMILY,
                            DBFactory::ID_SIGN  => "=",
                            DBFactory::ID_VAL   => "1",
                        ),
                    ),
                    array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_USERNAME, 
                        ),
                        DBFactory::SELECT_ORDER_BY => USERS_USERNAME
                    ));
        }
        catch(DBFactoryException $e)
        {
            throw new UserActionException(
                    "an error occurred while getting user data", $e);
        }

        if ($selected)
        {
            foreach($this->data as $key => $user)
            {
                if ($user[USERS_USERID] == $selected[USERS_USERID])
                {
                    $this->data[$key][DBForum::POSTCHILDREN] = FCore::LoadObject(
                        "pages/GetUserPageHierarchy",
                        array(
                            USERS_USERID    => $selected[USERS_USERID],
                            "extended"      => true,
                        ));
                    if (is_a($this->data[$key][DBForum::POSTCHILDREN], "DataCollection"))
                    {
                        $this->data[$key][DBForum::POSTCHILDREN] =
                                $this->data[$key][DBForum::POSTCHILDREN]->get_data();
                    }
                    break;
                }
            }
        }
    }
}

?>