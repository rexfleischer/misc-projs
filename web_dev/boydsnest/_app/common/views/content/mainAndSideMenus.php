

    <link href="<?php echo BN_URL_CSS; ?>/content/content.css" rel="stylesheet" type="text/css" />
    <div class="main_width_center">

        <div class="sidemenu_container">

            <div class="sidemenu_container_container"><?php
for ($i = 0; ; $i++)
{
    $_sidemenu = 'sidemenu_'.$i;
    if (isset($$_sidemenu))
    {
        echoln();
        echoln();
        echoln($$_sidemenu);
        htmlln();
    }
    else
    {
        break;
    }
}
?>

            </div>

        </div>

        <div class="usermanual_content background_color_accent_2">

            {{content}}

        </div>

    </div>