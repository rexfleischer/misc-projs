<?php

/**
 * this returns an array of all the values.. but if there is
 * a validation error or a rule formatting error, then it will
 * throw an exception
 *
 * @param array $values
 * $values = array(
 *  'key1'  => 'val1',
 *  'key2'  => 'val2,
 *  ...
 * );
 * @param array $rules this must be in the required format as follows:
 * $rules = array(
 *  'key1'  => array(
 *          DataRules::REQUIRED => 0/1,
 *          DataRules::DEFAULT_ => any
 *          DataRules::TYPE =>
 *                  DataRules::TYPE_STRING /
 *                  DataRules::TYPE_NUM /
 *                  DataRules::TYPE_INTBOOL,
 *          DataRules::MAXLENGTH => int,
 *          DataRules::MINLENGTH => int,
 *          DataRules::MAXVALUE => int,
 *          DataRules::MINVALUE => int,
 *          DataRules::PREGMATCH => string,
 *          DataRules::ENUM => array(
 *                  'enum1',
 *                  'enum2',
 *                  ...
 *              )
 *      )
 * )
 * @return array
 */

/**
 * Description of DataRules
 *
 * @author REx
 */
class DataRules
{
    const REQUIRED      = 'required';

    const DEFAULT_      = 'default';

    const DEFAULT_ON_ERROR  = 'default_on_error';

    const MAXLENGTH     = 'maxlength';

    const MINLENGTH     = 'minlength';

    const MAXVALUE      = 'maxvalue';

    const MINVALUE      = 'minvalue';

    const PREGMATCH     = 'pregmatch';

    const ENUM          = 'enum';

    const METHOD        = 'method';

    const METHOD_GET        = 'get';

    const METHOD_POST       = 'post';

    const METHOD_GETPOST    = 'getpost';

    const METHOD_SESSION    = 'session';

    const TYPE          = 'type';

    const TYPE_STRING       = 'string';

    const TYPE_NUM          = 'int';

    const TYPE_INTBOOL      = 'intbool';

    const TYPE_ANY          = 'any';

    private $rules;

    /**
     *
     * @param <type> $rules 
     */
    public function  __construct($rules = array())
    {
        $this->rules = $rules;
    }

    /**
     *
     * @param <type> $key 
     */
    public function remove_rule($key)
    {
        if (array_key_exists($key, $this->rules))
        {
            unset($this->rules[$key]);
        }
    }

    /**
     *
     * @param <type> $key
     * @return <type> 
     */
    public function get_rule($key)
    {
        if (array_key_exists($key, $this->rules))
        {
            return $this->rules[$key];
        }
        return null;
    }

    /**
     *
     * @return <type> 
     */
    public function get_rule_keys()
    {
        return array_keys($this->rules);
    }

    /**
     *
     * @param <type> $return_result
     * @return <type> 
     */
    public function get_global_data_and_validate()
    {
        $result = array();
        $keys = array_keys($this->rules);
        foreach($keys as $key)
        {
            $rule = $this->get_rule($key);
            $attempt = false;
            if (!isset($rule[DataRules::METHOD]))
            {
                continue;
            }
            switch($rule[DataRules::METHOD])
            {
                case DataRules::METHOD_GET:
                    $attempt = IsSetGet($key, false);
                    break;
                case DataRules::METHOD_POST:
                    $attempt = IsSetPost($key, false);
                    break;
                case DataRules::METHOD_SESSION:
                    $attempt = IsSetSession($key, false);
                    break;
                case DataRules::METHOD_GETPOST:
                    $attempt = IsSetGetPost($key, false);
                    break;
            }
            if ($attempt !== false)
            {
                $result[$key] = $attempt;
            }
        }
        return $this->validate_data($result, true);
    }

    /**
     *
     * @param array $data
     * @param <type> $return_result
     * @return <type> 
     */
    public function validate_data(array $data, $return_result = false)
    {
        if ($return_result)
        {
            $result = array();
        }
        $keys = array_keys($this->rules);
        foreach($keys as $key)
        {
            $rule = $this->get_rule($key);
            if ($rule == null)
            {
                continue;
            }
            
            $value = null;
            if (array_key_exists($key, $data))
            {
                $value = $data[$key];
            }

            if ($value == null && isset($rule[DataRules::DEFAULT_]))
            {
                $result[$key] = $rule[DataRules::DEFAULT_];
                continue;
            }

            if ($value == null)
            {
                if (array_key_exists(DataRules::REQUIRED, $rule))
                {
                    if ($rule[DataRules::REQUIRED])
                    {
                        throw new ValidationException(
                                "$key Is Required, But Not Set");
                    }
                }
                continue;
            }
            
            try
            {
                // check the type
                if (isset($rule[DataRules::TYPE]))
                {
                    switch($rule[DataRules::TYPE])
                    {
                        case DataRules::TYPE_NUM:
                            if (!is_numeric($value))
                            {
                                throw new ValidationException(
                                        "$key Is Not A Number ($value)");
                            }
                            break;
                        case DataRules::TYPE_STRING:
                            if (!is_string($value))
                            {
                                throw new ValidationException(
                                        "$key Is Not A String ($value)");
                            }
                            break;
                        case DataRules::TYPE_INTBOOL:
                            if (!is_numeric($value))
                            {
                                throw new ValidationException(
                                        "$key Is Not A NumBool ($value)");
                            }
                            if ($value != 1 && $value != 0)
                            {
                                throw new ValidationException(
                                        "$key Is Not A NumBool ($value)");
                            }
                            break;
                        case DataRules::TYPE_ANY:
                        default:
                            break;
                    }
                }

                // check number values
                if (isset($rule[DataRules::MAXVALUE]))
                {
                    if ($rule[DataRules::MAXVALUE] < $value)
                    {
                        throw new ValidationException(
                                "$key Is Larger Than The Allowed Value");
                    }
                }
                if (isset($rule[DataRules::MINVALUE]))
                {
                    if ($rule[DataRules::MINVALUE] > $value)
                    {
                        throw new ValidationException(
                                "$key Is Smaller Than The Allowed Value");
                    }
                }

                // check string lengths
                if (isset($rule[DataRules::MAXLENGTH]))
                {
                    if (strlen($value) > $rule[DataRules::MAXLENGTH])
                    {
                    var_dump($value);
                    exit();
                        throw new ValidationException(
                                "$key's Length Is Larger Than The ".
                                "Allowed Value (size=".strlen($value).")");
                    }
                }
                if (isset($rule[DataRules::MINLENGTH]))
                {
                    if (strlen($value) < $rule[DataRules::MINLENGTH])
                    {
                        throw new ValidationException(
                                "$key's Length Is Smaller Than The" .
                                " Allowed Value (size=".strlen($value).")");
                    }
                }

                if (isset($rule[DataRules::ENUM]))
                {
                    if (!is_array($rule[DataRules::ENUM]))
                    {
                        throw new DataRulesException(
                                "$key is configured incorrectly");
                    }
                    if (!array_search($value, $rule[DataRules::ENUM]))
                    {
                        throw new ValidationException(
                                "$value failed the enum test");
                    }
                }

                if (isset($rule[DataRules::PREGMATCH]))
                {
                    if (preg_match($rule[DataRules::PREGMATCH], $value) == 0)
                    {
                        throw new ValidationException(
                                "$value does not match the" .
                                " given regular expression");
                    }
                }
            }
            catch(ValidationException $e)
            {
                if (isset($rule[DataRules::DEFAULT_ON_ERROR]) &&
                        $rule[DataRules::DEFAULT_ON_ERROR] &&
                        isset($rule[DataRules::DEFAULT_]))
                {
                    $value = $rule[DataRules::DEFAULT_];
                }
                else
                {
                    throw new ValidationException($e->getMessage());
                }
            }

            if ($return_result)
            {
                $result[$key] = $value;
            }
        }
        if ($return_result)
        {
            return $result;
        }
    }
}

?>
