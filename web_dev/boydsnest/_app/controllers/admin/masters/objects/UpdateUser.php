<?php

/**
 * Description of UpdateUser
 *
 * @author REx
 */
class UpdateUser
{
    public function __construct()
    {
        $user_id = IsSetPost(USERS_USERID);
        if (!$user_id)
        {
            throw new UserActionException("a user must be selected to update");
        }
        if (!is_numeric($user_id))
        {
            throw new UserActionException("a user id must be numeric");
        }

        $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
        $data_rules = $user_factory->get_db_data_rules(
                DataRules::METHOD_POST, true);

        $data_rules->remove_rule(USERS_USERID);
        $data_rules->remove_rule(USERS_USERNAME);
        $data_rules->remove_rule(USERS_PASSWORD);
        $data_rules->remove_rule(USERS_SALT);
        $data_rules->remove_rule(USERS_LASTUPDATE);
        $data_rules->remove_rule(USERS_CREATEDWHEN);
        $data_rules->remove_rule(USERS_ISMASTER);

        $data = false;
        try
        {
            $data = $data_rules->get_global_data_and_validate();
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }

        try
        {
            $user_factory->update($data, $user_id);
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>