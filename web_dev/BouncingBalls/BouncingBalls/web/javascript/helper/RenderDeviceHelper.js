
function rdh_get_default_batcher(clear)
{
  return new BatchRender(
    null, 
    function(context, object)
    {
      object.render(context);
    }, 
    null,
    clear);
}
