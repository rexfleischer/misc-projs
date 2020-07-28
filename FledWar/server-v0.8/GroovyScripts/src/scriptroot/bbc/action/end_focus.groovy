
if (action.drop_key == null)
{
	throw new Exception("unable to process end focus without a drop key");
}

def focus_drops = connection.getFocusDrops();
synchronized(focus_drops)
{
	if (!focus_drops.containsKey(action.drop_key))
	{
		throw new Exception("drop key not found: ${action.drop_key}");
	}
	focus_drops.remove(action.drop_key).cancel();
}

return [
	"dropped" : action.drop_key
];