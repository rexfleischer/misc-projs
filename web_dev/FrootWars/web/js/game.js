
var game = {
  
  // game mode
  mode: "intro",
  slingshotX: 140,
  slingshotY: 280,
  
  // maximum panning speed per frame in pixels
  maxspeed: 3, 
  // min and max panning offset
  minoffset: 0,
  maxoffset: 300,
  // current panning offset
  offsetleft: 0,
  
  // the game score
  score: 0,
  
  currentHero: undefined,
  
  init: function(){
    // initialize objects
    levels.init();
    loader.init();
    mouse.init();
    
    game.backgroundMusic = loader.loadSound("audio/gurdonark-kindergarten");
    game.slingshotReleaseSound = loader.loadSound("audio/released");
    game.bounceSound = loader.loadSound("audio/bounce");
    game.breakSound = {
      "glass" : loader.loadSound("audio/glassbreak"),
      "wood"  : loader.loadSound("audio/woodbreak")
    }
    
    $('.gamelayer').hide();
    $('#gamestartscreen').show();
    
    game.canvas = $('#gamecanvas')[0];
    game.context = game.canvas.getContext('2d');
  },
  
  start: function() {
    $(".gamelayer").hide();
    // display the game canvas and score
    $("#gamecanvas").show();
    $("#scorescreen").show();
    
    game.startBackgroundMusic();
    
    game.mode = "intro";
    game.offsetleft = 0;
    game.ended = false;
    game.animationFrame = window.requestAnimationFrame(game.animate, game.canvas);
  },
  
  restartLevel: function() {
    window.cancelAnimationFrame(game.animationFrame);
    game.lastUpdateTime = undefined;
    levels.load(game.currentLevel.number);
  },
  
  startNextLevel: function() {
    window.cancelAnimationFrame(game.animationFrame);
    game.lastUpdateTime = undefined;
    levels.load(game.currentLevel.number + 1);
  },
  
  showLevelScreen: function(){
    $('.gamelayer').hide();
    $('#levelselectscreen').show('slow');
  },
  
  showEndingScreen: function() {
    game.stopBackgroundMusic();
    if (game.mode == "level-success") {
      if (game.currentLevel.number < levels.data.length - 1) {
        $('#endingmessage').html('Level Complete. Well Done!!!');
        $('#playnextlevel').show();
      } else {
        $('#endingmessage').html('All Levels Complete. Well Done!!!');
        $('#playnextlevel').hide();
      }
    } else if (game.mode == "level-failure") {
      $("#endingmessage").html("Failed. Play Again?");
      $("#playnextlevel").hide();
    }
    $("#endingscreen").show();
  },
  
  startBackgroundMusic: function() {
    var toggleImage = $("#togglemusic")[0];
    game.backgroundMusic.play();
    toggleImage.src = "images/icons/sound.png";
  },
  
  stopBackgroundMusic: function() {
    var toggleImage = $("#togglemusic")[0];
    toggleImage.src = "images/icons/nosound.png";
    game.backgroundMusic.pause();
    game.backgroundMusic.currentTime = 0;
  },
  
  toggleBackgroundMusic: function() {
    var toggleImage = $("#togglemusic")[0];
    if (game.backgroundMusic.paused) {
      game.backgroundMusic.play();
      toggleImage.src = "images/icons/sound.png";
    } else {
      game.backgroundMusic.pause();
      toggleImage.src = "images/icons/nosound.png";
    }
  },
  
  animate: function() {
    // animate the background
    game.handlePanning();
    
    // animate the characters
    var currenttime = new Date().getTime();
    var timestep;
    if (game.lastUpdateTime) {
      timestep = (currenttime - game.lastUpdateTime) / 1000;
      box2d.step(timestep);
    }
    game.lastUpdateTime = currenttime;
    
    // draw the background with parallax scrolling
    game.context.drawImage(game.currentLevel.backgroundImage, 
                           game.offsetleft/4.0, 0, 640, 480, 
                           0, 0, 640, 480);
    game.context.drawImage(game.currentLevel.foregroundImage, 
                           game.offsetleft, 0, 640, 480, 
                           0, 0, 640, 480);
                           
    // draw the slingshot
    game.context.drawImage(game.slingshotImage, 
                           game.slingshotX - game.offsetleft,
                           game.slingshotY);
                           
    // draw all the bodies
    game.drawAllBodies();
    if (game.mode == "firing") {
      game.drawSlingshotBand();
    }
    
    // draw the front of the slingshot
    game.context.drawImage(game.slingshotFrontImage, 
                           game.slingshotX - game.offsetleft,
                           game.slingshotY);
    
    if (!game.ended) {
      game.animateFrame = window.requestAnimationFrame(game.animate, game.canvas);
    }
  },
  
  drawAllBodies: function() {
    box2d.world.DrawDebugData();
    
    for(var body = box2d.world.GetBodyList(); body; body = body.GetNext()) {
      var entity = body.GetUserData();
      
      if(entity){
        var entityX = body.GetPosition().x*box2d.scale;
        if(entityX<0|| entityX>game.currentLevel.foregroundImage.width||(entity.health && entity.health <0)){
          box2d.world.DestroyBody(body);
          if (entity.type=="villain"){
            game.score += entity.calories;
            $('#score').html('Score: '+game.score);
          }
          if (entity.breakSound){
            entity.breakSound.play();
          }
        } else {
          entities.draw(entity,body.GetPosition(),body.GetAngle())				
        }	
      }
    }
  },
  
  drawSlingshotBand: function() {
		game.context.strokeStyle = "rgb(68,31,11)"; // Darker brown color
		game.context.lineWidth = 6; // Draw a thick line

		// Use angle hero has been dragged and radius to calculate coordinates of edge of hero wrt. hero center
		var radius = game.currentHero.GetUserData().radius;
		var heroX = game.currentHero.GetPosition().x*box2d.scale;
		var heroY = game.currentHero.GetPosition().y*box2d.scale;			
		var angle = Math.atan2(game.slingshotY+25-heroY,game.slingshotX+50-heroX);	
	
		var heroFarEdgeX = heroX - radius * Math.cos(angle);
		var heroFarEdgeY = heroY - radius * Math.sin(angle);
	
	
	
		game.context.beginPath();
		// Start line from top of slingshot (the back side)
		game.context.moveTo(game.slingshotX+50-game.offsetLeft, game.slingshotY+25);	

		// Draw line to center of hero
		game.context.lineTo(heroX-game.offsetleft,heroY);
		game.context.stroke();		
	
		// Draw the hero on the back band
		entities.draw(game.currentHero.GetUserData(),game.currentHero.GetPosition(),game.currentHero.GetAngle());
			
		game.context.beginPath();		
		// Move to edge of hero farthest from slingshot top
		game.context.moveTo(heroFarEdgeX-game.offsetleft,heroFarEdgeY);
	
		// Draw line back to top of slingshot (the front side)
		game.context.lineTo(game.slingshotX-game.offsetleft +10,game.slingshotY+30)
		game.context.stroke();
  },
  
  countHeroesAndVillains: function() {
    game.heroes = [];
    game.villains = [];
    for(var body = box2d.world.GetBodyList(); body; body = body.GetNext()) {
      var entity = body.GetUserData();
      if (entity) {
        if (entity.type == "hero") {
          game.heroes.push(body);
        } else if (entity.type == "villain") {
          game.villains.push(body);
        }
      }
    }
  },
  
  handlePanning: function() {
    if (game.mode == "intro") {
      if (game.panTo(700)) {
        game.mode = "load-next-hero";
      }
    }
    
    if (game.mode == "wait-for-firing") {
      if (mouse.dragging) {
        if (game.mouseOnCurrentHero()) {
          game.mode = "firing";
        } else {
          game.panTo(mouse.x + game.offsetleft);
        }
      } else {
        game.panTo(game.slingshotX);
      }
    }
    
    if (game.mode == "firing") {
      if (mouse.down) {
        game.panTo(game.slingshotX);
        game.currentHero.SetPosition({
          x:(mouse.x+game.offsetleft)/box2d.scale, 
          y:mouse.y/box2d.scale
        });
      } else {
        game.mode = "fired";
        game.slingshotReleaseSound.play();
        var impluseScaleFactor = 0.75;
        var impluse = new b2Vec2(
          (game.slingshotX+35-mouse.x-game.offsetleft) * impluseScaleFactor, 
          (game.slingshotY + 25 - mouse.y) * impluseScaleFactor);
        game.currentHero.ApplyImpulse(impluse, game.currentHero.GetWorldCenter());
      }
    }
    
    if (game.mode == "fired") {
      var heroX = game.currentHero.GetPosition().x * box2d.scale;
      game.panTo(heroX);
      
      if (!game.currentHero.IsAwake() || heroX<0 || heroX > game.currentLevel.foregroundImage.width) {
        box2d.world.DestroyBody(game.currentHero);
        game.currentHero = undefined;
        game.mode = "load-next-hero";
      }
    }
    
    if (game.mode == "load-next-hero") {
      game.countHeroesAndVillains();
      
      if (game.villains.length == 0) {
        game.mode = "level-success";
        return;
      }
      
      if (game.heroes.length == 0) {
        game.mode = "level-failure";
        return;
      }
      
      if (!game.currentHero) {
        game.currentHero = game.heroes[game.heroes.length - 1];
        game.currentHero.SetPosition({x:180/box2d.scale, y:200/box2d.scale});
        game.currentHero.SetLinearVelocity({x:0, y:0});
        game.currentHero.SetAngularVelocity(0);
        game.currentHero.SetAwake(true);
      } else {
        game.panTo(game.slingshotX);
        if (!game.currentHero.IsAwake()) {
          game.mode = "wait-for-firing";
        }
      }
    }
    
    if (game.mode == "level-success" || game.mode == "level-failure") {
      if (game.panTo(0)) {
        game.ended = true;
        game.showEndingScreen();
      }
    }
  },
  
  panTo: function(newCenter) {
    if (Math.abs(newCenter - game.offsetleft - game.canvas.width/4) > 0 &&
        game.offsetleft <= game.maxoffset && game.offsetleft >= game.minoffset) {
      var deltaX = Math.round((newCenter - game.offsetleft - game.canvas.width/4) * 0.5);
      if (deltaX && Math.abs(deltaX) > game.maxSpeed) {
        deltaX = game.maxspeed * Math.abs(deltaX) / deltaX;
      } 
      game.offsetleft += deltaX;
    } else {
      return true;
    }
    
    if (game.offsetleft < game.minoffset) {
      game.offsetleft = game.minoffset;
      return true;
    } else if (game.offsetleft > game.maxoffset) {
      game.offsetleft = game.maxoffset;
      return true;
    }
    return false;
  },
  
  mouseOnCurrentHero: function() {
    if (!game.currentHero) {
      return false;
    }
    var position = game.currentHero.GetPosition();
    var distanceSquared = 
      Math.pow(position.x * box2d.scale - mouse.x - game.offsetleft, 2) +
      Math.pow(position.y * box2d.scale - mouse.y, 2);
    var radiusSquared = Math.pow(game.currentHero.GetUserData().radius, 2);
//    var isover = distanceSquared <= radiusSquared;
//    if (isover){
//      alert("OMG!");
//    }
    return distanceSquared <= radiusSquared;
  }
};

var levels = {
  data: [
    {   // First level 
      foreground:'desert-foreground',
      background:'clouds-background',
      entities:[
        {type:"ground", name:"dirt", x:500,y:440,width:1000,height:20,isStatic:true},
        {type:"ground", name:"wood", x:185,y:390,width:30,height:80,isStatic:true},

        {type:"block", name:"wood", x:520,y:380,angle:90,width:100,height:25},
        {type:"block", name:"glass", x:520,y:280,angle:90,width:100,height:25},								
        {type:"villain", name:"burger",x:520,y:205,calories:590},

        {type:"block", name:"wood", x:620,y:380,angle:90,width:100,height:25},
        {type:"block", name:"glass", x:620,y:280,angle:90,width:100,height:25},								
        {type:"villain", name:"fries", x:620,y:205,calories:420},				

        {type:"hero", name:"orange",x:80,y:405},
        {type:"hero", name:"apple",x:140,y:405},
      ]
    },
    {   // Second level 
      foreground:'desert-foreground',
      background:'clouds-background',
      entities:[
        {type:"ground", name:"dirt", x:500,y:440,width:1000,height:20,isStatic:true},
        {type:"ground", name:"wood", x:185,y:390,width:30,height:80,isStatic:true},

        {type:"block", name:"wood", x:820,y:380,angle:90,width:100,height:25},
        {type:"block", name:"wood", x:720,y:380,angle:90,width:100,height:25},
        {type:"block", name:"wood", x:620,y:380,angle:90,width:100,height:25},
        {type:"block", name:"glass", x:670,y:317.5,width:100,height:25},
        {type:"block", name:"glass", x:770,y:317.5,width:100,height:25},				

        {type:"block", name:"glass", x:670,y:255,angle:90,width:100,height:25},
        {type:"block", name:"glass", x:770,y:255,angle:90,width:100,height:25},
        {type:"block", name:"wood", x:720,y:192.5,width:100,height:25},	

        {type:"villain", name:"burger",x:715,y:155,calories:590},
        {type:"villain", name:"fries",x:670,y:405,calories:420},
        {type:"villain", name:"sodacan",x:765,y:400,calories:150},

        {type:"hero", name:"strawberry",x:30,y:415},
        {type:"hero", name:"orange",x:80,y:405},
        {type:"hero", name:"apple",x:140,y:405},
      ]
    }
  ],
  
  // initialize level select screen
  init: function(){
    var html = "";
    for(var i = 0; i < levels.data.length; i++) {
      var level = levels.data[i];
      html += '<input type="button" value="'+(i+1)+'" />';
    }
    $('#levelselectscreen').html(html);
    
    // set the button click even handlers to load level
    $('#levelselectscreen input').click(function(){
      levels.load(this.value - 1);
      $('#levelselectscreen').hide();
    });
  },
  
  load: function(number) {
    
    box2d.init();
    
    game.currentLevel = {number: number, hero:[]};
    game.score = 0;
    $("#score").html("Score: "+game.score);
    game.currentHero = undefined;
    var level = levels.data[number];
    
    // load the background, foreground, and slingshot images
    game.currentLevel.backgroundImage = loader.loadImage("images/backgrounds/"+level.background+".png");
    game.currentLevel.foregroundImage = loader.loadImage("images/backgrounds/"+level.foreground+".png");
    game.slingshotImage = loader.loadImage("images/slingshot.png");
    game.slingshotFrontImage = loader.loadImage("images/slingshot-front.png");
    
    for(var i = level.entities.length - 1; i >= 0; i--) {
      var entity = level.entities[i];
      entities.create(entity);
    }
    
    // call game.start() once the assets have loaded
    if (loader.loaded) {
      game.start();
    } else {
      loader.onload = game.start;
    }
  }
};

var entities = {
  definitions: {
    "glass": {
      fullHealth: 100,
      density: 2.4,
      friction: 0.4,
      restitution: 0.15
    },
    "wood": {
      fullHealth: 500,
      density: 0.7,
      friction: 0.4,
      restitution: 0.4
    },
    "dirt": {
      density: 3.0,
      friction: 1.5,
      restitution: 0.2
    },
    "burger": {
      shape: "circle",
      fullHealth: 40,
      radius: 25,
      density: 1.0,
      friction: 0.5,
      restitution: 0.4
    },
    "sodacan": {
      shape: "rectangle",
      fullHealth: 80,
      width: 40,
      height: 60,
      density: 1.0,
      friction: 0.5,
      restitution: 0.7
    },
    "fries": {
      shape: "rectangle",
      fullHealth: 80,
      width: 40,
      height: 50,
      density: 1.0,
      friction: 0.5,
      restitution: 0.6
    },
    "apple": {
      shape: "circle",
      radius: 25,
      density: 1.0,
      friction: 0.5,
      restitution: 0.4
    },
    "orange": {
      shape: "circle",
      radius: 25,
      density: 1.5,
      friction: 0.5,
      restitution: 0.4
    },
    "strawberry": {
      shape: "circle",
      radius: 15,
      density: 2.0,
      friction: 0.5,
      restitution: 0.4
    }
  },
  
  // take the entity, create a Box2D body, and add it to the world
  create: function(entity) {
    var definition = entities.definitions[entity.name];
    if (!definition) {
      console.log("undefined entity name ["+entity.name+"]");
      return;
    }
    switch(entity.type) {
      case "block": {
        entity.health = definition.fullHealth;
        entity.fullHealth = definition.fullHealth;
        entity.shape = "rectangle";
        entity.sprite = loader.loadImage("images/entities/"+entity.name+".png");
        entity.breakSound = game.breakSound[entity.name];
        box2d.createRectangle(entity, definition);
        break;
      }
      case "ground": {
        entity.shape = 'rectangle';
        box2d.createRectangle(entity, definition);
        break;
      }
      case "hero":
      case "villain": {
        entity.health = definition.fullHealth;
        entity.fullHealth = definition.fullHealth;
        entity.shape = definition.shape;
        entity.sprite = loader.loadImage("images/entities/"+entity.name+".png");
        entity.bounceSound = game.bounceSound;
        if (definition.shape == "circle") {
          entity.radius = definition.radius;
          box2d.createCircle(entity, definition);
        } else if (definition.shape == "rectangle") {
          entity.width = definition.width;
          entity.height = definition.height;
          box2d.createRectangle(entity, definition);
        }
        break;
      }
      
      default: {
        console.log("undefined entity type ["+entity.type+"]");
        break;
      }
    }
  },
  
  draw: function(entity, position, angle) {
		game.context.translate(position.x*box2d.scale-game.offsetleft,position.y*box2d.scale);
		game.context.rotate(angle);
		switch (entity.type){
			case "block":
				game.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
						-entity.width/2-1,-entity.height/2-1,entity.width+2,entity.height+2);	
			break;
			case "villain":
			case "hero": 
				if (entity.shape=="circle"){
					game.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
							-entity.radius-1,-entity.radius-1,entity.radius*2+2,entity.radius*2+2);	
				} else if (entity.shape=="rectangle"){
					game.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
							-entity.width/2-1,-entity.height/2-1,entity.width+2,entity.height+2);
				}
				break;				
			case "ground":
				// do nothing... We will draw objects like the ground & slingshot separately
				break;
		}

		game.context.rotate(-angle);
		game.context.translate(-position.x*box2d.scale+game.offsetleft,-position.y*box2d.scale);
  }
};

var box2d = {
  scale: 30,
  
  world: false,
  
  init: function() {
    var gravity = new b2Vec2(0, 9.8);
    var allowSleep = true;
    box2d.world = new b2World(gravity, allowSleep);
    
    var debugcontext = document.getElementById("debugcanvas").getContext("2d");
    var debugdraw = new b2DebugDraw();
    debugdraw.SetSprite(debugcontext);
    debugdraw.SetDrawScale(box2d.scale);
    debugdraw.SetFillAlpha(0.3);
    debugdraw.SetLineThickness(1.0);
    debugdraw.SetFlags(b2DebugDraw.e_shapeBit | b2DebugDraw.e_jointBit);
    box2d.world.SetDebugDraw(debugdraw);
    
    var listener = new Box2D.Dynamics.b2ContactListener;
    listener.PostSolve = function(contact, impulse) {
      var body1 = contact.GetFixtureA().GetBody();
      var body2 = contact.GetFixtureB().GetBody();
      var entity1 = body1.GetUserData();
      var entity2 = body2.GetUserData();
      
      var impulseAlongNormal = Math.abs(impulse.normalImpulses[0]);
      
      if (impulseAlongNormal > 5) {
        if (entity1.health) {
          entity1.health -= impulseAlongNormal;
        }
        if (entity2.health) {
          entity2.health -= impulseAlongNormal;
        }
        
        if (entity1.bounceSound) {
          entity1.bounceSound.play();
        }
        if (entity2.bounceSound) {
          entity2.bounceSound.play();
        }
      }
    };
    box2d.world.SetContactListener(listener);
  },
  
  createRectangle: function(entity, definition) {
    var bodydef = new b2BodyDef;
    if (entity.isStatic) {
      bodydef.type = b2Body.b2_staticBody;
    } else {
      bodydef.type = b2Body.b2_dynamicBody;
    }
    bodydef.position.x = entity.x/box2d.scale;
    bodydef.position.y = entity.y/box2d.scale;
    if (entity.angle) {
      bodydef.angle = Math.PI * entity.angle / 180;
    }
    
    var fixturedef = new b2FixtureDef;
    fixturedef.density = definition.density;
    fixturedef.friction = definition.friction;
    fixturedef.restitution = definition.restitution;
    fixturedef.shape = new b2PolygonShape;
    fixturedef.shape.SetAsBox(entity.width/2/box2d.scale, entity.height/2/box2d.scale);
    
    var body = box2d.world.CreateBody(bodydef);
    body.SetUserData(entity);
    
    var fixture = body.CreateFixture(fixturedef);
    return body;
  },
  
  createCircle: function(entity, definition) {
    var bodydef = new b2BodyDef;
    if (entity.isStatic) {
      bodydef.type = b2Body.b2_staticBody;
    } else {
      bodydef.type = b2Body.b2_dynamicBody;
    }
    bodydef.position.x = entity.x/box2d.scale;
    bodydef.position.y = entity.y/box2d.scale;
    if (entity.angle) {
      bodydef.angle = Math.PI * entity.angle / 180;
    }
    
    var fixturedef = new b2FixtureDef;
    fixturedef.density = definition.density;
    fixturedef.friction = definition.friction;
    fixturedef.restitution = definition.restitution;
    fixturedef.shape = new b2CircleShape(entity.radius / box2d.scale);
    
    var body = box2d.world.CreateBody(bodydef);
    body.SetUserData(entity);
    
    var fixture = body.CreateFixture(fixturedef);
    return body;
  },
  
  step: function(timestep) {
    if (timestep > (2/60)) {
      timestep = (2/60);
    }
    
    box2d.world.Step(timestep, 8, 3);
  }
};

var b2Vec2 = Box2D.Common.Math.b2Vec2;
var b2BodyDef = Box2D.Dynamics.b2BodyDef;
var b2Body = Box2D.Dynamics.b2Body;
var b2FixtureDef = Box2D.Dynamics.b2FixtureDef;
var b2Fixture = Box2D.Dynamics.b2Fixture;
var b2World = Box2D.Dynamics.b2World;
var b2PolygonShape = Box2D.Collision.Shapes.b2PolygonShape;
var b2CircleShape = Box2D.Collision.Shapes.b2CircleShape;
var b2DebugDraw = Box2D.Dynamics.b2DebugDraw;

$(window).load(function(){
  game.init();
});
