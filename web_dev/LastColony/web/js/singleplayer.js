
var singleplayer = {
  
  // 
  currentlevel: 0,
  
  // begin single player campaign
  start: function() {
    // hide the starting menu layer
    $('.gamelayer').hide();
    
    // begin with the first level
    singleplayer.currentlevel = 0;
    game.type = "singleplayer";
    game.team = "blue";
    
    singleplayer.startCurrentLevel();
  },
  
  exit: function() {
    // show the starting menu layer
    $('.gamelayer').hide();
    $('#gamestartscreen').show();
  },
  
  play: function() {
    game.animationLoop();
    game.animationInterval = setInterval(game.animationLoop, game.animationTimeout);
    game.start();
  },
  
  startCurrentLevel: function() {
    // load all the items for the level
    var level = maps.singleplayer[singleplayer.currentlevel];
    
    // dont allow player to enter mission until all 
    // assets for the level are loaded
    $("#entermission").attr("disabled", true);
    
    // load all the assets for the level
    game.currentMapImage = loader.loadImage(level.mapimage);
    game.currentlevel = level;
    
    game.offsetX = level.startx * game.gridsize;
    game.offsetY = level.starty * game.gridsize;
//    alert("offsets: "+game.offsetX+","+game.offsetY);
    
    // enable the enter mission button once all assets are loaded
    if (loader.loaded) {
      $('#entermission').removeAttr("disabled");
    } else {
      loader.onload = function() {
        $('#entermission').removeAttr("disabled");
      }
    }
    
    $("#missionbriefing").html(level.briefing.replace(/\n/g, '<br><br>'));
    $("#missionscreen").show();
  }
  
};