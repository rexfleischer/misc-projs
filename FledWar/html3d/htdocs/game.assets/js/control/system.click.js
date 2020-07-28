

function SystemClickDefault(mouse) {
  var self = this;
  
  mouse.lclick = function() { 
    var intersects = mouse.objects_at_pointer();
    if (intersects.length > 0) {
      var first = intersects[0];
      console.log("yay! "+first.object.fled_);
    }
  };
}


SystemClickDefault.prototype.update = function(delta) {
  
};
