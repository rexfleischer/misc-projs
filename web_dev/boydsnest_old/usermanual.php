<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
require_once DIR_DATABASE_DATABASE;
require_once DIR_FORM_USERMANUAL_WRITING;
require_once DIR_HELPER_MENUANDCONTENTHELPER1;

//==============================================================================
//  Manual State State Defines
//==============================================================================
define("MANUALSTATE_UNKNOWN", 0);
define("MANUALSTATE_READ", 1);
define("MANUALSTATE_WRITE", 2);
define("MANUALSTATE_MANAGE", 3);


// <editor-fold defaultstate="collapsed" desc="_USERMANUAL">
class _USERMANUAL {
    const PAGELIST = 'pagelist';
    const CONTENT = 'content';
    const TITLE = 'title';

    const PAGETO = 'pageto';
    const ACTION = 'action';

    public static final function _MasterNavigationTitle() {
        return "Manual Management";
    }

    public static final function _MasterNavigation() {
        ?>
<a href="<?php echo URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=1"; ?>">Read</a>
<br />
<a href="<?php echo URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=2"; ?>">Write</a>
<br />
<a href="<?php echo URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=3"; ?>">Create/Delete/Move</a>
<?php
    }
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="manualstate_read">
class manualstate_read extends _baselayout implements _state, _mch1 {

public function menuContent1() {
        $count = sizeof($this->crossInfo[_USERMANUAL::PAGELIST]);
        for ($i = 0; $i < $count; $i++) {
            ?>
<div><a href="<?php echo URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=1&" . _USERMANUAL::PAGETO . "=" . $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_PAGEID]; ?>"><?php echo $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_TITLE] ?></a></div>
<?php
        }
    }

public function menuContent2() {
        _USERMANUAL::_MasterNavigation();
    }

public function pageContent() {
        ?>
<h2>
<?php echo $this->crossInfo[_USERMANUAL::TITLE]; ?>
</h2>
<?php echo $this->crossInfo[_USERMANUAL::CONTENT]; ?>
<?php
    }

    protected function thisPageLayout() {
        MenuAndContentHelper1_Layout($this, true, _SESSION::GetIsMaster(), "Chapters", _USERMANUAL::_MasterNavigationTitle());
    }

protected function thisPageStyle() {
        MenuAndContentHelper1_Style();
    }

protected function thisPagePreProcessing() {
        $db = _DB::_Connect();
        $this->crossInfo[_USERMANUAL::PAGELIST] = DB_USERMANUAL::_GetTitleList($db);
        if (!$this->crossInfo[_USERMANUAL::PAGELIST]) {
    $this->crossInfo[_USERMANUAL::TITLE] = "SOMETHING";
    $this->crossInfo[_USERMANUAL::CONTENT] = "Oops... Usermanual Seems To Be Empty. Blame Gregg";
    $db->disconnect();
    return;
        }
        $pageTo = _FCORE::IsSetGET(_USERMANUAL::PAGETO);
        if (!$pageTo) {
    $pageTo = $this->crossInfo[_USERMANUAL::PAGELIST][0][USERMANUAL_PAGEID];
        }
        $info = DB_USERMANUAL::_GetUserManualPage($db, $pageTo);
        if (!$info) {
    $this->crossInfo[_USERMANUAL::TITLE] = "Error Finding Page";
    $this->crossInfo[_USERMANUAL::CONTENT] = "An Error Finding The Page Seemed To Occur";
    $db->disconnect();
    return;
        }
        $this->crossInfo[_USERMANUAL::TITLE] = $info->getTitle();
        $this->crossInfo[_USERMANUAL::CONTENT] = $info->getContent();
        $db->disconnect();
    }

public function trigger($data = null) {
        $this->EchoBaseLayout();
    }

}

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="manualstate_write">
class manualstate_write extends _baselayout implements _state, _mch1 {

public function menuContent1() {
    _USERMANUAL::_MasterNavigation();
}

public function menuContent2() {

}

public function pageContent() {
        ?>
<h2>Writing The Manual</h2>
<?php if(isset($this->crossInfo[RESULTS])){ echo $this->crossInfo[RESULTS]."<br />"; } ?>
Chapter:
<select name="manualstate_write" title="manualstate_write">
<?php
    $count = sizeof($this->crossInfo[_USERMANUAL::PAGELIST]);
    for ($i = 0; $i < $count; $i++) {
    echo _FORM::Option(
            $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_TITLE],
            $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_PAGEID],
            $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_PAGEID] ==
            $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_PAGEID]);
    }
?>
</select>
<div id="manualstatewritetarget">
<?php
echo MakeWritingForm(
        $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_PAGEID],
        $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_CONTENT]);
?>
</div>
<?php


    }


    protected function thisPageLayout() {
        MenuAndContentHelper1_Layout($this, true, false, _USERMANUAL::_MasterNavigationTitle(), null);
    }

protected function thisPageStyle() {
        MenuAndContentHelper1_Style();
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT . "/jQuery.js");
        echo _FCORE::JavaScriptInclude(URL_JAVASCRIPT . "/usermanual.js");
    }

protected function thisPagePreProcessing() {
        $db = _DB::_Connect();
        if (_FCORE::IsSetPOST(ACTION_GO)) {
            $content = _FCORE::IsSetPOST(USERMANUAL_CONTENT);
            $pageID = _FCORE::IsSetPOST(USERMANUAL_PAGEID);
            if ($content != null && $pageID != null) {
                try {
                    $pageObj = DB_USERMANUAL::_GetUserManualPage($db, $pageID);
                    if ($pageObj) {
                        $pageObj->setContent($content);
                    }
                    $this->crossInfo[RESULTS] = "Successfully Saved Content";
                } catch(Exception $e){
                    $this->crossInfo[RESULTS] = "Failed To Save Content";
                }
            }
        }
        $this->crossInfo[_USERMANUAL::PAGELIST] = DB_USERMANUAL::_GetTitleList($db);
        if (!$this->crossInfo[_USERMANUAL::PAGELIST]) {
            return;
        }
        $this->crossInfo[_USERMANUAL::PAGETO] = _FCORE::IsSetPOST(USERMANUAL_PAGEID);
        if (!$this->crossInfo[_USERMANUAL::PAGETO]) {
            $this->crossInfo[_USERMANUAL::PAGETO] = $this->crossInfo[_USERMANUAL::PAGELIST][0][USERMANUAL_PAGEID];
        }
        $pagecontent = DB_USERMANUAL::_GetUserManualPage($db, $this->crossInfo[_USERMANUAL::PAGETO]);
        $this->crossInfo[_USERMANUAL::PAGETO] = array();
        $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_PAGEID] = $pagecontent->getPageID();
        $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_TITLE] = $pagecontent->getTitle();
        $this->crossInfo[_USERMANUAL::PAGETO][USERMANUAL_CONTENT] = $pagecontent->getContent();
        $db->disconnect();
    }

    public function trigger($data = null) {
        $this->EchoBaseLayout();
    }

}

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="manualstate_manage">
class manualstate_manage extends _baselayout implements _state, _mch1 {

public function menuContent1() {
        _USERMANUAL::_MasterNavigation();
    }

public function menuContent2() {

}

public function pageContent() {
        ?>
<h2>Create Chapter</h2>
<?php echo _FORM::_FormBegin("createmanualchapter", URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=3", "POST"); ?>
<?php echo _FORM::Hidden(ACTION_GO, ACTION_CREATE); ?>
    Title Of New Chapter: <?php echo _FORM::Text(USERMANUAL_TITLE, "", 40, 40); ?>
    <br />
    Initial Rank: <?php echo _FORM::Text(USERMANUAL_RANK, "0", 2, 2); ?>
    <br />
<?php echo _FORM::Submit("Create"); ?>
<?php echo _FORM::_FormEnd(); ?>
    <br />
<h2>Manage Chapters</h2>
<?php
    $count = sizeof($this->crossInfo[_USERMANUAL::PAGELIST]);
    for ($i = 0; $i < $count; $i++) {
?>
<?php echo _FORM::_FormBegin("rankmanualchapter", URL_PAGE_USERMANUAL . "?" . _USERMANUAL::ACTION . "=3", "POST"); ?>
<?php echo _FORM::Hidden(ACTION_GO, ACTION_UPDATE); ?>
<?php echo _FORM::Hidden(USERMANUAL_PAGEID, $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_PAGEID]); ?>
    Title <?php echo _FORM::Text(USERMANUAL_TITLE, $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_TITLE], 40, 40); ?>
    Rank <?php echo _FORM::Text(USERMANUAL_RANK, $this->crossInfo[_USERMANUAL::PAGELIST][$i][USERMANUAL_RANK], 2, 2); ?>
<?php echo _FORM::Submit("Update"); ?>
<?php echo _FORM::Submit("Delete"); ?>
<?php echo _FORM::_FormEnd(); ?>
<?php
        }
    }

    protected function thisPageLayout() {
        MenuAndContentHelper1_Layout($this, true, false, _USERMANUAL::_MasterNavigationTitle(), null);
    }

protected function thisPageStyle() {
        MenuAndContentHelper1_Style();
    }

protected function thisPagePreProcessing() {
        $db = _DB::_Connect();
        $action = _FCORE::IsSetPOST(ACTION_GO);
        if ($action == ACTION_UPDATE) {
            $submitvalue = _FCORE::IsSetPOST("submitvalue");
            if ($submitvalue == "Update") {
    $pageID = _FCORE::IsSetPOST(USERMANUAL_PAGEID);
    $pageTitle = _FCORE::IsSetPOST(USERMANUAL_TITLE);
    $pageRank = _FCORE::IsSetPOST(USERMANUAL_RANK);
    if ($pageID != null && $pageTitle != null && $pageRank != null) {
        $page = DB_USERMANUAL::_GetUserManualPage($db, $pageID);
        $page->setRank($pageRank);
        $page->setTitle($pageTitle);
    }
            } else if ($submitvalue == "Delete") {
    $pageID = _FCORE::IsSetPOST(USERMANUAL_PAGEID);
    if ($pageID) {
        DB_USERMANUAL::_DeleteUserManualPage($db, $pageID);
    }
            }
        } else if ($action == ACTION_CREATE) {
            $rank = _FCORE::IsSetPOST(USERMANUAL_RANK);
            $title = _FCORE::IsSetPOST(USERMANUAL_TITLE);
            if ($rank != null && $title != null) {
    DB_USERMANUAL::_CreateUserManualPage($db, "", $title, $rank);
            }
        }
        $this->crossInfo[_USERMANUAL::PAGELIST] = DB_USERMANUAL::_GetTitleList($db);
        $db->disconnect();
    }

public function trigger($data = null) {
        $this->EchoBaseLayout();
    }

}

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="page_usermanual">
class page_usermanual extends _stateMachine {

public function changeState($state) {
        switch ($state) {
            case MANUALSTATE_READ:
    $this->_CurrState = new manualstate_read();
    break;
            case MANUALSTATE_WRITE:
    $this->_CurrState = new manualstate_write();
    break;
            case MANUALSTATE_MANAGE:
    $this->_CurrState = new manualstate_manage();
    break;
            case MANUALSTATE_UNKNOWN:
            default:
    $this->_CurrState = new pagestate_unknown();
    break;
        }
    }

}

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="Page Control">
$thispage = new page_usermanual();
$state = MANUALSTATE_UNKNOWN;
if (_SESSION::GetIsMaster()) {
    $action = _FCORE::IsSetGET(_USERMANUAL::ACTION);
    if ($action == MANUALSTATE_READ || $action == MANUALSTATE_WRITE || $action == MANUALSTATE_MANAGE) {
        $state = $action;
    } else {
        $state = MANUALSTATE_READ;
    }
} else {
    $state = MANUALSTATE_READ;
}
$thispage->changeState($state);
$thispage->trigger();
// </editor-fold>


?>
