
/**
 * a collection of helpers for the game
 */
function GameContext(target_id)
{
  this.frames = 0;
  this.timer  = new FrameRateCounter(20, 20, "#000000", "20px _sans");
  this.keys   = new KeyActivity();
  this.mouse  = new MouseActivity(target_id);
  this.render = new RenderDevice(target_id);
  this.assets = new AssetManager();
}

function Viewport()
{
  
}


/**
 * this keeps track of how many frame per second there are.
 * it relys on something calling tick() every frame though.
 */
function FrameRateCounter()
{
    this.last_frame_count = 0;
    this.frame_ctr = 0;
    
    var date = new Date();
    this.frame_last = date.getTime();
    delete date;
    
    this.tick = function()
    {
        var date = new Date();
        this.frame_ctr++;

        if (date.getTime() >= this.frame_last + 1000)
        {
            this.last_frame_count = this.frame_ctr;
            this.frame_last = date.getTime();
            this.frame_ctr = 0;
        }

        delete date;
    }
}