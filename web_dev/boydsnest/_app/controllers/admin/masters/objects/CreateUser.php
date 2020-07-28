<?php

/**
 * Description of CreateUser
 *
 * @author REx
 */
class CreateUser extends DataCollection
{
    public function __construct()
    {
        parent::__construct();
    }

    public function do_create()
    {
        $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        $data_rules = $user_factory->get_db_data_rules(
                DataRules::METHOD_POST, false);
        $this->data = GrabDataFromGlobal($data_rules);

        $this->data[USERS_ISMASTER]       = "0";
        $this->data[USERS_SCHEMEUSING]    = 'default';
        $this->data[USERS_CREATEDWHEN]    = array(
            DBFactory::INSERT_ESCAPE_VAL    => false,
            DBFactory::INSERT_QUOTE         => false,
            DBFactory::INSERT_VALUE         => "NOW()"
        );

        $password = IsSetPost(USERS_PASSWORD);
        $this->data[USERS_SALT]     = GetNewSalt();
        $this->data[USERS_PASSWORD] = GetSecondOrderHash($password, $this->data[USERS_SALT]);

        try
        {
            $data_rules->validate_data($this->data);
        }
        catch(Exception $e)
        {
            $this->data[USERS_PASSWORD] = $password;
            throw new UserActionException($e->getMessage());
        }

        try
        {
            $this->data[USERS_USERID] = $user_factory->insert($this->data);
        }
        catch(DBFactoryException $e)
        {
            $this->data[USERS_PASSWORD] = $password;
            throw new UserActionException($e->getPrevious()->getMessage());
        }
        catch(Exception $e)
        {
            $this->data[USERS_PASSWORD] = $password;
            throw new UserActionException($e->getMessage());
        }
    }
}

?>