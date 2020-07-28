<!DOCTYPE html>
<html>
    <head>
        <title>Fled War</title>
        <meta charset="UTF-8" />
        
        <!-- bootstrap stuff -->
        <link rel="stylesheet" href="<?php echo $this->config->item("base_url"); ?>bootstrap/css/bootstrap.css" />
        <!--<link rel="stylesheet" href="<?php echo $this->config->item("base_url"); ?>bootstrap/css/bootstrap-responsive.css" />-->
        <!--<script src="<?php echo $this->config->item("base_url"); ?>bootstrap/js/bootstrap.js"></script>-->
        <style>
          body {
            padding-top: 60px; 
            /* 60px to make the container go all the way to the bottom of the topbar */
          }
        </style>
        
        
        <!-- jquery -->
        <script src="<?php echo $this->config->item("base_url"); ?>jquery/jquery.js"></script>
        
        <!-- jquery ui -->
        <script src="<?php echo $this->config->item("base_url"); ?>jquery/jquery-ui.js"></script>
        <link  href="<?php echo $this->config->item("base_url"); ?>jquery/ui1/jquery-ui-custom.min.css" rel="stylesheet"  type="text/css" />
        
    </head>
    <body>

        <div class="navbar navbar-inverse navbar-fixed-top">
          <div class="navbar-inner">
            <div class="container">
              <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="brand" href="<?php echo $this->config->item("base_url"); ?>aboutfledwar/">Fled War</a>
              <div class="nav-collapse collapse">
                <ul class="nav">
                  <li id="nav-home"><a href="<?php echo $this->config->item("base_url"); ?>home/">Home</a></li>
                  <li id="nav-about"><a href="<?php echo $this->config->item("base_url"); ?>about/">About</a></li>
                  <li id="nav-contactus"><a href="<?php echo $this->config->item("base_url"); ?>contactus/">Contact Us</a></li>
                  
                  <?php if (array_key_exists("username", $_SESSION)): ?>
                  <li id="nav-game"><a href="<?php echo $this->config->item("base_url"); ?>game/">Game</a></li>
                    <?php if (array_key_exists("is_admin", $_SESSION) && $_SESSION["is_admin"]): ?>
                    <li id="nav-admin"><a href="<?php echo $this->config->item("base_url"); ?>admin/">Admin</a></li>
                    <?php endif; ?>
                  <li><a href="<?php echo $this->config->item("base_url"); ?>logout/">Logout (<?php echo $_SESSION["username"]; ?>)</a></li>
                  <?php else: ?>
                  <li id="nav-login"><a href="<?php echo $this->config->item("base_url"); ?>login/">Login</a></li>
                  <?php endif; ?>
                  
                </ul>
              </div><!--/.nav-collapse -->
            </div>
          </div>
        </div>

        <div class="container">
        
        <!-- end header.php -->
        
        