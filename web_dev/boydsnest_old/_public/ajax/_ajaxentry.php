<?php

session_start();
require_once '../_definitions.php';
require_once DIR_CORE_FCORE;
require_once DIR_CORE_SESSION;
require_once DIR_DATABASE_DATABASE;
_SESSION::CheckLogin();

require_once DIR_FORM_USERMANUAL_WRITING;
require_once DIR_FORM_ADMIN_PAGECREATE;
require_once DIR_HELPER_MENUANDCONTENTHELPER1;
require_once DIR_HELPER_THREADEDFORUMBUILDER;
require_once DIR_HELPER_THREADEDFORUMVIEWER;

$page = _FCORE::IsSetGET("page");
if(!$page){
    return;
}
switch($page){
    case "usermanual":
        if (_SESSION::GetIsMaster()){
            $pageID = _FCORE::IsSetPOST(USERMANUAL_PAGEID);
            if ($pageID){
                $db = _DB::_Connect();
                $pageInfo = DB_USERMANUAL::_GetUserManualPage($db, $pageID);
                echo MakeWritingForm($pageID, $pageInfo->getContent());
                $db->disconnect();
            } else {
                echo "Invalid Param";
            }
        } else {
            echo "Access Denied";
        }
        break;
    case "admin_pagecreate":
        $userID = _FCORE::IsSetPostDefault(PAGES_USERID, false);
        if ($userID){
            if (($userID == _SESSION::GetUserID() && _SESSION::GetCanCDSelf()) ||
                ($userID != _SESSION::GetUserID() && _SESSION::GetCanCDOther())){
                $db = _DB::_Connect();
                echo MakeCreateUserChildOfSelect(
                    DB_PAGE::_GetPageListForUserID($db, $userID));
                $db->disconnect();
            } else {
                echo "Access Denied";
            }
        } else {
            echo "Invalid Param";
        }
        break;
    case "admin_pagesmanage":
        $userID = _FCORE::IsSetPostDefault(PAGES_USERID, false);
        if ($userID){
            if (($userID == _SESSION::GetUserID() && _SESSION::GetCanCDSelf()) ||
                ($userID != _SESSION::GetUserID() && _SESSION::GetCanCDOther())){
                $db = _DB::_Connect();

                $bypase = ($userID == _SESSION::GetUserID() || _SESSION::GetDefaultRight() > 0);
                $arr = $bypase ? DB_USER::_GetAllPageRightsForUser($db, $userID) : array();
                $forum = ThreadedForumBuilder::MakeFolderHierarchyForUser(
                        DB_PAGE::_GetPageListForUserID($db, $userID),
                        $arr,
                        1,
                        $bypase);

                echo ThreadedForumViewer::BuildViewForAdminPageManage($forum,
                        $userID == _SESSION::GetUserID() ?
                            _SESSION::GetCanCDSelf() :
                            _SESSION::GetCanCDOther());
                $db->disconnect();
            } else {
                echo "Access Denied";
            }
        } else {
            echo "Invalid Param";
        }
        break;
}

?>
