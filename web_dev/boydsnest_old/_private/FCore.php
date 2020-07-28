<?php

// <editor-fold defaultstate="collapsed" desc="FCore Definitions">
define("RESULTS", 'results');
define("ACTION", 'action');
define("ACTION_UPDATE", 1);
define("ACTION_CREATE", 2);
define("ACTION_PASS", 3);
define("ACTION_RIGHTS", 4);
define("ACTION_DELETE", 5);
define("ACTION_VIEW", 6);
define("ACTION_UNKNOWN", 7);
define("ACTION_GO", 'go');
define("ACTION_TRIGGERAJAX", 'triggerajax');

function echoln($str){
    echo $str."\n";
}

/*
 *
 * 
 */
class _T {
    const STRING = 'string';
    const NUMBER = 'numeric';
    const BOOL = 'bool';
    const BOOLEAN = 'boolean';
    const INT = 'int';
    const INTEGER = 'integer';
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="FCore Errors">

// general
class FCoreException extends Exception {
    public function  __construct($message) {
        parent::__construct($message);
    }
}
// general DBConnect Exception
class DBConnectErrorException extends FCoreException {
    public function  __construct($message) {
        parent::__construct($message);
    }
}
// invalid param
class InvalidParamException extends FCoreException {
    public function  __construct($arg, $exp) {
        parent::__construct("Invalid Param: Expected Arg[$arg] To Be $exp");
    }
}
// invalid value
class InvalidValueException extends FCoreException {
    public function  __construct($arg, $msg = null) {
        parent::__construct("Invalid Param: Unexpected Value With Arg[$arg].. ".($msg!=null ? $msg : ""));
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="FCore Interfaces">
interface _state {
    function trigger($data = null);
}
interface IComparable {

    public function isGreaterThan(IComparable $node);

    public function isEqualTo(IComparable $node);

    public function getNode();

}
interface IThreadedForum {

    public function getChildOf();
    public function getID();

    public function isGreaterThan(IThreadedForum $node);
    public function getGreaterCompare();

    public function getSuper();

}
interface IListable {

    public function getIndent();

    public function getNode();

}
interface IViewable {
    
    public function printView();

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="abstract class _stateMachine">
abstract class _stateMachine {
    protected $_CurrState = null;
    abstract function changeState($state);
    public function trigger($data = null){
        if ($this->_CurrState != null)
            $this->_CurrState->trigger($data);
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class FCoreTimer">
class FCoreTimer {
    private $times = array();
    private $_mark = 0;
    public function  __construct() {
        ;
    }
    public function start(){
        $this->_mark = microtime();
    }
    public function mark($name){
        $this->times[$name] = microtime() - $this->_mark;
    }
    public function getTime($name){
        return $this->times[$name];
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class FCoreMemory">
class FCoreMemory {
    private $memory = array();
    public function  __construct() {
        ;
    }
    public function mark(){
        array_push($this->memory, memory_get_usage());
    }
    public function getArray(){
        return $this->memory;
    }
    public function  __toString() {
        $returning = "";
        $count = sizeof($this->memory);
        for($i=0; $i<$count; $i++){
            $returning .= "$i: ".$this->memory[$i].' ';
        }
        return $returning;
    }
    public function getIndex(int $index){
        return $this->memory[$index];
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class fArray">
class fArray {
    
    private $arr;

    public function  __construct() {
        throw new FCoreException("Not Implemented Yet");
        $this->arr = array();
    }

    private function fix(){
        $temp = $this->arr;
        $tempCount = sizeof($temp);
        $this->arr = array();
        $count = 0;
        for($i=0; $i<$tempCount; $i++){
            if ($temp[$i] != null){
                $this->arr[$count] = $temp[$i];
                $count++;
            }
        }
    }

    public function size(){
        return sizeof($this->arr);
    }

    public function swap($i1, $i2){
        $temp = $this->arr[$i1];
        $this->arr[$i1] = $this->arr[$i2];
        $this->arr[$i2] = $temp;
    }
    public function add($node, $index = -1){
        if (0<=$index && $index<sizeof($this->arr)){
            $begin = array_slice($this->arr, 0, $index);
            $begin[] = $node;
            $end = array_slice($this->arr, $index);
            $this->arr = array_merge($begin, $end);
            $this->fix();
        } else {
            array_push($this->arr, $node);
        }
    }
    public function remove($index){
        if (0<=$index && $index<sizeof($this->arr)){
            $this->arr[$index] = null;
            $this->fix();
        } else {
            throw new FCoreException("Out Of Bounds");
        }
    }
    public function get($index){
        if (0<=$index && $index<sizeof($this->arr)){
            return $this->arr[$index];
        } else {
            throw new FCoreException("Out Of Bounds");
        }
    }
    public function set($index, $node){
        if (0<=$index && $index<sizeof($this->arr)){
            $this->arr[$index] = $node;
        } else {
            throw new FCoreException("Out Of Bounds");
        }
    }
    public function indexOf($node){
        $count = sizeof($this->arr);
        for($i=0; $i<$count; $i++){
            if ($this->arr[$i] == $node){
                return $i;
            }
        }
        return -1;
    }

    public function push($node){
        array_push($this->arr, $node);
    }
    public function pushFront($node){
        array_unshift($this->arr, $node);
    }
    public function pop(){ 
        return array_pop($this->arr);
    }
    public function popFirst(){
        return array_shift($this->arr);
    }

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _Array">
final class _Array {
    /**
     *
     * @param array $working the array that will have values swapped
     * @param <type> $field1 the first key to be swapped
     * @param <type> $field2 the second key to be swapped
     */
    public static final function swap(array &$arr, $field1, $field2){
        $temp = $arr[$field1];
        $arr[$field1] = $arr[$field2];
        $arr[$field2] = $temp;
    }
    /**
     *
     * @param array $arr
     * @param <type> $node
     * @param <type> $index
     */
    public static final function insert(array &$arr, $node, $index){
        $begin = array_slice($arr, 0, $index);
        $begin[] = $node;
        $end = array_slice($arr, $index);
        $arr = array_merge($begin, $end);
    }
    /**
     *
     * @param array $arr
     * @param <type> $index
     * @return <type> 
     */
    public static final function remove(array &$arr, $index){
        if ($index == 0){ $arr = array_slice($arr, 1); return; }
        $begin = array_slice($arr, 0, $index-1);
        $end = array_slice($arr, $index);
        $arr = array_merge($begin, $end);
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _FILE">
final class _FILE {
    public static final function MakeDirectory($path, $mode = 0755){
        $path = rtrim(preg_replace(array("/\\\\/", "/\/{2,}/"), "/", $path), "/");
        $e = explode("/", ltrim($path, "/"));
        if(substr($path, 0, 1) == "/") {
            $e[0] = "/".$e[0];
        }
        $c = count($e);
        $cp = $e[0];
        for($i = 1; $i < $c; $i++) {
            if(!is_dir($cp) && !@mkdir($cp, $mode)) {
                return false;
            }
            $cp .= "/".$e[$i];
        }
        return @mkdir($path, $mode);
    }
    public static final function DeleteDirectory($path){

    }
    public static final function IsDirectory($path){

    }
    public static final function CopyFile($from){

    }
    public static final function SaveFile($path){
        
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _S">
final class _S {
    public static final function _RESET(){
        $_SESSION = array();
    }
    public static final function _SET($name, $value){
        $_SESSION[$name] = $value;
    }
    public static final function _GET($name){
        return $_SESSION[$name];
    }
    public static final function _UNSET($name){
        unset($_SESSION[$name]);
    }
    /**
     * Returns true if session is not suspect to hijack. If
     * @return <boolean>
     */
    public static final function CheckSessionHijack(){
        if (isset($_SESSION['HTTP_USER_AGENT'])){
            return $_SESSION['HTTP_USER_AGENT'] == md5($_SERVER['HTTP_USER_AGENT']);
        }
        $_SESSION['HTTP_USER_AGENT'] = md5($_SERVER['HTTP_USER_AGENT']);
        return true;
    }
    /**
     * checks the login password, returns false if failed. sets $_SESSION['validated']
     * if successful
     * @param <string> $passAttempt
     * @param <string> $salt
     * @param <string> $password
     * @return <boolean>
     */
    public static final function CheckLoginAttempt($passAttempt, $salt, $password){
        if ($password != _FCORE::GetSecondOrderHash($passAttempt, $salt)){
            return false;
        }
        $_SESSION['validated'] = 1;
        return true;
    }
    /**
     * checks if validated is set from CheckLoginAttempt()
     * @return <boolean>
     */
    public static final function IsLoggedIn(){
        return isset($_SESSION['validated']);
    }
    /**
     * unsets $_SESSION['validated'] to log out
     */
    public static final function InvalidateLogin(){
        unset($_SESSION['validated']);
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _FCORE">
final class _FCORE {

    public static final function JavaScriptInclude($url){
        return "
<script type='text/javascript' src='$url'></script>";
    }
    public static final function CSSInclude($url){
        return "
<LINK href='$url' rel='stylesheet' type='text/css' />";
    }
    public static final function Redirect($url){
        header("Location: $url");
    }
    public static final function ToggleReadyValue($str){
        if($str[0]=='c' && $str[1]=='_'){
            $count = strlen($str);
            $returning = "";
            for($i=2; $i<$count; $i++){
                $returning[$i-2] = $str[$i];
            }
            return $returning;
        } else {
            return "c_".$str;
        }
    }
    public static final function IsSetPOST($var){
        if (isset($_POST[$var])){
            return $_POST[$var];
        }
        return null;
    }
    public static final function IsSetGET($var){
        if (isset($_GET[$var])){
            return $_GET[$var];
        }
        return null;
    }
    public static final function IsSetGetDefault($var, $default){
        if (isset($_GET[$var])){
            return $_GET[$var];
        }
        return $default;
    }
    public static final function IsSetPostDefault($var, $default){
        if (isset($_POST[$var])){
            return $_POST[$var];
        }
        return $default;
    }
    /**
     * Returns true if fingerprint is correct.
     * @param <type> $check
     * @param <type> $key
     */
    public static final function CheckFingerPrint($key){
        if (isset($_SESSION['fingerprint'])){
            return $_SESSION['fingerprint'] == md5($key);
        }
        return false;
    }
    /**
     * Sets a fingerpint for CheckFingerPrint() to check against
     * @param <type> $key
     */
    public static final function SetFingerPrint($key){
        $_SESSION['fingerprint'] = md5($key);
    }

    /**
     * takes a key and salt and double sha1's it
     * @param <type> $key
     * @param <type> $salt
     * @return <type>
     */
    public static final function GetSecondOrderHash($key, $salt){
        return sha1($salt.sha1($key));
    }
    /**
     * returns a new random salt
     * @return <type>
     */
    public static final function GetNewSalt(){
        return substr(md5(uniqid(rand(), true)), 0, 3);
    }
    /**
     * validates $value as a number and to be in a certian range
     * @param <int> $value variable to be checked
     * @param <int> $min [optional] min allowed value
     * @param <int> $max [optional] max allowed value
     * @return <boolean>
     */
    public static final function ValidateNumeric($value , $min = 'none' , $max = 'none'){
        if(!is_numeric($value))
            return false;
        if(is_numeric($min) && $min > $value)
            return false;
        if(is_numeric($max) && $max < $value)
            return false;
        return true;
    }
    /**
     * validates $address as a common email address
     * @param <string> $address string to be checked
     * @return <int>
     */
    public static final function ValidateEmailAddress($address){
        return preg_match( "/^([a-zA-Z0-9])+([a-zA-Z0-9\.\\+=_-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\._-]+)+$/", $address);
    }
    /**
     * validates $user as a username
     * @param <type> $user string to be checked as valid or not
     * @param <type> $minLength min allowed length of $user
     * @param <type> $maxLength max allowed length of $user
     * @return <type> 
     */
    public static final function ValidateUsername($user , $minLength, $maxLength){
        return !((eregi('[^A-Za-z0-9]',$user)>0) || (strlen($user)<$minLength) || (strlen($user)>$maxLength));
    }
    
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _FORM">
class _FORM {
    /**
     * <form action='$url' enctype='$enctype' name='$name' method='$method' id='$id'>
     * @param <type> $name
     * @param <type> $url
     * @param <type> $method
     * @param <type> $id
     * @param <type> $enctype
     * @return <string>
     */
    public static final function _FormBegin($name, $url, $method="POST", $id="", $enctype='application/x-www-form-urlencoded'){
        return "<form action='$url' enctype='$enctype' name='$name' method='$method' id='$id'>";
    }
    /**
     * </form>
     * @return <string>
     */
    public static final function _FormEnd(){
        return "</form>";
    }
    /**
     * <input type='text' name='$name' value='$value' size='$size' maxlength='$maxsize' id='$id' />
     * @param <type> $name
     * @param <type> $value
     * @param <type> $size
     * @param <type> $maxsize
     * @param <type> $id
     * @return <string>
     */
    public static final function Text($name, $value="", $size=15, $maxsize=15, $id=""){
        return "<input type='text' name='$name' value='$value' size='$size' maxlength='$maxsize' id='$id' />";
    }
    /**
     * <input type='checkbox' name='$name' value='$value' id='$id'".($checked?" checked='checked'":"")." />
     * @param <type> $name
     * @param <type> $value
     * @param <type> $checked
     * @param <type> $id
     * @return <string>
     */
    public static final function CheckBox($name, $value, $checked=false, $id=""){
        return "<input type='checkbox' name='$name' value='$value' id='$id'".($checked?" checked='checked'":"")." />";
    }
    /**
     * <input type='radio' name='$name' value='$value' id='$id'".($selected?" selected='selected'":"")." />
     * @param <type> $name
     * @param <type> $value
     * @param <type> $selected
     * @param <type> $id
     * @return <string>
     */
    public static final function Radio($name, $value, $selected=false, $id=""){
        return "<input type='radio' name='$name' value='$value' id='$id'".($selected?" selected='selected'":"")." />";
    }
    /**
     * <input type='button' value='$value' id='$id' />
     * @param <type> $value
     * @param <type> $id
     * @return <string>
     */
    public static final function Button($value, $id){
        return "<input type='button' value='$value' id='$id' />";
    }
    /**
     * <input type='image' src='$src' name='$name' alt='$alt' id='$id' />
     * @param <type> $src
     * @param <type> $name
     * @param <type> $alt
     * @param <type> $id
     * @return <string>
     */
    public static final function Image($src, $name, $alt, $id){
        return "<input type='image' src='$src' name='$name' alt='$alt' id='$id' />";
    }
    /**
     * <input type='hidden' name='$name' value='$value' id='$id' />
     * @param <type> $name
     * @param <type> $value
     * @param <type> $id
     * @return <string>
     */
    public static final function Hidden($name, $value, $id=""){
        return "<input type='hidden' name='$name' value='$value' id='$id' />";
    }
    /**
     * <input type='password' name='$name' id='$id' />
     * @param <type> $name
     * @param <type> $id
     * @return <string>
     */
    public static final function Password($name, $id=""){
        return "<input type='password' name='$name' id='$id' />";
    }
    /**
     * <input type='file' name='$name' />
     * @param <type> $name
     * @return <string>
     */
    public static final function File($name){
        return "<input type='file' name='$name' />";
    }
    /**
     * <input type='submit' value='$value' name='$name' />
     * @param <type> $value
     * @param <type> $name
     * @return <string>
     */
    public static final function Submit($value="Submit", $name="submitvalue"){
        return "<input type='submit' value='$value' name='$name' />";
    }
    /**
     * <input type='reset' value='$value' />
     * @param <type> $value
     * @return <string>
     */
    public static final function Reset($value){
        return "<input type='reset' value='$value' />";
    }
    /**
     * <textarea name='$name' cols='$cols' rows='$rows'>$value</textarea>
     * @param <type> $name
     * @param <type> $cols
     * @param <type> $rows
     * @param <type> $value
     * @return <string>
     */
    public static final function TextArea($name, $cols, $rows, $value){
        return "<textarea name='$name' cols='$cols' rows='$rows'>$value</textarea>";
    }
    /**
     * <label for='$for'>$text</label>
     * @param <type> $text
     * @param <type> $for
     * @return <string>
     */
    public static final function Label($text, $for){
        return "<label for='$for'>$text</label>";
    }
    /**
     * <select id='$id' name='$name' size='$size'".($multiple?" multiple='multiple'":"").">
     * @param <type> $name
     * @param <type> $id
     * @param <type> $size
     * @param <type> $multiple
     * @return <string>
     */
    public static final function SelectBegin($name, $id="", $size=1, $multiple=false){
        return "<select id='$id' name='$name' size='$size'".($multiple?" multiple='multiple'":"").">";
    }
    /**
     * </select>
     * @return <string>
     */
    public static final function SelectEnd(){
        return "</select>";
    }
    /**
     * <option value='$value' id='$id'".($selected?" selected='selected'":"").">$text</option>
     * @param <type> $text
     * @param <type> $value
     * @param <type> $selected
     * @param <type> $id
     * @return <string>
     */
    public static final function Option($text, $value, $selected=false, $id="", $onclick=""){
        return "<option value='$value' onclick='$onclick' id='$id'".($selected?" selected='selected'":"").">$text</option>";
    }
    /**
     * <optgroup label='$label'>
     * @param <type> $label
     * @return <string>
     */
    public static final function OptionGroupBegin($label){
        return "<optgroup label='$label'>";
    }
    /**
     * </optgroup>
     * @return <string>
     */
    public static final function OptionGroupEnd(){
        return "</optgroup>";
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _FORUM">
class _FORUM {

    private $forum;

    private $lost;

    public function __construct() {
        $this->forum = array();
        $this->lost = array();
    }

    /**
     *
     * @param <int> $index
     * @return <IThreadedForum>
     */
    public function getNode($index){
        return 0 <= $index && $index < sizeof($this->forum) ?
            $this->forum[$index]->getNode() :
            false;
    }
    /**
     *
     * @param <int> $index
     * @return <int>
     */
    public function getIndent($index){
        return 0 <= $index && $index < sizeof($this->forum) ?
            $this->forum[$index]->getIndent() :
            false;
    }

    public function getNodeCount(){
        return sizeof($this->forum);
    }

    public function isSomeLost(){
        return sizeof($this->lost) != 0;
    }

    public function placeLost(){
        $found = true;
        while($found){
            $found = false;
            $count = sizeof($this->lost);
            for($i=0; $i<$count && !$found; $i++){
                if ($this->placeNode($this->lost[$i], false)){
                    _Array::remove($this->lost, $i);
                    $found = true;
                }
            }
        }
    }

    public function placeNodeAtBottom(IThreadedForum $tf){
        array_push($this->forum, $tf);
    }

    public function placeNodeArray(array &$nodes){
        $count = sizeof($nodes);
        for($i=0; $i<$count; $i++){
            $this->placeNode($nodes[$i]);
        }
    }

    public function placeNode(IThreadedForum $tf, $saveLost = true){
        $inserted = false;
        $count = sizeof($this->forum);
        if ($tf->getChildOf() == 0){
            $i=0;
            for( ; $i<$count && !$inserted; $i++){
                $thisNode = $this->forum[$i]->getNode();
                if ($thisNode->getChildOf() == 0){
                    if ($thisNode->isGreaterThan($tf)){
                        _Array::insert($this->forum, new ForumNode($tf, 0), $i);
                        $inserted = true;
                    }
                }
            }
            if (!$inserted){
                _Array::insert($this->forum, new ForumNode($tf, 0), $i);
                $inserted = true;
            }
        } else {
            $parentFound = false;
            $indent = 0;
            $i=0;
            for( ; $i<$count && !$inserted; $i++){
                $thisNode = $this->forum[$i]->getNode();
                if (!$parentFound){
                    if ($tf->getChildOf() == $thisNode->getID()){
                        $parentFound = true;
                        $indent = $this->forum[$i]->getIndent();
                    }
                } else {
                    if ($thisNode->getChildOf() == $tf->getChildOf()){
                        $foundBro = true;
                        if (!$tf->isGreaterThan($thisNode)){
                            _Array::insert($this->forum, new ForumNode($tf, $indent+1), $i);
                            $inserted = true;
                        }
                    } else {
                        _Array::insert($this->forum, new ForumNode($tf, $indent+1), $i);
                        $inserted = true;
                    }
                }
            }
            if ($parentFound && !$inserted){
                _Array::insert($this->forum, new ForumNode($tf, $indent+1), $i);
                $inserted = true;
            }
        }
        if (!$inserted && $saveLost){
            array_push($this->lost, $tf);
        }
        return $inserted;
    }

}
class ForumNode {
    private $node;
    private $indent;

    public function getNode(){ return $this->node; }
    public function getIndent(){ return $this->indent; }
    
    public function  __construct(IThreadedForum $node, $indent) {
        $this->node = $node;
        $this->indent = $indent;
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _HTML">
class _HTML {
    const INDENT_0 = "
";
    const INDENT_1 = "
    ";
    const INDENT_2 = "
        ";
    const INDENT_3 = "
            ";
    const INDENT_4 = "
                ";
    const I_0 = "
";
    const I_1 = "
    ";
    const I_2 = "
        ";
    const I_3 = "
            ";
    const I_4 = "
                ";
    const I_5 = "
                    ";
    const I_6 = "
                        ";
    const BR = "<br />";
    const BR_N = "<br />\n";

    public static final function Comment($note){
        return "<!-- $note -->";
    }

    public static final function DIV_B($id){
        return "<div id=\"$id\">";
    }

    const DIV_E = "</div>";

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="final class _CSS">
class _CSS {
    public static final function FloatLeftWidth($width){
        return "float:left;width:".$width."px;overflow:hidden;";
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class FCoreTracer">
global $g_Logger;
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <int> $level
 */
function logger_Initiate($level){
    global $g_Logger;
    $g_Logger = new FCoreTracer($level);
}
/**
 *
 * @global FCoreTracer $g_Logger
 */
function logger_Finished(){
    global $g_Logger;
    $g_Logger = null;
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @return <string>
 */
function logger_GetHTML(){
    global $g_Logger;
    if ($g_Logger){
        return $g_Logger->getFormattedLogs_HTML();
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <int> $toGrab
 * @return <string>
 */
function logger_GetFilteredHTML($toGrab){
    global $g_Logger;
    if ($g_Logger){
        return $g_Logger->getFormattedLogs_HTML($toGrab);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @return <string>
 */
function logger_GetString(){
    global $g_Logger;
    if ($g_Logger){
        return $g_Logger->getFormattedLogs_String();
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <int> $toGrab
 * @return <string>
 */
function logger_GetFilteredString($toGrab){
    global $g_Logger;
    if ($g_Logger){
        return $g_Logger->getFormattedLogs_String($toGrab);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <int> $level
 * @param <string> $msg
 */
function logger_MSG($file, $line, $level, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, $level, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param Exception $e
 */
function logger_Exception(Exception $e){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($e->getFile(), $e->getLine(), FCoreTracer::LEVEL_ERROR, $e->getMessage());
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $func
 */
function logger_FuncCall($file, $line, $func){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_DEBUG, "$func called");
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Trace($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_TRACE, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Debug($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_DEBUG, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Info($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_INFO, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Warn($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_WARN, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Error($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_ERROR, $msg);
    }
}
/**
 *
 * @global FCoreTracer $g_Logger
 * @param <string> $file
 * @param <int> $line
 * @param <string> $msg
 */
function logger_Fatal($file, $line, $msg){
    global $g_Logger;
    if ($g_Logger){
        $g_Logger->pushLog($file, $line, FCoreTracer::LEVEL_FATAL, $msg);
    }
}
class FCoreTracer {
    const LINE = "line";
    const FILE = "file";
    const MSG = "msg";
    const LEVEL = "level";

    const LEVEL_TRACE = 1;
    const LEVEL_DEBUG = 2;
    const LEVEL_INFO = 3;
    const LEVEL_WARN = 4;
    const LEVEL_ERROR = 5;
    const LEVEL_FATAL = 6;
    const LEVEL_TRACE_COLOR = "#a0a000";
    const LEVEL_DEBUG_COLOR = "#64c864";
    const LEVEL_INFO_COLOR = "#afaf00";
    const LEVEL_WARN_COLOR = "#0000ff";
    const LEVEL_ERROR_COLOR = "#ff8c00";
    const LEVEL_FATAL_COLOR = "#ff0000";

    private $stack = array();
    private $level;
    public function convFile($file){
        $temp = explode("\\", $file);
        $tempCount = sizeof($temp);
        if (!isset($temp[$tempCount-1])){ $temp[$tempCount-1] = " "; }
        if (!isset($temp[$tempCount-2])){ $temp[$tempCount-2] = " "; }
        if (!isset($temp[$tempCount-3])){ $temp[$tempCount-3] = " "; }
        return $temp[$tempCount-3]."/".$temp[$tempCount-2]."/".$temp[$tempCount-1];
    }

    public function __construct($level){
        $this->level = $level;
        set_exception_handler("FCoreTracerExceptionHandler");
        set_error_handler("FCoreTracerErrorHandler", -1);
        register_shutdown_function("FCoreTracerOnExit");
    }
    public function __destruct() {
        // restore_error_handler();
        // restore_exception_handler();
    }
    public function getLevel(){
        return $this->level;
    }
    public function setLevel($value){
        $this->level = $value;
    }
    public function numOfLogs(){
        return sizeof($this->stack);
    }
    public function getLogs(){
        return $this->stack;
    }
    public function resetLogs(){
        $this->stack = array();
    }
    /**
     *
     * @param int $grab used to filter all out except logs at level 'grab'
     * @return string html of the logs
     */
    public function getFormattedLogs_HTML($grab = 0){
        $result = "
	<div style='background-color:#ffffff;'>";
        $amount = $this->numOfLogs();
        for ($i=0; $i<$amount; $i++){
            if ($grab){
                if ($grab != $this->stack[$i][self::LEVEL]){
                    continue;
                }
            }
            $result .=
                "
                <div style='color:".$this->levelToTagColor($this->stack[$i][self::LEVEL]).
                        ";'>$i [".$this->levelToTagString($this->stack[$i][self::LEVEL])."]".
                " <b>FILE:</b> ".$this->stack[$i][self::FILE].
                " <b>LINE:</b> ".$this->stack[$i][self::LINE].
                " <b>MSG:</b> ".$this->stack[$i][self::MSG]."</div>";
        }
        $result .= "
        </div>";
        return $result;
    }
    /**
     *
     * @param int $grab used to filter all out except logs at level 'grab'
     * @return string result of all logs
     */
    public function getFormattedLogs_String($grab = 0){
        $first = true;
        $amount = $this->numOfLogs();
        $result = "";
        for ($i=0; $i<$amount; $i++){
            if ($grab){
                if ($grab != $this->stack[$i][self::LEVEL]){
                    continue;
                }
            }
            if ($first){
                $first = false;
            } else {
                $result .= "\n";
            }
            $result .=
                    "$i [".$this->levelToTagString($this->stack[$i][self::LEVEL])."]".
                    " <b>FILE:</b> ".$this->stack[$i][self::FILE].
                    " <b>LINE:</b> ".$this->stack[$i][self::LINE].
                    " <b>MSG:</b> ".$this->stack[$i][self::MSG];
        }
        return $result."\n";
    }
    /**
     * generic log
     * @param string $file file the msg applies to
     * @param string $line line the msg applies to
     * @param int $level the level of importance
     * @param string $msg msg to be logged
     */
    public function pushLog($file, $line, $level, $msg = null){
        if ($this->level >= $level){
            return;
        }
        $thisPush = array();
        $thisPush[self::LINE] = $line;
        $thisPush[self::FILE] = $this->convFile($file);
        $thisPush[self::LEVEL] = $level;
        $thisPush[self::MSG] = $msg;
        array_push($this->stack, $thisPush);
    }
    /**
     *
     * @param <int> $level
     * @return <string>
     */
    public function levelToTagString($level){
        switch($level){
            case self::LEVEL_TRACE:
                return '[TRACE]';
            case self::LEVEL_DEBUG:
                return '[DEBUG]';
            case self::LEVEL_INFO:
                return '[INFO]';
            case self::LEVEL_WARN:
                return '[WARN]';
            case self::LEVEL_ERROR:
                return '[ERROR]';
            case self::LEVEL_FATAL:
                return '[FATAL]';
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
function FCoreTracerExceptionHandler(Exception $e){
    global $g_Logger;
    if (_D::DEBUG){
        if ($g_Logger){
            echo $g_Logger->getFormattedLogs_HTML();
        }
        echo "UNHANDLED EXCEPTION:<br />\n";
        echo "LINE: ".$e->getLine()."<br />\n";
        echo "FILE: ".$e->getFile()."<br />\n";
        echo "MESSAGE: ".$e->getMessage()."<br />\n";
        echo "STACK TRACE: ".$e->getTraceAsString();
        exit(1);
    } else {
        $message = "";
        if ($g_Logger){
            $message .= $g_Logger->getFormattedLogs_HTML();
        }
        $message .= "UNHANDLED EXCEPTION:<br />\n";
        $message .= "LINE: ".$e->getLine()."<br />\n";
        $message .= "FILE: ".$e->getFile()."<br />\n";
        $message .= "MESSAGE: ".$e->getMessage()."<br />\n";
        $message .= "STACK TRACE: ".$e->getTraceAsString();
        if (!mail(_D::WEBMASTER, "Boyds Nest Fatal Error", $message, "From: system@invalidresponse.com")){
            echo "Error And Failed To Send Message";
        }
        _FCORE::Redirect(_D::REDIRECT_UNKNOWNERROR);
    }
}
function FCoreTracerErrorHandler($errno, $errstr, $errfile, $errline){
    if (!(error_reporting() & $errno)){
        return;
    }
    switch ($errno) {
        case E_ERROR:
        case E_CORE_ERROR:
        case E_USER_ERROR:
        case E_COMPILE_ERROR:
            if (_D::DEBUG){
                global $g_Logger;
                if ($g_Logger){
                    echo $g_Logger->getFormattedLogs_HTML()."<br />\n";
                }
                echo "Fatal Error -- Dumping Log:<br />\n";
                echo "A FATAL ERROR OCCURED:<br />\n";
                echo "ERROR($errno): '$errstr'<br />\n";
                echo "In File $errfile At Line $errline\n";
                exit(1);
            } else {
                $message = "";
                global $g_Logger;
                if ($g_Logger){
                    $message .= $g_Logger->getFormattedLogs_HTML()."<br />\n";
                }
                $message .= "Fatal Error -- Dumping Log:<br />\n";
                $message .= "A FATAL ERROR OCCURED:<br />\n";
                $message .= "ERROR($errno): '$errstr'<br />\n";
                $message .= "In File $errfile At Line $errline\n";
                if (!mail(_D::WEBMASTER, "Boyds Nest Fatal Error", $message, "From: system@invalidresponse.com")){
                    echo "Error And Failed To Send Message";
                }
                _FCORE::Redirect(_D::REDIRECT_UNKNOWNERROR);
            }
            break;
        case E_RECOVERABLE_ERROR:
        case E_PARSE:
            global $g_Logger;
            if (!$g_Logger){ return true; }
            $g_Logger->pushLog($g_Logger->convFile($errfile), $errline, FCoreTracer::LEVEL_ERROR, $errstr);
            break;
        case E_WARNING:
        case E_CORE_WARNING:
        case E_DEPRECATED:
        case E_USER_DEPRECATED:
        case E_COMPILE_WARNING:
        case E_USER_DEPRECATED:
        case E_USER_WARNING:
            global $g_Logger;
            if (!$g_Logger){ return true; }
            $g_Logger->pushLog($g_Logger->convFile($errfile), $errline, FCoreTracer::LEVEL_WARN, $errstr);
            break;
        case E_STRICT:
        case E_USER_NOTICE:
        case E_NOTICE:
            global $g_Logger;
            if (!$g_Logger){ return true; }
            $g_Logger->pushLog($g_Logger->convFile($errfile), $errline, FCoreTracer::LEVEL_INFO, $errstr);
            break;
        default:
            return false;
    }
    return true;
}
function FCoreTracerOnExit(){
    global $g_Logger;
    if (!$g_Logger){
        return;
    }
    echo $g_Logger->getFormattedLogs_HTML();
    $g_Logger->resetLogs();
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class DBConnect">
class DBConnect {
    /**
     * @var <string>
     */
    private $db;
    /**
     * @var <mysqli>
     */
    private $conn;
    /**
     * @return <mysqli> current DB connection
     */
    public function getConnection(){
        return $this->conn;
    }
    /**
     * @return <boolean> true if a connection to DB exists
     */
    public function isConnected(){
        return $this->conn != null;
    }
    /**
     *
     * @param String $server address to the server
     * @param String $user the username used to connect to the DB
     * @param String $pass the password used to connect to the DB
     * @param String $db the DB to use in the server
     */
    public function connect($server, $user, $pass, $db) {
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        $this->db = $db;
        $this->conn = new mysqli($server, $user, $pass, $db);
    }
    /**
     * closes the current connection to the DB
     */
    public function disconnect() {
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        try {
            if ($this->conn != null){
                $this->conn->close();
                $this->conn = null;
            }
        } catch (Exception $e) { }
    }

    public function  __construct() {
    }
    /**
     * destroys the object always calls disconnect()
     */
    public function  __destruct() {
        $this->disconnect();
    }
    /**
     *
     * @param String $query the query to be made
     * @param boolean $makeArray false if you want the raw result returned, true if you want an array of the data
     * @return <mixed> sql result if makeArray==false, array of rows if makeArray==true
     */
    public function makeQuery($query, $makeArray = true){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($query) || !is_bool($makeArray)){
            throw new Exception("Invalid Param");
        }
        $result = $this->conn->query($query);
        if (!$result || $this->conn->error){
            throw new DBConnectErrorException($this->getError().": QUERY('$query')");
        }
        if (!$makeArray){
            return $result;
        }
        if ($result->num_rows == 0){
            return null;
        }
        $returning = array();
        try{
            for($i=0; $i<$result->num_rows; $i++){
                $returning[$i] = $result->fetch_assoc();
            }
        } catch (Exception $e) {
            logger_Exception($e);
            return $result;
        }
        $result->free();
        return $returning;
    }
    /**
     *
     * @param string $string string to be escaped
     * @return <string> escaped string
     */
    public function escapeString($string){
        if (!is_string($string)){
            throw new Exception("Invalid Param");
        }
        return $this->conn->real_escape_string($string);
    }
    /**
     * returns true if there is a sql error
     * @return <boolean> true if sql error
     */
    public function isError(){
        return $this->conn->error != null;
    }
    /**
     * gets the error that mysqli sent
     * @return <string> the sql error
     */
    public function getError(){
        return "Error(".$this->conn->errno."): ".$this->conn->error;
    }
    /**
     * returns true if there is warnings
     * @return <boolean> true if sql warnings
     */
    public function isWarning(){
        return $this->conn->warning_count != 0;
    }
    /**
     * returns a string of the warnings from mysqli
     * @return <array> array of string listing the sql warnings
     */
    public function getWarning(){
        $result = $this->conn->get_warnings();
        $count = sizeof($result);
        $returning = array();
        for ($i=0; $i<$count; $i++){
            $returning[$i] = "Warning $i: ".$result[$i]['errno'].": ".$result[$i]['message']."\n";
        }
        return $returning;
    }
    /**
     * gets a list of all the tables in the current DB
     * @return <array> array of strings listing all the table names
     */
    public function GetTableList(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        $result = _MYSQL::_MakeQuery("SHOW TABLES");
        if (!$result){
            return null;
        }
        $returning = array();
        $count = sizeof($result);
        for ($i=0; $i<$count; $i++){
            $returning[$i] = $result[$i]['Tables_in_'.$this->db];
        }
        return $returning;
    }
    /**
     *
     * @param string $str string the tables need to begin with
     * @return <array> array of strings with tables beginning with $str
     */
    public function GetTableListBeginningWith($str){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($str)){
            throw new Exception("Invalid Parameters");
        }
        $str = $this->escapeString($str);
        $tables = $this->GetTableList();
        if (!$tables){
            return null;
        }
        $retruning = array();
        $stringLen = strlen($str);
        $tableCount = sizeof($tables);
        $found = 0;
        for ($i=0; $i<$tableCount; $i++){
            $good = true;
            for ($j=0; $j<$stringLen && $good; $j++){
                if ($str[$j]!=$tables[$i][$j]){
                    $good = false;
                }
            }
            if ($good){
                $retruning[$found] = $tables[$i];
                $found++;
            }
        }
        return $returning;
    }

    /**
     * query: "SELECT * FROM $table".($where!=null ? " WHERE ".$where : "")
     * @param string $table table the record is being checked for
     * @param string $where the 'where' statement for the sql query (can be null)
     * @return <int> number of occurences the record exists
     */
    public function DoesRecordExist($table, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $result = $this->makeQuery("SELECT * FROM $table".($where!=null ? " WHERE ".$where : ""), false);
	return $result->num_rows ? $result->num_rows : false;
    }
    /**
     * query: "SHOW TABLES"
     * @param string $table name of the table to look for
     * @return <bool> true for success, false otherwise
     */
    public function DoesTableExist($table){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $result = _MYSQL::_MakeQuery("SHOW TABLES");
        if (!$result){
            return false;
        }
        $count = sizeof($result);
        for ($i=0; $i<$count; $i++){
            if ($result[$i]['Tables_in_'.$this->db]==$table){
                return true;
            }
        }
        return false;
    }
    /**
     * query: "SELECT $field FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT 1"
     * @param string $table table where the select statement is exicuted
     * @param string $field field that will be returned
     * @param string $where the WHERE statement in the call... if null, statement is left out
     * @return <mixed> null if no sql result, the field otherwise
     */
    public function SelectOneOfOneField($table, $field, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($field) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $field = $this->escapeString($field);
        $result = $this->makeQuery("SELECT $field FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT 1");
        if(!$result) {
            return null;
        }
        return $result[0][$field];
    }
    /**
     * query: "SELECT * FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT $start, $amount"
     * @param string $table table name the query is exicuted
     * @param int $start the first row selected
     * @param int $amount the amount of rows selected
     * @param string $where the WHERE statement in the sql command... can but null
     * @return <mixed> null if no results, array of rows otherwise
     */
    public function SelectAllFromRange($table, $start, $amount, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_numeric($start) || !is_numeric($amount) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("SELECT * FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT $start, $amount");
    }
    /**
     * query: "SELECT $field FROM $table".($where!=null ? " WHERE $where" : "")
     * @param string $field field to be selected
     * @param string $table name of the table the query is exicuted
     * @return <type> null if no results, array of rows otherwise
     */
    public function SelectAllOfOneField($field, $table, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($field) || !is_string($table)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $field = $this->escapeString($field);
        $result = $this->makeQuery("SELECT $field FROM $table".($where!=null ? " WHERE $where" : ""));
        if(!$result) {
            return null;
        }
        $returning = array();
        $count = sizeof($result);
        for($i=0; $i<$count; $i++){
            $returning[$i] = $result[$i][$field];
        }
        return $returning;
    }
    /**
     * query: "SELECT * FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT 1"
     * @param string $table name of the table the query is exicuted
     * @param string $where the WHERE statement in the sql select query
     * @return <type> null if no results, single row otherwise
     */
    public function SelectAllOfOneRecord($table, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $result = $this->makeQuery("SELECT * FROM $table".($where!=null ? " WHERE $where" : "")." LIMIT 1");
        if (!$result){
            return null;
        }
        return $result[0];
    }
    /**
     * query: "INSERT INTO $table SET $set"
     * @param string $table name of the table the query is exicuted
     * @param string $set the sets to be made in the query
     * @return <boolean> true if successfully inserted
     */
    public function Insert($table, $set){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($set)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("INSERT INTO $table SET $set", false);
    }
    /**
     * query: "INSERT INTO $table SET $set"
     * @param string $table name of the table the query is exicuted
     * @param string $set the sets to be made in the query
     * @return <int> the 'PRIMARY KEY' of the insert
     */
    public function InsertThenReturnID($table, $set){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($set)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $result = $this->makeQuery("INSERT INTO $table SET $set", false);
        if (!$result){
            return false;
        }
        $result = $this->makeQuery("SELECT LAST_INSERT_ID()");
        if (!$result){
            return false;
        }
        return $result[0]['LAST_INSERT_ID()'];
    }
    /**
     * query: "DELETE FROM $table".($where!=null ? " WHERE $where" : "")
     * @param string $table name of the table the query is exicuted
     * @param string $where the WHERE statement in the query, can be null..
     * @return <type> true if successfully deleted
     */
    public function Delete($table, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("DELETE FROM $table".($where!=null ? " WHERE $where" : ""), false);
    }
    /**
     * query: "UPDATE $table SET $set".($where!=null ? " WHERE $where" : "")
     * @param string $table name of the table the query is exicuted
     * @param string $set the SET statement for the updates
     * @param string $where the WHERE statement in the sql query, can be null
     * @return <boolean> true if successfully updated
     */
    public function Update($table, $set, $where = null){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($set) || (!is_string($where) && !is_null($where))){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("UPDATE $table SET $set".($where!=null ? " WHERE $where" : ""), false);
    }
    /**
     * query: "DROP TABLE IF EXISTS $table"
     * @param string $table name of the table to be dropped
     * @return <boolean> true if successfully dropped
     */
    public function TableDrop($table){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("DROP TABLE IF EXISTS $table", false);
    }
    /**
     * query: "CREATE TABLE $table ($decl) TYPE=$type"
     * @param string $name name of the new table
     * @param string $decl the declaration of the table columns
     * @param string $type the engine type
     * @return <boolean> true if successfully created
     */
    public function TableCreate($name, $decl, $type="INNODB"){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($name) || !is_string($decl) || !is_string($type)){
            throw new Exception("Invalid Param");
        }
        $name = $this->escapeString($name);
        $decl = $this->escapeString($decl);
        $type = $this->escapeString($type);
        if ($this->DoesTableExist($name)){
            return false;
        }
        return $this->makeQuery("CREATE TABLE $table ($decl) TYPE=$type", false);
    }
    /**
     * query: "ALTER TABLE $table ADD COLUMN $decl"
     * @param string $table tables a column is to be added on
     * @param string $decl declaration of the column
     * @return <boolean> true if successfully added
     */
    public function TableColumnAdd($table, $decl){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($decl)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $decl = $this->escapeString($decl);
        return $this->makeQuery("ALTER TABLE $table ADD COLUMN $decl", false);
    }
    /**
     * query: "ALTER TABLE $table DROP COLUMN $column"
     * @param string $table table a column is to be dropped from
     * @param string $column column to be dropped
     * @return <boolean> true if successfully dropped
     */
    public function TableColumnDrop($table, $column){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table) || !is_string($column)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        $column = $this->escapeString($column);
        return $this->makeQuery("ALTER TABLE $table DROP COLUMN $column", false);
    }

    /**
     * query: "TRUNCATE TABLE $table"
     * @param <string> $table name of the table being reset
     * @return <object>
     */
    public function ResetTable($table){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        if (!is_string($table)){
            throw new Exception("Invalid Param");
        }
        $table = $this->escapeString($table);
        return $this->makeQuery("TRUNCATE TABLE $table", false);
    }
}
// </editor-fold>

?>
