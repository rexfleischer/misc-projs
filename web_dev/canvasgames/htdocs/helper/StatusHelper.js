/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function StatusHelper(total_work, color, font, x_center, y_center)
{
    this.total_work     = total_work;
    this.finished_work  = 0;
    this.x_center       = x_center;
    this.y_center       = y_center;
    this.color          = color;
    this.font           = font;
    this.percentage     = 0;
    
    this.image          = new FledImage("http://localhost/canvasgames/assets/cycle_loading_2.png");
    this.image.x        = x_center;
    this.image.y        = y_center - 50;
    
    this.update = function(finished_work)
    {
        this.finished_work = finished_work;
        this.percentage = (this.finished_work / this.total_work)
        this.image.rotation = this.percentage * 30;
    }
    
    this.render = function(renderer)
    {
        this.image.render(renderer);
        
        renderer.fillStyle      = this.color;
        renderer.font           = this.font;
        renderer.textBaseline   = "top";
        renderer.fillText(
                "percent loaded: %" + Math.round(this.percentage * 100), 
                this.x_center - 85,
                this.y_center);
    }
}
