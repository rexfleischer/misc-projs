<%-- 
    Document   : index
    Created on : Apr 25, 2012, 8:43:04 PM
    Author     : REx
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="javascript/library/jquery-1.7.1.js" ></script>
    <script src="javascript/library/modernizr.js" ></script>
    <script src="javascript/helper/MenuHelper.js" ></script>
    <script src="javascript/helper/Renderable.js" ></script>
    <script src="javascript/helper/Util.js" ></script>
    <script src="javascript/helper/RenderDevice.js" ></script>
    <script src="javascript/helper/RenderDeviceHelper.js" ></script>
    <script src="javascript/helper/InputDevice.js" ></script>
    <script src="javascript/helper/GameContext.js" ></script>
    <script src="javascript/helper/AssetManager.js" ></script>
    <script src="javascript/game/bballs.js" ></script>
    <script src="javascript/fled.js" ></script>
    <title>JSP Page</title>
  </head>
  <body>
    <%= "<h2>hello world!</h2>" %>
    <button type="button" value="start" onclick="canvas_app.start();" >start</button>
    <button type="button" value="stop" onclick="canvas_app.stop();" >stop</button>
    <button type="button" value="pause" onclick="canvas_app.pause();" >pause</button>

    <div id="canvas_div">
      <canvas id="game_canvas">
        Your browser does not support HTML5 canvas.
      </canvas>
    </div>
  </body>
</html>
