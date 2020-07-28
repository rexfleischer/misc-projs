
var SYSTEMNAV_ZOOM = 100000000000000;
var SYSTEMNAV_ZOOM_I = (1 / SYSTEMNAV_ZOOM);
var SYSTEMNAV_AU = (AU / SYSTEMNAV_ZOOM);
var SYSTEMNAV_LY = (LIGHTYEAR / SYSTEMNAV_ZOOM);

function SystemNavFocus() {
    
  var curr = this;
  
  this.initialized = false;
  
  this.system_handler = new SystemNavFocus_Systems(this);
  
  
  
  
  
  
  /**
   * the grid
   */
  render.clear();
//  var grid = new CanvasGrid(50, 
//          "rgba(0, 0, 0, .2)",
//          fg_render.screen_width,
//          fg_render.screen_height);
//  grid.render(fg_render.context);
  
  /**
   * initial render view
   */
  render.user_view_width = SYSTEMNAV_LY * 50;
  render.user_center_x = 0;
  render.user_center_y = 0;
    
    
    
  // now lets set up the mouse activity for the rendering targets
  mouse.ldrag = function() {
    
    if (mouse.start_x == undefined ||
        mouse.start_y == undefined) {
      mouse.origin_x = render.user_center_x;
      mouse.origin_y = render.user_center_y;
      mouse.start_x = render.pixels_to_game_length(mouse.drag_start_x);
      mouse.start_y = render.pixels_to_game_length(mouse.drag_start_y);
    }
    
    var new_x = render.pixels_to_game_length(mouse.x);
    var new_y = render.pixels_to_game_length(mouse.y);
    var new_dx = (mouse.start_x - new_x) * 0.5;
    var new_dy = (mouse.start_y - new_y) * 0.5;
    
    var center_x = (mouse.origin_x - new_dx) * SYSTEMNAV_ZOOM;
    var center_y = (mouse.origin_y - new_dy) * SYSTEMNAV_ZOOM;
    
    curr.system_handler.set_focus_center(center_x, center_y);
  }
  mouse.lenddrag = function() {
    mouse.start_x = undefined;
    mouse.start_y = undefined;
    mouse.origin_x = undefined;
    mouse.origin_y = undefined;
  }
  mouse.lclick_miss = function() {
    
  }
  
  
  
  
  // now its time for the menu
  this.static_menu_init = function() {
    menus.enter(systemnav_main_json_nav_status, null, true);
  }
  
  
  
  
  
  this.clean_up = function() {
    
  }
  
  this.game_tick = function() {
    
    if (!this.system_handler.ready) {
      return;
    }
    
    if (!this.initialized) {
      this.initialized = true;
      this.static_menu_init();
      dialog.hide();
    }
    
    
    this.system_handler.update();
    menus.update();
    
    
    render.clear();
    render.ready_transform();
    
    
    this.system_handler.render();
  }
}

