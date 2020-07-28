

var menus = {
  
  menus : [],
  
  update : function() {
    for(var i = 0; i < menus.menus.length; i++) {
      if (menus.menus[i].update) {
        menus.menus[i].update();
      }
    }
  },
  
  enter : function(json, extra, animate) {
    var new_menu = new Menu(json, extra);
    menus.exit(json.id, animate);
    menus.menus.push(new_menu);
    new_menu.enter(animate);
  },
  
  exit : function(id, animate) {
    for(var i = 0; i < menus.menus.length; i++) {
      var check = menus.menus[i];
      if (check.menu_id === id) {
        menus.menus.splice(i, 1);
        check.exit(animate);
        i--;
      }
    }
  },
  
  toggle : function(json, extra, animate) {
    if (menus.contains(json.id)) {
      menus.exit(json.id, animate);
    }
    else {
      menus.enter(json, extra, animate);
    }
  },
  
  get : function(id) {
    for(var i = 0; i < menus.menus.length; i++) {
      var check = menus.menus[i];
      if (check.menu_id === id) {
        return check;
      }
    }
    return null;
  },
  
  contains : function(id) {
    return (menus.get(id) !== null);
  },
  
  reset : function(animate) {
    while(menus.menus.length !== 0) {
      var menu = menus.menus.pop();
      menu.exit(animate);
    }
  }
};




function Menu(json_data, extra) {
  
  json_data = $.extend({}, json_data);
  
  this.extra = extra;
  this.json_data = json_data;
  this._update = json_data.update;
  this._create = json_data.create;
  this._destroy = json_data.destroy;
  this.menu_elements = [];
  
  this.menu_id = json_data.id;
  
  this.menu_div = $('<div></div>');
  this.menu_div.attr("id", json_data.id);
  this.menu_div.css(json_data.start_css);
  
  if (json_data.classes) {
    this.menu_div.addClass(json_data.classes);
  }
  
  if (json_data.type === "static") {
    this.menu_div.addClass("gamemenucontainer");
  }
  else if (json_data.type === "popup") {
    this.menu_div.addClass("popupmenucontainer");
    this.menu_div.addClass("ui-widget-content");
    this.menu_div.addClass("ui-dialog");
    this.menu_div.addClass("ui-dialog-content");
    this.toolbar_element = menu_from_json(popup_menu_toolbar, this);
    this.menu_div.append(this.toolbar_element);
    this.menu_elements.push(this.toolbar_element);
  }
  else {
    throw "unknown type for json menu definition: "+json_data.type;
  }
  
  for(var i = 0; i < json_data.content.length; i++) {
    var part = json_data.content[i];
    if (typeof(part.condition) === "function" &&
        !part.condition(this)) {
      continue;
    }
    var new_element = menu_from_json(part, this);
    if (new_element) {
      this.menu_div.append(new_element);
      this.menu_elements.push(new_element);
      if (part.focus) {
        new_element.focus(part.focus);
      }
      if (part.blur) {
        new_element.blur(part.blur);
      }
      
      if (part.cols && part.rows) {
        new_element.attr("cols", resolve_value(part.cols));
        new_element.attr("rows", resolve_value(part.rows));
      }
    }
  }
  
  
  
  this.update = function() {
    if (this._update) {
      this._update();
    }
    for(var i = 0; i < this.menu_elements.length; i++) {
      if (this.menu_elements[i].update) {
        this.menu_elements[i].update();
      }
    }
  };
    
  this.enter = function(animate) {
    $("#"+MENU_CONTAINER).append(this.menu_div);
        
    if (this._create) {
      this._create();
    }
    
    if (json_data.enter_css) {
      if (animate && json_data.animate) {
        this.menu_div.animate(json_data.enter_css);
      }
      else {
        this.menu_div.css(json_data.enter_css);
      }
    }
  };
    
  this.exit = function(animate) {
    if (this._destroy) {
      this._destroy();
    }
    
    if (animate && json_data.animate) {
      this.menu_div.animate(
          json_data.start_css, {
            complete : function() {
              $("#"+json_data.id).remove();
            }
          }
        );
    }
    else {
      $("#"+json_data.id).remove();
    }
  };
}



function menu_from_json(json, parent_menu) {
  var element = $("<div></div>");
  element.attr("id", resolve_value(json.id, parent_menu));
    
  switch(json.type) {
    case "slider":
      menu_slider_from_json(element, json, parent_menu);
      break;

    case "button":
      menu_button_from_json(element, json, parent_menu);
      break;

    case "text":
      menu_text_from_json(element, json, parent_menu);
      break;
      
    case "textarea":
      element = $("<textarea></textarea>");
      element.attr("id", resolve_value(json.id, parent_menu));
      menu_text_from_json(element, json, parent_menu);
      break;

    case "toolbar":
      menu_toolbar_from_json(element, json, parent_menu);
      break;

    default:
      log("unknown menu type: " + JSON.stringify(json));
  }
  return element;
}

function menu_button_from_json(element, json, parent_menu) {
  element.button( proxy_object(json.input, parent_menu) );
  element.click( $.proxy(json.click, parent_menu, json) );
  if (json.update) {
    element.update = $.proxy(json.update, parent_menu, json);
  }
    
  if (json.classes) {
    element.addClass(json.classes);
  }
  element.removeClass("ui-widget");
}

function menu_slider_from_json(element, json, parent_menu) {
  element.slider( proxy_object(json.input, parent_menu) );
  if (json.update) {
    element.update = $.proxy(json.update, parent_menu, json);
  }
}

function menu_text_from_json(element, json, parent_menu) {
    
  if (json.value) {
    element.html(resolve_value(json.value, parent_menu));
  }
  if (json.update) {
    element.update = $.proxy(json.update, parent_menu, json);
  }
}

function menu_textarea_from_json(element, json, parent_menu) {
  
  element.textarea( proxy_object(json.input, parent_menu) );
  element.attr("cols", resolve_value(json.cols));
  element.attr("rows", resolve_value(json.rows));
  if (json.value) {
    element.html(resolve_value(json.value, parent_menu));
  }
  if (json.update) {
    element.update = $.proxy(json.update, parent_menu, json);
  }
  if (json.classes) {
    element.addClass(json.classes);
  }
  if (json.focus) {
    element.focus(json.focus);
  }
  if (json.focusout) {
    element.focusin(json.focusout);
  }
}

function menu_toolbar_from_json(element, json, parent_menu) {
  element.addClass("toolbar-helper");
  if (json.classes) {
    element.addClass(json.classes);
  }
    
  element.sub_stuff = [];
  for(var i = 0; i < json.content.length; i++) {
    var part = json.content[i];
    var new_element = menu_from_json(part, parent_menu);
    if (new_element) {
      element.append(new_element);
      element.sub_stuff.push(new_element);
    }
  }
    
  element.update = function() {
    for(var i = 0; i < element.sub_stuff.length; i++) {
      if (element.sub_stuff[i].update) {
        element.sub_stuff[i].update();
      }
    }
  };
}

