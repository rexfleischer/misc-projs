<?php
define("BN_START_NO_SESSION",   true);
define("BN_START_NO_FCORE",     true);
require_once '../../../Boydsnest.php';
header("Content-type: text/css");
?>
._mch1_m_upper
{
    background-image: url('<?php echo BN_URL_ASSETS; ?>/sidemenu_1/sn_upper.png');
    width:  200px;
    height: 36px;
    color:  #e0e0e0;
    text-align: center;
}
._mch1_m_middle
{
    background-image: url('<?php echo BN_URL_ASSETS; ?>/sidemenu_1/sn_middle.png');
    padding:0px 0px 0px 4px;
    width:196px;
    height: auto;
    overflow:auto;
}
._mch1_m_middle a
{
    display:inline-block;
    margin:0px 0px 1px 5px;
    color:white;
    height: 1.3em;
    overflow:auto;
    text-decoration:none;
}
._mch1_m_middle a:hover
{
    color:#c0c0c0;
}
._mch1_m_lower
{
    background-image: url('<?php echo BN_URL_ASSETS; ?>/sidemenu_1/sn_lower.png');
    width:200px;
    height: 36px;
}