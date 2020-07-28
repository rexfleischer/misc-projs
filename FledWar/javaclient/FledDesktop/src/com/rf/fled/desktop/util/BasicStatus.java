/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.util;

import com.rf.fled.desktop.game.GameTime;
import com.rf.fled.desktop.graphics.Text;
import com.rf.fledwar.javaclient.connection.ClientConnection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public class BasicStatus
{
    public static final int BYTES_TO_MB = 1024 * 1024;
    
    public static final int X_OFFSET = 20;
    
    public static final int Y_OFFSET = 40;
    
    public static final int Y_DELTA = 15;
    
    NumberFormat format = NumberFormat.getInstance();
    
    Text fps;
    
    Text connected;
    
    Text connectionstate;
    
    Text memory;
    
    ArrayList<Text> attached;
    
//    Text mouse;
//    
//    Text systems;
//    
//    Text view_x;
//    
//    Text view_y;
//    
//    Text view_w;
//    
//    Text view_h;

    public BasicStatus(ClientConnection client, Color color)
    {
        fps             = new Text(X_OFFSET, Y_OFFSET + Y_DELTA * 0, color, "");
        connected       = new Text(X_OFFSET, Y_OFFSET + Y_DELTA * 1, color, 
                String.format("connected to: %s", client.getConnectionAddress()));
        connectionstate = new Text(X_OFFSET, Y_OFFSET + Y_DELTA * 2, color, "");
        memory          = new Text(X_OFFSET, Y_OFFSET + Y_DELTA * 3, color, "");
        attached = new ArrayList<>();
    }
    
    public void attach(Text text)
    {
        text.setX(X_OFFSET);
        text.setY(Y_OFFSET + Y_DELTA * (4 + attached.size()));
        text.setColor(fps.getColor());
        attached.add(text);
    }

    public void update(GameTime gametime, ClientConnection client)
    {
        fps.setMessage(String.format("fps: %s", gametime.getActualFPS()));
        connectionstate.setMessage(String.format("connection state: %s", 
                                                 client.getConnectionState()));
        Runtime runtime = Runtime.getRuntime();
        long allocatedMemory = runtime.totalMemory();
        memory.setMessage(String.format("allocated memory: %s MB",
                                        format.format(allocatedMemory / BYTES_TO_MB)));
        
//        this.mouse.setMessage(String.format("mouse [x:%s, y:%s, window:%s]", 
//                                            mouse.getX(), 
//                                            mouse.getY(),
//                                            mouse.isOverWindow()));
//        if (viewport.getView() != null)
//        {
//            Rectangle2D view = viewport.getView();
//            view_x.setMessage(String.format("viewport: x -> %s", view.getX()));
//            view_y.setMessage(String.format("          y -> %s", view.getY()));
//            view_w.setMessage(String.format("          w -> %s", view.getWidth()));
//            view_h.setMessage(String.format("          h -> %s", view.getHeight()));
//        }
    }

    public void render(Graphics2D graphics)
    {
        fps.render(graphics);
        connected.render(graphics);
        connectionstate.render(graphics);
        memory.render(graphics);
        for(Text text : attached)
        {
            text.render(graphics);
        }
    }
}
