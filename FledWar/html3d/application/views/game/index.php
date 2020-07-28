<?php include VIEWPATH . "header.php"; ?>




<div id="gamecontainer">
    <div id="gameinterfacescreen" class="gamelayer">
        
    </div>

    <div id="dialog_div"></div>
    <?php if ($_SESSION["is_admin"]): ?>
    <!--<div id="command_div"></div>-->
    <?php endif; ?>
</div>


<link rel="stylesheet" href="<?php echo $this->config->item("base_url"); ?>game.assets/css/fledwar.css" />
<script src="<?php echo $this->config->item("base_url"); ?>jquery/jquery.mousewheel.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/three.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/stats.min.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/dat.gui.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game/js_config/?cache_buster=<?php echo time(); ?>"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/common.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/base/socket.js"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/control/keyboard.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/control/mouse.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/control/system.click.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/control/system.move.fly.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/control/system.move.orbit.js"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.points.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/system.units.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/systemnav.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/systemnav.systems.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/fledwar.js"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/menus/system.static.js"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point/point.star.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point/point.planet.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point/point.moon.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point/point.belt.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/point/point.marker.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/unit.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/action.js"></script>
<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/types/action/action.impulse.js"></script>

<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/main.js"></script>


<?php if ($_SESSION["is_admin"]): ?>
<!--<script src="<?php echo $this->config->item("base_url"); ?>game.assets/js/game/admin.js"></script>-->
<?php endif; ?>


<?php include VIEWPATH . "footer.php"; ?>
