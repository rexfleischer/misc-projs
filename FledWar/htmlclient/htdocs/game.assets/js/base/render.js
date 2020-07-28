
var render = {
  
  bg_target : RENDER_DEVICE_BG,
  bg_canvas : undefined,
  bg_context : undefined,
  bg_clear_color : "#272727",
  
  fg_target : RENDER_DEVICE_FG,
  fg_canvas : undefined,
  fg_context : undefined,
  fg_clear_color : "rgba(0, 0, 0, 0)",
  
  screen_width : undefined,
  screen_height : undefined,
  screen_aspect : undefined,
    
  user_view_width : undefined,
  user_center_x : undefined,
  user_center_y : undefined,
  
  
  init : function() {
    render.bg_canvas = $("#"+render.bg_target)[0];
    render.bg_context = render.bg_canvas.getContext('2d');
    render.fg_canvas = $("#"+render.fg_target)[0];
    render.fg_context = render.fg_canvas.getContext('2d');
    render.clear();
  },
  
  pixels_to_game_length : function(pixels) {
    return (render.user_view_width / render.screen_width) * pixels;
  },
    
  game_to_pixels_length : function(meters) {
    return (render.screen_width / render.user_view_width) * meters;
  },
    
  pixel_x_to_game_x : function(pixel_x) {
    var pixel_diff = (render.screen_width*0.5 - pixel_x);
    var game_diff = render.pixels_to_game_length(pixel_diff);
    return -(game_diff + render.user_center_x*2);
  },
    
  pixel_y_to_game_y : function(pixel_y) {
    var pixel_diff = (render.screen_height*0.5 - pixel_y);
    var game_diff = render.pixels_to_game_length(pixel_diff);
    return -(game_diff + render.user_center_y*2);
  },
  
  
  
  
  clear : function() {
    render.bg_clear();
    render.fg_clear();
  },
  
  bg_clear : function() {
    render.bg_context.save();
        
    render.bg_context.setTransform(1, 0, 0, 1, 0, 0);
    render.bg_context.clearRect(0, 0, render.screen_width, render.screen_height);
    render.bg_context.fillStyle = render.bg_clear_color;
    render.bg_context.fillRect(0, 0, render.screen_width, render.screen_height);
        
    render.bg_context.restore();
  },
  
  fg_clear : function() {
    render.fg_context.save();
        
    render.fg_context.setTransform(1, 0, 0, 1, 0, 0);
    render.fg_context.clearRect(0, 0, render.screen_width, render.screen_height);
    render.fg_context.fillStyle = render.fg_clear_color;
    render.fg_context.fillRect(0, 0, render.screen_width, render.screen_height);
        
    render.fg_context.restore();
  },
  
  set_physical_view : function(width, height) {
    render.bg_canvas.width = width;
    render.bg_canvas.height = height;
    render.fg_canvas.width = width;
    render.fg_canvas.height = height;
        
    render.screen_width = width;
    render.screen_height = height;
    render.screen_aspect = (width / height);
  },
    
  ready_transform : function() {
    
    // pre compute some values that will be reused
    var h_view_width = (render.user_view_width * 0.5);
    var screen_dx = (render.user_center_x / h_view_width) 
          * render.screen_width;
    var screen_dy = (render.user_center_y / h_view_width * render.screen_aspect) 
          * render.screen_height;
    var width_aspect = (render.screen_width / h_view_width * 0.5)
    var inverse_width = (1 / h_view_width);
    
    // compute the final values to be shared between the 
    // two context objects
    var t_width = inverse_width * render.screen_width * 0.5;
    var t_height = inverse_width * render.screen_width * 0.5;
    var d_width = (h_view_width * width_aspect) 
          + screen_dx;
    var d_height = (h_view_width * width_aspect / render.screen_aspect)
          + screen_dy;
    
    // now actually set the values
    render.bg_context.setTransform(t_width, 0, 0, t_height, d_width, d_height);
    render.fg_context.setTransform(t_width, 0, 0, t_height, d_width, d_height);
  }
};
