<?php include VIEWPATH . "header.php"; ?>




<div id="gamecontainer">
    <div id="gameinterfacescreen" class="gamelayer">
        <canvas id="gamebackgroundcanvas" height="500" width="700"></canvas>
        <canvas id="gameforegroundcanvas" height="500" width="700"></canvas>
    </div>

    <div id="loading_div_dialog"></div>
    <div id="fatal_error_dialog"></div>
</div>


<link rel="stylesheet" href="<?php echo $this->config->item("base_url"); ?>game.assets/css/fledwar.css" />
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/common.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/connection.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/main.system.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/render.primitives.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/render.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/input.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/test/test.js"></script>



<?php include VIEWPATH . "footer.php" ?>
