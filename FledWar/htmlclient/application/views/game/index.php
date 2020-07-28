<?php include VIEWPATH . "header.php"; ?>




<div id="gamecontainer">
    <div id="gameinterfacescreen" class="gamelayer">
        
        <canvas id="gamebackgroundcanvas" width="960px"></canvas>
        <canvas id="gameforegroundcanvas" width="960px"></canvas>
        
    </div>

    <div id="dialog_div"></div>
    <?php if ($_SESSION["is_admin"]): ?>
    <div id="command_div"></div>
    <?php endif; ?>
</div>


<link rel="stylesheet" href="<?php echo $this->config->item("base_url"); ?>game.assets/css/fledwar.css" />
<script src="<?php echo $this->config->item("base_url"); ?>jquery/jquery.mousewheel.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game/js_config/?cache_buster=<?php echo time(); ?>"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/common.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/keyboard.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/loader.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/menu.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/mouse.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/render.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/render.primitives.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/socket.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/unit.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/system.popup.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/system.static.admin.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/system.static.unit.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/system.static.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/systemnav.static.admin.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/systemnav.static.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.inputs.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.points.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.units.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/systemnav.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/systemnav.systems.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/fledwar.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/main.js"></script>


<?php if ($_SESSION["is_admin"]): ?>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/admin.js"></script>
<?php endif; ?>



<?php include VIEWPATH . "footer.php"; ?>
