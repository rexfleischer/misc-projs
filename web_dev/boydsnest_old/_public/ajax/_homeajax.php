<?php

session_start();
require_once '../_definitions.php';
require_once DIR_CORE_FCORE;
require_once DIR_CORE_SESSION;
require_once DIR_DATABASE_DATABASE;
_SESSION::CheckLogin();

if (_S::IsLoggedIn()){
    $action = _FCORE::IsSetPostDefault(ACTION, false);
    if ($action){
        switch($action){
            case "getchild":
                $pageID = (int)_FCORE::IsSetPostDefault(PAGES_PAGEID, false);
                if ($pageID){
                    $db = _DB::_Connect();
                    $pages = DB_PAGE::_GetPageChilren($db, $pageID);
                    $returning = "";
                    $count = sizeof($pages);
                    $first = true;
                    for($i=0; $i<$count; $i++){
                        if (_SESSION::GetCanViewPage($db, $pages[$i]->getPageID())){
                            if ($first){
                                $first = false;
                            } else {
                                $returning .= "::::";
                            }
                            $returning .=
                                $pages[$i]->getPageID()."::".
                                $pages[$i]->getTitle()."::".
                                ($pages[$i]->getHasChildren()>0 ? "1" : "0");
                        }
                    }
                    echo $returning;
                    $db->disconnect();
                }
                break;
            case "getroots";
                $userID = (int)_FCORE::IsSetPostDefault(USERS_USERID, false);
                if ($userID){
                    $db = _DB::_Connect();
                    $pages = DB_PAGE::_GetUserRoots($db, $userID);
                    $returning = "";
                    $count = sizeof($pages);
                    $first = true;
                    for($i=0; $i<$count; $i++){
                        if (_SESSION::GetCanViewPage($db, $pages[$i]->getPageID())){
                            if ($first){
                                $first = false;
                            } else {
                                $returning .= "::::";
                            }
                            $returning .=
                                $pages[$i]->getPageID()."::".
                                $pages[$i]->getTitle()."::".
                                ($pages[$i]->getHasChildren()>0 ? "1" : "0");
                        }
                    }
                    echo $returning;
                    $db->disconnect();
                }
                break;
            case "getpage":
                $pageID = (int)_FCORE::IsSetPostDefault(PAGES_PAGEID, false);
                if ($pageID){
                    $db = _DB::_Connect();
                    if (_SESSION::GetCanViewPage($db, $pageID)){
                        try {
                            $page = DB_PAGE::_GetPage($db, $pageID);
                            echo $page->getPageID()."::::".$page->getTitle()."::::".$page->getContentAsHTML();
                        } catch(Exception $e) { }
                    }
                    $db->disconnect();
                }
                break;
        }
    }
}

?>
