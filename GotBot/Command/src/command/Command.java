/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import com.rf.gotbot.screenshot.ShotImage;
import com.rf.gotbot.screenshot.ScreenShot;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class Command 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException, IOException 
    {
        Robot robot = new Robot();
//        robot.mouseMove(10, 10);
//        robot.mousePress(InputEvent.BUTTON1_MASK);
//        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        
        int[] ignored = {0, 1, 2, 9, 10, 18, 54, 63, 64, 72, 73, 74};
        ShotImage image1 = new ShotImage(
                "C:/Users/REx/Desktop/working images/nb_arrow.png", 
                ignored);
        ShotImage image2 = new ShotImage(
                "C:/Users/REx/Desktop/working images/nb_arrow2.png", 
                ignored);
        
        ScreenShot ss = new ScreenShot(robot);
        
        System.out.println(ss.toString());
        System.out.println(ss.checkForImage(image1, 0));
        System.out.println(ss.checkForImage(image2, 0));
        System.out.println(ss.checkForImage(image1, 5000));
        System.out.println(ss.checkForImage(image2, 5000));
    }
}
