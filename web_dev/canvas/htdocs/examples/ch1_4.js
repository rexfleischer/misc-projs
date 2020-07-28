
function canvasApp()
{
    var guesses = 0;
    var message = "Guess The Letter From 'a' to 'z'";
    var letters = ['a','b','c','d','e','f','g','h','i','j','k','l','m',
                   'n','o','p','q','r','s','t','u','v','w','x','y','z'];
    var today = new Date();
    var letterToGuess = '';
    var higherOrLower = '';
    var lettersGuessed = [];
    var gameOver = false;
    
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    initGame();
    
    function initGame()
    {
        var letterIndex = Math.floor(Math.random() * letters.length);
        letterToGuess = letters[letterIndex];
        guesses = 0;
        lettersGuessed = [];
        gameOver = false;
        window.addEventListener('keyup', eventKeyPressed, true);
        $("#createImageData").click(createImageDataPressed);
        drawScreen();
    }
    
    function eventKeyPressed(e)
    {
        if (!gameOver)
        {
            var letterPressed = String.fromCharCode(e.keyCode);
            letterPressed = letterPressed.toLowerCase();
            guesses++;
            lettersGuessed.push(letterPressed);
            
            if (letterPressed == letterToGuess)
            {
                gameOver = true;
            }
            else
            {
                letterIndex = letters.indexOf(letterToGuess);
                guessIndex = letters.indexOf(letterPressed);
                Debugger.log(guessIndex);
                if (guessIndex < 0)
                {
                    higherOrLower = "That is not a letter";
                }
                else if(guessIndex > letterIndex)
                {
                    higherOrLower = "lower";
                }
                else
                {
                    higherOrLower = "higher";
                }
            }
            drawScreen();
        }
    }
    
    function drawScreen()
    {
        // background
        context.fillStyle = "#ffffaa";
        context.fillRect(0, 0, 500, 300);
        
        // Box
        context.strokeStyle = "#000000";
        context.strokeRect(5, 5, 490, 290);
        context.textBaseline = "top";
        
        // date
        context.fillStyle = "#000000";
        context.font = "10px _sans";
        context.fillText(today, 100, 10);
        
        // message
        context.fillStyle = "#ff0000";
        context.font = "14px _sans";
        context.fillText(message, 175, 30);
        
        // Guesses
        context.fillStyle = "#109910";
        context.font = "16px _sans";
        context.fillText("Guesses: " + guesses, 215, 50);
        
        // higher or lower
        context.fillSytle = "#000000";
        context.font = "16px _sans";
        context.fillText("higher or lower: " + higherOrLower, 150, 65);
        
        // letters guessed
        context.fillSytle = "#ff0000";
        context.font = "16px _sans";
        context.fillText("letters guessed: " + lettersGuessed.toString(), 10, 260);
        
        if (gameOver)
        {
            context.fillStyle = "#ff0000";
            context.font = "40px _sans";
            context.fillText("You Got It!", 150, 180);
        }
    }
    
    function createImageDataPressed(e)
    {
        window.open(canvas.toDataUrl(), "canvasImage", "left=0,top=0,width=" + canvas.width + 
            ", height=" + canvas.height + ", toolbar = 0, resizable=0");
    }
}