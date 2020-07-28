<?php

if (!isset($session)){
    $session = BoydsnestSession::GetInstance();
}

?><!DOCTYPE html>
<html>
<head>
    <title><?php IsSetEcho($title); ?></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<?php IsSetEcho($meta); ?>

    <link href='<?php echo BN_URL_CSS . "/variable.php"; ?>' rel='stylesheet' type='text/css' />
    <link href='<?php echo BN_URL_CSS . "/common.css"; ?>' rel='stylesheet' type='text/css' />

<?php IsSetEcho($style); ?>

<?php IsSetEcho($javascript); ?>

</head>
<body>


<!-- Begin Header Menu Layout -->

    <div class="background_color_accent" id="_headercontainer">
        <div id="_hc_upper">
            <a class="text_color" href="<?php echo BN_URL_PAGE_INDEX; ?>">
                BOYDS NEST
            </a>
        </div>
        <div id="_hc_menu">
            <ul>
                <li><a href="<?php echo BN_URL_PAGE_HOME; ?>">HOME</a></li>
                <li><a href="<?php echo BN_URL_PAGE_ADMIN; ?>">ADMIN</a></li>
                <li><a href="<?php echo BN_URL_PAGE_TERMS; ?>">TERMS</a></li>
                <li><a href="<?php echo BN_URL_PAGE_CONTACTUS; ?>">CONTACT US</a></li>
                <li><a href="<?php echo BN_URL_PAGE_USERMANUAL; ?>">USER MANUAL</a></li><?php if($session->IsLoggedIn()){ ?>
                
                <li><a href="<?php echo BN_URL_PAGE_LOGOUT; ?>">LOGOUT</a></li><?php } else { ?>

                <li><a href="<?php echo BN_URL_PAGE_LOGIN; ?>">LOGIN</a></li><?php } ?>

            </ul>
        </div>
    </div>

<!-- End Section -->



<!-- Begin This Page Content -->

    <div id="_pagecontent">

<?php IsSetEcho($main_content); ?>

    </div>

<!-- End Section -->



<!-- Begin Footer Layout -->

    <div class="full_width background_color_accent">
        <div class="main_width_center" id="_footercontent">
            This Site Is Owned By The Boyd's Family, And Was Created By R And L
        </div>
    </div>

<!-- End Section -->


</body>
</html>