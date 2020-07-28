<?php

require_once '../../Boydsnest.php';
require_once FCORE_FILE_DBFACTORY;

header("Content-type: text/css");

$name = IsSetGet("name", "default");

FCore::$LOG_TO_HTML = false;

$scheme = FCore::LoadDBFactory(BN_DBFACTORY_SCHEMEMODEL);
$scheme_instance = $scheme->select_first(
        array(
            array(DBFactory::ID_KEY => SCHEME_SCHEMENAME,
                  DBFactory::ID_SIGN => "=",
                  DBFactory::ID_VAL => $name)
        ));

if ($scheme_instance == null){
    $scheme_instance = $scheme->select_first(
        array(
            array(DBFactory::ID_KEY => SCHEME_SCHEMENAME,
                  DBFactory::ID_SIGN => "=",
                  DBFactory::ID_VAL => "default"
            )
        ));

    if ($scheme_instance == null){
        echo "Error: no data found in database";
        return;
    }
}

?>

body
{
    background-color:#<?php echo $scheme_instance[SCHEME_BACKGROUND_COLOR]; ?>;
}

.background_color_accent
{
    background-color:#<?php echo $scheme_instance[SCHEME_BACKGROUND_COLOR_ACCENT]; ?>;
}

.background_color_accent_2
{
    background-color:#<?php echo "7070a0";//$scheme_instance[SCHEME_BACKGROUND_COLOR_ACCENT_2]; ?>;
}

#_hc_upper a
{
    background-image:url("<?php echo BN_URL_ASSETS . "/" . $scheme_instance[SCHEME_MAIN_MENU_INDEX_PIC]; ?>");
}

#_hc_menu a:hover
{
    color:#<?php echo $scheme_instance[SCHEME_MAIN_MENU_LINK_HOVER_COLOR]; ?>;
    border-color:#<?php echo $scheme_instance[SCHEME_MAIN_MENU_LINK_HOVER_COLOR]; ?>;
}

.text_color
{
    color:#<?php echo $scheme_instance[SCHEME_TEXT_COLOR]; ?>;
}

.text_color_accent
{
    color:#<?php echo $scheme_instance[SCHEME_TEXT_COLOR_ACCENT]; ?>;
}

a.a_link
{
    color:#094d7c;
}

a.a_link:hover
{
    color:green;
    border-color:green;
}