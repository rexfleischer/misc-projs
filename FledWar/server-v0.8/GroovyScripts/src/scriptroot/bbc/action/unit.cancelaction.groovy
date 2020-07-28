
/**
 * input vars:
 * - connection
 * - engine
 * - user
 * - action
**/


if (!action.scope || !action.unit_id || !action.action_id) {
  throw new Exception("scope, unit_id, and action_id must be set");
}

return connection.command("feature/unit/cancelaction.groovy", [
  scope : action.scope,
  unit_id : action.unit_id,
  action_id : action.action_id,
]);
