<?php

/**
 * Description of UpdateUserProfile
 *
 * @author REx
 */
class UpdateUserProfile
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

        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $rules = $user_factory->get_db_data_rules(DataRules::METHOD_POST);

            $rule_keys = $rules->get_rule_keys();
            foreach($rule_keys as $key)
            {
                if ($key != USERS_EMAIL &&
                    $key != USERS_SECRETANSWER &&
                    $key != USERS_SECRETQUESTION &&
                    $key != USERS_SCHEMEUSING)
                {
                    $rules->remove_rule($key);
                }
            }

            $data = $rules->get_global_data_and_validate();

            $user_factory->update($data, $user_id);
        }
        catch(ValidationException $e)
        {
            throw new UserActionException($e->getMessage());
        }
        catch(DBFactoryException $e)
        {
            throw new UserActionException(
                    "An Error Occurred While Trying To Update The Profile",
                    $e);
        }
    }
}

?>