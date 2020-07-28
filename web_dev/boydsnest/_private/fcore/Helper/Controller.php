<?php

/**
 * Description of Controller
 *
 * @author REx
 */
abstract class Controller
{
    const REDIRECTS     = 'redirects';
    const REDIRECT_TO   = 'redirect_to';
    const REDIRECT_IF   = 'redirect_if';
    const REDIRECT_FOR  = 'redirect_for';
    const REDIRECT_DATA = 'redirect_data';

    public abstract function get_local_dir();

    public abstract function get_route_map();

    private $route_map;
    
    private $local_dir;

    /**
     *
     * @var Logger
     */
    protected $logger;

    public function __construct()
    {
        $this->logger =& FCore::GetLogger();
        $this->route_map =& $this->get_route_map();
        $this->local_dir =  $this->get_local_dir();
    }

    /**
     *
     * @param <type> $model
     * @param <type> $data
     * @return class 
     */
    public function load_local_object($object, $data = null)
    {
        $file = $this->local_dir . "/$object.php";
        if (!is_file($file))
        {
            throw new ControllerException(
                    "cannot find $file while trying to load a model");
        }
        include_once($file);
        $info = pathinfo($file);
        $class = $info['filename'];
        $instance;
        if ($data != null)
        {
            $instance = new $class($data);
        }
        else
        {
            $instance = new $class();
        }
        return $instance;
    }

    /**
     *
     * @param <type> $view
     * @param <type> $data
     * @return <type> 
     */
    public function load_local_php_view($view, $data = array())
    {
        $file = $this->local_dir . "/$view.php";
        if (!is_file($file))
        {
            throw new ControllerException(
                    "cannot find $file while trying to load a php view");
        }
        extract($data);
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        return $contents;
    }

    /**
     *
     * @param <type> $view
     * @return <type> 
     */
    public function load_local_view($view)
    {
        $file = $this->local_dir . "/$view";
        if (!is_file($file))
        {
            throw new ControllerException(
                    "cannot find $file while trying to load a view");
        }
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        return $contents;
    }

    /**
     *
     * @param <type> $request 
     */
    public function init_request($request, $data = null)
    {
        if (ACTION_DEFAULT == $request)
        {
            $request = $this->route_map[ACTION_DEFAULT];
        }
        if (!array_key_exists($request, $this->route_map))
        {
            if (array_key_exists(ACTION_DEFAULT, $this->route_map))
            {
                $request = $this->route_map[ACTION_DEFAULT];
                if (!array_key_exists($request, $this->route_map))
                {
                    throw new ControllerException(
                            "cannot find requested route");
                }
            }
            else
            {
                FCore::ShowError(404);
            }
        }

        if (array_key_exists(self::REDIRECTS, $this->route_map))
        {
            $redirects = $this->route_map[self::REDIRECTS];
            foreach($redirects as $redirect)
            {
                if (!isset($redirect[self::REDIRECT_IF]) ||
                        !isset($redirect[self::REDIRECT_TO]))
                {
                    throw new ControllerException(
                            "redirects configured incorrectly");
                }
                if (array_key_exists(self::REDIRECT_FOR, $redirect))
                {
                    if (!array_search($request, $redirect[self::REDIRECT_FOR]))
                    {
                        continue;
                    }
                }
                if ($redirect[self::REDIRECT_IF])
                {
                    $request = $redirect[self::REDIRECT_TO];
                    $data = $redirect[self::REDIRECT_DATA];
                    break;
                }
            }
        }

        $func = $this->route_map[$request];
        try
        {
            if ($data != null)
            {
                return $this->$func($request, $data);
            }
            else
            {
                return $this->$func($request);
            }
        }
        catch(SecurityCheck $e)
        {
            FCore::ShowError(403);
        }
        catch(Exception $e)
        {
            FCore::ShowError(500, $e->getMessage());
        }
    }
}

?>