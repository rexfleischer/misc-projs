/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app;

import com.rf.fled.desktop.app.view.GameGraphicsWindow;
import com.rf.fled.desktop.game.Game;
import com.rf.fled.desktop.game.GameException;
import com.rf.fled.desktop.game.GameTime;
import com.rf.fled.desktop.game.KeyBoardState;
import com.rf.fled.desktop.game.MouseState;
import com.rf.fled.desktop.util.MessageFrame;
import com.rf.fledwar.javaclient.connection.ClientConnection;
import com.rf.fledwar.javaclient.connection.ClientConnectionFactory;
import com.rf.fledwar.javaclient.logging.Log4JHelper;
import com.rf.fledwar.socket.connection.ConnectionState;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * 
 * @author REx
 */
public class FledApp extends Game
{
    public static Logger logger = Logger.getLogger(FledApp.class);
    
    ClientConnection client;
    
    MessageFrame messages;
    
    GameGraphicsWindow mainView;
    
    Map config;
    
    double timescale;

    @Override
    public void initiate(GameTime gametime) throws GameException
    {
        // first, just get the main game frame
        mainView = new GameGraphicsWindow(
                new Dimension(800, 600), 20, 20, 
                "FledWar Concept", 
                new Color(35, 35, 35), 
                "main");
        mainView.setKeyListener(getKeyAdapter());
        mainView.setMouseListener(getMouseAdapter());
        mainView.setWindowListener(getDefaultWindowAdapter());
        
        // setup the logging window
        messages = new MessageFrame();
        messages.setLocationRelativeTo(mainView);
        messages.setLocation(900, 0);
        messages.setVisible(true);
        messages.addMessage("logger frame started!");
        
        try
        {
            Properties properties = new Properties();
            properties.load(FledApp.class.getResourceAsStream("log4j.properties"));
            Log4JHelper.startLogger(properties);
        }
        catch(IOException ex)
        {
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            mainView.showMessage(
                    String.format("could not init log4j:\n%s", 
                                  out.toString()),
                    "Unable To Connect", 
                    JOptionPane.ERROR_MESSAGE);
            throw new GameException("could not initiate logging", ex);
        }
        
        // may want to change this
        gametime.setTargetFPS(26);
        
        
        
        // now we try to connect
        try
        {
            client = ClientConnectionFactory.getDefault(true);
        }
        catch(Throwable ex)
        {
            logger.error("could not get client", ex);
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            mainView.showMessage(                 
                    String.format("could not connect to client:\n%s", 
                                  out.toString()),
                    "Unable To Connect", 
                    JOptionPane.ERROR_MESSAGE);
            throw new GameException(ex);
        }
        
        
        
        while(client.getConnectionState() == ConnectionState.OPEN)
        {
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex) {}
            if (!client.isAlive())
            {
                throw new GameException("lost connection");
            }
        }
        
        config = (Map) client.blockingQuery("config", null, 2000);
        timescale = Double.parseDouble(config.get("gm.timescale").toString());
        
        
        mainView.setClientConnection(client);
        mainView.requestFocus();
        mainView.setGameConfiguration(config);
        mainView.focusOnSystem("cluster-system17");
    }

    @Override
    public void update(GameTime gametime) throws GameException
    {
        if (!client.isAlive())
        {
            signalStop();
            return;
        }
        
        // get the state of the user input
        KeyBoardState keyboard = getKeyBoardState();
        MouseState mouse = getMouseState();
        
        if (!mainView.update(gametime, keyboard, mouse))
        {
            signalStop();
        }
    }

    @Override
    public void render(GameTime gametime) throws GameException
    {
        mainView.render(gametime);
        
    }

    @Override
    public void shutdown() throws GameException
    {
        if (client != null)
        {
            client.close();
        }
        if (messages != null)
        {
            messages.setVisible(false);
        }
    }
    
}
