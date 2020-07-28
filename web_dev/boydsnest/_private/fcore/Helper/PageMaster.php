<?php

/**
 * Description of Page
 *
 * @author REx
 */
class PageMaster
{
    const TEMPLATE_PARAM_BEGIN = "{{";

    const TEMPLATE_PARAM_END = "}}";

    const TEMPLATE_SEPERATOR = ":";

    private $view;

    private $params;

    private $base;

    public function  __construct($base)
    {
        if (!is_file($base))
        {
            throw new Exception("$base is not a file");
        }
        $this->view = null;
        $this->base = $base;
        $this->params = array();
    }

    public function apply_php_page($key, $file, array $params, $append = true)
    {
        if (!is_file($file))
        {
            throw new Exception("$file is not a file");
        }
        extract($params);
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        if ($append && isset($this->params[$key]))
        {
            $this->params[$key] .= $contents;
        }
        else
        {
            $this->params[$key] = $contents;
        }
    }

    public function apply_string($key, $str, $append = true)
    {
        if ($append && isset($this->params[$key]))
        {
            $this->params[$key] .= $str;
        } 
        else
        {
            $this->params[$key] = $str;
        }
    }

    public function apply_param($key, $value, $append = true)
    {
        if (!array_key_exists($key, $this->params))
        {
            $this->params[$key] = $value;
        } 
        else
        {
            if (is_numeric($value))
            {
                $this->params[$key] += $value;
            } 
            else if (is_string($value))
            {
                $this->params[$key] .= $value;
            } 
            else if (is_array($value))
            {
                foreach($value as $_key => $val)
                {
                    $this->params[$key][$_key] = $val;
                }
            } 
        }
    }

    public function commit_applies()
    {
        extract($this->params);
        ob_start();
        include($this->base);
        $this->view = ob_get_contents();
        ob_end_clean();
    }

    public function consume_array($group, array $values)
    {
        if ($this->base == null)
        {
            throw new Exception("must call commitApplies() first");
        }
        $worker = explode(
                self::TEMPLATE_PARAM_BEGIN.$group.self::TEMPLATE_PARAM_END,
                $this->view);
        if (sizeof($worker) != 3)
        {
            throw new Exception("template error");
        }
        $maniptemplate = $worker[1];
        $worker[1] = "";
        foreach($values as $set)
        {
            $thisTotal = $maniptemplate;
            foreach($set as $key => $vals)
            {
                $thisTotal = str_replace(
                        self::TEMPLATE_PARAM_BEGIN.$group.self::TEMPLATE_SEPERATOR.$key.self::TEMPLATE_PARAM_END,
                        $vals, $thisTotal);
            }
            $worker[1] .= $thisTotal;
        }
        $this->view = implode('', $worker);
    }

    public function consume_string($key, $str)
    {
        if ($this->base == null)
        {
            throw new Exception("must call commitApplies() first");
        }
        $this->view = str_replace(
                self::TEMPLATE_PARAM_BEGIN.$key.self::TEMPLATE_PARAM_END,
                $str, $this->view);
    }

    public function consume_php_page($key, $file, array $params)
    {
        if ($this->base == null)
        {
            throw new Exception("must call commitApplies() first");
        }
        if (!is_file($file))
        {
            throw new Exception("$file is not a file");
        }
        extract($params);
        ob_start();
        include($file);
        $contents = ob_get_contents();
        ob_end_clean();
        $this->comsumeString($key, $contents);
    }

    public function get_page()
    {
        return $this->view;
    }

}

?>