<?php

interface _mch1 {
    public function menuContent1();
    public function menuContent2();
    public function pageContent();
}

function MenuAndContentHelper1_Layout(_mch1 $obj, $menu1, $menu2, $menu1Title = null, $menu2Title = null){
    ?>

<div id="_mch1_m_container">
    <?php if ($menu1) { ?>
    <div id="_mch1_m_upper">
        <?php if ($menu1Title != null) {?>
        <div id="text"><?php echo $menu1Title; ?></div>
        <?php } ?>
    </div>
    <div id="_mch1_m_middle">
        <?php $obj->menuContent1(); ?>
    </div>
    <div id="_mch1_m_lower">
    </div>
    <?php } if ($menu2) { ?>
    <br />
    <div id="_mch1_m_upper">
        <?php if ($menu2Title != null) {?>
        <div id="text"><?php echo $menu2Title; ?></div>
        <?php } ?>
    </div>
    <div id="_mch1_m_middle">
        <?php $obj->menuContent2(); ?>
    </div>
    <div id="_mch1_m_lower">
    </div>
    <?php } ?>
</div>

<div id="_mch1_p_contentcontainer">
    <?php $obj->pageContent(); ?>
</div>
    <?php
}

function MenuAndContentHelper1_Style(){
    ?>

<style type="text/css">
#_mch1_m_container
{
    margin:2px 2px 2px 2px;
    width:200px;
    min-height:300px;
    max-width:200px;
    height:auto;
    overflow:auto;
    float:left;
}
#_mch1_m_upper
{
    background-image: url('<?php echo URL_ASSETS; ?>/SideNavigator_1/sn_upper.png');
    width:200px;
    height: 36px;
}
#_mch1_m_middle
{
    background-image: url('<?php echo URL_ASSETS; ?>/SideNavigator_1/sn_middle.png');
    padding:0px 0px 0px 4px;
    width:196px;
    height: auto;
    overflow:auto;
}
#_mch1_m_middle a
{
    display:inline-block;
    margin:0px 0px 1px 0px;
    color:white;
    height: 1.3em;
    overflow:auto;
    text-decoration:none;
}
#_mch1_m_middle a:hover
{
    color:#c0c0c0;
}
#_mch1_m_lower
{
    background-image: url('<?php echo URL_ASSETS; ?>/SideNavigator_1/sn_lower.png');
    width:200px;
    height: 36px;
}
#_mch1_p_contentcontainer
{
    margin: 2px 2px 2px 2px;
    background-color:chartreuse;
    float:left;
    min-height:300px;
    min-width:1000px;
    overflow: auto;
}
</style>
    <?php
}

?>
