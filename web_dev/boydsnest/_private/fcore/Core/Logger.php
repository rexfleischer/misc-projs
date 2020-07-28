<?php

/**
 * Description of Logger
 *
 * @author REx
 */
class Logger {

    const LEVEL = "level";
    const FILE  = "file";
    const LINE  = "line";
    const MSG   = "msg";

    const LEVEL_TRACE   = 1;
    const LEVEL_DEBUG   = 2;
    const LEVEL_INFO    = 3;
    const LEVEL_WARN    = 4;
    const LEVEL_ERROR   = 5;
    const LEVEL_FATAL   = 6;
    const LEVEL_TRACE_COLOR = "#a0a000";
    const LEVEL_DEBUG_COLOR = "#64c864";
    const LEVEL_INFO_COLOR  = "#afaf00";
    const LEVEL_WARN_COLOR  = "#0000ff";
    const LEVEL_ERROR_COLOR = "#ff8c00";
    const LEVEL_FATAL_COLOR = "#ff0000";

    private $isLoggin;

    private $logs;

    private $level;

    public function getIsLogging(){
        return $this->isLoggin;
    }

    public function setIsLogging($value){
        $this->isLoggin = $value;
    }

    public function  __construct($level) {
        $this->isLoggin = true;
        $this->level = $level;
        $this->logs = array();
    }

    public function log($level, $msg, $file = false, $line = false){
        if (!$this->isLoggin){
            return;
        }
        if ($this->level > $level){
            return;
        }
        if ($file === false || $line === false){
            $trace = debug_backtrace();
            $file = $trace[0]['file'];
            $line = $trace[0]['line'];
        }
        $val = array();
        $val[self::FILE] = basename($file);
        $val[self::LINE] = $line;
        $val[self::LEVEL] = $level;
        $val[self::MSG] = $msg;
        array_push($this->logs, $val);
    }

    public function size(){
        return sizeof($this->logs);
    }

    public function getRawLogs(){
        return $this->logs;
    }

    /**
     *
     * @param int $grab used to filter all out except logs at level 'grab'
     * @return string html of the logs
     */
    public function getFormattedLogs_Html($level){
        $result = "<div style='background-color:#ffffff;'>\n";
        $count = 1;
        foreach($this->logs as $log){
            if ($level > $log[self::LEVEL]){
                continue;
            }
            $result .= "<div style='color:".
                            $this->levelToTagColor($log[self::LEVEL]).
                        ";'>$count [".$this->levelToTagString($log[self::LEVEL])."]".
                " <b>FILE:</b> ".$log[self::FILE].
                " <b>LINE:</b> ".$log[self::LINE].
                " <b>MSG:</b> ".$log[self::MSG]."</div>\n";
            $count++;
        }
        $result .= "\n</div>";
        return $result;
    }
    
    /**
     *
     * @param int $grab used to filter all out except logs at level 'grab'
     * @return string result of all logs
     */
    public function getFormattedLogs_String($level){
        $result = "";
        $count = 1;
        foreach($this->logs as $log){
            if ($level > $log[self::LEVEL]){
                continue;
            }
            $result .=
                    "$count [".$this->levelToTagString($log[self::LEVEL])."]".
                    " FILE: ".$log[self::FILE].
                    " LINE: ".$log[self::LINE].
                    " MSG: ".$log[self::MSG]."\n";
            $count++;
        }
        return $result."\n";
    }

    /**
     * 
     */
    public function pushToDatabase($level, $column, $table){
        $conn = FCore::GetDefaultConnection();
        $count = 1;
        foreach($this->logs as $log){
            if ($level > $log[self::LEVEL]){
                continue;
            }
            try {
                $insert = $conn->escape_string(
                        "$count [".$this->levelToTagString($log[self::LEVEL])."]".
                        " FILE: ".$log[self::FILE].
                        " LINE: ".$log[self::LINE].
                        " MSG: ".$log[self::MSG]);
                $conn->quick_query("INSERT INTO $table SET $column='$insert'");
                $count++;
            } catch(Exception $e){}
        }
        $conn->commit();
    }

    /**
     *
     * @param <int> $level
     * @return <string>
     */
    public function levelToTagString($level){
        switch($level){
            case self::LEVEL_TRACE:
                return 'TRACE';
            case self::LEVEL_DEBUG:
                return 'DEBUG';
            case self::LEVEL_INFO:
                return 'INFO';
            case self::LEVEL_WARN:
                return 'WARN';
            case self::LEVEL_ERROR:
                return 'ERROR';
            case self::LEVEL_FATAL:
                return 'FATAL';
        }
        return '000000';
    }
    /**
     *
     * @param <int> $level
     * @return <string>
     */
    public function levelToTagColor($level){
        switch($level){
            case self::LEVEL_TRACE:
                return self::LEVEL_TRACE_COLOR;
            case self::LEVEL_DEBUG:
                return self::LEVEL_DEBUG_COLOR;
            case self::LEVEL_INFO:
                return self::LEVEL_INFO_COLOR;
            case self::LEVEL_WARN:
                return self::LEVEL_WARN_COLOR;
            case self::LEVEL_ERROR:
                return self::LEVEL_ERROR_COLOR;
            case self::LEVEL_FATAL:
                return self::LEVEL_FATAL_COLOR;
        }
        return '000000';
    }

}




?>