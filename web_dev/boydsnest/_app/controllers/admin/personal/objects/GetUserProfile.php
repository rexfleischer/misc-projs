<?php

/**
 * Description of GetUserProfile
 *
 * @author REx
 */
class GetUserProfile extends DataCollection
{
    public function __construct()
    {
        parent::__construct();

        $user_id = IsSetGetPost(USERS_USERID);
        if (!$user_id)
        {
            $user_id = BoydsnestSession::GetInstance()->get(USERS_USERID);
        }

        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $this->data = $user_factory->select_first($user_id, array(
                        DBFactory::SELECT_GET_ONLY => array(
                            USERS_USERID, USERS_USERNAME, USERS_SCHEMEUSING,
                            USERS_EMAIL, USERS_SECRETANSWER, USERS_SECRETQUESTION,
                            USERS_EXPIRESWHEN, USERS_CREATEDWHEN
                        )
                    ));
        }
        catch(DBFactoryException $e)
        {
            throw new UserActionException(
                    "An Error Occurred While Trying To Get The Profile",
                    $e);
        }
    }
}

?>