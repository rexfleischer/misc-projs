
/**
 * this will take care of getting and caching data.. this includes 
 * getting and setting cookies, images, json, xml... this trys to handle
 * anything that need to be grabbed from somewhere else.
 */
function AssetManager()
{
  
  
  this.load_xml_from_source = function(source)
  {
    $.ajax({
      type: "GET", 
      url: source,
      datatype: "xml"
    }).done(function(returnxml)
    {
      _xml = returnxml;
      _cursor = $(returnxml).find("page[name='start']");
    });
  }
}

function StatusHelper(total_work, x_center, y_center, text)
{
    this.total_work     = total_work;
    this.finished_work  = 0;
    this.x_center       = x_center;
    this.y_center       = y_center;
    this.percentage     = 0;
    this.text           = text;
    
    this.image          = new FledImage("assets/cycle_loading_2.png");
    this.image.x        = x_center;
    this.image.y        = y_center - 50;
    
    this.update = function(finished_work)
    {
        this.finished_work = finished_work;
        this.percentage = (this.finished_work / this.total_work)
        this.image.rotation = this.percentage * 30;
    }
    
    this.render = function(context)
    {
        this.image.render(context);
        
        var writing = "percent loaded: %" + Math.round(this.percentage * 100);
        this.text.text = writing;
        var width = context.measureText(text).width;
        this.text.x = this.x_center - (width / 2);
        this.text.y = this.y_center;
        this.text.render(context);
    }
}
