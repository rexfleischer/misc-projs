
/**
 * this is where menus can be implemented easily. it takes
 * the input of an xml string and builds a menu based
 * on triggers and visuals. an important part of this 
 * is there is a cursor within the xml file that defines
 * where in a menu someone is. this means that if someone 
 * wantes to move back to a previous menu, it should be 
 * trivial to implement.
 */
function MenuHelper(game, menu_id, render_index)
{
  var _game = game;
  var _cursor = "start";
  var _pages = new Object();
  var _menu_id = menu_id;
  var _base_posx = 0;
  var _base_posy = 0;
  
  _game.mouse.registerClick("menu"+_menu_id, function(x, y){
    var current_page = _pages[_cursor];
    var response = current_page.onclick(x, y, _base_posx, _base_posy);
//    alert("x:"+x+", y:"+y+", _base_posx:"+_base_posx+", _base_posy:"+_base_posy+", x total:"+(x - _base_posx)+", y total:"+(y - _base_posy)+", response:"+response);
//    alert(response);
    if (!response)
    {
      return;
    }
    
  });
  
  _game.render.batches.add_at_index(render_index, "menu"+_menu_id, this);
  
  this.register_page = function(name, page)
  {
    _pages[name] = page;
  }
  
  this.set_position = function(x, y)
  {
    _base_posx = x;
    _base_posy = y;
  }
  
  this.release = function()
  {
    _game.mouse.removeClick("menu"+_menu_id);
    _game.render.batches.remove("menu"+_menu_id);
  }
  
  /**
   * the render function to make this workable
   * with the RenderDevice object
   */
  this.flush = function(context)
  {
    var current_page = _pages[_cursor];
    
    context.save();
    current_page.render(context, _base_posx, _base_posy);
    context.restore();
  }
}

function MenuPage()
{
  this.options = new Array();
  
  this.render = function(context, _base_posx, _base_posy)
  {
    var i;
    for(i = 0; i < this.options.length; i++)
    {
      this.options[i].render(context, _base_posx, _base_posy);
    }
  }
  
  this.onclick = function(x, y, basex, basey)
  {
    var i;
    for(i = 0; i < this.options.length; i++)
    {
      var o = this.options[i];
      var response = o.onclick(x, y, basex, basey);
      if (response !== false)
      {
        return response;
      }
    }
    return false;
  }
}

function MenuOption()
{
  this.props = new Object();
  this.posx = 0;
  this.posy = 0;
  this.width = 0;
  this.height = 0;
  this.render = null;
  this.onclick = null;
}
