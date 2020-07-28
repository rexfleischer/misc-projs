<?php

require_once 'Action.php';
require_once 'Decision.php';
require_once 'RequestData.php';
require_once 'RequestView.php';

/**
 * Description of RequestPipeline
 *
 * @author REx
 */
abstract class RequestPipeline
{
    public abstract function get_file_root();

    public abstract function get_pipeline();

    public abstract function get_auto_route_paths();

    const CONTENT       = 'content';
    const CONTENT_TYPE  = 'content_type';
    const CONTENT_TYPE_DECISION = 'decision';
    const CONTENT_TYPE_ACTION   = 'action';
    const CONTENT_TYPE_DATA     = 'data';
    const CONTENT_TYPE_VIEW     = 'view';
    const CONTENT_TYPE_NO_ACTION= 'no_action';

    const CONTENT_ID            = 'content_id';
    const NEXT_ID               = 'next_id';
    const PUSH_DATA_TO_VAR      = 'push_data_to_var';
    const APPEND_DATA_TO_VAR    = 'append_data_to_var';
    const DATA_USE              = 'data_use';
    const END_POINT             = 'end_point';
    const RETURN_VAR            = 'return_var';
    const ON_RETURN_NULL_ID     = 'on_return_null_id';

    const ON_EXCEPTION_VIEW     = 'on_exception_view';
    const ON_EXCEPTION_ACTION   = 'on_exception_aciton';
    const ON_EXCEPTION_DATA     = 'on_exception_data';
    const ON_EXCEPTION_DECISION = 'on_exception_decision';

    const DECISION_ON_TRUE_ID   = 'decision_on_true';
    const DECISION_ON_FALSE_ID  = 'decision_on_false';

    private $auto_routes;

    private $pipeline;

    private $file_root;

    private $logger;

    public function __construct()
    {
        $this->logger =& FCore::GetLogger();
        $this->pipeline = array_values($this->get_pipeline());
        $this->file_root =& $this->get_file_root();
        $this->auto_routes =& $this->get_auto_route_paths();
    }

    public function auto_route($route)
    {
        if (array_key_exists($route, $this->auto_routes))
        {
            return $this->execute($this->auto_routes[$route]);
        }
        if (array_key_exists('default', $this->auto_routes))
        {
            return $this->execute($this->auto_routes['default']);
        }
        throw new RequestPipelineException(
                "route does not exist in auto route configuration");
    }

    public function execute($start_id)
    {
        $vars = array();
        $cursor = 0;
        try
        {
            while(true)
            {
                // if the curser
                if (!array_key_exists($cursor, $this->pipeline))
                {
                    throw new RequestPipelineException("out of bounds error");
                }

                // setup so decisions can make next line inserts
                $next_line = false;

                // get current array
                $current = $this->pipeline[$cursor];
                if (!array_key_exists(self::CONTENT_TYPE, $current))
                {
                    throw new RequestPipelineException(
                            "syntax error: no content type on command $cursor");
                }

                // if there is data to be used, then get it... if the data
                // is not found, then it will stay null
                $data_use = null;
                if (array_key_exists(self::DATA_USE, $current))
                {
                    if (array_key_exists($current[self::DATA_USE], $vars))
                    {
                        $data_use = $vars[$current[self::DATA_USE]];
                    }
                }

                // first, we execute the action. so, if there is
                $return = null;
                switch($current[self::CONTENT_TYPE])
                {
                    ///////////////////////////////////////////////////////////
                    // this is for actions to be performed
                    case self::CONTENT_TYPE_ACTION:
                        if (!array_key_exists(self::DO_ACTION, $data_use))
                        {
                            throw new RequestPipelineException(
                                "syntax error: command $cursor has no loadable command");
                        }
                        $action_instance = $this->load_local_action(
                                $current[self::DO_ACTION],
                                $data_use);
                        $action_instance->execute();
                        $return = $action_instance->get_data();
                        break;
                    ///////////////////////////////////////////////////////////
                    // data stuff
                    case self::CONTENT_TYPE_DATA:
                        if (!array_key_exists(self::DO_ACTION, $data_use))
                        {
                            throw new RequestPipelineException(
                                "syntax error: command $cursor has no loadable command");
                        }
                        $return = $this->load_local_data(
                                $current[self::DO_DATA],
                                $data_use);
                        break;
                    ///////////////////////////////////////////////////////////
                    // basically for if statements
                    case self::CONTENT_TYPE_DECISION:
                        // check to make sure the decision does something
                        if (!array_key_exists(
                                self::DECISION_ON_TRUE_ID,
                                $current) &&
                            !array_key_exists(
                                self::DECISION_ON_FALSE_ID,
                                $current))
                        {
                            throw new RequestPipelineException(
                                "syntax error: no decision tree on command $cursor");
                        }
                        // check to make sure a decision object actually exists
                        if (!array_key_exists(self::DO_DECISION, $current))
                        {
                            throw new RequestPipelineException(
                                "syntax error: command $cursor has no loadable command");
                        }
                        // do the decision check
                        $decision_instance = $this->load_local_decision(
                                $current[self::DO_DECISION],
                                $data_use);
                        if ($decision_instance->execute())
                        {
                            // if decision returns true, then go to true id
                            // if it is set
                            if (isset(array_key_exists(
                                    self::DECISION_ON_TRUE_ID,
                                    $current)))
                            {
                                $next_line = $this->get_index_of_id(
                                        $this->pipeline,
                                        $current[self::DECISION_ON_TRUE_ID]);
                            }
                        }
                        else
                        {
                            // else, go to the false id set
                            if (isset(array_key_exists(
                                    self::DECISION_ON_FALSE_ID,
                                    $current)))
                            {
                                $next_line = $this->get_index_of_id(
                                        $this->pipeline,
                                        $current[self::DECISION_ON_FALSE_ID]);
                            }
                        }
                        break;
                    ///////////////////////////////////////////////////////////
                    // content view
                    case self::CONTENT_TYPE_VIEW:
                        if (!array_key_exists(self::DO_VIEW, $data_use))
                        {
                            throw new RequestPipelineException(
                                "syntax error: command $cursor has no loadable command");
                        }
                        $view_instance = $this->load_local_view(
                                $current[self::DO_VIEW],
                                $data_use);
                        $view_instance->execute();
                        $return = $view_instance->get_view();
                        break;
                    ///////////////////////////////////////////////////////////
                    // no action
                    case self::CONTENT_TYPE_NO_ACTION:
                        // if there is no action, then simply pass the
                        // used data (if there is any) to the returned data
                        if (!is_null($data_use))
                        {
                            $return =& $data_use;
                        }
                        break;
                    default:
                        throw new RequestPipelineException(
                                "syntax error: invalid content type");
                }

                // because decisions only can return true or false,
                // there is no need for any of this to be processed
                // and could potentially break things
                if ($current[self::CONTENT_TYPE] != self::DO_DECISION)
                {
                    // if the return of the command is not null, then check
                    // if something needs to happen to it
                    if ($return != null)
                    {
                        // if push data is set, then set/override that data
                        if (isset($current[self::PUSH_DATA_TO_VAR]))
                        {
                            $vars[$current[self::PUSH_DATA_TO_VAR]] = $return;
                        }
                        // if append is set, try to append data if it is already
                        // set, else just set the data
                        if (isset($current[self::APPEND_DATA_TO_VAR]))
                        {
                            if (isset($vars[$current[self::APPEND_DATA_TO_VAR]]))
                            {
                                $this->append_data(
                                        $vars[$current[self::APPEND_DATA_TO_VAR]],
                                        $return);
                            }
                            else
                            {
                                $vars[$current[self::APPEND_DATA_TO_VAR]] = $return;
                            }
                        }
                    }
                    else if(array_key_exists(self::ON_RETURN_NULL_ID, $current))
                    {
                        // if there is a trigger for doing something when
                        // the return value is null, then go there
                        $next_line = $this->get_index_of_id(
                                $this->pipeline,
                                $current[self::ON_RETURN_NULL_ID]);
                    }
                }

                // if end point is set, then return all vars
                if (isset($current[self::END_POINT]) && $next_line === false)
                {
                    return $vars;
                }
                // if a var is supposed to be returned, then end pipeline
                if (isset($current[self::RETURN_VAR]))
                {
                    if (is_array($current[self::RETURN_VAR]))
                    {
                        $returning = array();
                        foreach($current[self::RETURN_VAR] as $val)
                        {
                            if (array_key_exists($val, $vars))
                            {
                                $returning[$val] = $vars[$val];
                            }
                            else
                            {
                                $returning[$val] = null;
                            }
                        }
                        return $returning;
                    }
                    if (array_key_exists($current[self::RETURN_VAR], $var))
                    {
                        return $var[$current[self::RETURN_VAR]];
                    }
                    return null;
                }
                // if a next id is supposed to be use, and next line
                // has not been set, then get that command
                if (isset($current[self::NEXT_ID]) &&
                        $next_line === false)
                {
                    $next_line = $this->get_index_of_id(
                            $this->pipeline,
                            $current[self::NEXT_ID]);
                }
                // if next line has not been set, go to next line... but
                // if it has been set then go to the next line
                if ($next_line === false)
                {
                    $cursor++;
                }
                else
                {
                    $cursor = $next_line;
                }
            }
        }
        catch(Exception $e)
        {
            
        }
    }

    private function get_index_of_id(&$pipeline, $id)
    {
        foreach($pipeline as $key => $value)
        {
            if (isset($value[self::CONTENT_ID]))
            {
                if ($value[self::CONTENT_ID] == $id)
                {
                    return $key;
                }
            }
        }
        if (array_key_exists($key, $pipeline))
        {
            return $key;
        }
        return false;
    }

    /**
     *
     * @param <type> $view_class
     * @param <type> $data
     * @return RequestView
     */
    public function load_local_view($view_class, $data = null)
    {
        return $this->load_class(
                $this->file_root . "/$view_class.php",
                $view_class, $data);
    }

    /**
     *
     * @param <type> $action_class
     * @param <type> $data
     * @return Action
     */
    public function load_local_action($action_class, $data = null)
    {
        return $this->load_class(
                $this->file_root . "/$action_class.php",
                $action_class, $data);
    }

    /**
     *
     * @param <type> $data_class
     * @param <type> $data
     * @return RequestData
     */
    public function load_local_data($data_class, $data = null)
    {
        return $this->load_class(
                $this->file_root . "/$data_class.php",
                $data_class, $data);
    }

    /**
     *
     * @param <type> $decision_class
     * @param <type> $data
     * @return Decision
     */
    public function load_local_decision($decision_class, $data = null)
    {
        return $this->load_class(
                $this->file_root . "/$decision_class.php",
                $decision_class, $data);
    }

    private function load_class($file, $class, $data = null)
    {
        if (!is_file($file))
        {
            throw new Exception("$file not found");
        }
        include_once($file);
        if ($data == null)
        {
            return new $class();
        }
        return new $class($data);
    }

    private function append_data(&$curr_data, &$new_data)
    {
        if (is_string($new_data))
        {
            if (!is_string($curr_data))
            {
                throw new RequestPipelineException(
                        'logic error: new_data is a string,' .
                        ' whereas curr_data is not');
            }
            $curr_data .= $new_data;
        }
        else if (is_numeric($new_data))
        {
            if (!is_numeric($curr_data))
            {
                throw new RequestPipelineException(
                        'logic error: new_data is numeric,' .
                        ' whereas curr_data is not');
            }
            $curr_data += $new_data;
        }
        else if (is_array($new_data))
        {
            if (!is_array($curr_data))
            {
                throw new RequestPipelineException(
                        'logic error: new_data is an array,' .
                        ' whereas curr_data is not');
            }
            foreach($new_data as $key => $value)
            {
                $curr_data[$key] = $value;
            }
        }
        else if ($new_data instanceof RequestData)
        {
            if (!($curr_data instanceof RequestData))
            {
                throw new RequestPipelineException(
                        'logic error: new_data is a RequestData,' .
                        ' whereas curr_data is not');
            }
            $curr_data->collide($new_data);
        }
        else
        {
            throw new RequestPipelineException(
                    'logic error: unknown data type for new_data');
        }
    }
}

?>