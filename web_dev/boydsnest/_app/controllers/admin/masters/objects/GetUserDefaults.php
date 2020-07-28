<?php

/**
 * Description of GetUserDefaults
 *
 * @author REx
 */
class GetUserDefaults extends DataCollection
{
    public function __construct()
    {
        parent::__construct();

        try
        {
            $user_factory =& FCore::LoadDBFactory(BN_DBFACTORY_USERMODEL);
            $rules = $user_factory->get_db_data_rules();

            foreach($rules->get_rule_keys() as $key)
            {
                $rule = $rules->get_rule($key);
                if (isset($rule[DataRules::DEFAULT_]))
                {
                    $this->data[$key] = $rule[DataRules::DEFAULT_];
                }
            }
        }
        catch(Exception $e)
        {
            throw new UserActionException($e->getMessage());
        }
    }
}

?>