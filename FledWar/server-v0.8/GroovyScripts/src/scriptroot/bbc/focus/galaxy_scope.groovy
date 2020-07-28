
import org.bson.types.ObjectId;

if (!focus.scope_id) {
  throw new Exception("unable to create focus "+
              "when there is no scope_id: ${focus}");
}
if (!focus.callback) {
  throw new Exception("unable to create focus "+
              "when there is no callback: ${focus}");
}

def scope_id = (focus.scope_id instanceof ObjectId) ? 
                        focus.scope_id : new ObjectId(focus.scope_id);
def simulation = engine.getBlackboxSimulation();
def scope = simulation.getScope(scope_id);
if (!scope)
{
	throw new Exception("unable to focus on scope that does not exist: ${focus}");
}

return scope.focus(focus.callback);
