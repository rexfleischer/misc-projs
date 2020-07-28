<?php

require_once '_config.php';


// <editor-fold defaultstate="collapsed" desc="ERRORs">
define("ERROR_PAGE", 'error_page');
define("ERROR_UNKNOWNACTION", -1);
define("ERROR_UNEXPECTEDERROR", -2);
define("ERROR_MUSTBELOGGEDIN", -3);
define("ERROR_ACCESSDENIED", -4);
define("ERROR_UNDERCONSTRUCTION", -5);
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="URLs">
define("URL_PAGE_ADMIN", _D::SERVER_ADDRESS . "/admin.php");
define("URL_PAGE_CONTACTUS", _D::SERVER_ADDRESS . "/contactus.php");
define("URL_PAGE_ERRORPAGES", _D::SERVER_ADDRESS . "/errorpages.php");
define("URL_PAGE_HOME", _D::SERVER_ADDRESS . "/home.php");
define("URL_PAGE_INDEX", _D::SERVER_ADDRESS . "/index.php");
define("URL_PAGE_LOGIN", _D::SERVER_ADDRESS . "/login.php");
define("URL_PAGE_LOGOUT", _D::SERVER_ADDRESS . "/logout.php");
define("URL_PAGE_TERMS", _D::SERVER_ADDRESS . "/terms.php");
define("URL_PAGE_USERMANUAL", _D::SERVER_ADDRESS . "/usermanual.php");

define("URL_ASSETS", _D::SERVER_ADDRESS . "/_public/assets");
define("URL_JAVASCRIPT", _D::SERVER_ADDRESS . "/_public/javascript");
define("URL_STYLE_BASESTYLE", _D::SERVER_ADDRESS . "/_public/template/_basestyle.css");

define("ERROR_HTTP_UNKNOWNACTION", URL_PAGE_ERRORPAGES."?".ERROR_PAGE."=".ERROR_UNKNOWNACTION);
define("ERROR_HTTP_UNEXPECTEDERROR", URL_PAGE_ERRORPAGES."?".ERROR_PAGE."=".ERROR_UNEXPECTEDERROR);
define("ERROR_HTTP_MUSTBELOGGEDIN", URL_PAGE_ERRORPAGES."?".ERROR_PAGE."=".ERROR_MUSTBELOGGEDIN);
define("ERROR_HTTP_ACCESSDENIED", URL_PAGE_ERRORPAGES."?".ERROR_PAGE."=".ERROR_ACCESSDENIED);
define("ERROR_HTTP_UNDERCONSTRUCTION", URL_PAGE_ERRORPAGES."?".ERROR_PAGE."=".ERROR_UNDERCONSTRUCTION);
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="Directories">
define("DIR_PAGE_ADMIN", _D::FILE_DIR_BASE . "/admin.php");
define("DIR_PAGE_CONTACTUS", _D::FILE_DIR_BASE . "/contactus.php");
define("DIR_PAGE_ERRORPAGES", _D::FILE_DIR_BASE . "/errorpages.php");
define("DIR_PAGE_HOME", _D::FILE_DIR_BASE . "/home.php");
define("DIR_PAGE_INDEX", _D::FILE_DIR_BASE . "/index.php");
define("DIR_PAGE_LOGIN", _D::FILE_DIR_BASE . "/login.php");
define("DIR_PAGE_LOGOUT", _D::FILE_DIR_BASE . "/logout.php");
define("DIR_PAGE_TERMS", _D::FILE_DIR_BASE . "/terms.php");
define("DIR_PAGE_USERMANUAL", _D::FILE_DIR_BASE . "/usermanual.php");

define("DIR_HELPER_MENUANDCONTENTHELPER1", _D::FILE_DIR_BASE . "/_public/helper/MenuAndContentHelper1.php");
define("DIR_HELPER_THREADEDFORUMBUILDER", _D::FILE_DIR_BASE . "/_public/helper/ThreadedForumBuilder.php");
define("DIR_HELPER_THREADEDFORUMVIEWER", _D::FILE_DIR_BASE . "/_public/helper/ThreadedForumViewer.php");
define("DIR_HELPER_UNDERCONSTRUCTION", _D::FILE_DIR_BASE . "/_public/helper/underConstruction.php");
define("DIR_HELPER_UNDERCONSTRUCTION_FORCE", _D::FILE_DIR_BASE . "/_public/helper/underConstruction_force.php");

define("DIR_FORM_FORMTEXTFIELDWRITER", _D::FILE_DIR_BASE . "/_public/forms/FormTextfieldWriter.php");
define("DIR_FORM_ADMIN_PAGECREATE", _D::FILE_DIR_BASE . "/_public/forms/admin_pagecreate.php");
define("DIR_FORM_USERMANUAL_WRITING", _D::FILE_DIR_BASE . "/_public/forms/usermanual_writing.php");

define("DIR_TEMPLATE_BASELAYOUT", _D::FILE_DIR_BASE . "/_public/template/_baselayout.php");
define("DIR_TEMPLATE_BASESTYLE", _D::FILE_DIR_BASE . "/_public/template/_basestyle.php");

define("DIR_ASSETS", _D::FILE_DIR_BASE . "/_public/assets");

define("DIR_CORE_FCORE", _D::FILE_DIR_BASE . "/_private/FCore.php");
define("DIR_CORE_SESSION", _D::FILE_DIR_BASE . "/_private/_session.php");
define("DIR_DATABASE_DATABASE", _D::FILE_DIR_BASE . "/_private/_database.php");
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class _ADMINPAGE">
class _ADMINPAGE {
    const MESSAGES_LIST = 1;
    const MESSAGES_VIEW = 2;
    const MESSAGES_WRITE = 3;
    const MESSAGES_CONTEXT = 'context';
    const MESSAGES_START = 'start';
    const MESSAGES_AMOUNT = 'amount';

    const PAGES_WRITE = 4;
    const PAGES_CREATE = 5;
    const PAGES_MANAGE = 6;
    const PAGES_LIST = 'pageslist';
    const PAGES_INFO = 'pageinfo';

    const RIGHTS_LIST = 'rightslist';

    const ANNOUNCEMENTS = 8;


    const USERS_CREATE = 9;
    const USERS_MANAGE = 10;
    const USERS_VIEW = 11;
    const USERS_LOGS = 12;
    const USERS_LIST = 'userlist';
    const USERS_LOGS_START = 'start';
    const USERS_LOGS_AMOUNT = 'amount';

    const LOGIN_LOGS = 15;
    const LOGIN_LOGS_START = 'start';
    const LOGIN_LOGS_AMOUNT = 'amount';
}
// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="class _HOMEPAGE">
class _HOMEPAGE {
    
    const MAINVIEW = 1;
    const MAINVIEW_USERID = 'userid';
    const MAINVIEW_PAGEID = 'pageid';
    const MAINVIEW_COMMENTID = 'commentid';

    const FAMILYLIST = 'family';
    const MENULIST = 'menulist';

    const DATABASE = 'database';

}
// </editor-fold>


?>
