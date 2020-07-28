
(function(){
  var lastTime = 0;
  var vendors = ["ms", "moz", "webkit", "o"];
  for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
    window.requestAnimationFrame = window[vendors[x] + "RequestAnimationFrame"];
    window.cancelAnimationFrame = 
      window[vendors[x] + "cancelAnimationFrame"] || 
      window[vendors[x] +  "CancelRequestAnimationFrame"];
  }
  
  if (!window.requestAnimationFrame) {
    window.requestAnimationFrame = function(callback, element) {
      var currTime = new Date().getTime();
      var timeToCall = Math.max(0, 16 - (currTime - lastTime));
      var id = window.setTimeout(
            function(){callback(currTime + timeToCall);}, 
            timeToCall);
      lastTime = (currTime + timeToCall);
      return id;
    }
  }
  if (!window.cancelAnimationFrame) {
    window.cancelAnimationFrame = function(id) {
      clearTimeout(id);
    }
  }
}());


var loader = {
  loaded: true,
  loadedCount: 0, // assets that have been loaded so far
  totalCount:0, // total number of assets that need to be loaded
  soundFileExtn: ".ogg",
  onload: undefined,
  
  init: function() {
    // cheeck for sound support
    var mp3Support, oggSupport;
    var audio = document.createElement("audio");
    if (audio.canPlayType) {
      // currently canPlayType returns: "", "maybe", or "probably"
      mp3Support = ("" != audio.canPlayType('audio/mpeg'));
      oggSupport = ("" != audio.canPlayType('audio/ogg; codecs = "vorbis"'))
    } else {
      mp3Support = false;
      oggSupport = false;
    }
    
    if (oggSupport) {
      loader.soundFileExtn = ".ogg";
    } else if (mp3Support) {
      loader.soundFileExtn = ".mp3";
    } else {
      loader.soundFileExtn = undefined;
    }
    
  },
  
  loadImage: function(url) {
    this.totalCount++;
    this.loaded = false;
    $('#loadingscreen').show();
    var image = new Image();
    image.src = url;
    image.onload = loader.itemLoaded;
    return image;
  },
  
  loadSound: function(url) {
    this.totalCount++;
    this.loaded = false;
    $("#loadingscreen").show();
    var audio = new Audio();
    audio.src = (url + loader.soundFileExtn);
    audio.addEventListener("canplaythrough", loader.itemLoaded, false);
    return audio;
  },
  
  itemLoaded: function() {
    loader.loadedCount++;
    $('#loadingmessage').html("Loaded "+loader.loadedCount+" of "+loader.totalCount);
    if (loader.loadedCount === loader.totalCount) {
      // loader has loaded completely
      loader.loaded = true;
      $("#loadingscreen").hide();
      // and call the loader.onload method if it exists
      if (loader.onload) {
        loader.onload();
        loader.onload = undefined;
      }
    }
  }
};

function loadItem(name) {
  var item = this.list[name];
  
  // if the item sprite array has already been loaded
  // then no need to do it again
  if (item.spriteArray) {
    return;
  }
  item.spriteSheet = loader.loadImage("images/"+this.defaults.type+"/")
}