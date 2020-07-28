<?php

require_once '../FCore.php';

class Cache
{

    private $root;

    public function  __construct($root)
    {
        $this->root = $root;
    }

    public function set($id)
    {
        $file = $this->root."/".md5($id).".".FCORE_EXT_CACHE;
        if (file_exists($file))
        {
            $cache = File_ReadDump($file);
            if ($cache)
            {
                $cache = unserialize($cache);
                if (time() < $cache['time'] + $cache['ttl'])
                {
                    return $cache['data'];
                } 
                else
                {
                    unlink($file);
                }
            }
        }
        return false;
    }

    public function Save($id, $ttl, $data)
    {
        return File_WriteDump(
                $this->root."/".md5($id).".".FCORE_EXT_CACHE,
                serialize(array(
                    'time'  => time(),
                    'ttl'   => $ttl,
                    'data'  => $data
                ))
            );
    }

    public function Clean()
    {
        
    }

    public function Refresh()
    {
        
    }

}

?>