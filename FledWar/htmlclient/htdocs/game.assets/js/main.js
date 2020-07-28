
var game = new FledWarGame();
$(window).load(function() {
  loader.init();
  render.init();
  render.set_physical_view(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  game.init();
  
});

$(window).unload(function() {
  main_socket.close();
  game.clean_up();
  
});
