
var AN_IMPULSE = "impulse";

function factory_action(base_action) {
  
  var result = null;
  switch(base_action.type) {
    
    case AN_IMPULSE:
      
      break;
      
    default:
      console.log("unknown action type: "+base_action);
      break;
  }
  return result;
}


function ActionCollection(actions) {
  
}


function BaseAction(base_action) {
  this.view_type      = "action";
  this.server_updates = 0;
  this.server_data    = base_action;
  this._id            = (base_action ? base_action._id : null);
  this.type           = (base_action ? base_action.type : null);
}
