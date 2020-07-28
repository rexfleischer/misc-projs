<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
require_once DIR_DATABASE_DATABASE;
require_once DIR_HELPER_MENUANDCONTENTHELPER1;
require_once DIR_HELPER_THREADEDFORUMBUILDER;
require_once DIR_HELPER_THREADEDFORUMVIEWER;
require_once DIR_PAGE_ERRORPAGES;


// <editor-fold defaultstate="collapsed" desc="abstract class page_homeabstract">
abstract class page_homeabstract extends _baselayout implements _mch1, _state {
    public abstract function absoluteContent();
    public abstract function absoluteComments();
    public abstract function absoluteStyle();
    public abstract function absolutePreProcessing();

    public function menuContent1(){
        ?>

<div id="_menutarget"><?php
    $count = sizeof($this->crossInfo[_HOMEPAGE::FAMILYLIST]);
    for($i=0; $i<$count; $i++){
        $thisuser = $this->crossInfo[_HOMEPAGE::FAMILYLIST][$i];
        echo _HTML::I_1."<div id=\"_sn_usercontainer\" title=\"".$thisuser->getUserID()."\">";
        
        echo _HTML::I_2."<div id=\"_sn_username\">";
        echo _HTML::I_3.$thisuser->getUsername();
        echo _HTML::I_2."</div>";

        echo _HTML::I_2."<div id=\"_sn_foldercontainer\">";
        echo _HTML::I_2._HTML::DIV_E;
        
        echo _HTML::I_1._HTML::DIV_E;
    }
    ?>
</div>

<?php
    }
    public function menuContent2(){}
    public function pageContent(){
        echo _HTML::DIV_B("_pagecontenttarget");
        $this->absoluteContent();
        echo _HTML::DIV_E;
        echo _HTML::DIV_B("_pagecommenttarget");
        $this->absoluteComments();
        echo _HTML::DIV_E;
    }


    protected function thisPageLayout(){
        MenuAndContentHelper1_Layout($this, true, false, "", "");
    }
    protected function thisPageStyle(){
        MenuAndContentHelper1_Style();

        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/jQuery.js");
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT."/home.js");

        ?>

<script type="text/javascript">

<?php
if ($this->crossInfo[_HOMEPAGE::MAINVIEW_USERID]){
    echo "\$(function(){\n";
    echo "var folder = getUserFolder(".$this->crossInfo[_HOMEPAGE::MAINVIEW_USERID].");\n";
    echo "var currentNode;\n";
    $count = $this->crossInfo[_HOMEPAGE::MENULIST]->getNodeCount();
    for($i=0; $i<$count; $i++){
        $node = $this->crossInfo[_HOMEPAGE::MENULIST]->getNode($i);
        echo "currentNode = buildMenuButton(\"".
                $node->getPageID()."\", \"".
                $node->getTitle()."\", ".
                (($this->crossInfo[_HOMEPAGE::MENULIST]->getIndent($i)+1)*2).", ".
                ($node->getHasChildren() ? "true" : "null").");\n";
        echo "folder.append(currentNode);\n";
    }
    echo "});\n";
}

?>

</script>

<style type="text/css">
#_sn_usercontainer
{
    
}
#_sn_username
{
    
}
#_sn_foldercontainer
{
    
}
#_sn_folder
{

}
#_sn_expandbutton
{
    color: steelblue;
}
#_sn_pagetitle
{
    color: papayawhip;
}
</style>
<?php

        $this->absoluteStyle();
    }
    protected function thisPagePreProcessing(){
        $this->crossInfo[_HOMEPAGE::DATABASE] = false;
        $this->crossInfo[_HOMEPAGE::MENULIST] = _FCORE::IsSetPostDefault(_HOMEPAGE::MENULIST, false);
        if (!$this->crossInfo[_HOMEPAGE::MENULIST]){
            $this->crossInfo[_HOMEPAGE::DATABASE] = _DB::_Connect();
            $this->crossInfo[_HOMEPAGE::MAINVIEW_USERID] =
                    _SESSION::GetIsFamily() ? _SESSION::GetUserID() : false;

            $this->crossInfo[_HOMEPAGE::MENULIST] = array();
            if ($this->crossInfo[_HOMEPAGE::MAINVIEW_USERID]){
                 $raw = DB_PAGE::_GetUserRoots(
                        $this->crossInfo[_HOMEPAGE::DATABASE],
                        $this->crossInfo[_HOMEPAGE::MAINVIEW_USERID]);
                 $this->crossInfo[_HOMEPAGE::MENULIST] = new _FORUM();
                 $this->crossInfo[_HOMEPAGE::MENULIST]->placeNodeArray($raw);
            }

            $this->crossInfo[_HOMEPAGE::FAMILYLIST] = DB_USER::_GetFamilyOnlyUserList($this->crossInfo[_HOMEPAGE::DATABASE]);
        }
        $this->absolutePreProcessing();
    }
    public function trigger($data = null){
        $this->EchoBaseLayout();
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class homestate_mainview">
class homestate_mainview extends page_homeabstract{

    public function absoluteComments(){
        
    }

    public function absoluteContent(){
        
    }

    public function absoluteStyle(){
        
    }

    public function absolutePreProcessing(){
        
    }

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class page_home">
class page_home extends _stateMachine {

    public function changeState($state) {
        switch ($state) {
            case _HOMEPAGE::MAINVIEW:
                $this->_CurrState = new homestate_mainview();
                break;
            case ERROR_HTTP_MUSTBELOGGEDIN:
                $this->_CurrState = new error_MustBeLoggedIn();
                break;
            default:
                $this->_CurrState = new error_UnknownAction();
                break;
        }
    }

}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="Page Control">
$thispage = new page_home();
$state;
if (_S::IsLoggedIn()){
    $state = _HOMEPAGE::MAINVIEW;
} else {
    $state = ERROR_HTTP_MUSTBELOGGEDIN;
}
$thispage->changeState($state);
$thispage->trigger();
// </editor-fold>


?>
