<?php

/**
 * Description of DataCollection
 *
 * @author REx
 */
class DataCollection
{
    /**
     *
     * @var array
     */
    protected $data;

    /**
     *
     * @param array $data
     */
    public function __construct(array $data = array())
    {
        $this->data = $data;
    }

    /**
     *
     * @param <type> $key
     * @return <type>
     */
    public function get($key)
    {
        if (array_key_exists($key, $this->data))
        {
            return $this->data[$key];
        }
        return null;
    }

    /**
     *
     * @param <type> $key
     * @param <type> $value
     */
    public function set($key, $value)
    {
        $this->data[$key] = $value;
    }

    /**
     *
     * @return int
     */
    public function size()
    {
        return sizeof($this->data);
    }

    /**
     *
     * @return array
     */
    public function get_data()
    {
        return $this->data;
    }

    /**
     *
     * @return array
     */
    public function get_data_keys()
    {
        return array_keys($this->data);
    }

    /**
     * does a deep copy and returns a new instance of the object
     * @return DataCollection 
     */
    public function deep_copy()
    {
        return new DataCollection($this->data);
    }
    
    /**
     *
     * @param <type> $data
     */
    public function collide(DataCollection $data)
    {
        foreach($data->get_data() as $key => $value)
        {
            $this->data[$key] = $value;
        }
    }
}

?>