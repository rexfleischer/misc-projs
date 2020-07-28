<?php 

require_once '../setup.php';
$example = IsSetGet("example", 'ch1_4');

?><!DOCTYPE html>
<html>
    <head>
        <title>book example: <?php echo $example; ?></title>
        <script src="javascript/jquery-1.7.1.js"></script>
        <script src="javascript/modernizr.js"></script>
        <script src="javascript/fled.js"></script>
        <script src="examples/<?php echo $example; ?>.js"></script>
        <meta charset="UTF-8" />
    </head>
    <body>
        <?php include 'common/header.php'; ?>
        
        <div class="canvas_div">
            <canvas id="canvas" width="500" height="500">
                Your browser does not support HTML5 canvas.
            </canvas>
        </div>
        
        <?php include 'common/footer.php'; ?>
    </body>
</html>