<?php

session_start();
require_once DIR_CORE_FCORE;
require_once DIR_CORE_SESSION;
_SESSION::Initiate();

abstract class _baselayout {
    protected abstract function thisPageLayout();
    protected abstract function thisPageStyle();
    protected abstract function thisPagePreProcessing();

    protected $crossInfo;

    public function  __construct() {
        $this->crossInfo = array();
        $this->thisPagePreProcessing();
    }

    //================================================
    //	page layout
    //================================================
    public function EchoBaseLayout(){
        logger_FuncCall(__FILE__, __LINE__, __FUNCTION__);
        ?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Boyd's Nest</title>
    <?php echo _FCORE::CSSInclude(URL_STYLE_BASESTYLE); ?>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

<!-- Begin This Page Style -->
    <?php $this->thisPageStyle(); ?>

<!-- End Section -->

</head>
<body>


<!-- Begin Header Menu Layout -->

    <div id="_headercontainer">
        <div id="_hc_upper">
            <a href="<?php echo URL_PAGE_INDEX; ?>">
                BOYDS NEST
            </a>
        </div>
        <div id="_hc_menu">
            <ul>
                <li><a href="<?php echo URL_PAGE_HOME; ?>">HOME</a></li>
                <li><a href="<?php echo URL_PAGE_ADMIN; ?>">ADMIN</a></li>
                <li><a href="<?php echo URL_PAGE_TERMS; ?>">TERMS</a></li>
                <li><a href="<?php echo URL_PAGE_CONTACTUS; ?>">CONTACT US</a></li>
                <li><a href="<?php echo URL_PAGE_USERMANUAL; ?>">USER MANUAL</a></li>
                <?php if(_S::IsLoggedIn()) { ?>
                <li><a href="<?php echo URL_PAGE_LOGOUT; ?>">LOGOUT</a></li>
                <?php } else { ?>
                <li><a href="<?php echo URL_PAGE_LOGIN; ?>">LOGIN</a></li>
                <?php } ?>
            </ul>
        </div>
    </div>

<!-- End Section -->

    <div id="_pagecontent">

<!-- Begin This Page Content -->
        <?php $this->thisPageLayout(); ?>

<!-- End Section -->

    </div>


<!-- Begin Footer Layout -->

    <div id="_footercontent">
        This Site Is Owned By The Boyd's Family, And Was Created By R And L
    </div>

<!-- End Section -->


</body>
</html>
        <?php
    }
}

?>
