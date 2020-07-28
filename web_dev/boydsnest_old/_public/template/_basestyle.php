<?php
header("Content-Type: text/css");
$pagewidth=1000;
$paddingwidth=30;
require_once '../_definitions.php';
?>

*
{
 border:0px;
 margin:0px;
}
body
{
 background-color:#20b006;
}

#_headercontainer
{
 background-color:#FFFF00;
 margin:0px auto 1px auto;
 padding:0px 0px 2px 0px;
}
#_hc_upper
{
 margin:0px auto 2px auto;
 height:<?php $headerupperheight=130; echo $headerupperheight; ?>px;
}
#_hc_upper a
{
 display:inline-block;
 width:100%;
 background-image:url("<?php echo URL_ASSETS; ?>/Chrysanthemum.jpg");
 color:#ffffff;
 text-align:center;
 text-decoration:none;
 font-size:<?php $headerfontsize=40; echo $headerfontsize; ?>px;
 padding:<?php echo ($headerupperheight-$headerfontsize-6)/2; ?>px 0px <?php echo ($headerupperheight-$headerfontsize-8)/2; ?>px 0px;
}
#_hc_menu
{
 width:960px;
 margin-left:auto;
 margin-right:auto;
}
#_hc_menu ul
{
 display:inline;
 list-style-type:none;
}
#_hc_menu li
{
 display:inline;
 float:left;
 text-align:center;
}
#_hc_menu a
{
 color:#000000;
 text-decoration:none;
 padding:0px <?php echo $paddingwidth; ?>px 0px <?php echo $paddingwidth; ?>px;
}
#_hc_menu a:hover
{
 color:#00a000;
 border-color:#00a000;
 padding:0px <?php echo $paddingwidth; ?>px 0px <?php echo $paddingwidth; ?>px;
 border-width:thin;
 border-bottom-style:solid;
 border-top-style:solid;
}
#_hc_menu_extend ul
{
 width:<?php echo $pagewidth; ?>px;
 margin-left:auto;
 margin-right:auto;
 display:inline;
 list-style-type:none;
}
#_hc_menu_extend li
{
 display:inline;
 float:right;
 text-align:center;
}
#_hc_menu_extend a
{
 color:#000000;
 text-decoration:none;
 padding:0px <?php echo $paddingwidth/2; ?>px 0px <?php echo $paddingwidth/2; ?>px;
}
#_hc_menu_extend a:hover
{
 color:#00a000;
 border-color:#00a000;
 padding:0px <?php echo $paddingwidth/2; ?>px 0px <?php echo $paddingwidth/2; ?>px;
 border-width:thin;
 border-bottom-style:solid;
 border-top-style:solid;
}

#_pagecontent
{
 margin: 2px 2px 2px 2px;
 min-height:300px;
 overflow:auto;
 width:99%;
}

#_footercontent
{
 margin:2px auto 2px auto;
 background-color:#FFFF00;
 text-indent:15px;
}

#text
{
 color:#ffffff;
 text-align:center;
}

#_listcontainer
{
    height: 1.3em;
    width: 100%;
}
#_listcontainer.on
{
    background-color: white;
}
#_listcontainer.off
{
    background-color: linen;
}
#_listcontainer a
{
    color: black;
    text-decoration: none;
}
#_listcontainer_width30px
{
    min-width: 30px;
    float: left;
}
#_listcontainer_width100px
{
    min-width: 100px;
    float: left;
}
#_listcontainer_width150px
{
    min-width: 150px;
    float: left;
}
#_listcontainer_width225px
{
    min-width: 225px;
    float: left;
}
#_listcontainer_width300px
{
    min-width: 300px;
    float: left;
}
#_listcontainer_width400px
{
    min-width: 400px;
    float: left;
}
#_listcontainer_tomato input[type=submit]
{
    background-color: tomato;
    float: left;
}
#_listcontainer_something input[type=submit]
{
    background-color: tomato;
    float: left;
}
#_listcontainer_something input[type=text]
{
    background-color: #999999;
}