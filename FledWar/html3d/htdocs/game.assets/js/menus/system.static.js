


function main_point_nav_menu(parent, container, points, root_id) {
  
  this.router = {};
  
  this.gui = new dat.GUI({ autoPlace: false });
  this.gui.closed = true;
  
  this._setup_recurse = function(id, folder) {
    var this_folder = false;
    
    var root = points[id];
    var root_name = root.name.split("-").join("");
    
    this.router[root_name] = function(){
      parent.follow_object(root);
      console.log("menu follow: "+root_name);
    };
    
    if (root.type === PT_PLANET && root.server_data.children.length > 0) {
      this_folder = this.gui.addFolder(root_name);
      this_folder.add(this.router, root_name);
    }
    else if (folder) {
      folder.add(this.router, root_name);
    }
    else {
      this.gui.add(this.router, root_name);
    }
    
    for(var i in root.server_data.children) {
      var child_id = root.server_data.children[i];
      this._setup_recurse(child_id, this_folder);
    }
  };
  this._setup_recurse(root_id);
  
  this.element = this.gui.domElement;
  container.append(this.element);
  
  $(this.element).css({
    position : "absolute",
    top : "1%",
    left : "1%"
  });
  
  
  
  this.remove = function() {
    game.render_container.remove(this.gui.domElement);
  };
}

