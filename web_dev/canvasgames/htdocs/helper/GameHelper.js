
/**
 * this keeps track of how many frame per second there are.
 * it relys on something calling tick() every frame though.
 */
function FrameRateCounter(x, y, color, font)
{
    this.x = x;
    this.y = y;
    this.color = color;
    this.font = font;
    
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
    
    this.render = function(context)
    {
        context.fillStyle    = this.color;
        context.font         = this.font;
        context.textBaseline = "top";
        context.fillText("FPS: " + this.last_frame_count, this.x, this.y);
    }
}

/**
 * key helper. basically you put in the function that you want 
 * to be called when a key is hit. for instance, if you wanted
 * something to happen on the a, w, s, d keys. then it would look like this:
 * 
 * var key_input = new KeyActivity();
 * key_input.register(KEY_A, function(event){...});
 * key_input.register(KEY_W, function(event){...});
 * key_input.register(KEY_S, function(event){...});
 * key_input.register(KEY_D, function(event){...});
 */
function KeyActivity()
{
    var inputs = new Array();
    
    $("body").keypress(function(event){
        
        if (typeof inputs[event.keyCode] != "undefined")
        {
            var func = inputs[event.keyCode];
            func(event);
        }
    });
    
    this.register = function(code, func)
    {
        inputs[code] = func;
    }
}

var KEY_BACKSPACE   = 8;
var KEY_TAB         = 9;
var KEY_ENTER       = 13;
var KEY_SHIFT       = 16;
var KEY_CTRL        = 17;
var KEY_ALT         = 18;
var KEY_PAUSE       = 19;
var KEY_ESCAPE      = 20;
var KEY_PAGE_UP     = 33;
var KEY_PAGE_DOWN   = 34;
var KEY_END         = 35;
var KEY_HOME        = 36;
var KEY_ARROW_LEFT  = 37;
var KEY_ARROW_UP    = 38;
var KEY_ARROW_RIGHT = 39;
var KEY_ARROW_DOWN  = 40;
var KEY_DELETE      = 46;
var KEY_0           = 48;
var KEY_1           = 49;
var KEY_2           = 50;
var KEY_3           = 51;
var KEY_4           = 52;
var KEY_5           = 53;
var KEY_6           = 54;
var KEY_7           = 55;
var KEY_8           = 56;
var KEY_9           = 57;
var KEY_A           = 65;
var KEY_B           = 66;
var KEY_C           = 67;
var KEY_D           = 68;
var KEY_E           = 69;
var KEY_F           = 70;
var KEY_G           = 71;
var KEY_H           = 72;
var KEY_I           = 73;
var KEY_J           = 74;
var KEY_K           = 75;
var KEY_L           = 76;
var KEY_M           = 77;
var KEY_N           = 78;
var KEY_O           = 79;
var KEY_P           = 80;
var KEY_Q           = 81;
var KEY_R           = 82;
var KEY_S           = 83;
var KEY_T           = 84;
var KEY_U           = 85;
var KEY_V           = 86;
var KEY_W           = 87;
var KEY_X           = 88;
var KEY_Y           = 89;
var KEY_Z           = 90;

/**
 * takes care of the mouse activity 
 */
function MouseActivity(target_id)
{
    var x = 0;
    var y = 0;
    
    var moved = false;
    var down  = false;
    
    var click   = function(x, y){};
    var drag    = function(x, y){};
    var enddrag = function(x, y){};
    
    this.getX = function()
    {
        return x;
    }
    
    this.getY = function()
    {
        return y;
    }
    
    this.registerDrag = function(func)
    {
        drag = func;
    }
    
    this.registerEndDrag = function(func)
    {
        enddrag = func;
    }
    
    this.registerClick = function(func)
    {
        click = func;
    }
    
    $("#" + target_id).mousemove(function(event){
        x = event.pageX - this.offsetLeft;
        y = event.pageY - this.offsetTop;
        moved = true;
        if (down)
        {
            drag(x, y);
        }
    });
    
    $("#" + target_id).mousedown(function(event){
        event.preventDefault();
        moved = false;
        down = true;
    });
    
    $("#" + target_id).mouseup(function(event){
        event.preventDefault();
        down = false;
        if (!moved)
        {
            click(x, y);
        }
        else
        {
            enddrag(x, y);
        }
    });
}

/**
 * 
 */
function FledViewport(width, height)
{
    this.loc_x = 0;
    this.loc_y = 0;
    
    this.logical_width  = width;
    this.logical_height = height;
    
    this.client_width   = width;
    this.client_height  = height;
}

/**
 * a collection of helpers for the game
 */
function GameContext(width, height, target_id)
{
    this.timer  = new FrameRateCounter(20, 20, "#000000", "20px _sans");
    this.keys   = new KeyActivity();
    this.mouse  = new MouseActivity(target_id);
    this.view   = new FledViewport(width, height);
}