
var game = {
  
  // the map is broken into square tiles of
  // this size (20px X 20px)
  gridsize: 20,
  
  // store whether or not the background moved and needs to be redrawn
  backgroundChanged: true,
  refreshBackground: true,
  
  // the map image for th current level
  currentMapImage: undefined,
  
  // the meta data for the current level
  currentlevel: undefined,
  
  // a control loop that runs at a fixed period of time
  animationTimeout: 100,
  animationInterval: undefined,
  offsetX: 0,
  offsetY: 0,
  
  // panning control 
  panningThreshold: 60,
  panningSpeed: 10,
  
  // whether or not the game is running
  running: false,
  
  backgroundCanvas: undefined,
  foregroundCanvas: undefined,
  
  backgroundContext: undefined,
  foregroundContext: undefined,
  
  canvasWidth: 0,
  canvasHeight: 0,
  
  init: function() {
    loader.init();
    mouse.init();
    
    $('.gamelayer').hide();
    $("#gamestartscreen").show();
    
    game.backgroundCanvas = document.getElementById('gamebackgroundcanvas');
    game.foregroundCanvas = document.getElementById('gameforegroundcanvas');
    
    game.backgroundContext = game.backgroundCanvas.getContext('2d');
    game.foregroundContext = game.foregroundCanvas.getContext('2d');
    
    game.canvasWidth = game.backgroundCanvas.width;
    game.canvasHeight = game.backgroundCanvas.height;
  },
  
  start: function() {
    $('.gamelayer').hide();
    $('#gameinterfacescreen').show();
    game.running = true;
    game.refreshBackground = true;
    
    game.drawingLoop();
  },
  
  animationLoop: function() {
    // animate each of the elements within the game
  },
  
  drawingLoop: function() {
    // handle panning the map
    game.handlePanning();
    
    // since drawing the background map is a fairly large operation,
    // we only redraw the background if it changes (due to panning)
    if (game.refreshBackground) {
      game.backgroundContext.drawImage(game.currentMapImage, 
            game.offsetX, game.offsetY, game.canvasWidth, game.canvasHeight, 
            0, 0, game.canvasWidth, game.canvasHeight);
      game.refreshBackground = false;
    }
    
    // clear the foreground canvas
    game.foregroundContext.clearRect(0, 0, game.canvasWidth, game.canvasHeight);
    
    // start drawing the foreground elements
    
    
    // draw the mouse
    mouse.draw();
    
    // call the drawing loop for the next frame
    // using request amination frame
    if (game.running) {
      requestAnimationFrame(game.drawingLoop);
    }
  },
  
  handlePanning: function() {
    // do not pan if mouse leaves the canvas
    if (!mouse.insideCanvas) {
      return;
    }
    
    if (mouse.x <= game.panningThreshold) {
      if (game.offsetX >= game.panningSpeed) {
        game.refreshBackground = true;
        game.offsetX -= game.panningSpeed;
      }
    } else if (mouse.x >= (game.canvasWidth - game.panningThreshold)) {
      if (game.offsetX + game.canvasWidth + game.panningSpeed <= game.currentMapImage.width) {
        game.refreshBackground = true;
        game.offsetX += game.panningSpeed;
      }
    }
    
    if (mouse.y <= game.panningThreshold) {
      if (game.offsetY >= game.panningSpeed) {
        game.refreshBackground = true;
        game.offsetY -= game.panningSpeed;
      }
    } else if (mouse.y >= (game.canvasHeight - game.panningThreshold)) {
      if (game.offsetY + game.canvasHeight + game.panningSpeed <= game.currentMapImage.height) {
        game.refreshBackground = true;
        game.offsetY += game.panningSpeed;
      }
    }
    
    if (game.refreshBackground) {
      // update mouse game coordinates based on game offsets
      mouse.calculateGameCoordinates();
    }
  }
};

$(window).load(function(){
  game.init();
});