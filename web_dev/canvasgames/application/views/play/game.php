<!DOCTYPE html>
<html>
    <head>
        <title>Medieval War game thingy</title>
        <meta charset="UTF-8" />
        <link rel="StyleSheet" href="<?php echo $this->config->item("base_url"); ?>css/common.css" type="text/css" />
        <script src="<?php echo $this->config->item("base_url"); ?>javascript/jquery-1.7.1.js"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>javascript/modernizr.js"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>javascript/Shapes.js"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>helper/StatusHelper.js"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>helper/GameHelper.js"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>helper/LandHelper.js"></script>
        <script src="<?php echo $main; ?>"></script>
        <script src="<?php echo $this->config->item("base_url"); ?>javascript/fled.js"></script>
    </head>
    <body>
        <!-- begin header -->
        <?php include APPPATH . 'views/header.php'; ?>
        
        <!-- end header -->
        
        
        <button type="button" value="start" onclick="canvas_app.start();" >start</button>
        <button type="button" value="stop" onclick="canvas_app.stop();" >stop</button>
        <button type="button" value="pause" onclick="canvas_app.pause();" >pause</button>
        
        <div id="canvas_div">
            <canvas id="game_canvas" width="700" height="500">
                Your browser does not support HTML5 canvas.
            </canvas>
        </div>
        
        
        <!-- begin footer -->
        <?php include APPPATH . 'views/footer.php'; ?>
        
        <!-- end footer -->
    </body>
</html>