<?php

/**
 * Description of GetNextUserManualRank
 *
 * @author REx
 */
class GetNextUserManualRank
{
    private $rank;

    public function __construct()
    {
        $this->collect();
    }
    
    public function get_rank()
    {
        return $this->rank;
    }

    public function collect()
    {
        $conn =& FCore::GetDefaultConnection();
        $this->rank = $conn->quick_query(
                "SELECT max(".USERMANUAL_RANK.") FROM ".USERMANUAL, true);
        if (!$this->rank)
        {
            $this->rank = 1;
        }
        else
        {
            $this->rank = $this->rank[0]["max(".USERMANUAL_RANK.")"];
            $this->rank++;
        }
    }
}

?>